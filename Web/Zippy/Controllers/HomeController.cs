using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Seguetech.Zippy.Database;

#pragma warning disable 1591
namespace Seguetech.Zippy.Controllers
{
    public class HomeController : Controller
    {
        private IZippyDatabase dbContext;

        public HomeController(IZippyDatabase dbContext)
        {
            this.dbContext = dbContext;
        }

        public ActionResult Index()
        {
            return View();
        }
    }
}
