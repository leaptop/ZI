package com.company;

import java.io.*;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;
import static java.lang.Math.pow;

public class ShamirByteToInt {
    public ShamirByteToInt() {
        p = getPrimeByte();
        pmo = (p - 1);
        do {
            ca = getPrimeByte();
        } while (!(gcd2(ca, pmo) == 1));//Алиса генерирует са взаимно простое с pmo
        long testInversDA = invers(pmo, ca)[1];//Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        if (testInversDA > Integer.MAX_VALUE) {
            System.out.println("                    inside Shamir(): testInversDA > Integer.MAX_VALUE");
        }
        // System.out.println("testInversDA = " + testInversDA);
        da = (int) testInversDA;

        do {
            cb = getPrimeByte();
        } while (!(gcd2(cb, pmo) == 1));//нахожу са взаимно простое с pmo

        long testinversDB = invers(pmo, cb)[1];//а что использовать вместо m? PMO!
        if (testinversDB > Integer.MAX_VALUE) {
            System.out.println("                    inside Shamir(): testinversDB > Integer.MAX_VALUE");
        }
        //System.out.println("testinversDB = " + testinversDB);
        db = (int) testinversDB;
    }

    public byte getPrimeByte() {
        byte l = (byte) pow(2, 7);//Задаю порядок возвращаемого числа в виде степени двойки
        byte n = 0;
        do {
            n = (byte) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4) && (p > m));
        // System.out.println("inside getPrimeByte n = " + n);
        return n;
    }
    public int m;
    public int ca;
    public int da;
    public int p;//Алиса генерирует р, шлёт его Бобу
    public int pmo;
    public int cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
    public int db;
    public int x1;
    public int x2;
    public int x3;
    public int x4;

    //метод для непосредственной работы с файлами:
    public byte cypherShamir(int m) {// Сюда может быть передано отрицательное число, это проблема...
        this.m = m;
        /*System.out.println("m = " + m);
        System.out.println("ca = " + ca);
        System.out.println("da = " + da);
        System.out.println("p = " + p);
        System.out.println("pmo = " + pmo);
        System.out.println("cb = " + cb);
        System.out.println("db = " + db);*/
        x1 = (int) powModMine((long) m, (long) ca, (long) p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        x2 = (int) powModMine(x1, cb, p);// B -> A
        x3 = (int) powModMine(x2, da, p);// A -> B. Пусть остановка будет здесь, т.е. зашифрованный файл будет
        // хранить в себе значения х3
        //long x4 = powModMine(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
/*        System.out.println("x1 = " + x1);
        System.out.println("x2 = " + x2);
        System.out.println("x3 = " + x3);
        System.out.println("x4 = " + x4);*/
        //Моя ошибка, видимо, в том, что я отрицательные значения не переводил обратно в отрицательные... Как на входе
        //так и на выходе ведь это надо было проделывать...
        if (x3 > (int) Byte.MAX_VALUE || x3 < (int) Byte.MIN_VALUE) {
            System.out.println("                      inside cypherShamir(): x3>Byte.MAX_VALUE || x3 < (int) Byte.MIN_VALUE");
        }
        byte x3b = (byte) (this.x3 & 0x000000ff);
        System.out.println("x3b = " + x3b);
        return x3b;
    }

    public byte decypherShamir(long m) {
        this.m = (int) m;
        x4 = (int) powModMine(m, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        if (x4 > (int) Byte.MAX_VALUE || x4 < (int) Byte.MIN_VALUE) {
            System.out.println("                      inside cypherShamir(): x4>Byte.MAX_VALUE || x4 < (int) Byte.MIN_VALUE");
        }
        System.out.println("inside decypher: x4 = " + x4);
        byte x4b = (byte) (this.x4 & 0x00000fff);
        System.out.println("x4b = " + x4b);
        return x4b;
    }
    public void executeShamir(){

        try {
            var in = new DataInputStream(new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/text.txt"));
          //  char[] arrayOfRawChars = in.
            char ch = 5645;
            int f  = 3;
            ch += f;
            System.out.println("ch = " + ch);
            byte[] arrOfRawBytes = in.readAllBytes();//здесь с вероятностью 1/8 будет отрицательное число в каждом байте...
            //по крайней мере кириллические символы расшифровываются вообще не как латинские. "я" как два байта
            // -47 и -113. Выход видится в расширении этих байтов до инта. Тогда можно будет работать с полученными
            //положительными интами как с беззнаковыми байтами. Потом, если результаты преобразований не увеличат число
            //бит, которые можно вернуть в байт, их можно будет вернуть в байт. Нужно проверить на практике, не будет
            //ли переполнения этих 8-ми битов  врезультате таких преобразований. В итоге из файла беру байт. Расширяю
            // беззнаково до инта, работаю с ним, проверяю не полиявились ли единицы дальше восьмого байта. Если нет,
            //то алгоритм можно будет продолжить реализовывать.
            int[] arrOfBytesConvertedUnsignedToInts = new int[arrOfRawBytes.length];
            for (int i = 0; i < arrOfBytesConvertedUnsignedToInts.length; i++) {
                arrOfBytesConvertedUnsignedToInts[i] = (arrOfRawBytes[i] & 0xff);//в инте по умолчанию и так все нули
                //поэтому для копирования всех единиц байта достаточно умножить эти единицы байта на 8 единиц(0xff)
                System.out.println("arrOfRawBytes[i] = " + arrOfRawBytes[i]);
                System.out.println("arrOfBytesConvertedUnsignedToInts[i] = " + arrOfBytesConvertedUnsignedToInts[i]);
                //в итоге например кириллическе символы кодируются уже не одним а несколькими байтами, которые
                // становятся отрицательными... Т.е. поялвляются сомнения в том, удастся ли после обработки(шифрования)
                //вернуть эти биты обратно в теже размерыдля возможности расшифровки.
            }

            for (int i = 0; i < arrOfRawBytes.length; i++) {
                //System.out.println(i + " bb[i] = " + arrOfRawBytes[i]);
                // byte ln = (byte) (arrOfRawBytes[i] & 0xff);
                //System.out.println("x3 = " + x3);
                arrOfRawBytes[i] = (byte) cypherShamir(arrOfBytesConvertedUnsignedToInts[i]);//вернул в тот же массив зашифрованный байт
                // System.out.println(i + " out = " + bb[i]);
            }
            in.close();
            DataOutputStream out = new DataOutputStream(new FileOutputStream("textChanged.txt"));
            out.write(arrOfRawBytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }
        try {
            var in = new DataInputStream(new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/textChanged.txt"));
            byte[] arrOfRawBytes = in.readAllBytes();
            int[] arrOfBytesConvertedUnsignedToInts = new int[arrOfRawBytes.length];
            for (int i = 0; i < arrOfRawBytes.length; i++) {
                arrOfBytesConvertedUnsignedToInts[i] = (arrOfRawBytes[i] & 0xff);
                System.out.println("arrOfBytesConvertedUnsignedToInts[i] = " + arrOfBytesConvertedUnsignedToInts[i]) ;
            }
            for (int i = 0; i < arrOfRawBytes.length; i++) {
                arrOfRawBytes[i] = (byte) decypherShamir(arrOfBytesConvertedUnsignedToInts[i]);//вернул в тот же массив зашифрованный байт
            }
            in.close();
            DataOutputStream out = new DataOutputStream(new FileOutputStream("/home/stepa/Documents/projetcs/ZI_01/textChangedDecyphered.txt"));
            out.write(arrOfRawBytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Does the file exist?");
        }
    }

}