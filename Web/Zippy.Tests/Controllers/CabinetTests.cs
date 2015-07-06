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
    public class CabinetTests : ControllerTestBase
    {
        private static Mock<IZippyDatabase> mockDatabase;
        private static List<Cabinet> cabinetsData;
        private static CabinetController controller;

        [ClassInitialize]
        public static void ClassInit(TestContext context)
        {
            cabinetsData = mockCabinets(3);

            mockDatabase = new Mock<IZippyDatabase>(MockBehavior.Strict);
            mockDatabase.SetupGet(x => x.Users)
                .Returns(new List<User>() { new User() { Id = 0 } }.AsQueryable());
            mockDatabase.SetupGet(x => x.Cabinets)
                .Returns(() => cabinetsData.AsQueryable());
            mockDatabase.Setup(x => x.Add(It.IsAny<Cabinet>()))
                .Callback<Cabinet>(x => { x.Id = new Random().Next(5, int.MaxValue); cabinetsData.Add(x); })
                .Returns((Cabinet x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<Cabinet>()))
                .Callback<Cabinet>(x => cabinetsData.Remove(x))
                .Returns((Cabinet x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<Medication>()))
                .Returns((Medication x) => x);
            mockDatabase.Setup(x => x.Commit())
                .Returns(0);

            controller = new CabinetController(mockDatabase.Object);
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
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
            verifyStandardCabinet(result, 2);
        }

        [TestMethod]
        public void TestGet404()
        {
            Expect404(() => controller.Get(-1));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
        }

        [TestMethod]
        public void TestHead()
        {
            var result = controller.Head(2);

            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
            Assert.AreEqual(2, result.Id);
            Assert.IsNull(result.Medications);
        }

        [TestMethod]
        public void TestHead404()
        {
            Expect404(() => controller.Head(-1));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
        }

        [TestMethod]
        public void TestDelete()
        {
            var delete = mockCabinet(4);
            cabinetsData.Add(delete);
            try
            {
                controller.Delete(4);
                mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
                mockDatabase.Verify(x => x.Remove(delete), Times.Once);
                mockDatabase.Verify(x => x.Commit(), Times.Once);
            }
            finally
            {
                cabinetsData.Remove(delete);
            }
        }

        [TestMethod]
        public void TestDelete404()
        {
            Expect404(() => controller.Delete(-1));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
        }

        [TestMethod]
        public void TestPost()
        {
            var result = controller.Post(mockCabinetModel(4));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
            materializeCabinet(result);
            Assert.IsTrue(result.Id > 3);
            verifyStandardCabinet(result, result.Id);
        }

        [TestMethod]
        public void TestPost404()
        {
            Expect404(() => controller.Post(new CabinetModel()
                {
                    Id = -1,
                    UserId = -1,
                    Medications = new List<MedicationModel>()
                }));
            mockDatabase.VerifyGet(x => x.Users, Times.Once);
        }

        [TestMethod]
        public void TestPut()
        {
            var put = mockCabinet(4);
            cabinetsData.Add(put);
            try
            {
                var result = controller.Put(new CabinetModel()
                {
                    UserId = 0,
                    Id = 4,
                    Name = "Cabinet 4",
                    Medications = mockMedicationModels(2, ".1")
                });
                materializeCabinet(result);
                Assert.AreEqual(4, result.Id);
                Assert.AreEqual(2, result.Medications.Count());
                foreach (var medication in result.Medications)
                {
                    Assert.AreEqual(String.Format("Medication {0}.1", medication.Id), medication.BrandName);
                    Assert.AreEqual(String.Format("Generic {0}.1", medication.Id), medication.GenericName);
                    Assert.AreEqual(medication.Id.ToString() + ".1", medication.FdaId);
                    Assert.AreEqual("Field.1", medication.ImageUrl);
                }
            }
            finally
            {
                cabinetsData.Remove(put);
            }
        }

        [TestMethod]
        public void TestPut404()
        {
            Expect404(() => controller.Put(new CabinetModel()
                {
                    Id = -1,
                    Medications = new List<MedicationModel>()
                }));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
        }
    }
}