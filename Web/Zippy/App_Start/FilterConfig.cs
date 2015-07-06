using System.Web;
using System.Web.Mvc;

namespace Seguetech.Zippy
{
    internal class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
