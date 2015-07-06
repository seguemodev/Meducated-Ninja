using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Globalization;
using System.Linq;
using System.Text.RegularExpressions;
using System.Xml;
using Newtonsoft.Json;
using RestSharp;
using Seguetech.Zippy.Database.Entities;

namespace Seguetech.Zippy.Database.Migrations
{
    public sealed class Configuration : DbMigrationsConfiguration<Seguetech.Zippy.Database.ZippyDatabase>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(Seguetech.Zippy.Database.ZippyDatabase context)
        {
            SeedAutocomplete(context);
        }

        public static void SeedAutocomplete(IZippyDatabase context)
        {
            if (!context.Autocomplete.Any())
            {
                RestClient client = new RestClient("http://rxnav.nlm.nih.gov/REST");
                var result = client.Get(new RestRequest("displaynames.json"));
                var rxData = JsonConvert.DeserializeObject<RxNormData>(result.Content);
                HashSet<string> terms = new HashSet<string>();
                foreach (var term in rxData.displayTermsList.term)
                {
                    // Only use the first alphabetic word in each list
                    var oneWord = Regex.Match(term, @"^[a-zA-Z][^\s\,]*");
                    if (oneWord.Success)
                    {
                        var normalizedTerm = CultureInfo.CurrentCulture.TextInfo.ToTitleCase(oneWord.Value.ToLower());
                        if (normalizedTerm.Length <= 80 && !terms.Contains(normalizedTerm))
                        {
                            context.Add(new Autocomplete() { Term = normalizedTerm });
                            terms.Add(normalizedTerm);
                        }
                    }
                }
                context.Commit();
            }
        }

        private class RxNormData
        {
            public DisplayTermsList displayTermsList { get; set; }
        }

        private class DisplayTermsList
        {
            public IEnumerable<string> term { get; set; }
        }
    }
}
