using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seguetech.Zippy.Database.Entities
{
    public class Medication
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public int CabinetId { get; set; }
        public string ImageUrl { get; set; }
        public string FdaId { get; set; }
        public string BrandName { get; set; }
        public string GenericName { get; set; }
        public string Manufacturer { get; set; }

        public virtual Cabinet Cabinet { get; set; }

    }
}
