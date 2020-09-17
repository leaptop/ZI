/*
package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Lab4 {
}

class Card {
    public Card(int lead, int value) {
        this.lead = lead;
        this.value = value;
    }

    public int lead;
    public int value;

    public void write() {
        switch (lead) {
            case 0:
                System.out.printf("S");

                break;

            case 1:
                System.out.printf("C");

                break;

            case 2:
                System.out.printf("H");

                break;

            case 3:
                System.out.printf("D");

                break;
        }

        switch (value) {
            case 14:
                System.out.printf("A");

                break;

            case 13:
                System.out.printf("K");

                break;

            case 12:
                System.out.printf("Q");

                break;

            case 11:
                System.out.printf("J");

                break;

            default:
                System.out.printf("%llu", value);

                break;
        }
    }
}

class Dealer {
    ArrayList<Player> players;
    ArrayList<Card> deck;
    ArrayList<Card> table;

    public void init(int playersNumber) {
        players.clear();
        deck.clear();
        table.clear();
        BigInteger q, p;
        Random rnd = new Random();
        do {
            q = BigInteger.probablePrime(21, rnd);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(10));
        for (int i = 0; i < playersNumber; i++) {
            players.add(new Player(p));
        }
        for (int l = 0; l < 4; ++l) {
            for (int v = 2; v < 15; ++v) {
                deck.add(new Card(l, v));
            }
        }
    }
    public void shuffle(){
        for (Player player:players
             ) {
            player.shuffle(deck);
        }
    }
    public void deal(){
        for (Player player:players
             ) {
            player.pickCards(deck, players);
        }
        for (int i = 0; i < 5; ++i) {
            //table.
        }
    }

}

class Player {
    public Player(BigInteger p) {

    }
    public void shuffle(ArrayList<Card> deck){

    }
    public void pickCards(ArrayList<Card> deck, ArrayList<Player> players){

    }
}*/
