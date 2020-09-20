package com.company;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class RGR01 {
    public RGR01(){
         rnd = new Random();
         keyVernam = rnd.nextInt(20);//the size of the key has to be of the same range as the vertices number
    }
    ArrayList<Vertice> vertices;
    ArrayList<Edge> edges;
    public int verticesNum;
    public int edgesNum;
    public String[] tokens;
    public int[] hamiltonianCycle;
    ArrayList<Edge> edgesIsomorphic;
    public int[] hamiltonianCycleIsomorphic;
    private int keyVernam;//a key for Vernam ciphering
    Random rnd;

    public static void main(String[] args) {
        RGR01 rgr = new RGR01();
        rgr.tokens = rgr.readAFile();
        rgr.createVerticesEdgesAndReadHamiltonianCycle();
        rgr.createIsomorphicGraphAndItsHamiltonianCycle();
       // rgr.printAll();
        rgr.printOriginalAndIsomorphicGraphs();
        rgr.checkTheGraph();
    }
public void printOriginalAndIsomorphicGraphs(){
    System.out.println("Original graph  Isomorphic graph");
    for (int i = 0; i < edgesNum; i++) {
        System.out.println(edges.get(i).v1Int + " " + edges.get(i).v2Int + "             " + edgesIsomorphic.get(i).v1Int + " " + edgesIsomorphic.get(i).v2Int);
    }
}
    public void checkTheGraph(){
        int ZeroOrOne = rnd.nextInt(2);//randomly decide whether to show the cycle, or to show the isomorphism
        if(ZeroOrOne == 0){
            System.out.println("\nThe cycle is: ");
            for (int i = 0; i < verticesNum; i++) {
                System.out.print(hamiltonianCycleIsomorphic[i] + " ");
            }
        }else{
            System.out.println("\nThe Vernam key is: " + keyVernam);
            System.out.println("\nThe deciphered graph is: ");
            for (int i = 0; i < edgesNum; i++) {
                System.out.println(decipherVernam(edgesIsomorphic.get(i).v1Int) + " " + decipherVernam(edgesIsomorphic.get(i).v2Int));
            }
        }
    }
    public int cipherVernam(int m) {
        return m ^ keyVernam;
    }
    public int decipherVernam(int m){
        return m ^ keyVernam;
    }

    public void printAll() {
        System.out.println("Original graph  Isomorphic graph");
        for (int i = 0; i < edgesNum; i++) {
            System.out.println(edges.get(i).v1Int + " " + edges.get(i).v2Int + "             " + edgesIsomorphic.get(i).v1Int + " " + edgesIsomorphic.get(i).v2Int);
        }
        System.out.println("Original Hamiltonian Cycle: ");
        for (int i = 0; i < verticesNum; i++) {
            System.out.print(hamiltonianCycle[i] + " ");
        }
        System.out.println("\nCiphered(isomorphic) Hamiltonian cycle: ");
        for (int i = 0; i < verticesNum; i++) {
            System.out.print(hamiltonianCycleIsomorphic[i] + " ");
        }
        System.out.println("\nA key for Vernam deciphering is: " + keyVernam);
    }

    public void createIsomorphicGraphAndItsHamiltonianCycle() {
        edgesIsomorphic = new ArrayList<>(edgesNum);
        for (int i = 0; i < edgesNum; i++) {
            int v1 = cipherVernam(edges.get(i).v1Int) ;
            int v2 = cipherVernam(edges.get(i).v2Int) ;
            edgesIsomorphic.add(i, new Edge(v1, v2));
        }
        hamiltonianCycleIsomorphic = new int[edgesNum];
        for (int i = 0; i < verticesNum; i++) {
            hamiltonianCycleIsomorphic[i] = cipherVernam(hamiltonianCycle[i]);
        }
    }

    public void createVerticesEdgesAndReadHamiltonianCycle() {
        verticesNum = Integer.parseInt(tokens[0]);
        edgesNum = Integer.parseInt(tokens[1]);
        this.vertices = new ArrayList<>(verticesNum);//the code is redundant, but more understandable
        this.edges = new ArrayList<>(edgesNum);
        for (int i = 0; i < verticesNum; i++) {
            vertices.add(i, new Vertice());
        }
        for (int i = 0; i < edgesNum * 2; i += 2) {
            edges.add(new Edge(Integer.parseInt(tokens[2 + i]), Integer.parseInt(tokens[3 + i])));
        }
        for (Edge edge : edges) {
            // System.out.println(edge.name);
        }
        hamiltonianCycle = new int[verticesNum];
        for (int i = 0; i < verticesNum; i++) {
            hamiltonianCycle[i] = Integer.parseInt(tokens[Integer.parseInt(tokens[1]) * 2 + i + 2]);
            // System.out.println(" hamiltonianCycle[i] = " + hamiltonianCycle[i]);
        }
    }

    public String[] readAFile() {
        Path path = Path.of("/home/stepa/Documents/projetcs/ZI_01/src/resources/graph.txt");
        System.out.println(path);
        boolean exists = Files.exists(path);
        //System.out.println("exists = " + exists);
        String s;
        String[] tokens = new String[1];
        try {
            s = Files.readString(path);// UTF 8
            //   System.out.println("s = \n" + s);
            String delims = "[ \n]+";
            tokens = s.split(delims);

        } catch (Exception e) {
        }
        //System.out.println("tokens = \n");
        for (String str : tokens
        ) {
            //System.out.println(str);
        }
        return tokens;
    }

    public void checkPath() {

    }

    public void checkIsomorphism() {

    }
}

class Edge {
    public Edge(int v1, int v2) {
        this.v1Int = v1;
        this.v2Int = v2;
        this.name = v1 + " " + v2;
    }

    String name;
    public Vertice v1;
    public Vertice v2;
    public int v1Int;
    public int v2Int;
}

class Vertice {
    String name;
}