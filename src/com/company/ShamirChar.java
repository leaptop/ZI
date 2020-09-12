package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    char[] contentsInCharArray;
    char[] contentsInCharArrayCyphered;
    char[] contentsInCharArrayDecyphered;

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

    public static void main(String[] args) {
        ShamirChar sch = new ShamirChar();
       // sch.executeCypherDecypherOnATextFile();
       // sch.cypherDecypherAnyFile();
        //sch.cypherDecypherAnyFileFinal();
        sch.cypherDecypherAnyTXTFile();

    }

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

    public  void cypherDecypherAnyFileFinal(){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("binaryPDF.pdf")));
            //System.out.println(content);
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayCyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayCyphered[i] = cypherShamir(contentsInCharArray[i]);
            }
            System.out.println("Зашифрованное: ");
            for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                System.out.print(contentsInCharArrayCyphered[i]);
            }
            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("binaryPDFCiphered.pdf"));

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
                contentsInCharArrayDecyphered[i] = decypherShamir(contentsInCharArrayCyphered[i]);
            }
            System.out.println("Расшифрованное: ");
            for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                System.out.print(contentsInCharArrayDecyphered[i]);
            }//here I have a deciphered char array contentsInCharArrayDecyphered

            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("binaryPDFDeciph.pdf"));

                // write data to file
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                    // fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes(), 0,
                    //(Character.toString(contentsInCharArrayDecyphered[i])).length());//doesn't decipher cyrillic
                    fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayDecyphered.length = " + contentsInCharArrayDecyphered.length);
                // fos.write("\n".getBytes());
                // fos.write("How are you doing?".getBytes());
                // close the writer
                fos.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void cypherDecypherAnyTXTFile(){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(Constants.FILENAME)));
            //System.out.println(content);
            contentsInCharArray = content.toCharArray();
            contentsInCharArrayCyphered = new char[contentsInCharArray.length];
            for (int i = 0; i < contentsInCharArray.length; i++) {
                contentsInCharArrayCyphered[i] = cypherShamir(contentsInCharArray[i]);
            }
            System.out.println("Зашифрованное: ");
            for (int i = 0; i < contentsInCharArrayCyphered.length; i++) {
                System.out.print(contentsInCharArrayCyphered[i]);
            }
            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("textCiphered.txt"));

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
                contentsInCharArrayDecyphered[i] = decypherShamir(contentsInCharArrayCyphered[i]);
            }
            System.out.println("Расшифрованное: ");
            for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
               System.out.print(contentsInCharArrayDecyphered[i]);
            }//here I have a deciphered char array contentsInCharArrayDecyphered

            String inputFile = "/home/stepa/Documents/projetcs/ZI_01/binaryPDF.pdf";
            String outputFile = "/home/stepa/Documents/projetcs/ZI_01/outputt.pdf";

            String targetFile = "binary-file.pdf";

            try {
                // create a writer
                FileOutputStream fos = new FileOutputStream(new File("textDeciph.txt"));

                // write data to file
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                   // fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes(), 0,
                            //(Character.toString(contentsInCharArrayDecyphered[i])).length());//doesn't decipher cyrillic
                    fos.write((Character.toString(contentsInCharArrayDecyphered[i])).getBytes());
                }
                System.out.println("contentsInCharArrayDecyphered.length = " + contentsInCharArrayDecyphered.length);
                // fos.write("\n".getBytes());
                // fos.write("How are you doing?".getBytes());
                // close the writer
                fos.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Create a new file and override when already exists:
      /*      try (OutputStream output = openFile(targetFile)) {
                for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
                    output.write(getUtf8Bytes(Character.toString(contentsInCharArrayDecyphered[i])));
                    System.out.print(Character.toString(contentsInCharArrayDecyphered[i]));
                }

            }catch (Exception e) {
            }*/

            // Reopen the file but for appending:
     /*       try (OutputStream output = openFile(targetFile, true)) {
                output.write(getUtf8Bytes("Some more data!"));
            }catch (Exception e) {
            }*/



/*
            try (
                    InputStream inputStream = new InputStreamReader("");
                    OutputStream outputStream = new FileOutputStream(outputFile);
            ) {
                String contents = contentsInCharArrayDecyphered.toString();
                long fileSize = new File(inputFile).length();
                //byte[] allBytes = Files.readAllBytes()
                inputStream.read(contents.getBytes());
                outputStream.write(allBytes);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
*/

            /*try (
                    InputStream inputStream = new FileInputStream("/home/stepa/Documents/projetcs/ZI_01/binaryPDF.pdf");
                    OutputStream outputStream = new FileOutputStream("/home/stepa/Documents/projetcs/ZI_01/output.pdf");
            ) {

                int byteRead;

                while ((byteRead = inputStream.read()) != -1) {
                    outputStream.write(byteRead);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }*/

         /*   Path path = Paths.get("output.pdf");
            String contents = contentsInCharArrayDecyphered.toString();
            System.out.println("contents = " );*/
           // byte[] bytes = contents.getBytes();
           // System.out.println("bytes.length = " + bytes.length);

        /*    try {
                //Files.writeString(path, contents, StandardCharsets.UTF_8);
                Files.write(path, bytes);
            } catch (IOException ex) {
                // Handle exception
            }*/

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
    public char cypherShamir(char m) {// Сюда может быть передано отрицательное число, это проблема...
        this.m = m;
        System.out.println("m = " + (int) m);
        x1 = powModMine((long) m, (long) ca, (long) p);// A -> B (A вычисляет x1, шлёт Бобу, дальше аналогично)
       // System.out.println("x1 = " + x1);
        x2 = powModMine(x1, (long) cb, (long) p);// B -> A
      //  System.out.println("x2 = " + x2);
        x3 = powModMine(x2, (long) da, (long) p);// A -> B. Пусть остановка будет здесь, т.е. зашифрованный файл будет
        System.out.println("x3 = " + x3);
        // хранить в себе значения х3
        if (x3 < Character.MIN_VALUE || x3 > Character.MAX_VALUE) {
            System.out.println(" x3 is out of bounds");
        }
        //Моя ошибка, видимо, в том, что я отрицательные значения не переводил обратно в отрицательные... Как на входе
        //так и на выходе ведь это надо было проделывать... Теперь работаю с чарами, они вроде беззнаковые, но
        //всё равно не работает. Сам алгоритм перестал работать, я вручную проверял... m не равно x4...
        //Дело явно в параметрах(ключах)... Теперь х4 = m, но не получается это в файл засунуть...
        return (char) x3;
    }

    public char decypherShamir(char x3) {
        x4 = powModMine(x3, db, p);// Bob расшифровывает x4, x4 должен быть равен m в итоге
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

    public void executeCypherDecypherOnATextFile() {
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
        System.out.println("\n-----------------inside executeCypherDecypherOnATextFile()---------------------------\n");
        contentsInCharArrayDecyphered = new char[contentsInCharArray.length];
        for (int i = 0; i < contentsInCharArray.length; i++) {
            contentsInCharArrayDecyphered[i] = decypherShamir(contentsInCharArrayCyphered[i]);
        }
        System.out.println("Расшифрованное: ");
        for (int i = 0; i < contentsInCharArrayDecyphered.length; i++) {
            System.out.print(contentsInCharArrayDecyphered[i]);
        }
    }



}