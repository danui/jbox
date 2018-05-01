package com.danui.jbox;

public class App {
    public static void main(String[] args) {
        String op = System.getProperty("jbox.op");
        if ("encrypt".equals(op)) {
            encryptOp();
        }
        else if ("decrypt".equals(op)) {
            decryptOp();
        }
        else {
            System.err.println(String.format("Unknown jbox.op: %s", op));
            System.exit(1);
        }
    }

    private static void encryptOp() {
        String inputPath = System.getProperty("jbox.in");
    }

    private static void decryptOp() {
    }
}
