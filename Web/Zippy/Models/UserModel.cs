using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Models
{
    /// <summary>
    /// User model used to construct JSON results
    /// </summary>
    public class UserModel
    {
        /// <summary>
        /// Zippy ID of the user
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// When the user was created
        /// </summary>
        public DateTime Created { get; set; }
        /// <summary>
        /// When the user was last updated
        /// </summary>
        public DateTime LastUpdated { get; set; }
        /// <summary>
        /// List of cabinets for the user
        /// </summary>
        public IEnumerable<CabinetModel> Cabinets { get; set; }

        internal User ToDbUser()
        {
            return new User()
            {
                Created = DateTime.Now,
                LastUpdated = DateTime.Now,
                Cabinets = Cabinets.ToDbModel()
            };
        }
    }
}