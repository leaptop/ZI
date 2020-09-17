/*
package com.company;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;

public class ShamirBigInteger {
    public static void main(String[] args) {
        ShamirBigInteger sch = new ShamirBigInteger();
        // sch.executeCypherDecypherOnATextFile();
        // sch.cypherDecypherAnyFile();
        sch.cipherDecipherAnyFileFinal();
        // sch.cipherDecipherAnyTXTFile();

    }
    public ShamirBigInteger() {
        Random rnd = new Random();
        p = BigInteger.probablePrime(40, rnd);
        pmo =  (p.subtract(BigInteger.ONE));
        do {
            ca = BigInteger.probablePrime(40, rnd);
        } while ((gcd2BigInteger(ca, pmo).compareTo(BigInteger.ONE) != 0));//Алиса генерирует са взаимно простое с pmo
      //Алиса находит инверсию dа, т.е. сa*da mod pmo = 1
        da = ca.modInverse(pmo);

        do {
            cb = BigInteger.probablePrime(40, rnd);
        } while ((gcd2BigInteger(cb, pmo).compareTo(BigInteger.ONE) != 0));//нахожу са взаимно простое с pmo
        db =  cb.modInverse(pmo);
    }
    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;

    public char m;
    public BigInteger ca;
    public BigInteger da;
    public BigInteger p;//Алиса генерирует р, шлёт его Бобу
    public BigInteger pmo;
    public BigInteger cb;//Боб получает р от Алисы и также, как Алиса генерирует числа cb & db
    public BigInteger db;
    public BigInteger x1;
    public BigInteger x2;
    public BigInteger x3;
    public BigInteger x4;

    private static byte[] getUtf8Bytes(String s) {
        // Always specify encoding and not rely on default!
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private static BufferedOutputStream openFile(String fileName)
            throws IOException {
        return openFile(fileName, false);
    }

    private static BufferedOutputStream openFile(String fileName, boolean append)
            throws IOException {
        // Don't forget to add buffering to have better performance!
        return new BufferedOutputStream(new FileOutputStream(fileName, append));
    }

    public  void cipherDecipherAnyFileFinal(){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("inversion.png")));
            //System.out.println(content);
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayCyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayCyphered[i] = cipher(contentsInCharArray[i]);
            }
            System.out.println("Зашифрованное в Шамире: ");
            for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                System.out.print(contentsInCharArrayCyphered[i]);
            }
            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("inversionCyphered.png"));

                // write data to file
                for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                    fos.write((Character.toString(contentsInCharArrayCyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayCyphered.length = " + contentsInCharArrayCyphered.length);
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//На этом этапе закодировал чары в массиве
            System.out.println("\n-----------------inside cypherDecypherAnyFile()---------------------------\n");
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayDecyphered[i] = decipher(contentsInCharArrayCyphered[i]);
            }
            System.out.println("Расшифрованное в Шамире: ");
            for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                System.out.print(contentsInCharArrayDecyphered[i]);
            }//here I have a deciphered char array contentsInCharArrayDecyphered

            try {
                FileOutputStream fos = new FileOutputStream(new File("inversionDeciphered.png"));
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                    fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayDecyphered.length = " + contentsInCharArrayDecyphered.length);
                fos.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void cipherDecipherAnyTXTFile(){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("textToCiph.txt")));
            //System.out.println(content);
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayCyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayCyphered[i] = cipher(contentsInCharArray[i]);
            }
            System.out.println("Зашифрованное в Шамире: ");
            for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                System.out.print(contentsInCharArrayCyphered[i]);
            }
            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("textCipheredShamir.txt"));

                // write data to file
                for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                    fos.write((Character.toString(contentsInCharArrayCyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayCyphered.length = " + contentsInCharArrayCyphered.length);
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//На этом этапе закодировал чары в массиве
            System.out.println("\n-----------------inside cypherDecypherAnyTXTFile()---------------------------\n");
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayDecyphered[i] = decipher(contentsInCharArrayCyphered[i]);
            }
            System.out.println("Расшифрованное в Шамире: ");
            for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                System.out.print(contentsInCharArrayDecyphered[i]);
            }//here I have a deciphered char array contentsInCharArrayDecyphered

            String inputFile = "/home/stepa/Documents/projetcs/ZI_01/binaryPDF.pdf";
            String outputFile = "/home/stepa/Documents/projetcs/ZI_01/outputt.pdf";

            String targetFile = "binary-file.pdf";

            try {
                FileOutputStream fos = new FileOutputStream(new File("textDecipheredShamir.txt"));

                // write data to file
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                    fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayDecyphered.length = " + contentsInCharArrayDecyphered.length);

                fos.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void createACharArrayFromAFile() {//works with text files and similar
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



    //метод для непосредственной работы с файлами:
    public BigInteger cipher(char m) {// Сюда может быть передано отрицательное число, это проблема...
        this.m = m;
        System.out.println("m = " + (int) m);
        x1 = BigInteger.valueOf((long)m).modPow(ca, p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
        // System.out.println("x1 = " + x1);
        x2 = x1.modPow(cb, p);// B -> A
        //  System.out.println("x2 = " + x2);
        x3 = x2.modPow(da, p);// A -> B. Пусть остановка будет здесь, т.е. зашифрованный файл будет
        System.out.println("x3 = " + x3);
        // хранить в себе значения х3

        //Моя ошибка, видимо, в том, что я отрицательные значения не переводил обратно в отрицательные... Как на входе
        //так и на выходе ведь это надо было проделывать... Теперь работаю с чарами, они вроде беззнаковые, но
        //всё равно не работает. Сам алгоритм перестал работать, я вручную проверял... m не равно x4...
        //Дело явно в параметрах(ключах)... Теперь х4 = m, но не получается это в файл засунуть...
        return x3;
    }

    public BigInteger decipher(BigInteger x3) {
        x4 = x3.modPow(db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
        System.out.println("x4 = " + x4);

        return  x4;
    }


    public void executeCipherDecipherOnATextFile() {
        System.out.println("ca = " +  ca);
        System.out.println("da = " +  da);
        System.out.println("p = " +  p);
        System.out.println("pmo = " +  pmo);
        System.out.println("cb = " +  cb);
        System.out.println("db = " +  db);

 */
/*        if (m >= p) {
            System.out.println("-------------------------------------- m >= p");
        }*/
/*
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
            contentsInCharArrayCyphered[i] = cipher(contentsInCharArray[i]).toString().;
        }
        System.out.println("Зашифрованное в Шамире: ");
        for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
            System.out.print(contentsInCharArrayCyphered[i]);
        }
//На этом этапе закодировал чары в массиве
        System.out.println("\n-----------------inside executeCypherDecypherOnATextFile()---------------------------\n");
        contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
        for (int i = 0; i < contentsInCharArray.length; i++) {
            contentsInCharArrayDecyphered[i] = decipher(contentsInCharArrayCyphered[i]);
        }
        System.out.println("Расшифрованное в Шамире: ");
        for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
            System.out.print(contentsInCharArrayDecyphered[i]);
        }
    }



}
*/