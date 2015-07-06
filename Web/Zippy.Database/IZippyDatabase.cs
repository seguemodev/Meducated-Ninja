using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Database
{
    public interface IZippyDatabase
    {
        IQueryable<User> Users { get; }
        IQueryable<Cabinet> Cabinets { get; }
        IQueryable<Medication> Medications { get; }
        IQueryable<Autocomplete> Autocomplete { get; }

        T Add<T>(T entity)    where T : class;
        T Remove<T>(T entity) where T : class;
        int Commit();
    }
}
