#!/bin/sh

#############################################################################
## Copyright (c) 2013 InMobi. All rights reserved ###########################
#############################################################################

set -o errexit
set -o nounset
set -o xtrace

#############################################################################
## Bundle the SDK ###########################################################
#############################################################################

## Current timestamp
timestamp=$(date "+%Y-%m-%d.%H.%M.%S")

## Move to the Library directory.
cd "${LIBRARY_DESTINATION_DIR}";

## Bundle destination directory
package_dir="${PRODUCT_NAME}"

## Remove existing package_dir
if [ -d "$package_dir" ]; then
chmod -R a+w "${package_dir}"
rm -rf "${package_dir}"
fi

## Create package_dir
mkdir -p "${package_dir}"

## Move header and library files to package_dir.
mv ./*.[ah] "${package_dir}"/

## Make header and library files in package_dir readonly.
chmod a-w "${package_dir}"/*.[ah]

## Move docs to package_dir.
if [ -d ./Docs ]; then
mv ./Docs "${package_dir}"/
fi

rm -f "${package_dir}.zip"
/usr/bin/zip -r -X "${package_dir}.zip" "${package_dir}"

## Remove package_dir
chmod -R a+w "${package_dir}"/*
rm -rf "$package_dir"

exit 0;
#############################################################################