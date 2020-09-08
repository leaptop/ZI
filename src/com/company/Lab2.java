package com.company;

import static com.company.Lab1.*;

public class Lab2 {
    public static void cypherShamir() {
        long p = getLargePrimeNum();
        System.out.println("p = " + p);// Алиса генерирует простое число p. Теперь ей надо сгенерировать взаимно простое
        // с (p - 1) число ca. Делается это с помощью обобщённого алгоритма Евклида. Т.е. надо генерировать случайные
        //числа, проверять их на простоту(тест Миллера-Рабина) p - 1 - чётное число, его не надо проверять на простоту
        // , после этого проверять их на взаимную простоту.
        System.out.println("p - 1 = " + (p - 1));
        long pmo = p - 1;

/*        while( ! (gcd2(p, pmo) == 1)){// пока НОД не равен 1 числа не взаимно простые
            while(!isPrime(pmo, 4)){//пока pmo не простое генерируем новое p, считаем pmo заново
                p = getLargePrimeNum();
                pmo = p - 1;
            }//получил два простых числа. Теперь надо проверить их на взаимную простоту.
        }*/
        long ca;// = getLargePrimeNum();
        do {
            ca = getLargePrimeNum();
        } while (!(gcd2(ca, pmo) == 1));//нахожу са взаимно простое с pmo
        long da = invers(pmo, ca)[1];//а что использовать вместо m? p or pmo? PMO!
        System.out.println("ca = " + ca);
        System.out.println("da = " + da);

        long cb;// = getLargePrimeNum();
        do {
            cb = getLargePrimeNum();
        } while (!(gcd2(cb, pmo) == 1));//нахожу са взаимно простое с pmo
        long db = invers(pmo, cb)[1];//а что использовать вместо m? p or pmo? PMO!
        System.out.println("cb = " + cb);
        System.out.println("db = " + db);

        // ca = 7; da = 19; p = 23; pmo = 22; cb = 5; db = 9;// тестирую на примере из методички
        long m = 10;
        long x1 = powMod(m, ca, p);
        long x2 = powMod(x1, cb, p);
        long x3 = powMod(x2, da, p);
        long x4 = powMod(x3, db, p);
        System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);
    }
    public static void main(String[] args) {
        cypherShamir();
        //System.out.println(powMod(310900522, 2528721779L, 2623797101L));;

    }


}
/*
m = 10
p = 2623797101
p - 1 = 2623797100
ca = 271709621
da = 2471218581
cb = 2528721779
db = 1638670519
310900522 1928225701 34374866 608554630 решение моей программы
310900522 928548146      https://planetcalc.ru/8326/
310900522 149247219  744639632 10   https://abakbot.ru/online-16/254-ostatok-chisla-v-stepeni
 */