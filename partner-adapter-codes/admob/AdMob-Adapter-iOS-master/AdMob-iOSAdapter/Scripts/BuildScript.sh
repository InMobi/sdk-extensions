#!/bin/sh

#############################################################################
## Copyright (c) 2013 InMobi. All rights reserved ###########################
#############################################################################

set -o errexit
set -o nounset
set -o xtrace

if [ $# -lt 1 ]; then
    echo "___SYNTAX_ERROR___ $0 <libraryName.a>";
    exit 1;
fi
__FINAL_LIBRARY_NAME=$1
echo "__FINAL_LIBRARY_NAME = ${__FINAL_LIBRARY_NAME}"

if [ -z "${LIBRARY_TARGET_NAME}" -o -z "${LIBRARY_DESTINATION_DIR}" ]; then
    echo '"LIBRARY_TARGET_NAME" or "LIBRARY_DESTINATION_DIR" not defined.';
    exit 2;
fi

#############################################################################
## COMPILE AND BUILD BINARIES FOR ALL ARCHITECHTURES ########################
#############################################################################

xcodebuild_arch() {
  xcodebuild \
    -target "${LIBRARY_TARGET_NAME}" \
    -configuration "${CONFIGURATION}" \
    -sdk $1 \
    ARCHS="$2" \
    IPHONEOS_DEPLOYMENT_TARGET="$3" \
    CACHE_ROOT="${CACHE_ROOT}" \
    OBJROOT="${OBJROOT}/$2" \
    SYMROOT="${SYMROOT}/$2" \
    EFFECTIVE_PLATFORM_NAME=
  echo "========>>>>>> xcodebuild done <<<<<<<=========="
}

cd "${SRCROOT}"
xcodebuild_arch iphoneos armv7 6.0
xcodebuild_arch iphoneos armv7s 6.0
xcodebuild_arch iphonesimulator i386 6.0
xcodebuild_arch iphoneos arm64 7.0
xcodebuild_arch iphonesimulator x86_64 7.0

#############################################################################
## USE LIPO TO CREATE A UNIVERSAL BINARY ####################################
#############################################################################

## Final lipo library location.
lib_name="lib${LIBRARY_TARGET_NAME}.a"
lib_dir="${BUILD_ROOT}/${CONFIGURATION}-Lib"

## Clean up the existing lipo library location.
rm -rf "${lib_dir}/${lib_name}"
mkdir -p "$lib_dir"

## Library expected location format:
######## (Project)/build/(ARCHS)/(CONFIG)/lib(LIBRARY_BASE_NAME).a
armv7_lib="${BUILD_ROOT}/armv7/${CONFIGURATION}/${lib_name}"
armv7s_lib="${BUILD_ROOT}/armv7s/${CONFIGURATION}/${lib_name}"
i386_lib="${BUILD_ROOT}/i386/${CONFIGURATION}/${lib_name}"
arm64_lib="${BUILD_ROOT}/arm64/${CONFIGURATION}/${lib_name}"
x86_64_lib="${BUILD_ROOT}/x86_64/${CONFIGURATION}/${lib_name}"

## Use lipo to create the universal binary.
lipo -create "$i386_lib" "$armv7_lib" "$armv7s_lib"  "$arm64_lib" "$x86_64_lib" -output "${lib_dir}/${lib_name}"

## Clean up the destination dir.
rm -f "${LIBRARY_DESTINATION_DIR}/${lib_name}"
mkdir -p "${LIBRARY_DESTINATION_DIR}"

## Copy the created lib to the destination.
final_lib="${LIBRARY_DESTINATION_DIR}/${__FINAL_LIBRARY_NAME}"
cp "${lib_dir}/${lib_name}" "${final_lib}"

exit 0;
#############################################################################