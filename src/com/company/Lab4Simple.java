package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import static com.company.Lab1.*;

public class Lab4Simple {
    public static void main(String[] args) {
        Lab4Simple l4s = new Lab4Simple();
    }
    public Lab4Simple() {
        Random rnd = new Random();
        do {
            q = BigInteger.probablePrime(bitLength, rnd);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(10));
        pmo = p.subtract(BigInteger.ONE);
        this.KLength = 52;//a standard deck minus jokers
        this.m = 3;//number of cards to give to each player
        this.n = 6;// players + table + remaining deck
        k = new ArrayList<>();
        for (int i = 0; i < KLength; i++) {//created a deck
            k.add(i, BigInteger.valueOf((long) i + 2));//0 & 1 will not be ciphered normally, so the cards will have numbers from 2 to 53
        }
        players = new Player[n];//the array also keeps a table and the remaining deck
        for (int i = 0; i < n; i++) {
            players[i] = new Player(p, q, pmo, rnd, bitLength, k, i);//created n players

        }
        //Lab4Simple.printADeckInNumbers(k, KLength);
        for (int i = 0; i < n; i++) {
            players[i].cipherADeck(k);//every player ciphered the deck
            players[i].shuffle(k);//every player shuffled the deck
        }
        // Lab4Simple.printADeckInNumbers(k, KLength);
        for (int i = 0; i < n; i++) {
            //players[i].decipher(k);//every player deciphered the deck. This code was used for testing
        }
        // Lab4Simple.printADeckInNumbers(k, KLength);
        for (int i = 0; i < n - 2; i++) {
            players[i].takeMCards(k, m);//every player took m cards out of the deck
        }
        players[n - 2].takeMCards(k, 5);// 5 cards are put on the table
        players[n - 1].takeMCards(k, (KLength - ((n - 2) * m + 5)));//the remaining cards are left in the deck, but still have to be deciphered, so the deck takes them
        //all players should consistently decipher each others decks and apply the same operation to their decks finally. Metodichka pseudoCode looks incorrect
        //so I will invent my own way.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j)
                    players[i].decipherADeck(players[j].takenCards);//the i-th player deciphers others decks on the inner loop
            }
            players[i].decipherADeck(players[i].takenCards);//at the end the i-th player deciphers his own deck
        }
        for (int i = 0; i < n - 2; i++) {//print every player's cards
            System.out.println("Cards of player № " + i + ": ");
            Lab4Simple.printADeckInNames(players[i].takenCards, m);
        }
        System.out.println("A table:");
        Lab4Simple.printADeckInNames(players[n - 2].takenCards, 5);//print a table
        System.out.println("A remaining deck:");
        Lab4Simple.printADeckInNames(players[n - 1].takenCards, (KLength - ((n - 2) * m + 5)));//print the remaining cards
    //A table and the remaining cards of the deck should also be deciphered. For that I'm deciding to put those two into the array of players.
    }

    Player[] players;
    ArrayList<BigInteger> k;//a standard deck of cards(without jokers)
    BigInteger p;
    BigInteger q;
    BigInteger pmo;
    int KLength;
    int bitLength = 21;
    int n;//number of players. Can be from 2 to 23. Though I've decided to put a table and the remaining deck at the players array, so it has to be from 4 to 25
    int m;// a number of cards given to every Player

    public static void printADeckInNumbers(ArrayList<BigInteger> k, int numberOfCards) {//prints a passed deck(ArrayList<BigInteger> k) to console
        for (int i = 0; i < numberOfCards; i++) {//testing the correctness of shuffling. All works out
            System.out.print(k.get(i) + " ");
        }
        System.out.println("\n----------------------------------");
    }

    public static void printADeckInNames(ArrayList<BigInteger> k, int numberOfCards) {//prints a passed deck(ArrayList<BigInteger> k) to console
        for (int i = 0; i < numberOfCards; i++) {
            switch (k.get(i).toString()) {
                case "2":
                    System.out.printf("2 Diamonds; ");
                    break;
                case "3":
                    System.out.printf("3 Diamonds; ");
                    break;
                case "4":
                    System.out.printf("4 Diamonds; ");
                    break;
                case "5":
                    System.out.printf("5 Diamonds; ");
                    break;
                case "6":
                    System.out.printf("6 Diamonds; ");
                    break;
                case "7":
                    System.out.printf("7 Diamonds; ");
                    break;
                case "8":
                    System.out.printf("8 Diamonds; ");
                    break;
                case "9":
                    System.out.println("9 Diamonds; ");
                    break;
                case "10":
                    System.out.println("10 Diamonds; ");
                    break;
                case "11":
                    System.out.printf("Jack Diamonds; ");
                    break;
                case "12":
                    System.out.printf("Queen Diamonds; ");
                    break;
                case "13":
                    System.out.printf("King Diamonds; ");
                    break;
                case "14":
                    System.out.printf("Ace Diamonds; ");
                    break;
                case "15":
                    System.out.printf("3 Spades; ");
                    break;
                case "16":
                    System.out.printf("4 Spades; ");
                    break;
                case "17":
                    System.out.printf("5 Spades; ");
                    break;
                case "18":
                    System.out.printf("6 Spades; ");
                    break;
                case "19":
                    System.out.printf("7 Spades; ");
                    break;
                case "20":
                    System.out.printf("8 Spades; ");
                    break;
                case "21":
                    System.out.printf("10 Spades; ");
                    break;
                case "22":
                    System.out.printf("Jack Spades; ");
                    break;
                case "23":
                    System.out.printf("Queen Spades; ");
                    break;
                case "24":
                    System.out.printf("King Spades; ");
                    break;
                case "25":
                    System.out.printf("Ace Spades; ");
                    break;
                case "26":
                    System.out.printf("2 Hearts; ");
                    break;
                case "27":
                    System.out.printf("3 Hearts; ");
                    break;
                case "28":
                    System.out.printf("4 Hearts; ");
                    break;
                case "29":
                    System.out.printf("5 Hearts; ");
                    break;
                case "30":
                    System.out.printf("6 Hearts; ");
                    break;
                case "31":
                    System.out.printf("7 Hearts; ");
                    break;
                case "32":
                    System.out.printf("8 Hearts; ");
                    break;
                case "33":
                    System.out.printf("10 Hearts; ");
                    break;
                case "34":
                    System.out.printf("Jack Hearts; ");
                    break;
                case "35":
                    System.out.printf("Queen Hearts; ");
                    break;
                case "36":
                    System.out.printf("King Hearts; ");
                    break;
                case "37":
                    System.out.printf("Ace Hearts; ");
                    break;
                case "38":
                    System.out.printf("2 Clubs; ");
                    break;
                case "39":
                    System.out.printf("3 Clubs; ");
                    break;
                case "40":
                    System.out.printf("4 Clubs; ");
                    break;
                case "41":
                    System.out.printf("5 Clubs; ");
                    break;
                case "42":
                    System.out.printf("6 Clubs; ");
                    break;
                case "43":
                    System.out.printf("7 Clubs; ");
                    break;
                case "44":
                    System.out.printf("8 Clubs; ");
                    break;
                case "45":
                    System.out.printf("10 Clubs; ");
                    break;
                case "46":
                    System.out.printf("Jack Clubs; ");
                    break;
                case "47":
                    System.out.printf("Queen Clubs; ");
                    break;
                case "48":
                    System.out.printf("King Clubs; ");
                    break;
                case "49":
                    System.out.printf("9 Clubs; ");
                    break;
                case "50":
                    System.out.printf("2 spades; ");
                    break;
                case "51":
                    System.out.printf("9 spades; ");
                    break;
                case "52":
                    System.out.printf("Ace clubs; ");
                    break;
                case "53":
                    System.out.printf("9 hearts; ");
                    break;
            }
        }
        System.out.println("\n----------------------------------");
    }
}

class Player {
    public Player(BigInteger p, BigInteger q, BigInteger pmo, Random rnd, int bitLength, ArrayList<BigInteger> k, int serialNumber) {
        do {
            c = BigInteger.probablePrime(bitLength, rnd);
        } while (gcd2BigInteger(c, pmo).compareTo(BigInteger.ONE) != 0);
        d = c.modInverse(pmo);
        this.rnd = rnd;
        this.p = p;
        this.serialNumber = serialNumber;
        deckSize = k.size();
        takenCards = new ArrayList<>(52);
    }

    public ArrayList<BigInteger> takenCards;
    public BigInteger p;
    public Random rnd;
    public BigInteger c;
    public BigInteger d;
    public BigInteger temp;
    public int deckSize;
    public int serialNumber;

    public void shuffle(ArrayList<BigInteger> k) {
        for (int i = 0; i < 100; i++) {//randomly change card places 100 times
            int a = rnd.nextInt(52);
            int b = rnd.nextInt(52);
            temp = k.get(a);
            k.set(a, k.get(b));
            k.set(b, temp);
        }
        // Lab4Simple.printADeckInNumbers(k, k.size());
    }

    public void cipherADeck(ArrayList<BigInteger> k) {
        for (int i = 0; i < k.size(); i++) {
            k.set(i, k.get(i).modPow(c, p));
        }
    }

    public void decipherIAndJDifferently(ArrayList<BigInteger> k) {//it's not the right way apparently... players should invoke their decipher methods in a certain order
        int i = serialNumber;
        int j;
        for (j = 0; j < k.size(); j++) {
            if (j != i) {
                k.set(j, k.get(j).modPow(d, p));
            }
        }
        k.set(j, k.get(j).modPow(d, p));
    }

    public void decipherADeck(ArrayList<BigInteger> k) {
        for (int i = 0; i < k.size(); i++) {
            k.set(i, k.get(i).modPow(d, p));
        }
    }

    public BigInteger decipherACard(BigInteger card) {
        return card.modPow(d, p);
    }

    public void takeMCards(ArrayList<BigInteger> k, int m) {
        int iLeft = k.size();
        int iTaken = takenCards.size();
        int mUsed = m - 1;
        for (int i = 0; i < m; i++) {//I didn't know the way to replace the cards in a more concise way
            takenCards.add(iTaken + i, k.get(mUsed));// even if there are already cards, I can still add more to the needed index,
            // and taking it from the end of the given deck
            k.remove(mUsed--);
        }
    }
}