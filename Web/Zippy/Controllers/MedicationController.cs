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
    /// Provides RESTful API for Medications contained within a Cabinet
    /// </summary>
    public class MedicationController : ApiController
    {
        private IZippyDatabase dbContext;

        /// <summary>
        /// Constructs an instance of MedicationController with a database context instance
        /// </summary>
        /// <param name="dbContext">Database context instance</param>
        public MedicationController(IZippyDatabase dbContext)
        {
            this.dbContext = dbContext;
        }

        /// <summary>
        /// Get the details of a specific cabinet-saved medication by ID
        /// </summary>
        /// <param name="id">Zippy ID of the medication</param>
        /// <returns>Medication details</returns>
        [HttpGet]
        public MedicationModel Get(int id)
        {
            var medication = dbContext.Medications
                .Where(m => m.Id == id)
                .Select(m => new MedicationModel()
                {
                    Id = m.Id,
                    GenericName = m.GenericName,
                    BrandName = m.BrandName,
                    ImageUrl = m.ImageUrl,
                    FdaId = m.FdaId,
                    CabinetId = m.CabinetId,
                })
                .FirstOrDefault();
            if (medication == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            return medication;
        }

        /// <summary>
        /// Adds a new medication to a cabinet
        /// </summary>
        /// <param name="model">Medication details</param>
        /// <returns>Medication as loaded from the database (with DB-generated ID)</returns>
        [HttpPost]
        public MedicationModel Post(MedicationModel model)
        {
            if (!dbContext.Cabinets.Any(u => u.Id == model.CabinetId))
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            var medication = model.ToDbMedication();
            dbContext.Add(medication);
            dbContext.Commit();
            return Get(medication.Id);
        }

        /// <summary>
        /// Delete a medication from a cabinet
        /// </summary>
        /// <param name="id">Zippy ID of the medication</param>
        /// <returns>True if the delete succeeded, otherwise a non-200 status</returns>
        [HttpDelete]
        public bool Delete(int id)
        {
            var medication = dbContext.Medications.FirstOrDefault(m => m.Id == id);
            if (medication == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            dbContext.Remove(medication);
            dbContext.Commit();
            return true;
        }

        /// <summary>
        /// Update a medication stored in a cabinet
        /// </summary>
        /// <param name="model">Medication details</param>
        /// <returns>Medication as loaded from the database</returns>
        [HttpPut]
        public MedicationModel Put(MedicationModel model)
        {
            var medication = dbContext.Medications.FirstOrDefault(m => m.Id == model.Id);
            if (medication == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            model.UpdateDbMedication(medication);
            dbContext.Commit();
            return Get(model.Id);
        }
    }
}