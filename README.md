# A simple encrypt/decrypt program

## Install

There's no install. Build the JAR using `mvn package` and use the
`target/jobx.jar` file. You can move it anywhere you like.

## Usage Examples

### Help

Run the JAR without options to see a help menu.

```
$ java -jar jbox.jar
Unknown jbox.op: null
Commands and args are specified via system properties:

  jbox.op=?            Specify 'encrypt' or 'decrypt'.
  jbox.in=?            Read input from this file. Default is to read from stdin.
  jbox.out=?           Write output to this file. Default is to write to stdout.
  jbox.password=?      Use this password to derive a secret key.
  jbox.password.file=? Read password from this file

You can safely pass in password using 'read -s' in bash.

    -Djbox.password=$(echo -n "Password: "; read -s x; echo $x)
```

### Encrypt a file

```
$ java -Djbox.op=encrypt -Djbox.in=secret.txt -Djbox.out=ciphertext -Djbox.password.file=~/.jbox/password -jar jbox.jar
```

### Decrypt File

```
$ java -Djbox.op=decrypt -Djbox.in=ciphertext -Djbox.out=decrypted.txt -Djbox.password.file=~/.jbox/password -jar jbox.jar
```

### Verify

```
$ diff secret.txt decrypted.txt
```

## Scripts

The scripts are for setting up a directory for vaults.

1. Create new directory to house vaults.
2. Copy scripts to the directory.
3. Copy jbox.jar to the directory.

Each vault is a normal directory that contains files we wish to keep
private.

This will transform `myvault` directory into `myvault.encrypted` and
then delete `myvault`.

```
$ ./jbox-pack.sh myvault
```

This will decrypt `myvault.encrypted` into the `myvault` directory.  It
will leave the `myvault.encrypted` file intact.

```
$ ./jbox-unpack.sh myvault
```

If we wish only to encrypt `myvault`, for example so that we can upload
to Google Drive, then we can use the following. It is like packing, but
without removing the `myvault` directory.

```
$ ./jbox-encrypt.sh myvault
```
