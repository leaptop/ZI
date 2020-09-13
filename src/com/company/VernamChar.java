package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.company.Lab1.getLargePrimeNum;
import static com.company.Lab1.getPrimeChar;

public class VernamChar {
    public VernamChar() {
        key = getPrimeChar();
    }

    char key;
    char m;
    char e;
    char mi;
    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;

    public static void main(String[] args) {
        VernamChar vc = new VernamChar();
        vc.cipherDecipher();
        //vc.cipherDecipherPDF();
    }

    public char cipher(char m) {
        e = (char) (m ^ key);// ^ - bitwise excluding OR// побитовое исключающее ИЛИ
        return e;
    }

    public char decipher(char e) {
        mi = (char) (e ^ key);
        return mi;
    }
    public void cipherDecipher() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("textToCiph.txt")));
            contentsInCharArray = content.toCharArray();
            System.out.println("contentsInCharArray.length = " + contentsInCharArray.length);
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                System.out.println("contentsInCharArray[i] = " + contentsInCharArray[i]);
                contentsInCharArrayDecyphered[i] = decipher(cipher(contentsInCharArray[i]));
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File("textDecipheredVernam.txt"));
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
    public void cipherDecipherPDF() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("Newpdf.pdf")));
            contentsInCharArray = content.toCharArray();
            System.out.println("contentsInCharArray.length = " + contentsInCharArray.length);
            contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                System.out.println("contentsInCharArray[i] = " + contentsInCharArray[i]);
                contentsInCharArrayDecyphered[i] = decipher(cipher(contentsInCharArray[i]));
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File("NewpdfVernam.pdf"));
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
}
