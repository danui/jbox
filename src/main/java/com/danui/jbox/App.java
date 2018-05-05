package com.danui.jbox;

import java.io.*;

public class App {
    public static void main(String[] args) throws Exception {
        String op = System.getProperty("jbox.op");
        if ("encrypt".equals(op)) {
            encryptOp();
        }
        else if ("decrypt".equals(op)) {
            decryptOp();
        }
        else {
            System.err.println(String.format("Unknown jbox.op: %s", op));
            System.err.println(
                "Commands and args are specified via system properties:\n"+
                "\n"+
                "  jbox.op=?            Specify 'encrypt' or 'decrypt'.\n"+
                "  jbox.in=?            Read input from this file. Default is to read from stdin.\n"+
                "  jbox.out=?           Write output to this file. Default is to write to stdout.\n"+
                "  jbox.password=?      Use this password to derive a secret key.\n"+
                "  jbox.password.file=? Read password from this file\n"+
                "\n"+
                "You can safely pass in password using 'read -s' in bash.\n"+
                "\n"+
                "    -Djbox.password=$(read -s x; echo $x)\n"+
                "");
            System.exit(1);
        }
    }

    private static void encryptOp() throws Exception {
        Crypto crypto = new Crypto();
        crypto.encryptWithPassword(
            getInputStream(), getOutputStream(), getPassword());
    }

    private static void decryptOp() throws Exception {
        Crypto crypto = new Crypto();
        crypto.decryptWithPassword(
            getInputStream(), getOutputStream(), getPassword());
    }

    private static char[] getPassword() throws Exception {
        String p = System.getProperty("jbox.password");
        if (p != null) {
            return p.toCharArray();
        }
        return new BufferedReader(
            new FileReader(
                new File(
                    System.getProperty("jbox.password.file")))
        ).readLine().toCharArray();
    }

    private static InputStream getInputStream() throws Exception {
        String filename = System.getProperty("jbox.in");
        if (filename != null) {
            return new FileInputStream(new File(filename));
        }
        return System.in;
    }

    private static OutputStream getOutputStream() throws Exception {
        String filename = System.getProperty("jbox.out");
        if (filename != null) {
            return new FileOutputStream(new File(filename));
        }
        return System.out;
    }
}
