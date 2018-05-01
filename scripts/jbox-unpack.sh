#!/bin/bash

# Decrypt vault.encrypted into the vault directory.

set -e

VAULT=$1

if [[ -z "$VAULT" ]]; then
    echo "No vault specified"
    echo "$0 <vault>"
    exit 1
fi

if [[ -e "${VAULT}" ]]; then
    echo "The ${VAULT} directory already exists"
    exit 1
fi

# Read Password
echo -n 'Password: '
read -s password
echo

echo "Decrypting ${VAULT}.encrypted"
java -Djbox.op=decrypt -Djbox.in=${VAULT}.encrypted -Djbox.out=${VAULT}.$$.tmp.tgz -Djbox.password=$password -jar jbox.jar

echo "Expanding tgz"
tar zxf ${VAULT}.$$.tmp.tgz

echo "Removing tgz"
rm -f ${VAULT}.$$.tmp.tgz
