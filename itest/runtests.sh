#!/bin/bash

if [ "verbose" == "$1" ]; then
    set -e -x
else
    set -e
fi

DIR=$(cd $(dirname $0) && pwd)

function cleanup {
    rm -f $DIR/tmp.$$.*
}
trap cleanup EXIT

JAR="$DIR/../target/jbox.jar"
if [ ! -f $JAR ]; then
    echo "Cannot find $JAR"
    exit 1
fi

PLAINTEXT=$DIR/tmp.$$.plaintext
CIPHERTEXT=$DIR/tmp.$$.ciphertext
DECRYPTED=$DIR/tmp.$$.decrypted

# Test 01 - Specify file names via system properties
dd if=/dev/urandom of=$PLAINTEXT bs=1024 count=128 2> /dev/null
java -Djbox.password=abcd1234 -Djbox.in=$PLAINTEXT -Djbox.out=$CIPHERTEXT -Djbox.op=encrypt -jar $JAR
java -Djbox.password=abcd1234 -Djbox.in=$CIPHERTEXT -Djbox.out=$DECRYPTED -Djbox.op=decrypt -jar $JAR
if diff $PLAINTEXT $DECRYPTED; then
    echo "PASS Test 01"
else
    echo "FAIL Test 01"
fi

# Test 02 - Stream from stdin and out to stdout
dd if=/dev/urandom of=$PLAINTEXT bs=1024 count=128 2> /dev/null
java -Djbox.password=abcd1234 -Djbox.op=encrypt -jar $JAR < $PLAINTEXT > $CIPHERTEXT
java -Djbox.password=abcd1234 -Djbox.op=decrypt -jar $JAR < $CIPHERTEXT > $DECRYPTED
if diff $PLAINTEXT $DECRYPTED; then
    echo "PASS Test 02"
else
    echo "FAIL Test 02"
fi
