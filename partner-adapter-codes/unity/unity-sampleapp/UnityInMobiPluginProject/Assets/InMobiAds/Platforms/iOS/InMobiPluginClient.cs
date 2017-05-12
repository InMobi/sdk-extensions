#if UNITY_IOS
using System;
using InMobiAds.Common;

namespace InMobiAds.Platforms.iOS
{
	public class InMobiPluginClient : IInMobiPluginClient
	{
		public InMobiPluginClient ()
		{
		}


		//Initialize InMobi SDK using Publisher Account Id
		public void Init(string accountId){
			InMobiBinding.Init (accountId);
		}

		//Set log level
		public void SetLogLevel(string logLevel){
			InMobiBinding.SetLogLevel (logLevel);
		}
		//Add Id Type login/session
		public void AddIdType(string idType, string value){
			InMobiBinding.AddIdType (idType, value);
		}
		//Remove Id Type login/session
		public void RemoveIdType(string idType){
			InMobiBinding.RemoveIdType (idType);
		}

		//Set Age
		public void SetAge(int age){
			InMobiBinding.SetAge (age);
		}

		//Set AgeGroup
		public void SetAgeGroup(string ageGroup){
			InMobiBinding.SetAgeGroup (ageGroup);
		}

		//Set AreaCode
		public void SetAreaCode(string areaCode){
			InMobiBinding.SetAreaCode (areaCode);
		}

		//Set PostalCode
		public void SetPostalCode(string postalCode){
			InMobiBinding.SetPostalCode (postalCode);
		}

		//Set Location With City State Country
		public void SetLocationWithCityStateCountry(string city, string state, string country){
			InMobiBinding.SetLocationWithCityStateCountry (city, state, country);
		}

		//Set Year of Birth
		public void SetYearOfBirth(int yearOfBirth){
			InMobiBinding.SetYearOfBirth (yearOfBirth);
		}
	
		//Set Gender GENDER_MALE or GENDER_FEMALE
		public void SetGender(string gender){
			InMobiBinding.SetGender (gender);
		}

		//set Education EDUCATION_HIGHSCHOOLORLESS, EDUCATION_COLLEGEORGRADUATE, EDUCATION_POSTGRADUATEORABOVE
		public void SetEducation(string education){
			InMobiBinding.SetEducation (education);
		}

		//set Ethnicity ASIAN AFRICAN_AMERICAN CAUCASIAN HISPANIC OTHER
		public void SetEthnicity(string ethnicity){
			InMobiBinding.SetEthnicity (ethnicity);
		}

		//set Language
		public void SetLanguage(string language){
			InMobiBinding.SetLanguage (language);
		}

		//set Income
		public void SetIncome(int income){
			InMobiBinding.SetIncome (income);
		}

		//set HouseHold Income
		//BELOW_USD_5K
		//BETWEEN_USD_5K_AND_10K
		//BETWEEN_USD_10K_AND_15K
		//BETWEEN_USD_15K_AND_20K
		//BETWEEN_USD_20K_AND_25K
		//BETWEEN_USD_25K_AND_50K
		//BETWEEN_USD_50K_AND_75K
		//BETWEEN_USD_75K_AND_100K
		//BETWEEN_USD_100K_AND_150K
		//ABOVE_USD_150K
		public void SetHouseHoldIncome(string incomeLevel){
			InMobiBinding.SetHouseHoldIncome (incomeLevel);
		}

		//set Interests
		public void SetInterests(string interests){
			InMobiBinding.SetInterests (interests);
		}

		//set Nationality
		public void SetNationality(string nationality){
			InMobiBinding.SetNationality (nationality);
		}
	}
}
#endif

