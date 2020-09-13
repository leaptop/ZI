package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.company.Lab1.*;
import static com.company.Lab1.powModMine;

public class ElGamalChar {
    public ElGamalChar() {

    }

    public static void main(String[] args) {
        //cypherElGamalShowLab();
        ElGamalChar eg = new ElGamalChar();
       // eg.cypher('я');
       // System.out.println("eg.decipher(eg.cypher('я')); = " +  eg.decipher(eg.cypher('я')));
        eg.cipherDecipherTXT();
       // eg.cypherDecypherAny();
    }
    char[] gp;
    char G;
    char P;
    char x;
    char y;
    char m;// m должно быть меньше Р
    char k;
    char a;
    char b;
    char mShtrih;
    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;

    public char[] cipher(char m) {
        gp = getGPChar();
        G = gp[0];
        P = gp[1];
        x = getPrimeChar();//секретное число, сгенерированное Бобом
        while (x >= P) x = getPrimeChar();
        //if(x>=P)System.out.println("x>=P");
        y = (char) powModMine(G, x, P);//открытое число, сгенерированное Бобом
        k = getPrimeChar();// секретный сессионный ключ Алисы. 1 < k < p - 1
        while (k > (P - 1)) k = getPrimeChar();
       // System.out.println(k < (P - 1));
        //P = 23; G = 5; x = 13; y = 21; k = 7;
        a = (char) powModMine(G, k, P);//a & b  вычисляются Алисой и шлются Бобу
        b = (char) ((powModMine(y, k, P) * m) % P);
        char [] arr = {a, b};
        return arr ;
    }
    public char decipher(char[] arr){
        a = arr[0];
        b = arr[1];
        mShtrih = (char) ((powModMine(a, (P - 1 - x), P) * b) % P);// Боб расшифровывает сообщение Алисы и получает m
        System.out.println(" m' = " +  mShtrih);
        return mShtrih;
    }

    public void cipherDecipherAny(){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("Newpdf.pdf")));
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {      //в зашифрованном виде это был бы двумерный
                // массив чисел a & b. Поэтому я решил его нигде не сохарнять,а представить, что просто идёт общение
                //между Алисой и Бобом в мессенджере
                contentsInCharArrayDecyphered[i] =  decipher(cipher(contentsInCharArray[i]));
            }

            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("NewpdfDeciphElGamal.pdf"));
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
    public void cipherDecipherTXT(){//works!
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(Constants.FILENAME)));
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {      //в зашифрованном виде это был бы двумерный
                // массив чисел a & b. Поэтому я решил его нигде не сохарнять,а представить, что просто идёт общение
                //между Алисой и Бобом в мессенджере
              contentsInCharArrayDecyphered[i] =  decipher(cipher(contentsInCharArray[i]));
            }
            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("textDecipheredElGamal.txt"));
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

    public static void cipherElGamalShowLab() {
        char[] gp = getGPChar();
        char G = gp[0];
        char P = gp[1];
        char x = getPrimeChar();//секретное число, сгенерированное Бобом
        while (x >= P) x = getPrimeChar();
        //System.out.println(x < P);
        char y = (char) powModMine(G, x, P);//открытое число, сгенерированное Бобом
        char m = 15;// m должно быть меньше Р
        char k = getPrimeChar();// секретный сессионный ключ Алисы. 1 < k < p - 1
        while (k > (P - 1)) k = getPrimeChar();
        System.out.println(k < (P - 1));
        //P = 23; G = 5; x = 13; y = 21; k = 7;
        char a = (char) powModMine(G, k, P);//a & b  вычисляются Алисой и шлются Бобу
        char b = (char) ((powModMine(y, k, P) * m) % P);
        char mShtrih = (char) ((powModMine(a, (P - 1 - x), P) * b) % P);// Боб расшифровывает сообщение Алисы и получает m
        System.out.println(" m' = " + (int) mShtrih);
    }


}
