using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Database
{
    public class ZippyDatabase : DbContext, IZippyDatabase
    {
        public DbSet<User> UserSet { get; set; }
        public DbSet<Cabinet> CabinetSet { get; set; }
        public DbSet<Medication> MedicationSet { get; set; }
        public DbSet<Autocomplete> Autocomplete { get; set; }

        public ZippyDatabase()
            : base("DefaultConnection")
        {
        }

        public ZippyDatabase(string nameOrConnectionString)
            : base(nameOrConnectionString)
        {

        }

        #region IZippyDatabase
        IQueryable<Entities.User> IZippyDatabase.Users { get { return UserSet; } }
        IQueryable<Entities.Cabinet> IZippyDatabase.Cabinets { get { return CabinetSet; } }
        IQueryable<Entities.Medication> IZippyDatabase.Medications { get { return MedicationSet; } }
        IQueryable<Entities.Autocomplete> IZippyDatabase.Autocomplete { get { return Autocomplete; } }

        T IZippyDatabase.Add<T>(T entity)
        {
            return this.Set<T>().Add(entity);
        }

        T IZippyDatabase.Remove<T>(T entity)
        {
            return this.Set<T>().Remove(entity);
        }

        int IZippyDatabase.Commit()
        {
            return this.SaveChanges();
        }
        #endregion
    }
}