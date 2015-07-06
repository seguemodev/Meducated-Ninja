using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Models
{
    /// <summary>
    /// Medicine Cabinet model used to construct JSON results
    /// </summary>
    public class CabinetModel
    {
        /// <summary>
        /// Zippy ID of the user owning the cabinet
        /// </summary>
        [Required]
        public int UserId { get; set; }
        /// <summary>
        /// Zippy ID of the cabinet
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// Cabinet Name
        /// </summary>
        [Required]
        public string Name { get; set; }
        /// <summary>
        /// When the cabinet was created
        /// </summary>
        public DateTime Created { get; set; }
        /// <summary>
        /// When the cabinet was last updated
        /// </summary>
        public DateTime LastUpdated { get; set; }
        /// <summary>
        /// List of medications stored within the cabinet
        /// </summary>
        public IEnumerable<MedicationModel> Medications { get; set; }

        internal Cabinet ToDbCabinet()
        {
            return new Cabinet()
            {
                UserId = UserId,
                Name = Name,
                Created = DateTime.Now,
                LastUpdated = DateTime.Now,
                Medications = Medications.ToDbModel()
            };
        }
    }
}