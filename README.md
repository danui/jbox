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
