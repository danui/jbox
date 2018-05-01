# A simple encrypt/decrypt program

## Encrypt File

```
$ java -Djbox.op=encrypt -Djbox.in=secret.txt -Djbox.out=ciphertext -Djbox.password.file=~/.jbox/password -jar jbox.jar
```

## Decrypt File

```
$ java -Djbox.op=decrypt -Djbox.in=ciphertext -Djbox.out=decrypted.txt -Djbox.password.file=~/.jbox/password -jar jbox.jar
```

## Verify

```
$ diff secret.txt decrypted.txt
```

## Scripts

The scripts are for setting up a vault directory.

1. Create new directory to house vaults.
2. Copy scripts to the directory.
3. Copy jbox.jar to the directory.

A vault directory is a normal directory that contains files we wish to
keep private.

This will transform `myvault` directory into `myvault.encrypted` and
then delete `myvault`.

```
$ ./jbox-pack.sh myvault
```

This will decrypt `myvault.encrypted` into the `myvault` directory.  It
will leave the `myvault.encrypted` file in tact.

```
$ ./jbox-unpack.sh myvault
```

If we wish only to encrypt `myvault`, for example so that we can upload
to Google Drive, then we can use the following. It is like packing, but
without removing the `myvault` directory.

```
$ ./jbox-encrypt.sh myvault
```
