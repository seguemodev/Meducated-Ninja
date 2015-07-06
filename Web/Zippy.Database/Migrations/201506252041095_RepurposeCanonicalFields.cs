namespace Seguetech.Zippy.Database.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class RepurposeCanonicalFields : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Medications", "ImageUrl", c => c.String());
            AddColumn("dbo.Medications", "FdaId", c => c.String());
            DropColumn("dbo.Medications", "RecordIdField");
            DropColumn("dbo.Medications", "RecordId");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Medications", "RecordId", c => c.String());
            AddColumn("dbo.Medications", "RecordIdField", c => c.String());
            DropColumn("dbo.Medications", "FdaId");
            DropColumn("dbo.Medications", "ImageUrl");
        }
    }
}
