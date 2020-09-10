package com.company;

import java.io.*;
import java.util.Scanner;

import static com.company.Lab1.*;

public class Lab2 {
    public static long cypherShamirShowLab(long m) {//
        long p = getLargePrimeNum();//Алиса генерирует р, шлёт его Бобу
        //System.out.println("p = " + p);// Алиса генерирует простое число p. Теперь ей надо сгенерировать взаимно простое
        // с (p - 1) число ca. Делается это с помощью обобщённого алгоритма Евклида. Т.е. надо генерировать случайные
        //числа, проверять их на простоту(тест Миллера-Рабина) p - 1 - чётное число, его не надо проверять на простоту
        // , после этого проверять их на взаимную простоту.
        System.out.println("p - 1 = " + (p - 1));
        long pmo = p - 1;
        long ca;
        do {
            ca = getLargePrimeNum();
        } while (!(gcd2(ca, pmo) == 1));//Алиса генерирует са взаимно простое с pmo
        long da = invers(pmo, ca)[1];//Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        System.out.println("ca = " + ca);
        System.out.println("da = " + da);
        long cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
        do {
            cb = getLargePrimeNum();
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
        return x4;
    }


    public static void cypherElGamal() {

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

    public static void cypherRSA() {
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
        long m = 15;//Alice sends m to Bob. m < N
        long e = powModMineUsingBigInteger(m, d, N);//Alice uses Bob's keys to make e and sends it to him
        long mStrih = powModMineUsingBigInteger(e, c, N);//Bob deciphers it and gets m
        System.out.println("in RSA cypher m' = " + mStrih);
    }

    public static void main(String[] args) {
        Shamir sh = new Shamir();
        try  {
            var in = new DataInputStream(new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/text.txt"));
            byte[] bb = in.readAllBytes();
            for (int i = 0; i < bb.length; i++) {
                System.out.println(i + " in. = " + bb[i]);
                byte ln = (byte) (bb[i] & 0xff);
                byte x3 = sh.cypherShamir(ln);
                //System.out.println("x3 = " + x3);
                bb[i] = (byte) x3;//не факт, что это приведение типов сработает правильно...
                System.out.println(i + " out = " + bb[i]);
            }
            in.close();
            DataOutputStream out = new DataOutputStream(new FileOutputStream("textChanged.txt"));
            out.write(bb);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }
        try {
            var in = new DataInputStream(new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/textChanged.txt"));
            byte[] bb = in.readAllBytes();
            for (int i = 0; i < bb.length; i++) {
                System.out.println(i + " in. = " + bb[i]);
                long ln = bb[i] & 0xff;
                long x4 = sh.decypherShamir(ln);
               // System.out.println("x4 = " + x4);
                bb[i] = (byte) x4;//не факт, что это приведение типов сработает правильно...
                System.out.println(i + " out = " + bb[i]);
            }
            in.close();
            DataOutputStream out = new DataOutputStream(new FileOutputStream("/home/stepa/Documents/projetcs/ZI_01/textChangedDecyphered.txt"));
            out.write(bb);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }
       /* Scanner sc;
        try { var in = new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/text.txt");
            sc = new Scanner(in, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream("textChanged.txt"));

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }
        try (var in = new DataInputStream(new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/textChanged.txt"))) {
            byte[] bb = in.readAllBytes();
            for (int i = 0; i < bb.length; i++) {
                System.out.println(i + " in. = " + bb[i]);
                bb[i] = (byte) sh.decypherShamir((long)bb[i]);//не факт, что это приведение типов сработает правильно...
                System.out.println(i + " out = " + bb[i]);
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream("/home/stepa/Documents/projetcs/ZI_01/textChangedDecyphered.txt"));
            out.write(bb);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }*/
        // System.out.println("cypherShamirShowLab = " + cypherShamirShowLab(4));;
        /*try {
            var out = new DataOutputStream(new FileOutputStream("textChanged.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        /*BigInteger bi1, bi2, bi3;
        // create a BigInteger exponent
        BigInteger exponent = new BigInteger("521527");
        bi1 = new BigInteger("15");
        bi2 = new BigInteger("499303855789");
        // perform modPow operation on bi1 using bi2 and exp
        bi3 = bi1.modPow(exponent, bi2);
        String str = bi1 + "^" +exponent+ " mod " + bi2 + " is " +bi3;
        // print bi3 value
        System.out.println( str );*/
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