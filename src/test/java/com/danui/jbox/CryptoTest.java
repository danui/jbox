package com.danui.jbox;

import java.util.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * CyrptoTest
 *
 * @author Jin
 */
public class CryptoTest {

    /**
     * R1001 decryptWithPassword can encrypt that which was encrypted by
     * encryptWithPassword.
     */
    @Test
    public void test_R1001() throws Exception {
        Crypto crypto = new Crypto();
        char[] password = "TeUGriio3o4".toCharArray();
        byte[] plaintext = new byte[1048576];
        Random rng = new Random();
        rng.nextBytes(plaintext);
        ByteArrayOutputStream out;
        crypto.encryptWithPassword(
            new ByteArrayInputStream(plaintext),
            out = new ByteArrayOutputStream(),
            password);
        byte[] ciphertext = out.toByteArray();
        assertFalse(same(plaintext, ciphertext));
        crypto.decryptWithPassword(
            new ByteArrayInputStream(ciphertext),
            out = new ByteArrayOutputStream(),
            password);
        byte[] decrypted = out.toByteArray();
        assertTrue(same(plaintext, decrypted));
    }

    private boolean same(byte[] x, byte[] y) {
        if (x == null) return false;
        if (y == null) return false;
        if (x.length != y.length) return false;
        for (int i = 0; i < x.length; ++i) {
            if (x[i] != y[i]) return false;
        }
        return true;
    }
}
