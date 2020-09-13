package com.company;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.company.Lab2.*;
import static com.company.ShamirChar.*;

import static java.lang.Math.pow;

public class Lab1 {

    public static void main(String[] args) {
        System.out.println("a^x mod p = " + powModMine(3, 100, 7));
        System.out.println("a^x mod p = " + powModMine(2, 34, 61));
        long[] g = generEucl(28, 8);
        System.out.println("gcd(a, b) = " + g[0] + ", x = " + g[1] + ", y = " + g[2]);
        long[] g2 = invers(11, 7);
        System.out.println("invers(11, 7) = " + g2[1] + ", gcd(11, 7) = " + g2[0]);
        funcDifHel(153656, 535544, 10667, 3);
        System.out.println("находим простые числа: ");
        for (int n = 1; n < 1000; n++) {
            if (isPrime(n, 4))
                System.out.print(n + " ");
        }
        System.out.println("\nНаходим \nG, P:");
        for (int i = 0; i < 1; i++) {
            long[] arr = getGP();
            System.out.println(arr[0] + ", " + arr[1]);
        }
       /* System.out.println("babystep(5, 7, 1) = " + babyStep(5, 7, 1));// 6 incorrect. Must be 12. My solving by hand
        System.out.println("babystep(5, 7, 4) = " + babyStep(5, 7, 4));// -1 incorrect. Must be 20. Metodichka
        System.out.println("babystep(10, 23, 14) = " + babyStep(10, 23, 14));// 7
        System.out.println("babystep(14, 23, 15) = " + babyStep(14, 23, 15));// 5
        System.out.println("babystep(15, 23, 19) = " + babyStep(15, 23, 19));// 19
        System.out.println("babystep(19, 23, 10) = " + babyStep(19, 23, 10));// 9
        System.out.println("babystep(3, 7, 4) = " + babyStep(3, 7, 4));// 4 incorrect. Must be 100. Ryabko
        System.out.println("babystep(5, 7, 1) = " + babyStepGiantStep(5, 7, 1));// 6 incorrect. Must be 12.
        */
        System.out.println("babystep(14, 23, 15) = " + babyStepGiantStep(14, 23, 15));// 5
        System.out.println("babystep(2344, 233, 52523) = " + babyStepGiantStep(2344, 233, 52523));// 5
        System.out.println("babystep(1432, 23323, 14535) = " + babyStepGiantStep(1432, 23323, 14535));// 5
    }

    //метод для вычисления возведения в степень по модулю:
    public static long powModMine(long a, long x, long p) {
        if (a < 0) {
            System.out.println("                        inside powModMine(): a<0");
        } else if (x < 0) {
            System.out.println("                      inside powModMine():    x<0");
        } else if (p < 0) {
            System.out.println("                         inside powModMine():   p<0");
        }
        BigInteger y = new BigInteger("1");
        BigInteger s = BigInteger.valueOf(a);
        BigInteger pp = BigInteger.valueOf(p);
        while (x > 0) {
            if ((x & 1) >= 1) {//вычленяю из икс степени двойки(перемножая икс по очереди на эти степени в
                // двоичном виде). Как только нахожу, что степень есть в составе икс(стоит единичка в двоичном
                // представлении икс), то умножаю её на соответсвующий
                //остаток от s(a) в этой степени(на каждой итерации s принимает значение этого остатка, а из этих
                // значений соответственно составлен ряд a^1, a^2, ... a^ последняя, по этому ряду можно то же делать
                // на бумаге) .
                y = y.multiply(s).mod(pp);
            }
            x >>= 1;
            s = s.multiply(s).mod(pp);
            //остаток от s(a) в этой степени(на каждой итерации s принимает значение этого остатка, а из этих
            // значений соответственно составлен ряд a^1, a^2, ... a^ последняя, по этому ряду можно то же делать
            // на бумаге) .
        }
        return y.longValue();
    }

    public static long powModMineUsingBigInteger(long a, long x, long p) {
        BigInteger y = new BigInteger("1");
        BigInteger s = BigInteger.valueOf(a);
        BigInteger pp = BigInteger.valueOf(p);
        while (x > 0) {
            if ((x & 1) >= 1) {
                y = y.multiply(s).mod(pp);
            }
            x >>= 1;
            s = s.multiply(s).mod(pp);
        }
        return y.longValue();
    }
    public static BigInteger powModMBigInteger(BigInteger a, BigInteger x, BigInteger p) {
        BigInteger y = new BigInteger("1");
        BigInteger s = a;
        BigInteger pp = p;
       // while (x.compareTo(BigInteger.ZERO) > 0) {//Check if x > 0
            //if ((x & 1) >= 1) {//Here I only have a BigInteger function getLowestSetBit, that returns the number
            //of a bit with the first meaning "1". So the implementation is going to be different. Though, apparently
            //there is also powMod function in the Biginteger library... So, I'll just return it))
       // }
        return a.modPow(x, p);
    }

    public static long gcd2(long p, long q) {// простой алгоритм Евклида
        if (p < 0 || q < 0) {
            System.out.println("                      inside gcd2(): p<0||q<0)");
        }
        while (q != 0) {
            long temp = q;
            q = p % q;
            p = temp;
        }
        return p;
    }
    public static BigInteger gcd2BigInteger(BigInteger p, BigInteger q) {
        if (p.compareTo(BigInteger.ZERO) < 0 || q.compareTo(BigInteger.ZERO) < 0) {
            System.out.println("                      inside gcd2BigInteger(): p<0||q<0)");
        }
        while (q.compareTo(BigInteger.ZERO) != 0) {
            BigInteger temp = q;
            q = p.mod(q) ;
            p = temp;
        }
        return p;
    }
    //Утверждение 4: Пусть a & b - два целых положительных числа. Тогда существуют целые x & y, такие,
    // что ax + by = gcd(a, b)(greatest common divisor). Представленный далее метод возвращает вектор, на первом
    // месте которого наибольший общий делитель, два других - x & y.
    //метод для реализации обобщённого алгоритма Евклида(a>b, оба числа д.б. положительными) :
    public static long[] generEucl(long a, long b) {
        if (a < 0 || b < 00) {
            System.out.println("                               inside generEucl: (a < 0 || b < 0)");
        }
        long[] u = {a, 1, 0};
        long[] v = {b, 0, 1};
        long q = 0;
        long[] t = {0, 0, 0};
        while (v[0] != 0) {
            q = u[0] / v[0];
            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            t[2] = u[2] - q * v[2];
            System.arraycopy(v, 0, u, 0, 3);
            System.arraycopy(t, 0, v, 0, 3);
        }
        return u;
    }

    public static long generEuclInt(long a, long b) {
        long[] u = {a, 1, 0};
        long[] v = {b, 0, 1};
        long q = 0;
        long[] t = {0, 0, 0};
        while (v[0] != 0) {
            q = u[0] / v[0];
            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            t[2] = u[2] - q * v[2];
            System.arraycopy(v, 0, u, 0, 3);
            System.arraycopy(t, 0, v, 0, 3);
        }
        return u[0];
    }
    //Поиск Инверсии - это тот же обобщённый алгоритм Евклида, только ответ не м.б. отрицательным
    //Для заданных чисел c & m (с & m - взаимно простые) число d (0 < d < m) называется инверсией числа по модулю m,
    //если выполняется условие c*d mod m = 1. c - чило, d - инверсия, m - модуль.
    //Инверсия обозначается: d = (c^(-1))mod m. Метод нахождения инверсии(выход: первое число - gcd, второе - инверсия):
    public static long[] invers(long m, long c) {
        if (m < 0 || c < 0) {
            System.out.println("                                  inside invers(): m<0||c<0");
        }
        long[] u = {m, 0};
        long[] v = {c, 1};
        long[] t = {0, 0};
        long q = 0;
        while (v[0] != 0) {
            q = u[0] / v[0];
            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            System.arraycopy(v, 0, u, 0, 2);
            System.arraycopy(t, 0, v, 0, 2);
        }
        if (u[1] < 0) {
            u[1] = u[1] + m;
        }
        return u;
    }
    //Метод нахождения общего ключа по схеме Диффи-Хеллмана
    //Есть А - Алиса и B - Боб. Алиса шлёт Бобу закодированное сообщение: 84.
    //int p = 17, g = 3;// эти числа генерируются разработчиком системы
    // Xa & Xb, Ya & Yb - закрытые и открытые ключи соответственно Алисы и Боба
    //Открытые ключи вычислыются по формуле: Ya = (g^(Xa)) mod P
    public static long funcDifHel(long Xa, long Xb, long p, long g) {
        //Вычисляем открытые ключи и шлём друг другу:
        long Ya = powModMine(g, Xa, p);
        long Yb = powModMine(g, Xb, p);
        //Вычисляем общий секретный ключ:
        long Zab = powModMine(Yb, Xa, p);
        long Zba = powModMine(Ya, Xb, p);
        System.out.println("Ключ Алисы: " + Zab + ", Ключ Боба: " + Zba);
        return Zab;
    }
    //Проверка на простоту(тест Миллера-Рабина):
    // Возвращает ложь, если n - составное. Если возвращает истину, то n вероятно простое. Вероятность увеличивается
    //с увеличением k
    static boolean isPrime(long n, long k) {
        if (n <= 1 || n == 4)//сначала простые проверки на числа от 1 до 4, потом на чётность(чётные отметаются)
            return false;
        if (n <= 3)
            return true;
// Find r such that n = 2^d * r + 1
        // for some r >= 1
        long d = n - 1;

        while (d % 2 == 0)
            d /= 2;
// Iterate given nber of 'k' times
        for (int i = 0; i < k; i++)
            if (!miillerTest(d, n))
                return false;
        return true;
    }

    static boolean miillerTest(long d, long n) {
        long a = 2 + (long) (Math.random() % (n - 4));

        long x = powModMine(a, d, n);
        if (x == 1 || x == n - 1)
            return true;
// Keep squaring x while one of the
        // following doesn't happen
        // (i) d does not reach n-1
        // (ii) (x^2) % n is not 1
        // (iii) (x^2) % n is not n-1
        while (d != n - 1) {
            // Pick a random number in [2..n-2]
            // Corner cases make sure that n > 4
            x = (x * x) % n;
            d *= 2;
            if (x == 1)
                return false;
            if (x == n - 1)
                return true;
        }
        return false;
    }
    /*static boolean miillerTestBigInteger(BigInteger d, BigInteger n) {
        BigInteger a = 2 +  (Math.random() % (n - 4));

        BigInteger x = x.modPow(d, n);
        if (x == 1 || x == n - 1)
            return true;

        while (d != n - 1) {
            x = (x * x) % n;
            d *= 2;
            if (x == 1)
                return false;
            if (x == n - 1)
                return true;
        }
        return false;
    }*/

    //Находим g, p:
    //G - первообразный корень по модулю P
    //вообще эти числа прописаны в стандартах, их не надо вот так каждый раз вычислять.
    public static long[] getGP() {
        long G, P;
        long Q = getLargePrimeNum();
        P = (2 * Q) + 1;
        while (!isPrime(P, 3)) {
            Q = getLargePrimeNum();
            P = 2 * Q + 1;
        }
        for (G = 2; ; G += 1) {
            if (powModMine(G, Q, P) == 1) break;//получили нужный G -> break
        }
        long[] arr = {G, P};
        return arr;
    }

    public static char[] getGPChar() {
        char G, P;
        char Q = getPrimeChar();
        P = (char) ((2 * Q) + 1);
        while (!isPrime(P, 3) && P > 0) {
            Q = getPrimeChar();
            P = (char) (2 * Q + 1);
        }
        for (G = 2; ; G += 1) {
            if (powModMine(G, Q, P) == 1) break;
        }
        char[] arr = {G, P};
        return arr;
    }

    public static char getPrimeChar() {
        char l = (char) ((char) pow(2, 17) - 1);//Задаю порядок возвращаемого числа в виде степени двойки
        char n = 0;
        do {
            n = (char) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4));
        // System.out.println("inside getPrimeByte n = " + n);
        return n;
    }

    public static char getPrimeChar(int powerOfTwo) {
        char l = (char) ((char) pow(2, powerOfTwo) - 1);//Задаю порядок возвращаемого числа в виде степени двойки
        char n = 0;
        do {
            n = (char) (Math.random() * l);
            if (n < 0) n *= -1;
        } while (!isPrime(n, 4));
        // System.out.println("inside getPrimeByte n = " + n);
        return n;
    }

    public static long getLargePrimeNum() {
        long l = (long) pow(2, 20);//Задаю порядок возвращаемого числа в виде степени двойки
        long n = 0;
        do {
            n = (long) (Math.random() * l);
        } while (!isPrime(n, 4));
        return n;
    }

    public static long getLargePrimeNum(int power) {
        long l = (long) pow(2, power);
        long n = 0;
        do {
            n = (long) (Math.random() * l);
        } while (!isPrime(n, 4));
        return n;
    }

    //   алгоритм "Шаг младенца, шаг великана"
    // ищем x в выражении y = a^x mod p по данным a, p, y.
    public static long babyStepGiantStep(long a, long p, long y) {
        //1: Здесь всё тупо сделано по методичке
        long k = (long) Math.ceil(Math.sqrt((double) p));//m = k = sqrt(p)
        System.out.println("\nInside babyStep(): k = m = " + k);
        //2:
        int cnt1 = 0;
        int cnt2 = 0;
        int cnt3 = 0;
        long[] ya = new long[(int) k];
        long[] am = new long[(int) k];
        for (int j = 0; j <= (k - 1); j++) {
            cnt1++;
            ya[j] = (y * powModMine(a, j, p)) % p;
            // System.out.println("ya[j] = " + ya[j]);
        }
        for (int i = 0; i < k; i++) {
            cnt2++;
            am[i] = powModMine(a, (i + 1) * k, p);
            // System.out.println("am[i] = " + am[i]);
        }
        //3: Ищем i & j такие, что a^(i*m) == a^(j)*y Здесь опять наверное имеется в виду, что от второго берётся % p...
//Число x = i*m - j - решение уравнения a^x = y(mod P), т.е. то, что должна находить функция babyStep
        Map<Long, Long> dict = new HashMap<Long, Long>();
        for (int i = 0; i < k; ++i) {
            dict.put(ya[i], (long) i);
        }
        for (int i = 0; i < k; ++i) {//там какая-то хрень с индексами... В методичке написано, что во втором массиве
            //(с индексами i, видимо) индексация идёт с 1. Т.о. принял решение просто прибавить 1 к i в return
            cnt3++;
            //System.out.println("i = " + i);
            if (dict.containsKey(am[i])) {
                System.out.println(" i = " + i + ", dict[am[" + i + "] = dict.get(am[" + i + "] = " + dict.get(am[i]));
                System.out.println("p = " + p + ", (sqrt(P) * log2(P)) = " + Math.sqrt((double) p) * customLog(2, (double) p));
                System.out.println("cnt1 = " + cnt1);
                System.out.println("cnt2 = " + cnt2);
                System.out.println("cnt3 = " + cnt3);
                return (i + 1) * k - dict.get(am[i]);
            }
        }
        System.out.println("Couldn't solve");
        return -1;
    }

    private static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }
}
