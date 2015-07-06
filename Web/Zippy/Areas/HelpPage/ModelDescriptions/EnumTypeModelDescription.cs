using System.Collections.Generic;
using System.Collections.ObjectModel;

#pragma warning disable 1591
namespace Seguetech.Zippy.Areas.HelpPage.ModelDescriptions
{
    public class EnumTypeModelDescription : ModelDescription
    {
        public EnumTypeModelDescription()
        {
            Values = new Collection<EnumValueDescription>();
        }

        public Collection<EnumValueDescription> Values { get; private set; }
    }
}