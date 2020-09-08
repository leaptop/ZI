package com.company;

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
        long x1 = powMod(m, ca, p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        long x2 = powMod(x1, cb, p);// B -> A
        long x3 = powMod(x2, da, p);// A -> B
        long x4 = powMod(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);
    }


    public static void cypherElGamal() {

        long[] gp = getGP();
        long G = gp[0];
        long P = gp[1];
        long x = getLargePrimeNum();//секретное число, сгенерированное Бобом
        while (x > P) x = getLargePrimeNum();
        System.out.println(x < P);
        long y = powMod(G, x, P);//открытое число, сгенерированное Бобом
        long m = 15;// m должно быть меньше Р
        long k = getLargePrimeNum();// секретный сессионный ключ Алисы. 1 < k < p - 1
        while (k > (P - 1)) k = getLargePrimeNum();
        System.out.println(k < (P - 1));
        //P = 23; G = 5; x = 13; y = 21; k = 7;
        long a = powMod(G, k, P);//a & b  вычисляются Алисой и шлются Бобу
        long b = ((powMod(y, k, P) * m) % P);
        long mShtrih = ((powMod(a, (P - 1 - x), P) * b) % P);// Боб расшифровывает сообщение Алисы и получает m
        System.out.println(" m' = " + mShtrih);

    }

    public static void main(String[] args) {
        //cypherShamir();
        cypherElGamal();

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