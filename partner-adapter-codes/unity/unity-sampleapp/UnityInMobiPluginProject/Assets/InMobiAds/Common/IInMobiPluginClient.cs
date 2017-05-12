using System;
using UnityEngine;

namespace InMobiAds.Common
{
	internal interface IInMobiPluginClient
	{
		//Initialize InMobi SDK using Publisher Account Id
		void Init(string accountId);

		//Set log level
		void SetLogLevel(string logLevel);

		//Add Id Type login/session
		void AddIdType(string idType, string value);

		//Remove Id Type login/session
		void RemoveIdType(string idType);

		//Set Age
		void SetAge(int age);

		//Set AgeGroup
		void SetAgeGroup(string ageGroup);

		//Set AreaCode
		void SetAreaCode(string areaCode);

		//Set PostalCode
		void SetPostalCode(string postalCode);

		//Set Location With City State Country
		void SetLocationWithCityStateCountry(string city, string state, string country);

		//Set Year of Birth
		void SetYearOfBirth(int yearOfBirth);

		//Set Gender GENDER_MALE or GENDER_FEMALE
		void SetGender(string gender);

		//set Education EDUCATION_HIGHSCHOOLORLESS, EDUCATION_COLLEGEORGRADUATE, EDUCATION_POSTGRADUATEORABOVE
		void SetEducation(string education);

		//set Ethnicity ASIAN AFRICAN_AMERICAN CAUCASIAN HISPANIC OTHER
		void SetEthnicity(string ethnicity);

		//set Language
		void SetLanguage(string language);

		//set Income
		void SetIncome(int income);

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
		void SetHouseHoldIncome(string incomeLevel);

		//set Interests
		void SetInterests(string interests);

		//set Nationality
		void SetNationality(string nationality);

		//set Location
		//void SetLocation(
	}
}

