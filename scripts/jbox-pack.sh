#!/bin/bash

# Encrypt the vault directory into vault.encrypted and then REMOVE the
# vault directory.

set -e

VAULT=$1

if [[ -z "$VAULT" ]]; then
    echo "No vault specified"
    echo "$0 <vault>"
    exit 1
fi

echo "Creating tgz"
tar zcf ${VAULT}.$$.tmp.tgz ${VAULT}

echo -n 'Password: '
read -s password
echo

echo "Encrypting"
java -Djbox.op=encrypt -Djbox.in=${VAULT}.$$.tmp.tgz -Djbox.out=${VAULT}.encrypted -Djbox.password=$password -jar jbox.jar

echo "Cleaning up"
rm -f ${VAULT}.$$.tmp.tgz

echo -n 'Repeat Password: '
read -s repeated_password
echo

if [[ "$password" != "$repeated_password" ]]; then
    echo "Passwords do not match. Will not remove ${VAULT}"
else
    echo "Removing ${VAULT}"
    rm -rf ${VAULT}
fi
