package com.company;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;

public class ElGamalChar {
    public static void cypherElGamalShowLab() {
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

    public static void main(String[] args) {

    }
}
