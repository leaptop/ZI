package com.company;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.company.Lab1.*;
//import com.company.RSAChar;

public class ElectronicSignatureRSA {
    public static void main(String[] args) {
        //String st = "devcolibri";
        //System.out.println("Custom MD5:");
        // System.out.println(ElectronicSignatureRSA.md5Custom(st));
        //RSAChar rs = new RSAChar();
        ElectronicSignatureRSA esrsa = new ElectronicSignatureRSA();
        esrsa.sign();
        esrsa.checkSign();

    }

    BigInteger hDecimal;
    BigInteger s;
    BigInteger P;
    BigInteger Q;
    BigInteger N;//N - open
    BigInteger F;
    BigInteger d;//d - open
    //P = 3; Q = 11; N = P*Q; F = (P-1)*(Q-1); d = 3;//it works with these parameters...
    BigInteger c;//c - closed
    BigInteger m;
    BigInteger e;
    String hHexadeci;

    public void sign() {
        Random rnd = new Random();
        P = BigInteger.probablePrime(80, rnd);//Bob initializes P, Q, N, F, d < F, gcd(d, F) = 1, c, c*d mod F = 1
        Q = BigInteger.probablePrime(80, rnd);//N & d - open, c - closed key
        N = (P.multiply(Q));//N - open
        System.out.println("P = " + P + ", Q = " + Q + ", N = " + N);
        F = ((P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE)));
        do {
            d = BigInteger.probablePrime(20, rnd);//d - open
        } while (d.compareTo(F) > 0 || gcd2BigInteger(d, F).compareTo(BigInteger.ONE) != 0);// i.e. while gcd(d, F) != 1
        c = d.modInverse(F);//c - closed //Bob works up until here. my implementation would be: c = (char) invers(F, d)[1];
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("binaryPDF.pdf")));//"textToCiph.txt")));//got a string of bytes from the hashed file
        } catch (Exception e) {
            e.printStackTrace();
        }
        hHexadeci = md5Custom(content);//    counted hash sum of content               ALICE'S FIRST ACTION after counting keys
        System.out.println("hHexadec = " + hHexadeci);
        hDecimal = new BigInteger(hHexadeci, 16); //got a BigInteger out of hexadecimal string
        System.out.println("hDecimal = " + hDecimal);
        s = hDecimal.modPow(c, N); // s - signature for our content                        //SECOND ACTION
        //eventually content & s - is what can be passed to Bob, so he could check the validity of the signature
        //System.out.println("content = \n" + content);

        System.out.println("s        = " + s);
    }

    public void checkSign() {// Bob checks if the signature is correct. If the sign is correct, then e equals to h
        e = s.modPow(d, N);
        String str = (String.valueOf(e));
        BigInteger toHex = new BigInteger(str, 10);
        String res = toHex.toString(16);
        System.out.println("e        = " + res);
    }

    public static String md5Custom(String st) {//this method is able to hash any strings
        MessageDigest messageDigest = null;//Created a referenceto an object, that can digest data to hash function
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");//created an MD5 implemented in the MessageDigest object
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }
}
