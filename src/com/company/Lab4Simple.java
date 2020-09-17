package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import static com.company.Lab1.*;

public class Lab4Simple {
    public Lab4Simple() {
        Random rnd = new Random();
        do {
            q = BigInteger.probablePrime(bitLength, rnd);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(10));
        pmo = p.subtract(BigInteger.ONE);
        this.KLength = 52;
        k = new ArrayList<>();
        for (int i = 0; i < KLength; i++) {
            k.add(i, BigInteger.valueOf((long) i));
        }
        players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player(p, q, pmo, rnd, bitLength, KLength, k);
            players[i].shuffle(k);
        }
        Lab4Simple.printADeck(k);
        for (int i = 0; i < n; i++) {
            players[i].cipher(k);
        }
        Lab4Simple.printADeck(k);
        for (int i = 0; i < n; i++) {
            players[i].decipher(k);
        }
        Lab4Simple.printADeck(k);
    }

    Player[] players;
    ArrayList<BigInteger> k;//a deck of cards
    BigInteger p;
    BigInteger q;
    BigInteger pmo;
    int KLength;
    int bitLength = 21;
    int n = 4;//number of players. Can be from 2 to 23
    int mi = 2;// a number of cards given to every Player

    public static void printADeck(ArrayList<BigInteger> k) {
        for (int i = 0; i < 52; i++) {//testing the correctness of shuffling. All works out
            System.out.print(k.get(i) + " ");
        }
        System.out.println("\n----------------------------------");
    }

    public static void main(String[] args) {
        Lab4Simple l4s = new Lab4Simple();

    }
}

class Player {
    public Player(BigInteger p, BigInteger q, BigInteger pmo, Random rnd, int bitLength, int KLength, ArrayList<BigInteger> k) {
        do {
            c = BigInteger.probablePrime(bitLength, rnd);
        } while (gcd2BigInteger(c, pmo).compareTo(BigInteger.ONE) != 0);
        d = c.modInverse(pmo);
        this.rnd = rnd;
        this.p = p;
        deckSize = k.size();
    }
    public ArrayList<BigInteger> takenCards;
    public BigInteger p;
    public Random rnd;
    public BigInteger c;
    public BigInteger d;
    public BigInteger temp;
    public int deckSize;

    public void shuffle(ArrayList<BigInteger> k) {
        for (int i = 0; i < 100; i++) {//randomly change card places 100 times
            int a = rnd.nextInt(52);
            int b = rnd.nextInt(52);
            temp = k.get(a);
            k.set(a, k.get(b));
            k.set(b, temp);
        }
        Lab4Simple.printADeck(k);
    }

    public void cipher(ArrayList<BigInteger> k) {
        for (int i = 0; i < deckSize; i++) {
            k.set(i, k.get(i).modPow(c, p));
        }
    }
    public void decipher(ArrayList<BigInteger> k) {
        for (int i = 0; i < deckSize; i++) {
            k.set(i, k.get(i).modPow(d, p));
        }
    }
}