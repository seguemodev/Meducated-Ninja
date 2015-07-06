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
    public class MedicationTests : ControllerTestBase
    {
        private static Mock<IZippyDatabase> mockDatabase;
        private static List<Medication> medicationsData;
        private static MedicationController controller;

        [ClassInitialize]
        public static void ClassInit(TestContext context)
        {
            medicationsData = mockMedications(3);

            mockDatabase = new Mock<IZippyDatabase>(MockBehavior.Strict);
            mockDatabase.SetupGet(x => x.Cabinets)
                .Returns(new List<Cabinet>() { new Cabinet() { Id = 0 } }.AsQueryable());
            mockDatabase.SetupGet(x => x.Medications)
                .Returns(() => medicationsData.AsQueryable());
            mockDatabase.Setup(x => x.Add(It.IsAny<Medication>()))
                .Callback<Medication>(x => { x.Id = new Random().Next(5, int.MaxValue); medicationsData.Add(x); })
                .Returns((Medication x) => x);
            mockDatabase.Setup(x => x.Remove(It.IsAny<Medication>()))
                .Callback<Medication>(x => medicationsData.Remove(x))
                .Returns((Medication x) => x);
            mockDatabase.Setup(x => x.Commit())
                .Returns(0);

            controller = new MedicationController(mockDatabase.Object);
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

            mockDatabase.VerifyGet(x => x.Medications, Times.Once);
            verifyStandardMedication(result, 2);
        }

        [TestMethod]
        public void TestGet404()
        {
            Expect404(() => controller.Get(10));
            mockDatabase.VerifyGet(x => x.Medications, Times.Once);
        }

        [TestMethod]
        public void TestPost()
        {
            var result = controller.Post(mockMedicationModel(4));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
            materializeMedication(result);
            Assert.IsTrue(result.Id > 3);
            verifyStandardMedication(result, result.Id);
        }

        [TestMethod]
        public void TestPost404()
        {
            Expect404(() => controller.Post(new MedicationModel()
            {
                CabinetId = -1,
            }));
            mockDatabase.VerifyGet(x => x.Cabinets, Times.Once);
        }

        [TestMethod]
        public void TestDelete()
        {
            var delete = mockMedication(4);
            medicationsData.Add(delete);
            try
            {
                controller.Delete(4);
                mockDatabase.VerifyGet(x => x.Medications, Times.Once);
                mockDatabase.Verify(x => x.Remove(delete), Times.Once);
                mockDatabase.Verify(x => x.Commit(), Times.Once);
            }
            finally
            {
                medicationsData.Remove(delete);
            }
        }

        [TestMethod]
        public void TestDelete404()
        {
            Expect404(() => controller.Delete(-1));
            mockDatabase.VerifyGet(x => x.Medications, Times.Once);
        }

        [TestMethod]
        public void TestPut()
        {
            var result = controller.Put(new MedicationModel()
            {
                Id = 1,
                BrandName = "New Brand",
                GenericName = "New Generic",
                FdaId = "New Record Id",
                ImageUrl = "New Field"
            });
            // Verify calls
            mockDatabase.VerifyGet(x => x.Medications, Times.Exactly(2));
            mockDatabase.Verify(x => x.Commit(), Times.Once);
            // Verify result
            Assert.AreEqual(1, result.Id);
            Assert.AreEqual("New Brand", result.BrandName);
            Assert.AreEqual("New Generic", result.GenericName);
            Assert.AreEqual("New Record Id", result.FdaId);
            Assert.AreEqual("New Field", result.ImageUrl);

            // Verify persistent change
            var medication = medicationsData.FirstOrDefault(x => x.Id == 1);
            Assert.AreEqual(1, medication.Id);
            Assert.AreEqual("New Brand", medication.BrandName);
            Assert.AreEqual("New Generic", medication.GenericName);
            Assert.AreEqual("New Record Id", medication.FdaId);
            Assert.AreEqual("New Field", medication.ImageUrl);
        }

        [TestMethod]
        public void TestPut404()
        {
            Expect404(() => controller.Put(new MedicationModel()
                {
                    Id = -1,
                    BrandName = "New Brand",
                    GenericName = "New Generic",
                    FdaId = "New Record Id",
                    ImageUrl = "New Field"
                }));
            mockDatabase.VerifyGet(x => x.Medications, Times.Once);
        }
    }
}