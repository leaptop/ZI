package com.company;

import java.io.*;
import java.util.Scanner;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;
import static java.lang.Math.pow;

public class ShamirChar {
    public ShamirChar() {
        p = getPrimeChar();
        pmo = (char) (p - 1);
        do {
            ca = getPrimeChar(10);
        } while ((gcd2(ca, pmo) != 1));//Алиса генерирует са взаимно простое с pmo
        long testInversDA = invers(pmo, ca)[1];//Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        if (testInversDA < Character.MIN_VALUE || testInversDA > Character.MAX_VALUE) {
            System.out.println("                    inside Shamir(): testInversDA < Character.MIN_VALUE || testInversDA > Character.MAX_VALUE, testInversDA = " + testInversDA);
        }
        da = (char) testInversDA;

        do {
            cb = getPrimeChar(10);
        } while ((gcd2(cb, pmo) != 1));//нахожу са взаимно простое с pmo

        long testInversDB = invers(pmo, cb)[1];//а что использовать вместо m? PMO!
        if (testInversDB < Character.MIN_VALUE || testInversDB > Character.MAX_VALUE) {
            System.out.println("                    inside Shamir(): testInversDB < Character.MIN_VALUE || testInversDB > Character.MAX_VALUE, testInversDB = " + testInversDB);
        }
        //System.out.println("testinversDB = " + testinversDB);
        db = (char) testInversDB;
    }

    public static void main(String[] args) {
        ShamirChar sch = new ShamirChar();
        sch.executeCypherDecypher();
    }

    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;
    public void createACharArrayFromAFile() {
        StringBuilder fileContents = new StringBuilder();
        File file = new File(Constants.FILENAME);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(Constants.NEWLINE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        contentsInCharArray = fileContents.toString().toCharArray();
        System.out.println(contentsInCharArray);
    }

    public char getPrimeChar() {
        char l = (char) ((char) pow(2, 16) - 1);//Задаю порядок возвращаемого числа в виде степени двойки
        char n = 0;
        do {
            n = (char) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4));
        // System.out.println("inside getPrimeByte n = " + n);
        return n;
    }
    public char getPrimeChar(int powerOfTwo) {
        char l = (char) ((char) pow(2, powerOfTwo) - 1);//Задаю порядок возвращаемого числа в виде степени двойки
        char n = 0;
        do {
            n = (char) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4) );
        // System.out.println("inside getPrimeByte n = " + n);
        return n;
    }

    public char m;
    public char ca;
    public char da;
    public char p;//Алиса генерирует р, шлёт его Бобу
    public char pmo;
    public char cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
    public char db;
    public long x1;
    public long x2;
    public long x3;
    public long x4;

    //метод для непосредственной работы с файлами:
    public char cypherShamir(char m) {// Сюда может быть передано отрицательное число, это проблема...
        this.m = m;
        System.out.println("m = " + (int) m);
        x1 = powModMine((long) m, (long) ca, (long) p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        System.out.println("x1 = " + x1);
        x2 = powModMine(x1, (long) cb, (long) p);// B -> A
        System.out.println("x2 = " + x2);
        x3 = powModMine(x2, (long) da, (long) p);// A -> B. Пусть остановка будет здесь, т.е. зашифрованный файл будет
        System.out.println("x3 = " + x3);
        // хранить в себе значения х3
        if (x3 < Character.MIN_VALUE || x3 > Character.MAX_VALUE) {
            System.out.println(" x3 is out of bounds");
        }
        //Моя ошибка, видимо, в том, что я отрицательные значения не переводил обратно в отрицательные... Как на входе
        //так и на выходе ведь это надо было проделывать... Теперь работаю с чарами, они вроде беззнаковые, но
        //всё равно не работает. Сам алгоритм перестал работать, я вручную проверял... m не равно x4...
        //Дело явно в параметрах(ключах)...
        return (char) x3;
    }

    public char decypherShamir(char m) {
        x4 = powModMine(m, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println("x4 = " + x4);
        if (x4 > Character.MAX_VALUE || x4 < Character.MIN_VALUE) {
            System.out.println("                      inside cypherShamir(): x4 > Character.MAX_VALUE || x4 <  Character.MIN_VALUE");
        }

        return (char) x4;
    }

    public void checkParams() {
        if (powModMine(ca * da, 1, pmo) != 1) {
            System.out.println("----------------powModMine(ca * da, 1, (p - 1)) != 1");
        }
        if (powModMine(cb * db, 1, pmo) != 1) {
            System.out.println("------------------powModMine(cb * db, 1, (p - 1)) != 1");
        }
        if (m >= p) {
            System.out.println("---------------------------------------------m >= p");
        }
    }

    public void executeCypherDecypher() {
        checkParams();
        System.out.println("ca = " + (int) ca);
        System.out.println("da = " + (int) da);
        System.out.println("p = " + (int) p);
        System.out.println("pmo = " + (int) pmo);
        System.out.println("cb = " + (int) cb);
        System.out.println("db = " + (int) db);
        if (m >= p) {
            System.out.println("-------------------------------------- m >= p");
        }
        StringBuilder fileContents = new StringBuilder();
        File file = new File(Constants.FILENAME);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(Constants.NEWLINE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        contentsInCharArray = fileContents.toString().toCharArray();
        //System.out.println(contentsInCharArray);
        contentsInCharArrayCyphered = new char[contentsInCharArray.length];
        for (int i = 0; i < contentsInCharArray.length; i++) {
            contentsInCharArrayCyphered[i] = cypherShamir(contentsInCharArray[i]);
        }
        System.out.println("Зашифрованное: ");
        for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
            System.out.print(contentsInCharArrayCyphered[i]);
        }
//На этом этапе закодировал чары в массиве
        System.out.println("--------------------------------------------");
        contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
        for (int i = 0; i < contentsInCharArray.length; i++) {
            contentsInCharArrayDecyphered[i] = decypherShamir(contentsInCharArrayCyphered[i]);
        }
        System.out.println("Расшифрованное: ");
        for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
            System.out.print(contentsInCharArrayDecyphered[i]);
        }


        /*try {
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
        }*/
    }

}