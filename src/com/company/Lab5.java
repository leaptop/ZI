package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import static com.company.Lab1.*;
import static com.company.ElectronicSignatureRSA.*;

public class Lab5 {
    public static void main(String[] args) {
        initVoting();
    }

    public static void initVoting() {
        BigInteger[] result_n_s;
        Server server = new Server();
        Alice alice = new Alice(server.N, server.d);//d & N are known by all the voters, and are, apparently, the same for everybody
        //server.hCap = alice.hCap;//Alice sent hCap to Server via a protected verified channel)//it's pretier to pass hCap as an argument of a method
        server.workOn_hCapAndReturn_sCap(alice.hCap);
        result_n_s = alice.workOn_sCapAndReturn_n_s_bulletin(server.sCap);
        server.checkAlicesVoteAndAddItTo_votedArrayIfCorrectlyDeciphered(result_n_s);

        Alice alice2 = new Alice(server.N, server.d);//two people can vote normally
        server.workOn_hCapAndReturn_sCap(alice2.hCap);
        result_n_s = alice2.workOn_sCapAndReturn_n_s_bulletin(server.sCap);
        server.checkAlicesVoteAndAddItTo_votedArrayIfCorrectlyDeciphered(result_n_s);
    }
}

class Alice {//ideally each object of Alice(any other voter) should have his/her own vote. So to perform multiple voting I need to create a few objects of Alice
    BigInteger rndAlice;
    Random rnd;
    BigInteger v;
    BigInteger one = BigInteger.ONE;
    BigInteger zero = BigInteger.ZERO;
    BigInteger n;//has to be less than 1024 bits
    BigInteger r;
    String h;
    BigInteger hCap;
    BigInteger s;
    BigInteger N;
    BigInteger[] signedBulletin_n_s;//final Alice's vote to store on server

    public Alice(BigInteger N, BigInteger d) {
        this.N = N;
        rnd = new Random();
        rndAlice = BigInteger.probablePrime(512, rnd);//service information about voting
        v = one;//vote result. Could be for example 1 for yes, 0 for no, etc
        n = concatTwoBigintegersInDecimalForm(rndAlice, v);//Alice's eventual vote
        do {
            r = BigInteger.probablePrime(500, rnd);
        } while (!(gcd2BigInteger(r, N).compareTo(one) == 0));
        h = n.toString();
        h = md5Custom(h);
        hCap = (new BigInteger(h, 16).multiply(r.modPow(d, N)));//converted h to decimal out of hexadecimal String and multiplied on modPow
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

    public BigInteger[] workOn_sCapAndReturn_n_s_bulletin(BigInteger sCap) {
        s = sCap.multiply(r.modInverse(N));
        signedBulletin_n_s = new BigInteger[]{n, s};
        return signedBulletin_n_s;
    }

    ;
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
    BigInteger hCap;
    BigInteger sCap;
    BigInteger[] result_n_s;
    ArrayList<BigInteger[]> votedArray;

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
        c = d.modInverse(F);
        result_n_s = new BigInteger[2];
        votedArray = new ArrayList<>();
    }

    public BigInteger workOn_hCapAndReturn_sCap(BigInteger hCap) {
        this.hCap = hCap;
        sCap = hCap.modPow(c, N);
        return sCap;
    }

    public boolean checkAlicesVoteAndAddItTo_votedArrayIfCorrectlyDeciphered(BigInteger[] result_n_s) {
        this.result_n_s = result_n_s;
        String shaOfn = md5Custom(result_n_s[0].toString());
        BigInteger check = result_n_s[1].modPow(d, N);
        BigInteger shaOfnBigInteger = new BigInteger(shaOfn, 16);
        if (check.compareTo(shaOfnBigInteger) == 0) {
            votedArray.add(result_n_s);
            System.out.println("The n & s are correct, the voice is inserted to database (votedArray)");
            return true;
        } else {
            System.out.println("something went wrong");
            return false;
        }
    }
}
