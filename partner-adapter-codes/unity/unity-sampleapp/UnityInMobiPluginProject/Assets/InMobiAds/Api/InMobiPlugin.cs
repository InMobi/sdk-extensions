using System;
using InMobiAds;
using InMobiAds.Common;

namespace InMobiAds.Api
{
	public enum InMobiAdPosition
	{
		TopLeft,
		TopCenter,
		TopRight,
		Centered,
		BottomLeft,
		BottomCenter,
		BottomRight
	}

	public class InMobiPlugin
	{
		private IInMobiPluginClient inmobiPluginClient;
		public InMobiPlugin (string accountId)
		{
			inmobiPluginClient = InMobiAdsClientFactory.BuildInMobiPluginClient ();
			inmobiPluginClient.Init (accountId);
		}

		//Set log level
		public void SetLogLevel(string logLevel){
			inmobiPluginClient.SetLogLevel (logLevel);
		}

		//Add Id Type login/session
		public void AddIdType(string idType, string value){
			inmobiPluginClient.AddIdType (idType, value);
		}

		//Remove Id Type login/session
		public void RemoveIdType(string idType){
			inmobiPluginClient.RemoveIdType (idType);
		}

		//Set Age
		public void SetAge(int age){
			inmobiPluginClient.SetAge (age);
		}

		//Set AgeGroup
		public void SetAgeGroup(string ageGroup){
			inmobiPluginClient.SetAgeGroup (ageGroup);
		}

		//Set AreaCode
		void SetAreaCode(string areaCode){
			inmobiPluginClient.SetAreaCode(areaCode);
		}

		//Set PostalCode
		public void SetPostalCode(string postalCode){
			inmobiPluginClient.SetPostalCode (postalCode);
		}

		//Set Location With City State Country
		public void SetLocationWithCityStateCountry(string city, string state, string country){
			inmobiPluginClient.SetLocationWithCityStateCountry (city, state, country);
		}

		//Set Year of Birth
		public void SetYearOfBirth(int yearOfBirth){
			inmobiPluginClient.SetYearOfBirth (yearOfBirth);
		}

		//Set Gender GENDER_MALE or GENDER_FEMALE
		public void SetGender(string gender){
			inmobiPluginClient.SetGender (gender);
		}

		//set Education EDUCATION_HIGHSCHOOLORLESS, EDUCATION_COLLEGEORGRADUATE, EDUCATION_POSTGRADUATEORABOVE
		public void SetEducation(string education){
			inmobiPluginClient.SetEducation (education);
		}

		//set Ethnicity ASIAN AFRICAN_AMERICAN CAUCASIAN HISPANIC OTHER
		public void SetEthnicity(string ethnicity){
			inmobiPluginClient.SetEthnicity (ethnicity);
		}

		//set Language
		public void SetLanguage(string language){
			inmobiPluginClient.SetLanguage (language);
		}

		//set Income
		public void SetIncome(int income){
			inmobiPluginClient.SetIncome (income);
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
			inmobiPluginClient.SetHouseHoldIncome (incomeLevel);
		}

		//set Interests
		public void SetInterests(string interests){
			inmobiPluginClient.SetInterests (interests);
		}

		//set Nationality
		public void SetNationality(string nationality){
			inmobiPluginClient.SetNationality (nationality);
		}
	}
}

