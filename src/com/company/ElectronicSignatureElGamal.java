package com.company;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static com.company.ElectronicSignatureRSA.md5Custom;
import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;

public class ElectronicSignatureElGamal {
    public static void main(String[] args) {
        ElectronicSignatureElGamal ese = new ElectronicSignatureElGamal();
        ese.sign();
        ese.checkSign();
    }

    BigInteger[] gp;
    BigInteger G;//open
    BigInteger P;//open
    BigInteger x;//closed секретное число, сгенерированное Бобом
    BigInteger y;//open
    //BigInteger m;// m должно быть меньше Р
    BigInteger k;
    BigInteger r;
    BigInteger hDecimal;
    String hHexadeci;
    BigInteger u;
    BigInteger s;

    public void sign() {
        gp = getGPBigInteger();
        G = gp[0];
        P = gp[1];
        Random rnd = new Random();
        do {
            x = BigInteger.probablePrime(20, rnd);
        }//секретное число, сгенерированное не Бобом, как в шифровании, а Алисой
        while (x.compareTo(P.subtract(BigInteger.ONE)) > 0);
        //if(x>=P)System.out.println("x>=P");
        y = G.modPow(x, P);//открытое число, сгенерированное не Бобом, как в шифровании, а Алисой
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("binaryPDF.pdf")));//"textToCiph.txt")));//got a string of bytes from the hashed file
        } catch (Exception e) {
            e.printStackTrace();
        }
        hHexadeci = md5Custom(content);//    counted hash sum of content               ALICE'S FIRST ACTION after counting keys
        System.out.println("hHexadec = " + hHexadeci);
        do {
            k = BigInteger.probablePrime(20, rnd);// секретный сессионный ключ Алисы. 1 < k < p - 1
        } while (k.compareTo(P.subtract(BigInteger.ONE)) > 0 && ! (gcd2BigInteger(k, P).compareTo(BigInteger.ONE) == 0));
        hDecimal = new BigInteger(hHexadeci, 16); //got a BigInteger out of hexadecimal string
        System.out.println("hDecimal = " + hDecimal);
        r = G.modPow(k, P);
        u = (hDecimal.subtract(x.multiply(r))).mod(P.subtract(BigInteger.ONE));
        s = (k.modInverse(P.subtract(BigInteger.ONE))).mod(P.subtract(BigInteger.ONE));
    }
    public void checkSign() {
        System.out.println("The sign is correct check: " + ((y.modPow(r, P)).multiply(r.modPow(s, P)).compareTo(G.modPow(hDecimal, P)) == 0 ));
    }
}
