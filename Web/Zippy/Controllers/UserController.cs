using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Seguetech.Zippy.Models;
using Seguetech.Zippy.Database;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Controllers
{
    /// <summary>
    /// Provides RESTful API for Users Cabinets
    /// </summary>
    public class UserController : ApiController
    {
        private IZippyDatabase dbContext;

        /// <summary>
        /// Constructs an instance of UserController with a database context instance
        /// </summary>
        /// <param name="dbContext">Database context instance</param>
        public UserController(IZippyDatabase dbContext)
        {
            this.dbContext = dbContext;
        }

        /// <summary>
        /// Get a user, including medicine cabinets, and medications contained therein
        /// </summary>
        /// <param name="id">Zippy ID of the user</param>
        /// <returns>User header and body</returns>
        [HttpGet]
        public UserModel Get(int id)
        {
            var user = dbContext.Users
                .Where(u => u.Id == id)
                .Select(u => new UserModel()
                {
                    Id = u.Id,
                    Created = u.Created,
                    LastUpdated = u.LastUpdated,
                    Cabinets = u.Cabinets.Select(c => new CabinetModel()
                    {
                        Id = c.Id,
                        UserId = c.UserId,
                        Name = c.Name,
                        Created = c.Created,
                        LastUpdated = c.LastUpdated,
                        Medications = c.Medications.Select(m => new MedicationModel()
                        {
                            Id = m.Id,
                            GenericName = m.GenericName,
                            BrandName = m.BrandName,
                            Manufacturer = m.Manufacturer,
                            ImageUrl = m.ImageUrl,
                            FdaId = m.FdaId,
                        })
                    })
                })
                .FirstOrDefault();
            if (user == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            return user;
        }

        /// <summary>
        /// Gets a user without its medicine cabinets or medications
        /// </summary>
        /// <param name="id">Zippy ID of the user</param>
        /// <returns>User header only</returns>
        [HttpHead]
        public void Head(int id)
        {
            if (!dbContext.Users.Any(u => u.Id == id))
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
        }

        /// <summary>
        /// Creates a new user
        /// </summary>
        /// <param name="model">User details</param>
        /// <returns>User as loaded from the database (with DB-generated IDs for user, cabinets, and medications)</returns>
        [HttpPost]
        public UserModel Post(UserModel model)
        {
            if (model == null)
            {
                // null model is just a request to create a new account, since we
                // don't have any user fields this is permitted.
                model = new UserModel();
            }
            var user = model.ToDbUser();
            dbContext.Add(user);
            dbContext.Commit();
            return Get(user.Id);
        }

        /// <summary>
        /// Deletes a user
        /// </summary>
        /// <param name="id">Zippy ID of the user to be deleted</param>
        /// <returns>True if the delete succeeded, otherwise a non-200 status</returns>
        [HttpDelete]
        public bool Delete(int id)
        {
            var user = dbContext.Users.FirstOrDefault(u => u.Id == id);
            if (user == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            dbContext.Remove(user);
            dbContext.Commit();
            return true;
        }

        /// <summary>
        /// Updates an existing user
        /// </summary>
        /// <param name="model">User details</param>
        /// <returns>User as loaded from the database (with DB-generated IDs for new cabinets and medications)</returns>
        [HttpPut]
        public UserModel Put(UserModel model)
        {
            var user = dbContext.Users
                .Include(u => u.Cabinets.Select(c => c.Medications))
                .FirstOrDefault(u => u.Id == model.Id);
            if (user == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            user.LastUpdated = DateTime.Now;
            if (model.Cabinets != null)
            {
                // Remove cabinets which are not in the new model
                foreach (var dbCabinet in user.Cabinets.Where(c => !model.Cabinets.Any(mc => mc.Id == c.Id)).ToList())
                {
                    dbContext.Remove(dbCabinet);
                    user.Cabinets.Remove(dbCabinet);
                }
                // Update each cabinet
                foreach (var cabinet in model.Cabinets)
                {
                    var dbCabinet = user.Cabinets.FirstOrDefault(c => c.Id == cabinet.Id);
                    // If the cabinet doesn't exist, create a new one
                    if (dbCabinet == null)
                    {
                        dbCabinet = cabinet.ToDbCabinet();
                        user.Cabinets.Add(dbCabinet);
                    }
                    if (cabinet.Medications != null)
                    {
                        // Remove all medications from the cabinet
                        foreach (var dbMedication in dbCabinet.Medications)
                        {
                            dbContext.Remove(dbMedication);
                        }
                        // Re-populate the cabinet
                        dbCabinet.Medications = cabinet.Medications.Select(m => m.ToDbMedication()).ToList();
                    }
                }
            }
            dbContext.Commit();
            return Get(user.Id);
        }
    }
}
