package com.company;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.company.ElectronicSignatureRSA.md5Custom;
import static com.company.Lab1.getGPBigInteger;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class ElectronicSignatureGOSTP34_10_94 {
    BigInteger r;
    BigInteger s;
    BigInteger hDecimal;
    BigInteger q;
    BigInteger a;
    BigInteger p;
    BigInteger y;
    public static void main(String[] args) {
        ElectronicSignatureGOSTP34_10_94 e34 = new ElectronicSignatureGOSTP34_10_94();
        e34.sign();
        e34.checkSignature();
    }

    public void sign() {
        Random rnd = new Random();
        LinearCongruentialGenerator g = new LinearCongruentialGenerator(
                0x3DFC46F1,
                97781173,
                0xD,
                (long) pow(2, 32)
        );
        PrimesGenerator generator = new PrimesGenerator(g);
        BigInteger[] primes = generator.generatePrimes1024();
        BigInteger p = primes[0];
        System.out.println("p                  = " + p);
        BigInteger q = primes[1];
        System.out.println("q                  = " + q);
        //BigInteger a = generator.generateA(p, q);
        BigInteger b = (p.subtract(BigInteger.ONE)).divide(q);//it's not a fact, that b is gonna be integer...
        System.out.println("b                  = " + b);
        BigInteger bmq = (p.subtract(BigInteger.ONE)).mod(q);
        System.out.println("(p - 1) mod q  = b = " + bmq);
        BigInteger a = generator.getAMine(p, q, b);
        System.out.println("a                  = " + a);
        BigInteger x = BigInteger.probablePrime(250, rnd);
        System.out.println("x                  = " + x);                  //secret key
        BigInteger y = a.modPow(x, p);
        System.out.println("y                  = " + y);                 //open key
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("binaryPDF.pdf")));//"textToCiph.txt")));//got a string of bytes from the hashed file
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hHex = md5Custom(content);//    counted hash sum of content
        BigInteger hDecimal = new BigInteger(hHex, 16); //got a BigInteger out of hexadecimal string. Has to be less than q
        System.out.println("hDecimal           = " + hDecimal);
        BigInteger k;
        BigInteger r;
        BigInteger s;
        do {
            do {
                k = BigInteger.probablePrime(250, rnd);
                System.out.println("k                  = " + k);
                r = (a.modPow(k, p)).mod(q);
            } while (r.compareTo(BigInteger.ZERO) == 0);//if r equals zero, make a new k and r
            s = ((k.multiply(hDecimal)).add(x.multiply(r))).mod(q);

        } while (s.compareTo(BigInteger.ZERO) == 0);
        System.out.println("r                  = " + r);//also signature apparently
        System.out.println("s                  = " + s);//signature
        this.r = r;
        this.s = s;
        this.hDecimal = hDecimal;
        this.q = q;
        this.a = a;
        this.p = p;
        this.y = y;
    }
    public void checkSignature(){
        System.out.println("r.compareTo(q) = " + r.compareTo(q) + ", -1 means: correct");
        System.out.println("s.compareTo(q) = " + s.compareTo(q) + ", -1 means: correct");
        BigInteger u1 = s.multiply(hDecimal.modInverse(q));
        System.out.println("u1                 = " + u1);
        BigInteger u2 = (r.multiply(hDecimal.modInverse(q))).multiply(BigInteger.valueOf(-1));
        System.out.println("u2                 = " + u2);
        BigInteger v = (((a.modPow(u1, p)).multiply(y.modPow(u2, p))).mod(p)).mod(q);
        System.out.println("v.compareTo(r) = " + v.compareTo(r) + ", 0 - means, that the signature is correct");
    }
}

class PrimesGenerator {
    public BigInteger getAMine(BigInteger p, BigInteger q, BigInteger b) {
        BigInteger a = Lab1.getABigInteger(q, p, b);
        //BigInteger a = g.modPow(b, p);
        return a;
    }

    private LinearCongruentialGenerator generator;

    PrimesGenerator(LinearCongruentialGenerator generator) {
        this.generator = generator;
    }

    private BigInteger[] generatePrimes512() {//generates 5 prime numbers of 10, 20, 40, 80, 160 decimal digits( 160 is the first in the list, 80 - the second, etc)
        int bitLength = 512;
        List<Integer> t = new ArrayList<>();
        t.add(bitLength);
        int index = 0;
        while (t.get(index) >= 33) {
            int value = (int) floor(t.get(index) / 2);
            System.out.println("inside generatePrimes512(): value = " + value);
            t.add(value);
            index++;
        }
        BigInteger[] primes = new BigInteger[t.size()];//created an array of size index?
        primes[index] = BigInteger.probablePrime(t.get(index), new Random());//assigned the last element as a prime of 32 bits(last inserted value in t)
        System.out.println("primes[index] = " + primes[index]);//2^32 ~= 4 billions
        int m = index - 1;
        boolean flag = true;
        do {
            int r = (int) Math.ceil(t.get(m) / 32);
            BigInteger n = BigInteger.ZERO;
            BigInteger k = BigInteger.ZERO;
            do {
                if (flag) {
                    long[] y = generator.rand().limit(r).toArray();// just generates r random integers(2 in first case)
                    BigInteger sum = BigInteger.ZERO;
                    for (int i = 0; i < r - 1; i++) {
                        BigInteger tmp = BigInteger.valueOf(y[i]).multiply(BigInteger.TWO.pow(32));
                        sum = sum.add(tmp);
                    }
                    sum = sum.add(BigInteger.valueOf(generator.getSeed()));
                    generator.setSeed(y[r - 1]);
                    BigInteger tmp1 = new BigDecimal(BigInteger.TWO.pow(t.get(m) - 1))
                            .divide(new BigDecimal(primes[m + 1]), 0, RoundingMode.CEILING)
                            .toBigInteger();
                    BigInteger tmp2 = new BigDecimal(BigInteger.TWO.pow(t.get(m) - 1).multiply(sum))
                            .divide(new BigDecimal(primes[m + 1].multiply(BigInteger.TWO.pow(32 * r))), 0, RoundingMode.FLOOR)
                            .toBigInteger();
                    n = tmp1.add(tmp2);
                    if (!(n.mod(BigInteger.TWO).equals(BigInteger.ZERO))) {
                        n = n.add(BigInteger.ONE);
                    }
                    k = BigInteger.ZERO;
                }
                primes[m] = primes[m + 1].multiply(n.add(k)).add(BigInteger.ONE);
                if (primes[m].compareTo(BigInteger.TWO.pow(t.get(m))) > 0) {
                    flag = true;
                    continue;
                }
                if (!(BigInteger.TWO.modPow(primes[m + 1].multiply(n.add(k)), primes[m]).equals(BigInteger.ONE))
                        || BigInteger.TWO.modPow(n.add(k), primes[m]).equals(BigInteger.ONE)) {
                    flag = false;
                    k = k.add(BigInteger.TWO);
                } else {
                    flag = true;
                    break;
                }
            } while (true);
            m--;
        } while (m >= 0);
        return primes;
    }

    BigInteger[] generatePrimes1024() {
        int bitLength = 1024;
        BigInteger q = generatePrimes512()[1];
        BigInteger Q = generatePrimes512()[0];
        BigInteger p;
        BigInteger n = BigInteger.ZERO;
        BigInteger k = BigInteger.ZERO;
        int yLength = 32;
        boolean flag = true;
        do {
            if (flag) {
                long[] y = generator.rand().limit(yLength).toArray();
                BigInteger sum = BigInteger.ZERO;
                for (int i = 0; i < y.length - 1; i++) {
                    BigInteger tmp = BigInteger.valueOf(y[i]).multiply(BigInteger.TWO.pow(32));
                    sum = sum.add(tmp);
                }
                sum = sum.add(BigInteger.valueOf(generator.getSeed()));
                generator.setSeed(y[y.length - 1]);
                BigInteger tmp1 = new BigDecimal(BigInteger.TWO.pow(bitLength - 1))
                        .divide(new BigDecimal(q.multiply(Q)), 0, RoundingMode.CEILING)
                        .toBigInteger();
                BigInteger tmp2 = new BigDecimal(BigInteger.TWO.pow(bitLength - 1).multiply(sum))
                        .divide(new BigDecimal(q.multiply(Q).multiply(BigInteger.TWO.pow(bitLength))), 0, RoundingMode.FLOOR)
                        .toBigInteger();
                n = tmp1.add(tmp2);
                if (!(n.mod(BigInteger.TWO).equals(BigInteger.ZERO))) {
                    n = n.add(BigInteger.ONE);
                }
                k = BigInteger.ZERO;
            }
            p = q.multiply(Q).multiply(n.add(k)).add(BigInteger.ONE);
            if (p.compareTo(BigInteger.TWO.pow(bitLength)) > 0) {
                flag = true;
                continue;
            }
            if (!(BigInteger.TWO.modPow(q.multiply(Q).multiply(n.add(k)), p).equals(BigInteger.ONE))
                    || BigInteger.TWO.modPow(q.multiply(n.add(k)), p).equals(BigInteger.ONE)) {
                flag = false;
                k = k.add(BigInteger.TWO);
            } else {
                break;
            }
        } while (true);
        return new BigInteger[]{p, q};
    }

    BigInteger generateA(BigInteger p, BigInteger q) {
        BigInteger d;
        BigInteger f;
        do {
            do {
                d = new BigInteger(p.bitLength(), new Random());
            } while (d.compareTo(BigInteger.ONE) <= 0 || d.compareTo(p.subtract(BigInteger.ONE)) >= 0);
            f = d.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        } while (f.equals(BigInteger.ONE));
        return f;
    }
}