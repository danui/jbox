package com.danui.jbox;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;

/**
 * Cryptographic operations
 *
 * @author Jin
 */
public class Crypto {

    private static final byte MAGIC_BYTE = (byte)0x88;
    
    /**
     * Encrypt input stream using a password into an output stream.
     *
     * @param inputStream Data to encrypt.
     *
     * @param outputStream Where to write encryption parameters and
     * cipher text.
     *
     * @param password Password for the key derivation function.
     *
     * Default key derivation function and parameters would be used.
     */
    public void encryptFileWithPassword(
        InputStream inputStream,
        OutputStream outputStream,
        char[] password)
        throws IOException, NoSuchAlgorithmException,
        InvalidKeySpecException, InvalidKeyException,
        NoSuchPaddingException {

        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[16];
        rng.nextBytes(salt);
        int iterCount = 100000;
        int keyLength = 128;
        String kdfAlgo = "PBKDF2WithHmacSHA1";
        String cipherSuite = "AES/ECB/PKCS5Padding";
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(kdfAlgo);
        PBEKey key = (PBEKey)factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(cipherSuite);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Write header magic byte and KDF parameters.
        DataOutputStream dataOut = new DataOutputStream(outputStream);
        dataOut.writeByte(MAGIC_BYTE);
        dataOut.writeInt(salt.length);
        dataOut.write(salt);
        dataOut.writeInt(iterCount);
        dataOut.writeInt(keyLength);
        dataOut.writeUTF(kdfAlgo);
        dataOut.writeUTF(cipherSuite);

        // Write ciphertext.
        CipherOutputStream cipherOut = new CipherOutputStream(dataOut, cipher);
        pump(inputStream, cipherOut);
        cipherOut.flush();
    }


    /**
     * Decrypt input stream and write plain text into output stream.
     *
     * @param inputStream Input stream that contains encryption
     * parameters and cipher text.
     *
     * @param outputStream Where plain text would be written.
     *
     * @param password Password for the key derivation function.
     *
     * Key derivation function parameters would be read from the input
     * stream.
     */
    public void decryptFileWithPassword(
        InputStream inputStream,
        OutputStream outputStream,
        char[] password)
        throws Exception {

        DataInputStream dataIn = new DataInputStream(inputStream);
        byte magic = dataIn.readByte();
        if (magic != MAGIC_BYTE) {
            throw new Exception(
                String.format(
                    "Bad magic byte. Expected 0x%02x but got 0x%02x",
                    MAGIC_BYTE, magic));
        }
        int saltLength = dataIn.readInt();
        byte[] salt = new byte[saltLength];
        dataIn.read(salt);
        int iterCount = dataIn.readInt();
        int keyLength = dataIn.readInt();
        String kdfAlgo = dataIn.readUTF();
        String cipherSuite = dataIn.readUTF();
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(kdfAlgo);
        PBEKey key = (PBEKey)factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(cipherSuite);
        cipher.init(Cipher.DECRYPT_MODE, key);
        CipherInputStream cipherIn = new CipherInputStream(dataIn, cipher);
        pump(cipherIn, outputStream);
        outputStream.flush();
    }

    private void pump(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[65536];
        int n;
        while (0 < (n = inputStream.read(buf))) {
            outputStream.write(buf, 0, n);
        }
    }
}
