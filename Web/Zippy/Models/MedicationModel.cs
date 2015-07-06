using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Models
{
    /// <summary>
    /// Medication model used to construct JSON results
    /// </summary>
    public class MedicationModel
    {
        /// <summary>
        /// Zippy ID of the cabinet the medication is in
        /// </summary>
        public int CabinetId { get; set; }
        /// <summary>
        /// Zippy ID of the medication
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// Generic name of the medication
        /// </summary>
        public string GenericName { get; set; }
        /// <summary>
        /// Brand name of the medication
        /// </summary>
        public string BrandName { get; set; }
        /// <summary>
        /// Manufacturer
        /// </summary>
        public string Manufacturer { get; set; }
        /// <summary>
        /// api.fda.gov field used to uniquely identify the medication from the FDA API (e.g. id, spl_set_id, openfda.spl_set_id, etc.)
        /// </summary>
        public string ImageUrl { get; set; }
        /// <summary>
        /// Unique ID of the medication frmo the FDA API (the value of contained in the field identified by RecordIdField)
        /// </summary>
        public string FdaId { get; set; }

        internal Medication ToDbMedication()
        {
            return UpdateDbMedication(new Medication());
        }

        internal Medication UpdateDbMedication(Medication medication)
        {
            medication.CabinetId = CabinetId;
            medication.BrandName = BrandName;
            medication.GenericName = GenericName;
            medication.FdaId = FdaId;
            medication.ImageUrl = ImageUrl;
            medication.Manufacturer = Manufacturer;
            return medication;
        }
    }
}