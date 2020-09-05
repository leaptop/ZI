package com.company;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.pow;

public class Main {

    public static void main(String[] args) {
        System.out.println("a^x mod p = " + powMod(3, 100, 7));
        int[] g = generEucl(28, 8);
        System.out.println("gcd(a, b) = " + g[0] + ", x = " + g[1] + ", y = " + g[2]);
        int[] g2 = invers(11, 7);
        System.out.println("invers(11, 7) = " + g2[1] + ", gcd(11, 7) = " + g2[0]);
        funcDifHel(153656, 535544, 10667, 3);
        System.out.println("находим простые числа: ");
        for (int n = 1; n < 1000; n++) {
            if (isPrime(n, 4))
                System.out.print(n + " ");
        }
        System.out.println("\nНаходим \nG, P:");
        for (int i = 0; i < 3; i++) {
            int[] arr = getGP();
            System.out.println(arr[0] + ", " + arr[1]);
        }
        System.out.println("babystep = " + babystep(2, 61, 45));  ;
    }

    //метод для вычисления возведения в степень по модулю:
    public static int powMod(int a, int x, int p) {
        int y = 1;
        int s = a;
        int counter = 1;
        for (int i = 1; i <= pow(i, 31); i *= 2) {//31 - число значащих битов в int
            //System.out.println(counter++ +") x & i = "+ (x & i));// не уверен насчёт байтов(их числа для подсчёта)
            if ((x & i) >= 1) {
                y = (y * s) % p;
            }
            s = (s * s) % p;
        }
        return y;
    }

    //Утверждение 4: Пусть a & b - два целых положительных числа. Тогда существуют целые x & y, такие,
    // что ax + by = gcd(a, b)(greatest common divisor). Представленный далее метод возвращает вектор, на первом
    // месте которого наибольший общий делитель, два других - x & y.
    //метод для реализации обобщённого алгоритма Евклида(a>b, оба числа д.б. положительными) :
    public static int[] generEucl(int a, int b) {
        int[] u = {a, 1, 0};
        int[] v = {b, 0, 1};
        int q = 0;
        int[] t = {0, 0, 0};
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

    //Поиск Инверсии - это тот же обобщённый алгоритм Евклида, только ответ не м.б. отрицательным
    //Для заданных чисел c & m (с & m - взаимно простые) число d (0 < d < m) называется инверсией числа по модулю m,
    //если выполняется условие c*d mod m = 1. c - чило, d - инверсия, m - модуль.
    //Инверсия обозначается: d = (c^(-1))mod m. Метод нахождения инверсии(выход: первое число - gcd, второе - инверсия):
    public static int[] invers(int m, int c) {
        int[] u = {m, 0};
        int[] v = {c, 1};
        int[] t = {0, 0};
        int q = 0;
        while (v[0] != 0) {
            q = u[0] / v[0];
            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            System.arraycopy(v, 0, u, 0, 2);
            System.arraycopy(t, 0, v, 0, 2);
        }
        return u;
    }

    //Метод нахождения общего ключа по схеме Диффи-Хеллмана
    //Есть А - Алиса и B - Боб. Алиса шлёт Бобу закодированное сообщение: 84.
    //int p = 17, g = 3;// эти числа генерируются разработчиком системы
    // Xa & Xb, Ya & Yb - закрытые и открытые ключи соответственно Алисы и Боба
    //Открытые ключи вычислыются по формуле: Ya = (g^(Xa)) mod P

    public static int funcDifHel(int Xa, int Xb, int p, int g) {
        //Вычисляем открытые ключи и шлём друг другу:
        int Ya = powMod(g, Xa, p);
        int Yb = powMod(g, Xb, p);
        //Вычисляем общий секретный ключ:
        int Zab = powMod(Yb, Xa, p);
        int Zba = powMod(Ya, Xb, p);
        System.out.println("Ключ Алисы: " + Zab + ", Ключ Боба: " + Zba);
        return Zab;
    }

    //Проверка на простоту(тест Миллера-Рабина):
    // Возвращает ложь, если n - составное. Если возвращает истину, то n вероятно простое. Вероятность увеличивается
    //с увеличением k
    static boolean isPrime(int n, int k) {
        if (n <= 1 || n == 4)
            return false;
        if (n <= 3)
            return true;
        // Find r such that n = 2^d * r + 1
        // for some r >= 1
        int d = n - 1;

        while (d % 2 == 0)
            d /= 2;

        // Iterate given nber of 'k' times
        for (int i = 0; i < k; i++)
            if (!miillerTest(d, n))
                return false;
        return true;
    }

    static boolean miillerTest(int d, int n) {
        // Pick a random number in [2..n-2]
        // Corner cases make sure that n > 4
        int a = 2 + (int) (Math.random() % (n - 4));
        // Compute a^d % n
        int x = powMod(a, d, n);
        if (x == 1 || x == n - 1)
            return true;
        // Keep squaring x while one of the
        // following doesn't happen
        // (i) d does not reach n-1
        // (ii) (x^2) % n is not 1
        // (iii) (x^2) % n is not n-1
        while (d != n - 1) {
            x = (x * x) % n;
            d *= 2;
            if (x == 1)
                return false;
            if (x == n - 1)
                return true;
        }
        return false;
    }

    //Находим g, p:
    //G - первообразный корень по модулю P
    //вообще эти числа прописаны в стандартах, их не надо вот так каждый раз вычислять.
    public static int[] getGP() {
        int G, P;
        int Q = getLargePrimeNum();
        P = (2 * Q) + 1;
        while (!isPrime(P, 3)) {
            Q = getLargePrimeNum();
            P = 2 * Q + 1;
        }
        for (G = 2; ; G += 1) {
            if (powMod(G, Q, P) == 1) break;//получили нужный G -> break
        }
        int[] arr = {G, P};
        return arr;
    }

    public static int getLargePrimeNum() {
        int l = (int) pow(2, 31);
        int n = 0;
        do {
            n = (int) (Math.random() * l);
        } while (!isPrime(n, 4));
        return n;
    }
//   алгоритм "Шаг младенца, шаг великана"
   public static int babystep(int  a, int  p, int  y) {

        int  k = (int)Math.sqrt((double)p) +1 ;
        Map<Integer, Integer> vals = new HashMap<Integer, Integer>();
        vals.put(10, 13);
        vals.put(10, 35);
        for (int i = 1; i <= k; i++)
        //первый ряд(индекс и значение)
        vals.put(powMod(a, i * k, p), i);

        for (int j = 0; j <= k; ++j) {
            int  cur = (powMod(a, j, p) * y) % p; //второй ряд
            if (vals.containsKey(cur) ) {//если совпали то берется индекс
                int ans = vals.get(cur) * k - j; //x = i*m-j где m == k
                if (ans < p)
                    return ans;
            }
        }
        return -1;
    }
}
