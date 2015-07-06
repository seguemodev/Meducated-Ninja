namespace Seguetech.Zippy.Database.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddAutocomplete : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Autocompletes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Term = c.String(maxLength: 80),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.Term);
            
        }
        
        public override void Down()
        {
            DropIndex("dbo.Autocompletes", new[] { "Term" });
            DropTable("dbo.Autocompletes");
        }
    }
}
