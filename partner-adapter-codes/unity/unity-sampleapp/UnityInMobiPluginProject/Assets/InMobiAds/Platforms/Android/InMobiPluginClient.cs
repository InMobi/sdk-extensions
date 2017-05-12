#if UNITY_ANDROID
using System;
using System.Collections.Generic;
using UnityEngine;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds.Platforms.Android
{
	internal class InMobiPluginClient : AndroidJavaProxy, IInMobiPluginClient
	{
		private AndroidJavaObject inmobiPlugin;
		public InMobiPluginClient () : base(Utils.InMobiPluginDummyListener)
		{
			this.inmobiPlugin = new AndroidJavaObject (Utils.InMobiPluginClassName);
		}

		//Initialize InMobi SDK using Publisher Account Id
		public void Init(string accountId){
			this.inmobiPlugin.Call ("init", new object[1]{ accountId });
		}

		//Set log level
		public void SetLogLevel(string logLevel){
			this.inmobiPlugin.Call ("setLogLevel", new object[1]{ logLevel });
		}

		//Add Id Type login/session
		public void AddIdType(string idType, string value){
			this.inmobiPlugin.Call ("addIdType", new object[2]{ idType, value });
		}

		//Remove Id Type login/session
		public void RemoveIdType(string idType){
			this.inmobiPlugin.Call ("removeIdType", new object[1]{ idType });
		}

		//Set Age
		public void SetAge(int age){
			this.inmobiPlugin.Call ("setAge", new object[1]{ age });
		}

		//Set AgeGroup
		public void SetAgeGroup(string ageGroup){
			this.inmobiPlugin.Call ("setAgeGroup", new object[1]{ ageGroup });
		}

		//Set AreaCode
		public void SetAreaCode(string areaCode){
			this.inmobiPlugin.Call ("setAreaCode", new object[1]{ areaCode });
		}

		//Set PostalCode
		public void SetPostalCode(string postalCode){
			this.inmobiPlugin.Call ("setPostalCode", new object[1]{ postalCode });
		}

		//Set Location With City State Country
		public void SetLocationWithCityStateCountry(string city, string state, string country){
			this.inmobiPlugin.Call ("setLocationWithCityStateCountry", new object[3]{ city, state, country });
		}

		//Set Year of Birth
		public void SetYearOfBirth(int yearOfBirth){
			this.inmobiPlugin.Call ("setYearOfBirth", new object[1]{ yearOfBirth });
		}

		//Set Gender GENDER_MALE or GENDER_FEMALE
		public void SetGender(string gender){
			this.inmobiPlugin.Call ("setGender", new object[1]{ gender });
		}

		//set Education EDUCATION_HIGHSCHOOLORLESS, EDUCATION_COLLEGEORGRADUATE, EDUCATION_POSTGRADUATEORABOVE
		public void SetEducation(string education){
			this.inmobiPlugin.Call ("setEducation", new object[1]{ education });
		}
		
		//set Ethnicity ASIAN AFRICAN_AMERICAN CAUCASIAN HISPANIC OTHER
		public void SetEthnicity(string ethnicity){
			this.inmobiPlugin.Call ("setEthnicity", new object[1]{ ethnicity });
		}
		
		//set Language
		public void SetLanguage(string language){
			this.inmobiPlugin.Call ("setLanguage", new object[1]{ language });
		}
		
		//set Income
		public void SetIncome(int income){
			this.inmobiPlugin.Call ("setIncome", new object[1]{ income });
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
			this.inmobiPlugin.Call ("setHouseHoldIncome", new object[1]{ incomeLevel });
		}
		
		//set Interests
		public void SetInterests(string interests){
			this.inmobiPlugin.Call ("setInterests", new object[1]{ interests });
		}
		
		//set Nationality
		public void SetNationality(string nationality){
			this.inmobiPlugin.Call ("setNationality", new object[1]{ nationality });
		}
	}
}
#endif

