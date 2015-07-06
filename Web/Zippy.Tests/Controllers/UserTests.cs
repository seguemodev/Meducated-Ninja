using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Seguetech.Zippy.Controllers;
using Seguetech.Zippy.Models;
using Seguetech.Zippy.Database;
using Seguetech.Zippy.Database.Entities;

namespace Zippy.Tests.Controllers
{
    [TestClass]
    public class UserTests : ControllerTestBase
    {
        private static Mock<IZippyDatabase> mockDatabase;
        private static List<User> usersData;
        private static UserController controller;

        [ClassInitialize]
        public static void ClassInit(TestContext context)
        {
            usersData = mockUsers(3);

            mockDatabase = new Mock<IZippyDatabase>(MockBehavior.Strict);
            mockDatabase.SetupGet(x => x.Users)
                .Returns(() => usersData.AsQueryable());
            mockDatabase.Setup(x => x.Add(It.IsAny<User>()))
                .Callback<User>(x => { x.Id = new Random().Next(5, int.MaxValue); usersData.Add(x); })
                .Returns((User x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<User>()))
                .Callback<User>(x => usersData.Remove(x))
                .Returns((User x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<Medication>()))
                .Returns((Medication x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<Cabinet>()))
                .Returns((Cabinet x) => x);
            mockDatabase.Setup(x => x.Commit())
                .Returns(0);

            controller = new UserController(mockDatabase.Object);
        }

        [ClassCleanup]
        public static void ClassCleanup()
        {
        }

        [TestInitialize]
        public void Initialize()
        {
            mockDatabase.ResetCalls();
        }

        [TestMethod]
        public void TestGet()
        {
            var result = controller.Get(2);
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
            verifyStandardUser(result, 2);
        }

        [TestMethod]
        public void TestGet404()
        {
            Expect404(() => controller.Get(-1));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }

        [TestMethod]
        public void TestHead()
        {
            controller.Head(2);
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }

        [TestMethod]
        public void TestHead404()
        {
            Expect404(() => controller.Head(-1));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }

        [TestMethod]
        public void TestDelete()
        {
            var delete = mockUser(4);
            usersData.Add(delete);
            try
            {
                controller.Delete(4);
                mockDatabase.VerifyGet(x => x.Users, Times.Once);
                mockDatabase.Verify(x => x.Remove(delete), Times.Once);
                mockDatabase.Verify(x => x.Commit(), Times.Once);
            }
            finally
            {
                usersData.Remove(delete);
            }
        }

        [TestMethod]
        public void TestDelete404()
        {
            Expect404(() => controller.Delete(-1));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }

        [TestMethod]
        public void TestPost()
        {
            var result = controller.Post(new UserModel()
            {
                Id = 4,
                Cabinets = mockCabinetModels(3)
            });
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
            materializeUser(result);
            Assert.IsTrue(result.Id > 3);
            verifyStandardUser(result, result.Id);
        }

        [TestMethod]
        public void TestPut()
        {
            var put = mockUser(4);
            usersData.Add(put);
            try
            {
                var putModel = new UserModel()
                {
                    Id = 4,
                    Cabinets = mockCabinetModels(3, ".1")
                };
                // Update 2 existing cabinets, add 1 new
                putModel.Cabinets.ElementAt(0).Id = 1;
                putModel.Cabinets.ElementAt(2).Id = 2;
                var result = controller.Put(putModel);
                materializeUser(result);
                Assert.AreEqual(4, result.Id);
                Assert.AreEqual(3, result.Cabinets.Count());
                foreach (var cabinet in result.Cabinets)
                {
                    Assert.AreEqual(3, cabinet.Medications.Count());
                    foreach (var medication in cabinet.Medications)
                    {
                        Assert.AreEqual(String.Format("Medication {0}.1", medication.Id), medication.BrandName);
                        Assert.AreEqual(String.Format("Generic {0}.1", medication.Id), medication.GenericName);
                        Assert.AreEqual(medication.Id.ToString() + ".1", medication.FdaId);
                        Assert.AreEqual("Field.1", medication.ImageUrl);
                    }
                }
            }
            finally
            {
                usersData.Remove(put);
            }
        }

        [TestMethod]
        public void TestPut404()
        {
            Expect404(() => controller.Put(new UserModel()
                {
                    Id = -1,
                    Cabinets = new List<CabinetModel>()
                }));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }
    }
}