#!/bin/bash

#if [ $# -ne 1 ]; then
#	echo "Usage: ./generateAdapterLibrary.sh <SDK library URL>"
#	exit 1
#fi
#echo "SDK library url is: $1"
#echo "Downloading the library .."
#wget $1

export ANDROID_HOME=/Users/niranjan.agrawal/Library/Android/sdk
export ANDROID_SDK=/Users/niranjan.agrawal/Library/Android/sdk
export PATH=$PATH:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/munki:/Users/niranjan.agrawal/Downloads/apache-jmeter-2.13/bin:/Users/niranjan.agrawal/Downloads/apache-maven-3.3.9/bin:/Users/niranjan.agrawal/Library/Android/sdk:/Users/niranjan.agrawal/Library/Android/sdk/platform-tools/:/Library/PostgreSQL/9.5/bin:/Users/niranjan.agrawal/Library/Android/sdk/tools:/Users/niranjan.agrawal/Dev/GitCode/AndroidViewClient/

wget https://artifactory.corp.inmobi.com/artifactory/snapshots/com/inmobi/monetization/InMobiAds/6.0.0-SNAPSHOT/InMobiAds-6.0.0-20161102.060351-23.zip

mkdir /tmp/adapterlibtemp/
for i in `ls *.zip`
do
	echo $i
	unzip -d /tmp/adapterlibtemp/ $i
done
for i in `ls /tmp/adapterlibtemp/*.zip`
do
	unzip -d /tmp/adapterlibtemp/ $i
done
for i in `ls /tmp/adapterlibtemp/`
do
	echo $i
	if [ -d $i ]
	then
		cp /tmp/adapterlibtemp/$i/libs/*.jar ./AdMob-AndroidAdapter/app/libs/
		cp /tmp/adapterlibtemp/$i/libs/*.jar ./AdMob-AndroidTestApp/app/libs/
	fi
done
rm -Rf /tmp/adapterlibtemp/
rm -Rf InMobiAds*.zip
cd AdMob-AndroidAdapter
gradle clean build
#./gradlew clean
#./gradlew build

#echo "=========================="
#echo "Building the sample app!!"
#cp ./AdMob-AndroidAdapter/app/build/intermediates/bundles/release/classes.jar ./AdMob-AndroidTestApp/app/libs/
#cd ./AdMob-AndroidTestApp
#./AdMob-AndroidAdapter/gradlew clean
#./AdMob-AndroidAdapter/gradlew build
#echo "========================="
#echo "Installing the sample app on the device"
#adb -d shell pm list packages | grep -s "com.google.ads.mediation.testapp"
#if [ $? -eq 0 ]
#then
#	echo "App already exists! Uninstalling the app"
#	adb uninstall com.google.ads.mediation.testapp
#fi
#adb install app/build/outputs/apk/app-debug.apk
