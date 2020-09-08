package com.company;
import java.math.*;

import static com.company.Lab1.*;

public class Lab2 {
    public static void cypherShamir() {//
        long p = getLargePrimeNum();//Алиса генерирует р, шлёт его Бобу
        System.out.println("p = " + p);// Алиса генерирует простое число p. Теперь ей надо сгенерировать взаимно простое
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
        long m = 10;//m должно быть обязательно меньше Р, либо должно разбиваться на m0m1 ... mt, где каждый элемент
        // полседовательности mi < p, 0 <= i <= t
        long x1 = powModMine(m, ca, p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        long x2 = powModMine(x1, cb, p);// B -> A
        long x3 = powModMine(x2, da, p);// A -> B
        long x4 = powModMine(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);
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
        System.out.println("P = " + P);
        System.out.println("Q = " + Q);
        long N = P * Q;
        System.out.println("N = " + N);
        long F = (P - 1) * (Q - 1);
        System.out.println("F = " + F);
        long d;
        do {
            d = getLargePrimeNum();
        } while (d > F || gcd2(d, F) != 1);
        System.out.println("d = " + d);
        //P = 3; Q = 11; N = P*Q; F = (P-1)*(Q-1); d = 3;//it works with these parameters...
        long c = invers(F, d)[1];
        System.out.println("инверсия d по модулю F: c = " + c);
        long m = 15;//Alice sends m to Bob. m < N
        //long e = powMod(m, d, N);//Alice uses Bob's keys to make e and sends it to him
        //long e = powMod(15, 521527, 499303855789L);
        long e = powModMineUsingBigInteger(m, d, N);
        long test = powModMineUsingBigInteger (15, 521527, 499303855789L);
        System.out.println("test = " + test);
        //long mStrih = powMod(e, c, N);//Bob deciphers it and gets m
        long mStrih = powModMineUsingBigInteger(e, c, N);
        System.out.println("in RSA cypher m' = " + mStrih);
/*
P = 482746709
Q = 678609583
N = 327596542889112347
F = 327596541727756056
d = 1006448483
c = 266748212981469299    82360772450676032  https://www.extendedeuclideanalgorithm.com/calculator.php?a=327596541727756056&b=1006448483&mode=2#c
e = -300920979817763362   138870496614015900
in RSA cypher m' = -182723206433006417
 */
    }

    public static void main(String[] args) {
        cypherShamir();
        System.out.println("---------------------------");
        cypherElGamal();
        System.out.println("---------------------------");
        cypherRSA();


        BigInteger bi1, bi2, bi3;
        // create a BigInteger exponent
        BigInteger exponent = new BigInteger("521527");
        bi1 = new BigInteger("15");
        bi2 = new BigInteger("499303855789");
        // perform modPow operation on bi1 using bi2 and exp
        bi3 = bi1.modPow(exponent, bi2);
        String str = bi1 + "^" +exponent+ " mod " + bi2 + " is " +bi3;
        // print bi3 value
        System.out.println( str );
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