using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Models
{
    internal static class ModelExtensions
    {
        public static List<Cabinet> ToDbModel(this IEnumerable<CabinetModel> cabinets)
        {
            if (cabinets == null)
            {
                return new List<Cabinet>();
            }
            return cabinets.Select(c=>c.ToDbCabinet()).ToList();
        }

        public static List<Medication> ToDbModel(this IEnumerable<MedicationModel> medications)
        {
            if (medications == null)
            {
                return new List<Medication>();
            }
            return medications.Select(m => m.ToDbMedication()).ToList();
        }
    }
}
