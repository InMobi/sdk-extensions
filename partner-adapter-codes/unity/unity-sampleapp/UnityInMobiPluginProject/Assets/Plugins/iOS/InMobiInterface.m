//
//  InMobiInterface.m
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

#import <Foundation/Foundation.h>
#import "InMobiTypes.h"
#import "InMobiBanner.h"
#include "InMobiObjectCache.h"
#include "InMobiInterstitial.h"

/// Returns an NSString copying the characters from |bytes|, a C array of UTF8-encoded bytes.
/// Returns nil if |bytes| is NULL.
static NSString *InMobiStringFromUTF8String(const char *bytes) { return bytes ? @(bytes) : nil; }


/// Returns a C string from a C array of UTF8-encoded bytes.
static const char *cStringCopy(const char *string) {
    if (!string) {
        return NULL;
    }
    char *res = (char *)malloc(strlen(string) + 1);
    strcpy(res, string);
    return res;
}

/// Returns a C string from a C array of UTF8-encoded bytes.
static const char **cStringArrayCopy(NSArray *array) {
    if (array == nil) {
        return nil;
    }
    
    const char **stringArray;
    
    stringArray = malloc(array.count * sizeof(char *));
    for (int i = 0; i < array.count; i++) {
        stringArray[i] = cStringCopy([array[i] UTF8String]);
    }
    return stringArray;
}

#pragma InMobi SDK APIs
void Init(const char* accountId){
    NSLog(@"InMobi SDK initialized called");
    [IMSdk initWithAccountID:InMobiStringFromUTF8String(accountId)];
}

void SetLogLevel(const char* logLevel){
    NSString *level = InMobiStringFromUTF8String(logLevel);
    if(level){
        if([level isEqualToString:@"None"]){
            [IMSdk setLogLevel:kIMSDKLogLevelNone];
        }
        else if ([level isEqualToString:@"Debug"]){
            [IMSdk setLogLevel:kIMSDKLogLevelDebug];
        }
        else if ([level isEqualToString:@"Error"]){
            [IMSdk setLogLevel:kIMSDKLogLevelError];
        }
    }
}

void AddIdType(const char* idType, const char* value){
    NSString *idTypeString = InMobiStringFromUTF8String(idType);
    NSString *valueString = InMobiStringFromUTF8String(value);
    if([idTypeString isEqualToString:@"LOGIN"]){
        [IMSdk addId:valueString forType:kIMSDKIdTypeLogin];
    }
    else if ([idTypeString isEqualToString:@"SESSION"]){
        [IMSdk addId:valueString forType:kIMSDKIdTypeSession];
    }
}

void RemoveIdType(const char* idType){
    NSString *idTypeString = InMobiStringFromUTF8String(idType);
    if([idTypeString isEqualToString:@"LOGIN"]){
        [IMSdk removeIdType:kIMSDKIdTypeLogin];
    }
    else if ([idTypeString isEqualToString:@"SESSION"]){
        [IMSdk removeIdType:kIMSDKIdTypeSession];
    }
}

void SetAge(int age){
    [IMSdk setAge:age];
}

void SetAgeGroup(const char* ageGroup){
    NSString *ageGroupType = InMobiStringFromUTF8String(ageGroup);
    if (ageGroupType) {
        if ([ageGroupType isEqualToString:@"Below18"]) {
            [IMSdk setAgeGroup:kIMSDKAgeGroupBelow18];
        }
        else if ([ageGroupType isEqualToString:@"Between18And20"]) {
            [IMSdk setAgeGroup:kIMSDKAgeGroupBetween18And20];
        }
        else if ([ageGroupType isEqualToString:@"Between21And24"]){
            [IMSdk setAgeGroup:kIMSDKAgeGroupBetween21And24];
        }
        else if ([ageGroupType isEqualToString:@"Between25And34"]){
            [IMSdk setAgeGroup:kIMSDKAgeGroupBetween25And34];
        }
        else if ([ageGroupType isEqualToString:@"Between35To54"]){
            [IMSdk setAgeGroup:kIMSDKAgeGroupBetween35And54];
        }
        else if ([ageGroupType isEqualToString:@"Above55"]){
            [IMSdk setAgeGroup:kIMSDKAgeGroupAbove55];
        }
    }
}

void SetAreaCode(const char* areaCode){
    [IMSdk setAreaCode:InMobiStringFromUTF8String(areaCode)];
}

void SetPostalCode(const char* postalCode){
    [IMSdk setPostalCode:InMobiStringFromUTF8String(postalCode)];
}

void SetLocationWithCityStateCountry(const char* city, const char* state, const char* country){
    [IMSdk setLocationWithCity:InMobiStringFromUTF8String(city) state:InMobiStringFromUTF8String(state) country:InMobiStringFromUTF8String(country)];
}

void SetYearOfBirth(int yearOfBirth){
    [IMSdk setYearOfBirth:(NSInteger)yearOfBirth];
}

void SetGender(const char* gender){
    NSString* genderType = InMobiStringFromUTF8String(gender);
    if (genderType) {
        if ([genderType isEqualToString:@"GENDER_FEMALE"]) {
            [IMSdk setGender:kIMSDKGenderFemale];
        } else if ([genderType isEqualToString:@"GENDER_MALE"]) {
            [IMSdk setGender:kIMSDKGenderMale];
        }
    }
}

void SetEthnicity(const char* ethnicityType){
    NSString* ethnicity = InMobiStringFromUTF8String(ethnicityType);
    if (ethnicity) {
        if ([ethnicity isEqualToString:@"ASIAN"]) {
            [IMSdk setEthnicity:kIMSDKEthnicityAsian];
        } else if ([ethnicity isEqualToString:@"HISPANIC"]) {
            [IMSdk setEthnicity:kIMSDKEthnicityHispanic];
        } else if ([ethnicity isEqualToString:@"AFRICAN_AMERICAN"]) {
            [IMSdk setEthnicity:kIMSDKEthnicityAfricanAmerican];
        } else if ([ethnicity isEqualToString:@"CAUCASIAN"]) {
            [IMSdk setEthnicity:kIMSDKEthnicityCaucasian];
        } else if ([ethnicity isEqualToString:@"OTHER"]) {
            [IMSdk setEthnicity:kIMSDKEthnicityOther];
        }
    }
}

void SetEducation(const char* educationType){
    NSString* education = InMobiStringFromUTF8String(educationType);
    if (education) {
        if ([education isEqualToString:@"EDUCATION_HIGHSCHOOLORLESS"]) {
            [IMSdk setEducation:kIMSDKEducationHighSchoolOrLess];
        } else if ([education isEqualToString:@"EDUCATION_COLLEGEORGRADUATE"]) {
            [IMSdk setEducation:kIMSDKEducationCollegeOrGraduate];
        } else if ([education isEqualToString:@"EDUCATION_POSTGRADUATEORABOVE"]) {
            [IMSdk setEducation:kIMSDKEducationPostGraduateOrAbove];
        }
    }
}

void SetLanguage(const char* language){
    [IMSdk setLanguage:InMobiStringFromUTF8String(language)];
}

void SetIncome(int income){
    [IMSdk setIncome:income];
}

void SetHouseHoldIncome(const char* incomeLevel){
    NSString *houseHoldIncome = InMobiStringFromUTF8String(incomeLevel);
    if (houseHoldIncome) {
        if ([houseHoldIncome isEqualToString:@"BELOW_USD_5K"]) {
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBelow5kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_5K_AND_10K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetweek5kAnd10kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_10K_AND_15K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween10kAnd15kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_15K_AND_20K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween15kAnd20kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_20K_AND_25K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween20kAnd25kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_25K_AND_50K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween25kAnd50kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_50K_AND_75K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween50kAnd75kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_75K_AND_100K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween75kAnd100kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"BETWEEN_USD_100K_AND_150K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeBetween100kAnd150kUSD];
        }
        else if ([houseHoldIncome isEqualToString:@"ABOVE_USD_150K"]){
            [IMSdk setHouseholdIncome:kIMSDKHouseholdIncomeAbove150kUSD];
        }
    }
}

void SetInterests(const char* interests){
    [IMSdk setInterests:InMobiStringFromUTF8String(interests)];
}

void SetNationality(const char* nationality){
    [IMSdk setNationality:InMobiStringFromUTF8String(nationality)];
}

#pragma End of InMobi SDK APIS

#pragma Start InMobiBanner APIs
/// Creates a GADBannerView with the specified width, height, and position. Returns a reference to
/// the GADUBannerView.
InMobiBannerRef InMobiCreateBannerAd(InMobiBannerClientRef *bannerClient, const char *placementId,
                                     int width, int height, int position) {
    InMobiBanner *banner = [[InMobiBanner alloc] initBannerAd:bannerClient
                                                  placementId:InMobiStringFromUTF8String(placementId)
                                                        width:width
                                                       height:height
                                                     position:position];
    
    InMobiObjectCache *cache = [InMobiObjectCache sharedInstance];
    [cache.references setObject:banner forKey:[banner inmobi_referenceKey]];
    return (__bridge InMobiBannerRef)banner;
}

void SetBannerKeyWords(InMobiBannerRef banner, const char *keywords){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    [internalBanner setKeywords:InMobiStringFromUTF8String(keywords)];
}

void SetBannerEnableAutoRefresh(InMobiBannerRef banner, bool flag){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    [internalBanner setEnableAutoRefresh:flag];
}

void SetBannerRefreshInterval(InMobiBannerRef banner, int refreshInterval){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    [internalBanner setRefreshInterval:refreshInterval];
}

void LoadBannerAd(InMobiBannerRef banner){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    [internalBanner loadAd];
}

void DestroyBannerAd(InMobiBannerRef banner){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    [internalBanner destroy];
}

///Sets the banner callbacks to be invoked durin banner ad events
void SetBannerCallbacks(InMobiBannerRef banner,
                        InMobiBannerOnAdLoadSucceeded onAdLoadSucceeded,
                        InMobiBannerOnAdLoadFailed onAdLoadFailed,
                        InMobiBannerOnAdDisplayed onAdDisplayed,
                        InMobiBannerOnAdDismissed onAdDismissed,
                        InMobiBannerOnAdInteraction onAdInteraction,
                        InMobiBannerOnUserLeftApplication onUserLeftApplication,
                        InMobiBannerOnAdRewardActionCompleted onAdRewardActionCompleted){
    InMobiBanner *internalBanner = (__bridge InMobiBanner *)banner;
    internalBanner.onAdLoadSucceeded = onAdLoadSucceeded ;
    internalBanner.onAdLoadFailed = onAdLoadFailed;
    internalBanner.onAdDisplayed = onAdDisplayed;
    internalBanner.onAdDismissed = onAdDismissed;
    internalBanner.onAdInteraction = onAdInteraction;
    internalBanner.onUserLeftApplication = onUserLeftApplication;
    internalBanner.onAdRewardActionCompleted = onAdRewardActionCompleted;
}
#pragma End InMobiBanner APIs

#pragma InmobiInterstitial APIs
InMobiInterstitialRef InMobiCreateInterstitialAd(InMobiInterstitialClientRef *interstitialClient,
                                                 const char* placementId)
{
    InMobiInterstitial *interstital = [[InMobiInterstitial alloc] initInterstitialAd:interstitialClient
                                                                         placementId:InMobiStringFromUTF8String(placementId)];
    InMobiObjectCache *cache = [InMobiObjectCache sharedInstance];
    [cache.references setObject:interstital forKey:[interstital inmobi_referenceKey]];
    return (__bridge InMobiInterstitialRef)interstital;
}

void SetInterstitialKeyWords(InMobiInterstitialRef interstitial, const char *keywords){
    InMobiInterstitial *internalInterstitial = (__bridge InMobiInterstitial *)interstitial;
    [internalInterstitial setKeywords:InMobiStringFromUTF8String(keywords)];
}

void LoadInterstitialAd(InMobiInterstitialRef interstitial){
    InMobiInterstitial *internalInterstitial = (__bridge InMobiInterstitial *)interstitial;
    [internalInterstitial loadAd];
}

bool IsInterstitialAdReady(InMobiInterstitialRef interstitial)
{
    InMobiInterstitial *internalInterstitial = (__bridge InMobiInterstitial *)interstitial;
    return [internalInterstitial isReady];
}

void ShowInterstitialAd(InMobiInterstitialRef interstitial){
    InMobiInterstitial *internalInterstitial = (__bridge InMobiInterstitial *)interstitial;
    [internalInterstitial showAd];
}

///Sets the banner callbacks to be invoked durin banner ad events
void SetInterstitialCallbacks(InMobiInterstitialRef interstitial,
                              InMobiInterstitialOnAdReceived onAdReceived,
                              InMobiInterstitialOnAdLoadSucceeded onAdLoadSucceeded,
                              InMobiInterstitialOnAdLoadFailed onAdLoadFailed,
                              InMobiInterstitialOnAdDisplayFailed onAdDisplayFailed,
                              InMobiInterstitialWillDisplay onAdWillDisplay,
                              InMobiInterstitialOnAdDisplayed onAdDisplayed,
                              InMobiInterstitialOnAdDismissed onAdDismissed,
                              InMobiInterstitialOnAdInteraction onAdInteraction,
                              InMobiInterstitialOnUserLeftApplication onUserLeftApplication,
                              InMobiInterstitialOnAdRewardActionCompleted onAdRewardActionCompleted){
    InMobiInterstitial *internalInterstitial = (__bridge InMobiInterstitial *)interstitial;
    internalInterstitial.onAdReceived = onAdReceived;
    internalInterstitial.onAdLoadSucceeded = onAdLoadSucceeded ;
    internalInterstitial.onAdLoadFailed = onAdLoadFailed;
    internalInterstitial.onAdDisplayFailed = onAdDisplayFailed;
    internalInterstitial.onAdWillDisplay = onAdWillDisplay;
    internalInterstitial.onAdDisplayed = onAdDisplayed;
    internalInterstitial.onAdDismissed = onAdDismissed;
    internalInterstitial.onAdInteraction = onAdInteraction;
    internalInterstitial.onUserLeftApplication = onUserLeftApplication;
    internalInterstitial.onAdRewardActionCompleted = onAdRewardActionCompleted;
}

#pragma End InMobiInterstitial APIs

void InMobiRelease(InMobiRef ref){
    if(ref){
        InMobiObjectCache *cache = [InMobiObjectCache sharedInstance];
        [cache.references removeObjectForKey:[(__bridge NSObject *)ref inmobi_referenceKey]];
    }
}
