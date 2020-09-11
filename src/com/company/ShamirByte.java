package com.company;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;
import static java.lang.Math.pow;

public class ShamirByte {
    public ShamirByte() {
        p = getPrimeByte();
        pmo = (byte) (p - 1);
        do {
            ca = getPrimeByte();
        } while (!(gcd2(ca, pmo) == 1));//Алиса генерирует са взаимно простое с pmo
        long testInversDA = invers(pmo, ca)[1];//Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        System.out.println("testInversDA = " + testInversDA);
        da = (byte) testInversDA;

        do {
            cb = getPrimeByte();
        } while (!(gcd2(cb, pmo) == 1));//нахожу са взаимно простое с pmo
        long testinversDB = invers(pmo, cb)[1];//а что использовать вместо m? PMO!
        System.out.println("testinversDB = " + testinversDB);
        db = (byte) testinversDB;
    }

    public byte getPrimeByte() {
        byte l = (byte) pow(2, 7);//Задаю порядок возвращаемого числа в виде степени двойки
        byte n = 0;
        do {
            n = (byte) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4) && (p > m));
        System.out.println("inside getPrimeByte n = " + n);
        return n;
    }

    public byte m;
    public byte ca;
    public byte da;
    public byte p;//Алиса генерирует р, шлёт его Бобу
    public byte pmo;
    public byte cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
    public byte db;
    public byte x1;
    public byte x2;
    public byte x3;
    public byte x4;
//метод для непосредственной работы с файлами:
    public byte cypherShamir(byte m) {// Сюда может быть передано отрицательное число, это проблема...
        this.m =  m;
        System.out.println("m = " +m);
        System.out.println("ca = " + ca);
        System.out.println("da = " + da);
        System.out.println("p = " + p);
        System.out.println("pmo = " + pmo);
        System.out.println("cb = " + cb);
        System.out.println("db = " + db);


        x1 = (byte) powModMine((long) m, (long) ca, (long) p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        x2 = (byte) powModMine(x1, cb, p);// B -> A
        x3 = (byte) powModMine(x2, da, p);// A -> B. Пусть остановка будет здесь, т.е. зашифрованный файл будет
        // хранить в себе значения х3
        //long x4 = powModMine(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println("x1 = " + x1);
        System.out.println("x2 = " + x2);
        System.out.println("x3 = " + x3);
        System.out.println("x4 = " + x4);
        return x3;
    }


    public byte decypherShamir(long m) {
        this.m = (byte) m;
        x4 = (byte) powModMine(m, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        return x4;
    }
}
