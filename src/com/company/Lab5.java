package com.company;

import java.math.BigInteger;
import java.util.Random;

import static com.company.Lab1.*;
import static com.company.ElectronicSignatureRSA.*;
public class Lab5 {
    public Lab5() {
    }

    public static void main(String[] args) {
        Lab5 lab5 = new Lab5();
        Server server = new Server();
        Alice alice = new Alice(server.N, server.d);

    }
}

class Alice {
    BigInteger rndAlice;
    Random rnd;
    BigInteger v;
    BigInteger one = BigInteger.ONE;
    BigInteger zero = BigInteger.ZERO;
    BigInteger n;//have to be less than 1024 bits
    BigInteger r;
    String h;
    public Alice(BigInteger N, BigInteger d) {
        rnd = new Random();
        rndAlice = BigInteger.probablePrime(512, rnd);
        v = one;
        n = concatTwoBigintegersInDecimalForm(rndAlice, v);
        do {
            r = BigInteger.probablePrime(500, rnd);
        } while (!(gcd2BigInteger(r, N).compareTo(one) == 0));
        //h = md5Custom()
    }

    public BigInteger concatTwoBigintegersInDecimalForm(BigInteger e, BigInteger n) {//concatenates two bigInteger's decimal digits
        String a = String.valueOf(e);
        //System.out.println("a     = " + a);
        String b = String.valueOf(n);
        // System.out.println("b     = " + b);
        String val = a + b;
        // System.out.println("val   = " + val);
        BigInteger myval = new BigInteger(val);
        //System.out.println("myval = " + myval);
        return myval;
    }
}

class Server {
    BigInteger p;
    BigInteger q;
    public BigInteger N;//open
    BigInteger one = BigInteger.ONE;
    BigInteger F;
    public BigInteger d;//open
    private BigInteger c;//closed
    Random rnd;

    public Server() {
        rnd = new Random();
        p = BigInteger.probablePrime(1024, rnd);//Bob initializes P, Q, N, F, d < F, gcd(d, F) = 1, c, c*d mod F = 1
        q = BigInteger.probablePrime(1024, rnd);//N & d - open, c - closed key
        // System.out.println("P = " + P);
        //System.out.println("Q = " + Q);
        N = p.multiply(q);
        //  System.out.println("N = " + N);
        F = (p.subtract(one)).multiply(q.subtract(one));
        //  System.out.println("F = " + F);
        do {
            d = BigInteger.probablePrime(1024, rnd);
        } while (!(d.compareTo(F) < 0) || !(gcd2BigInteger(d, F).compareTo(one) == 0));//have to find d<F so that gcd(d,F) = 1
        // System.out.println("d = " + d);
        //P = 3; Q = 11; N = P*Q; F = (P-1)*(Q-1); d = 3;//it works with these parameters...
        c = F.modInverse(d);
    }

//    // System.out.println("инверсия d по модулю F: c = " + c);
//    BigInteger m = 1103;//Alice sends m to Bob. m < N
//    BigInteger e = powModMineUsingBigInteger(m, d, N);//Alice uses Bob's keys to make e and sends it to him
//    BigInteger mStrih = powModMineUsingBigInteger(e, c, N);//Bob deciphers it and gets m
//        System.out.println("in RSA cypher m' = "+mStrih);
}
