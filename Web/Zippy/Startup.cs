using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Reflection;
using System.Web.Http;
using System.Web.Mvc;
using Autofac;
using Autofac.Integration.Mvc;
using Autofac.Integration.WebApi;
using Microsoft.Owin;
using Owin;
using Seguetech.Zippy.Database;
using Seguetech.Zippy.Database.Migrations;

[assembly: OwinStartup(typeof(Seguetech.Zippy.Startup))]
#pragma warning disable 1591
namespace Seguetech.Zippy
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            System.Data.Entity.Database.SetInitializer(
                new MigrateDatabaseToLatestVersion<ZippyDatabase, Configuration>());
            RegisterDependencyInjection(app, ConfigureAutofac());
        }

        private void RegisterDependencyInjection(IAppBuilder app, IContainer container)
        {
            // Enable Autofac for MVC and WebAPI
            app.UseAutofacMvc();
            app.UseAutofacWebApi(GlobalConfiguration.Configuration);
            // Set MVC5 dependency resolver
            DependencyResolver.SetResolver(new AutofacDependencyResolver(container));
            // Set API2 dependency resolver
            GlobalConfiguration.Configuration.DependencyResolver = new AutofacWebApiDependencyResolver(container);
            // Set OWIN dependency resolver
            app.UseAutofacMiddleware(container);
        }

        private IContainer ConfigureAutofac()
        {
            var builder = new ContainerBuilder();
            // Auto-configuration
            builder.RegisterControllers(Assembly.GetExecutingAssembly());
            builder.RegisterModelBinders(Assembly.GetExecutingAssembly());
            builder.RegisterApiControllers(Assembly.GetExecutingAssembly());
            builder.RegisterModelBinderProvider();
            builder.RegisterModule<AutofacWebTypesModule>();
            builder.RegisterSource(new ViewRegistrationSource());
            builder.RegisterFilterProvider();
            builder.RegisterWebApiFilterProvider(GlobalConfiguration.Configuration);
            // Zippy configuration
            builder.RegisterType<ZippyDatabase>().AsImplementedInterfaces();

            var container = builder.Build();
            return container;
        }
    }
}
