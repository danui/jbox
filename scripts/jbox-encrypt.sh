#!/bin/bash

# Encrypt vault directory into vault.encrypted but do not remove the
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

echo -n 'Repeat Password: '
read -s repeated_password
echo

if [[ "$password" != "$repeated_password" ]]; then
    echo "Passwords do not match."
    exit 1
fi

echo "Encrypting"
java -Djbox.op=encrypt -Djbox.in=${VAULT}.$$.tmp.tgz -Djbox.out=${VAULT}.encrypted -Djbox.password=$password -jar jbox.jar

echo "Removing tgz"
rm -f ${VAULT}.$$.tmp.tgz
