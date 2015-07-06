namespace Seguetech.Zippy.Database.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class ChangeCabinetNumberToName : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Cabinets", "Name", c => c.String());
            DropColumn("dbo.Cabinets", "Number");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Cabinets", "Number", c => c.Int(nullable: false));
            DropColumn("dbo.Cabinets", "Name");
        }
    }
}
