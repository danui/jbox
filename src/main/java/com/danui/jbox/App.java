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
                "jbox.op=encrypt jbox.in=? jbox.out=? jbox.password.file=?\n"+
                "jbox.op=encrypt jbox.in=? jbox.out=? jbox.password=?\n"+
                "jbox.op=decrypt jbox.in=? jbox.out=? jbox.password.file=?\n"+
                "jbox.op=decrypt jbox.in=? jbox.out=? jbox.password=?\n"+
                "");
            System.exit(1);
        }
    }

    private static void encryptOp() throws Exception {
        Crypto crypto = new Crypto();
        crypto.encryptWithPassword(
            new FileInputStream(new File(System.getProperty("jbox.in"))),
            new FileOutputStream(new File(System.getProperty("jbox.out"))),
            readPassword());
    }

    private static void decryptOp() throws Exception {
        Crypto crypto = new Crypto();
        crypto.decryptWithPassword(
            new FileInputStream(new File(System.getProperty("jbox.in"))),
            new FileOutputStream(new File(System.getProperty("jbox.out"))),
            readPassword());
    }

    private static char[] readPassword() throws Exception {
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
}
