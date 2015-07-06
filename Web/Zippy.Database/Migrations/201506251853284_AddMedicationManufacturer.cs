namespace Seguetech.Zippy.Database.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddMedicationManufacturer : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Medications", "Manufacturer", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Medications", "Manufacturer");
        }
    }
}
