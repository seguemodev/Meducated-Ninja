using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Seguetech.Zippy.Database;

namespace Seguetech.Zippy.Controllers
{
    /// <summary>
    /// Provides RESTful API for looking up medication terms
    /// </summary>
    public class TermController : ApiController
    {
        private IZippyDatabase dbContext;

        /// <summary>
        /// Constructs an instance of TermController with a database context instance
        /// </summary>
        /// <param name="dbContext">Database context instance</param>
        public TermController(IZippyDatabase dbContext)
        {
            this.dbContext = dbContext;
        }

        /// <summary>
        /// Gets a list of medication terms beginning with the id
        /// </summary>
        /// <param name="id">Partial term to lookup</param>
        /// <param name="max">Maximum number of results, defaults to 25</param>
        /// <returns></returns>
        public IEnumerable<string> Get(string id, int max = 25)
        {
            return dbContext.Autocomplete
                .Select(x => x.Term)
                .Where(x => x.StartsWith(id))
                .Take(max);
        }
    }
}
