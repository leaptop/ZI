package com.company;

import java.io.*;
import java.util.Scanner;
import java.util.function.LongBinaryOperator;

import static com.company.Lab1.*;

public class Lab2 {
    public static void main(String[] args) {
       // cipherShamirShowLab(Long.MAX_VALUE-178L);
       // System.out.println("-----------------------------------");
        cipherRSAShowLab();
        //  System.out.println("-----------------------------------");
        //cipherVernamShowLab();
        //System.out.println("-----------------------------------");

        // cipherElGamalShowLab();
        //  ShamirChar sh = new ShamirChar();
        // sh.executeShamir();
    }
    public static long cipherShamirShowLab(long m) {//
        long p = getLargePrimeNum(50);//Алиса генерирует р, шлёт его Бобу
        System.out.println("p  = "+ p);
        long m0, m1;
        if (m > p) {
            m0 = (m >> 32);//got 32 left(bigger) bits
            m1 = (m & 0xffffffffL);//to avoid casting to int I have to write L at the end of the hexadecimal value  //4294967117

            System.out.println("m  = " + m);
            System.out.println("m0 = " + m0);
            System.out.println("m1 = " + m1);
           long m0deciphered =  cipherShamirShowLab(m0);
            System.out.println("m0deciphered = " + m0deciphered);
           long m1deciphered = cipherShamirShowLab(m1);
            System.out.println("m1deciphered = " + m1deciphered);//got the same results(same bits on the out of the method calls)
            //now I have to assemble the bits back together... but the question was: how would Bob see the cyphered halves?


        }
        //System.out.println("p = " + p);// Алиса генерирует простое число p. Теперь ей надо сгенерировать взаимно простое
        // с (p - 1) число ca. Делается это с помощью обобщённого алгоритма Евклида. Т.е. надо генерировать случайные
        //числа, проверять их на простоту(тест Миллера-Рабина) p - 1 - чётное число, его не надо проверять на простоту
        // , после этого проверять их на взаимную простоту.
        System.out.println("p - 1 = " + (p - 1));
        long pmo = p - 1;
        long ca;
        do {
            ca = getLargePrimeNum(50);
        } while (!(gcd2(ca, pmo) == 1));//Алиса генерирует са взаимно простое с pmo
        long da = invers(pmo, ca)[1];//Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        System.out.println("ca = " + ca);
        System.out.println("da = " + da);
        long cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
        do {
            cb = getLargePrimeNum(50);
        } while (!(gcd2(cb, pmo) == 1));//нахожу са взаимно простое с pmo
        long db = invers(pmo, cb)[1];//а что использовать вместо m? PMO!
        System.out.println("cb = " + cb);
        System.out.println("db = " + db);

        // ca = 7; da = 19; p = 23; pmo = 22; cb = 5; db = 9;// тестирую на примере из методички
        //long m = 10;//m должно быть обязательно меньше Р, либо должно разбиваться на m0m1 ... mt, где каждый элемент
        // полседовательности mi < p, 0 <= i <= t
        long x1 = powModMine(m, ca, p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        long x2 = powModMine(x1, cb, p);// B -> A
        long x3 = powModMine(x2, da, p);// A -> B
        long x4 = powModMine(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);
        System.out.println("x3 = " + x3);
        System.out.println("x4 = " + x4);
        return x4;
    }

    public static void cipherElGamalShowLab() {
        long[] gp = getGP();
        long G = gp[0];
        long P = gp[1];
        long x = getLargePrimeNum();//секретное число, сгенерированное Бобом
        while (x > P) x = getLargePrimeNum();
        System.out.println(x < P);
        long y = powModMine(G, x, P);//открытое число, сгенерированное Бобом
        long m = 15;// m должно быть меньше Р
        long k = getLargePrimeNum();// секретный сессионный ключ Алисы. 1 < k < p - 1
        while (k > (P - 1)) k = getLargePrimeNum();
        System.out.println(k < (P - 1));
        //P = 23; G = 5; x = 13; y = 21; k = 7;
        long a = powModMine(G, k, P);//a & b  вычисляются Алисой и шлются Бобу
        long b = ((powModMine(y, k, P) * m) % P);
        long mShtrih = ((powModMine(a, (P - 1 - x), P) * b) % P);// Боб расшифровывает сообщение Алисы и получает m
        System.out.println(" m' = " + mShtrih);
    }

    public static void cipherRSAShowLab() {
        long P = getLargePrimeNum();//Bob initializes P, Q, N, F, d < F, gcd(d, F) = 1, c, c*d mod F = 1
        long Q = getLargePrimeNum();//N & d - open, c - closed key
        // System.out.println("P = " + P);
        //System.out.println("Q = " + Q);
        long N = P * Q;
        //  System.out.println("N = " + N);
        long F = (P - 1) * (Q - 1);
        //  System.out.println("F = " + F);
        long d;
        do {
            d = getLargePrimeNum();
        } while (d > F || gcd2(d, F) != 1);
        // System.out.println("d = " + d);
        //P = 3; Q = 11; N = P*Q; F = (P-1)*(Q-1); d = 3;//it works with these parameters...
        long c = invers(F, d)[1];
        // System.out.println("инверсия d по модулю F: c = " + c);
        long m = 1103;//Alice sends m to Bob. m < N
        long e = powModMineUsingBigInteger(m, d, N);//Alice uses Bob's keys to make e and sends it to him
        long mStrih = powModMineUsingBigInteger(e, c, N);//Bob deciphers it and gets m
        System.out.println("in RSA cypher m' = " + mStrih);
    }

    public static void cipherVernamShowLab() {// в этом фире предполагается, что получающая сторона владеет ключом изначально
        System.out.println("Vernam's cypher: ");
        long key = getLargePrimeNum(50);
        long m = 123456789123456L;
        System.out.println("m = " + m);
        long e = m ^ key;// ^ - bitwise excluding OR// побитовое исключающее ИЛИ
        System.out.println("e = " + e);
        long mi = e ^ key;
        System.out.println("mi = " + mi);
    }
    public void mToM0_M1_char(long m) {
        System.out.println("Long.toBinaryString(m) = " + Long.toBinaryString(m));//gives 64 zeros and ones in case of numbers like Long.MAX_VALUE - 1
        String mStr = Long.toBinaryString(m);
        char[] mCharArr = mStr.toCharArray();
        int mCharArrLength = mCharArr.length;
        //mCharArrLength = 61;
        int mCharArr0Length = mCharArrLength / 2;
        int mCharArr1Length = mCharArr0Length + mCharArrLength % 2;
        System.out.println("mCharArrLength = " + mCharArrLength);
        System.out.println("mCharArr0Length = " + mCharArr0Length);
        System.out.println("mCharArr1Length = " + mCharArr1Length);
        char[] mCharArr0 = new char[mCharArr0Length];
        char[] mCharArr1 = new char[mCharArr1Length];
        for (int i = 0; i < mCharArr0Length; i++) {
            mCharArr0[i] = mCharArr[i];
        }
        for (int i = 0; i < mCharArr1Length; i++) {
            mCharArr1[i] = mCharArr[mCharArr1Length + 1];
        }//finally divided m to m0 & m1... the same thing could be done with the long itself... tried and didn't succeed
        //char[][] returned = new char[][];
        // returned[0][] =
        //return {mCharArr0, mCharArr1};
    }
}
/*
m = 10 Проверяю шифр Шамира
p = 2623797101
p - 1 = 2623797100
ca = 271709621
da = 2471218581
cb = 2528721779
db = 1638670519
310900522 1928225701 34374866 608554630 решение моей программы до того, как исправил powMod
310900522 928548146      https://planetcalc.ru/8326/
310900522 149247219  744639632 10   https://abakbot.ru/online-16/254-ostatok-chisla-v-stepeni
 */