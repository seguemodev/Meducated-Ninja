using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Seguetech.Zippy.Models;
using Seguetech.Zippy.Database.Entities;

namespace Zippy.Tests.Controllers
{
    public class ControllerTestBase
    {
        public static void materializeUser(UserModel result)
        {
            // Underlying database will materialize result.Cabinets but our mock does not, so materialize it here
            result.Cabinets = result.Cabinets.ToList();
            foreach (var cabinet in result.Cabinets)
            {
                // Underlying database will set IDs but our mock does not, so reset them prior to verifying cabinet
                cabinet.Id = int.Parse(cabinet.Name.Replace("Cabinet ", ""));
                materializeCabinet(cabinet);
            }
        }

        public static void materializeCabinet(CabinetModel cabinet)
        {
            // Underlying database will materialize result.Medications but our mock does not, so materialize it here
            cabinet.Medications = cabinet.Medications.ToList();
            // Underlying database will set IDs but our mock does not, so reset them prior to verifying cabinet
            foreach (var medication in cabinet.Medications)
            {
                materializeMedication(medication);
            }
        }

        public static void materializeMedication(MedicationModel medication)
        {
            medication.Id = int.Parse(medication.FdaId.Split('.')[0]);
        }

        public void Expect404(Action testAction)
        {
            try
            {
                testAction();
                Assert.Fail("Expected HttpResponseException but did not get it");
            }
            catch (HttpResponseException ex)
            {
                if (ex.Response.StatusCode != System.Net.HttpStatusCode.NotFound)
                {
                    Assert.Fail(String.Format("Expected 404 but got {0} instead", (int)ex.Response.StatusCode));
                }
            }
        }

        public void verifyStandardUser(UserModel user, int id)
        {
            Assert.AreEqual(id, user.Id);
            Assert.AreEqual(3, user.Cabinets.Count());
            foreach (var cabinet in user.Cabinets)
            {
                verifyStandardCabinet(cabinet, cabinet.Id);
            }
        }

        public void verifyStandardCabinet(CabinetModel cabinet, int id)
        {
            Assert.AreEqual(id, cabinet.Id);
            Assert.AreEqual(3, cabinet.Medications.Count());
            foreach (var medication in cabinet.Medications)
            {
                verifyStandardMedication(medication, medication.Id);
            }
        }

        public void verifyStandardMedication(MedicationModel medication, int id)
        {
            Assert.AreEqual(id, medication.Id);
            Assert.AreEqual(String.Format("Medication {0}", id), medication.BrandName);
            Assert.AreEqual(String.Format("Generic {0}", id), medication.GenericName);
            Assert.AreEqual(id.ToString(), medication.FdaId);
            Assert.AreEqual("Field", medication.ImageUrl);
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public static List<User> mockUsers(int count)
        {
            return mockList<User>(count, null, mockUser);
        }

        public static User mockUser(int id, string suffix = "")
        {
            return new User()
            {
                Id = id,
                Cabinets = mockCabinets(3)
            };
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public static List<Cabinet> mockCabinets(int count)
        {
            return mockList<Cabinet>(count, null, mockCabinet);
        }

        public static Cabinet mockCabinet(int id, string suffix = "")
        {
            return new Cabinet()
            {
                Id = id,
                Name = String.Format("Cabinet {0}", id),
                UserId = 0,
                Medications = mockMedications(3)
            };
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public static List<CabinetModel> mockCabinetModels(int count, string suffix = "")
        {
            return mockList<CabinetModel>(count, suffix, mockCabinetModel);
        }

        public static CabinetModel mockCabinetModel(int id, string suffix = "")
        {
            return new CabinetModel()
            {
                UserId = 0,
                Name = String.Format("Cabinet {0}", id),
                Medications = mockMedicationModels(3, suffix)
            };
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public static List<Medication> mockMedications(int count)
        {
            return mockList<Medication>(count, null, mockMedication);
        }

        public static Medication mockMedication(int id, string suffix = "")
        {
            return new Medication()
            {
                Id = id,
                BrandName = String.Format("Medication {0}", id),
                GenericName = String.Format("Generic {0}", id),
                FdaId = id.ToString(),
                ImageUrl = "Field"
            };
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public static List<MedicationModel> mockMedicationModels(int count, string suffix = "")
        {
            return mockList<MedicationModel>(count, suffix, mockMedicationModel);
        }

        public static MedicationModel mockMedicationModel(int id, string suffix = "")
        {
            return new MedicationModel()
            {
                CabinetId = 0,
                Id = id,
                FdaId = id.ToString() + suffix,
                ImageUrl = "Field" + suffix,
                BrandName = String.Format("Medication {0}{1}", id, suffix),
                GenericName = String.Format("Generic {0}{1}", id, suffix),
            };
        }

        private static List<T> mockList<T>(int count, string suffix, Func<int, string, T> mockEntity)
           where T : class, new()
        {
            var list = new List<T>();
            for (int i = 1; i <= count; i++)
            {
                list.Add(mockEntity(i, suffix));
            }
            return list;
        }
    }
}