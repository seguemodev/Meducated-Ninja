namespace Seguetech.Zippy.Database.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Initialize : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Cabinets",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.Int(nullable: false),
                        Number = c.Int(nullable: false),
                        Created = c.DateTime(nullable: false),
                        LastUpdated = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.Medications",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        CabinetId = c.Int(nullable: false),
                        RecordIdField = c.String(),
                        RecordId = c.String(),
                        BrandName = c.String(),
                        GenericName = c.String(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Cabinets", t => t.CabinetId, cascadeDelete: true)
                .Index(t => t.CabinetId);
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Created = c.DateTime(nullable: false),
                        LastUpdated = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Cabinets", "UserId", "dbo.Users");
            DropForeignKey("dbo.Medications", "CabinetId", "dbo.Cabinets");
            DropIndex("dbo.Medications", new[] { "CabinetId" });
            DropIndex("dbo.Cabinets", new[] { "UserId" });
            DropTable("dbo.Users");
            DropTable("dbo.Medications");
            DropTable("dbo.Cabinets");
        }
    }
}
