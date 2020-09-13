package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMineUsingBigInteger;

public class RSAChar {
    public static void main(String[] args) {
        RSAChar rs = new RSAChar();
          rs.cipherDecipher();
      /*  char m = 15;
        System.out.println("m = " + (int) m);
        char ch = rs.cipher(m);
        System.out.println("ch = " + (int) ch);
        char ch2 = rs.decipher(ch);
        System.out.println(" ch2 = " + (int) ch2);*/
    }

    char P;
    char Q;
    char N;//N - open
    char F;
    char d;//d - open
    //P = 3; Q = 11; N = P*Q; F = (P-1)*(Q-1); d = 3;//it works with these parameters...
    char c;//c - closed
    char m;
    char e;
    char mStrih;
    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;

    public char cipher(char m) {
        do {
            P = getPrimeChar(7);//Bob initializes P, Q, N, F, d < F, gcd(d, F) = 1, c, c*d mod F = 1
            Q = getPrimeChar(7);//N & d - open, c - closed key
            N = (char) (P * Q);//N - open
        } while (m >= N || P * Q > Character.MAX_VALUE);
        System.out.println("P * Q = "+(int) P*Q+ ", Character.MAX_VALUE = "+ (int)Character.MAX_VALUE);
        System.out.println("P = " + (int) P + ", Q = " + (int) Q + ", N = " + (int) N);
        F = (char) ((P - 1) * (Q - 1));
        do {
            d = getPrimeChar();//d - open
        } while (d > F || gcd2(d, F) != 1);
        // P = 3; Q = 11; N = (char) (P*Q); F = (char) ((P-1)*(Q-1)); d = 3;//it works with these parameters...
        c = (char) invers(F, d)[1];//c - closed //Bob works up until here
        //m = 15;//Alice sends m to Bob. m < N
        e = (char) powModMine(m, d, N);//Alice uses Bob's keys to make e and sends it to him
        return e;
    }

    public char decipher(char e) {
        mStrih = (char) powModMine(e, c, N);//Bob deciphers it and gets m
        System.out.println("in RSA cypher m' = " + (int) mStrih);
        return mStrih;
    }

    public void cipherDecipher() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("textToCiph.txt")));
            contentsInCharArray = content.toCharArray();
            System.out.println("contentsInCharArray.length = " + contentsInCharArray.length);
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                System.out.println("contentsInCharArray[i] = " + contentsInCharArray[i]);
                contentsInCharArrayDecyphered[i] = decipher(cipher(contentsInCharArray[i]));
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File("textDecipheredRSA.txt"));
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                    fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayDecyphered.length = " + contentsInCharArrayDecyphered.length);
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

