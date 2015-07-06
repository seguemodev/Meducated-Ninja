using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Seguetech.Zippy.Models;
using Seguetech.Zippy.Database;

namespace Seguetech.Zippy.Controllers
{
    /// <summary>
    /// Provides RESTful API for Medicine Cabinets
    /// </summary>
    public class CabinetController : ApiController
    {
        private IZippyDatabase dbContext;

        /// <summary>
        /// Constructs an instance of CabinetController with a database context instance
        /// </summary>
        /// <param name="dbContext">Database context instance</param>
        public CabinetController(IZippyDatabase dbContext)
        {
            this.dbContext = dbContext;
        }

        /// <summary>
        /// Get a medicine cabinet, including medications contained therein
        /// </summary>
        /// <param name="id">Zippy ID of the medicine cabinet</param>
        /// <returns>Cabinet header and body</returns>
        [HttpGet]
        public CabinetModel Get(int id)
        {
            var cabinet = dbContext.Cabinets
                .Where(c => c.Id == id)
                .Select(c => new CabinetModel()
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
                .FirstOrDefault();
            if (cabinet == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            return cabinet;
        }

        /// <summary>
        /// Gets a medicine cabinet without its medications
        /// </summary>
        /// <param name="id">Zippy ID of the medicine cabinet</param>
        /// <returns>Cabinet header only</returns>
        [HttpHead]
        public CabinetModel Head(int id)
        {
            var m = dbContext.Cabinets
                .Where(c => c.Id == id)
                .Select(c => new CabinetModel()
                {
                    Id = c.Id,
                    Created = c.Created,
                    LastUpdated = c.LastUpdated,
                })
                .FirstOrDefault();
            if (m == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            return m;
        }

        /// <summary>
        /// Creates a new medicine cabinet for a user
        /// </summary>
        /// <param name="model">Medicine cabinet details</param>
        /// <returns>Cabinet as loaded from the database (with DB-generated IDs for cabinet and medications)</returns>
        [HttpPost]
        public CabinetModel Post(CabinetModel model)
        {
            if (!dbContext.Users.Any(u => u.Id == model.UserId))
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            var cabinet = model.ToDbCabinet();
            dbContext.Add(cabinet);
            dbContext.Commit();
            return Get(cabinet.Id);
        }

        /// <summary>
        /// Deletes a medicine cabinet
        /// </summary>
        /// <param name="id">Zippy ID of the cabinet to be deleted</param>
        /// <returns>True if the delete succeeded, otherwise a non-200 status</returns>
        [HttpDelete]
        public bool Delete(int id)
        {
            var cabinet = dbContext.Cabinets.FirstOrDefault(c => c.Id == id);
            if (cabinet == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            dbContext.Remove(cabinet);
            dbContext.Commit();
            return true;
        }

        /// <summary>
        /// Updates an existing medicine cabinet
        /// </summary>
        /// <param name="model">Medicine cabinet details</param>
        /// <returns>Cabinet as loaded from the database (with DB-generated IDs for new medications)</returns>
        [HttpPut]
        public CabinetModel Put(CabinetModel model)
        {
            var cabinet = dbContext.Cabinets
                .Include(c => c.Medications)
                .FirstOrDefault(m => m.Id == model.Id);
            if (cabinet == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            cabinet.Name = model.Name ?? cabinet.Name;
            cabinet.LastUpdated = DateTime.Now;
            if (model.Medications != null)
            {
                // Remove all medications from the cabinet
                foreach (var dbMedication in cabinet.Medications)
                {
                    dbContext.Remove(dbMedication);
                }
                // Re-populate the cabinet
                cabinet.Medications = model.Medications.Select(m => m.ToDbMedication()).ToList();
            }
            dbContext.Commit();
            return Get(model.Id);
        }
    }
}