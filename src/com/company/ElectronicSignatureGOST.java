package com.company;

import java.math.BigInteger;
import java.util.Random;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;
import static java.util.stream.LongStream.iterate;
import java.security.MessageDigest;
import static java.lang.Math.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import static java.lang.Math.pow;

public class ElectronicSignatureGOST {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MD4 md4 = new MD4();
        System.out.println("Input file:");
        File inputFile = new File(scanner.nextLine());
        System.out.println("Action:");
        System.out.println("1. Generate signature");
        System.out.println("2. Verify signature");
        int action = Integer.parseInt(scanner.nextLine());
        if(action == 1) {
            LinearCongruentialGenerator g = new LinearCongruentialGenerator(
                    0x3DFC46F1,
                    97781173,
                    0xD,
                    (long) pow(2, 32)
            );
            Generator generator = new Generator(g);
            BigInteger[] primes = generator.generatePrimes1024();
            BigInteger p = primes[0];
            BigInteger q = primes[1];
            BigInteger a = generator.generateA(p, q);
            System.out.println("File for p, q, a:");
            String fileName = scanner.nextLine();
            File file = new File(fileName);
            String params = p.toString(16) + "\n" + q.toString(16) + "\n" + a.toString(16);
            writeToFile(file, params);
            GOSTSignature ds = new GOSTSignature(p, q, a);
            System.out.println("Key generation:");
            System.out.println("1. Random");
            System.out.println("2. By password");
            int answer = Integer.parseInt(scanner.nextLine());
            BigInteger x;
            if(answer == 1) {
                System.out.println("Enter the length of the key:");
                int length = Integer.parseInt(scanner.nextLine());
                x = ds.getRandomPrivateKey(length);
            } else if(answer == 2) {
                System.out.println("Enter password");
                String password = scanner.nextLine();
                String passwordHash = md4.toHexString(md4.digest(password.getBytes()));
                System.out.println("Password hash: " + passwordHash);
                x = ds.getPrivateKeyByPassword(passwordHash);
            } else {
                throw new RuntimeException("Unknown action");
            }
            BigInteger y = ds.getPublicKey(x);
            System.out.println("File for public key:");
            fileName = scanner.nextLine();
            File publicKey = new File(fileName);
            writeToFile(publicKey, y.toString(16));
            byte[] message = readFromFile(inputFile);
            md4.engineUpdate(message, 0, message.length);
            String hash = md4.toHexString(md4.engineDigest());
            String signature = ds.sign(hash, x);
            System.out.println("File for signature:");
            fileName = scanner.nextLine();
            file = new File(fileName);
            writeToFile(file, signature);
            System.out.println("Hash:" + hash);
            System.out.println("Signature:" + signature);
        } else if(action == 2) {
            System.out.println("File with p, q, a:");
            File file = new File(scanner.nextLine());
            byte[] data = readFromFile(file);
            String[] params = new String(data).split("\n");
            BigInteger p = new BigInteger(params[0], 16);
            BigInteger q = new BigInteger(params[1], 16);
            BigInteger a = new BigInteger(params[2], 16);
            System.out.println("File with public key:");
            file = new File(scanner.nextLine());
            data = readFromFile(file);
            BigInteger y = new BigInteger(new String(data), 16);
            GOSTSignature ds = new GOSTSignature(p, q, a);
            data = readFromFile(inputFile);
            md4.engineUpdate(data, 0, data.length);
            String hash = md4.toHexString(md4.engineDigest());
            System.out.println("File with signature:");
            file = new File(scanner.nextLine());
            data = readFromFile(file);
            String signature = new String(data);
            System.out.println("Hash:" + hash);
            System.out.println("Verification of a signature: ");
            boolean result = ds.verify(hash, signature, y);
            if(result) {
                System.out.print("OK");
            } else {
                System.out.print("FAIL");
            }
        } else {
            throw new RuntimeException("Unknown action");
        }
    }

    private static void writeToFile(File file, String data) {
        try {
            Files.write(file.toPath(), data.getBytes(), StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFromFile(File file) {
        byte[] data = null;
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
 class ElectronicSignatureGOSTInitial{
    public static void main2(String[] args) {
    }
    BigInteger p;//open
    BigInteger q;//open
    BigInteger b;//open
    BigInteger a;
    BigInteger g;
    BigInteger x;//closed key of a user
    BigInteger y;//open key of a user for checking
    BigInteger k;
    String h;
    BigInteger r;
    BigInteger s;
    BigInteger u1;
    BigInteger u2;
    BigInteger v;
    public void sign(){
        Random rnd = new Random();
        q = BigInteger.probablePrime(256, rnd);
        p = BigInteger.probablePrime(1024, rnd);
        b = (p.subtract(BigInteger.ONE)).divide(q);//it's not a fact, that b is gonna be integer...
        BigInteger bg = (p.subtract(BigInteger.ONE)).mod(q);
        System.out.println("q = " +q);
        System.out.println("p = " +p);
        System.out.println("b = " +b);
        System.out.println("bg = " + bg);//ofcourse, it has a modulus. So it's not the way of implementing it...


    }

}


class Generator {

    private LinearCongruentialGenerator generator;

    Generator(LinearCongruentialGenerator generator) {
        this.generator = generator;
    }

    private BigInteger[] generatePrimes512() {//generates 5 prime numbers of 10, 20, 40, 80, 160 decimal digits( 160 is the first in the list, 80 - the second, etc)
        int bitLength = 512;
        List<Integer> t = new ArrayList<>();
        t.add(bitLength);
        int index = 0;
        while(t.get(index) >= 33) {
            int value = (int) floor(t.get(index) / 2);
            System.out.println("inside generatePrimes512(): value = " + value);
            t.add(value);
            index++;
        }
        BigInteger[] primes = new BigInteger[t.size()];//created an array of size index?
        primes[index] = BigInteger.probablePrime(t.get(index), new Random());//assigned the last element as a prime of 32 bits(last inserted value in t)
        System.out.println("primes[index] = "+primes[index]);//2^32 ~= 4 billions
        int m = index - 1;
        boolean flag = true;
        do {
            int r = (int) Math.ceil(t.get(m) / 32);
            BigInteger n = BigInteger.ZERO;
            BigInteger k = BigInteger.ZERO;
            do {
                if(flag) {
                    long[] y = generator.rand().limit(r).toArray();// just generates r random integers(2 in first case)
                    BigInteger sum = BigInteger.ZERO;
                    for (int i = 0; i < r - 1; i++) {
                        BigInteger tmp = BigInteger.valueOf(y[i]).multiply(BigInteger.TWO.pow(32));
                        sum = sum.add(tmp);
                    }
                    sum = sum.add(BigInteger.valueOf(generator.getSeed()));
                    generator.setSeed(y[r - 1]);
                    BigInteger tmp1 = new BigDecimal(BigInteger.TWO.pow(t.get(m) - 1))
                            .divide(new BigDecimal(primes[m + 1]), 0, RoundingMode.CEILING)
                            .toBigInteger();
                    BigInteger tmp2 = new BigDecimal(BigInteger.TWO.pow(t.get(m) - 1).multiply(sum))
                            .divide(new BigDecimal(primes[m + 1].multiply(BigInteger.TWO.pow(32 * r))), 0, RoundingMode.FLOOR)
                            .toBigInteger();
                    n = tmp1.add(tmp2);
                    if (!(n.mod(BigInteger.TWO).equals(BigInteger.ZERO))) {
                        n = n.add(BigInteger.ONE);
                    }
                    k = BigInteger.ZERO;
                }
                primes[m] = primes[m + 1].multiply(n.add(k)).add(BigInteger.ONE);
                if(primes[m].compareTo(BigInteger.TWO.pow(t.get(m))) > 0) {
                    flag = true;
                    continue;
                }
                if(!(BigInteger.TWO.modPow(primes[m + 1].multiply(n.add(k)), primes[m]).equals(BigInteger.ONE))
                        || BigInteger.TWO.modPow(n.add(k), primes[m]).equals(BigInteger.ONE)) {
                    flag = false;
                    k = k.add(BigInteger.TWO);
                } else {
                    flag = true;
                    break;
                }
            } while (true);
            m--;
        } while (m >= 0);
        return primes;
    }

    BigInteger[] generatePrimes1024() {
        int bitLength = 1024;
        BigInteger q = generatePrimes512()[1];
        BigInteger Q = generatePrimes512()[0];
        BigInteger p;
        BigInteger n = BigInteger.ZERO;
        BigInteger k = BigInteger.ZERO;
        int yLength = 32;
        boolean flag = true;
        do {
            if(flag) {
                long[] y = generator.rand().limit(yLength).toArray();
                BigInteger sum = BigInteger.ZERO;
                for (int i = 0; i < y.length - 1; i++) {
                    BigInteger tmp = BigInteger.valueOf(y[i]).multiply(BigInteger.TWO.pow(32));
                    sum = sum.add(tmp);
                }
                sum = sum.add(BigInteger.valueOf(generator.getSeed()));
                generator.setSeed(y[y.length - 1]);
                BigInteger tmp1 = new BigDecimal(BigInteger.TWO.pow(bitLength - 1))
                        .divide(new BigDecimal(q.multiply(Q)), 0, RoundingMode.CEILING)
                        .toBigInteger();
                BigInteger tmp2 = new BigDecimal(BigInteger.TWO.pow(bitLength - 1).multiply(sum))
                        .divide(new BigDecimal(q.multiply(Q).multiply(BigInteger.TWO.pow(bitLength))), 0, RoundingMode.FLOOR)
                        .toBigInteger();
                n = tmp1.add(tmp2);
                if (!(n.mod(BigInteger.TWO).equals(BigInteger.ZERO))) {
                    n = n.add(BigInteger.ONE);
                }
                k = BigInteger.ZERO;
            }
            p = q.multiply(Q).multiply(n.add(k)).add(BigInteger.ONE);
            if(p.compareTo(BigInteger.TWO.pow(bitLength)) > 0) {
                flag = true;
                continue;
            }
            if(!(BigInteger.TWO.modPow(q.multiply(Q).multiply(n.add(k)), p).equals(BigInteger.ONE))
                    || BigInteger.TWO.modPow(q.multiply(n.add(k)), p).equals(BigInteger.ONE)) {
                flag = false;
                k = k.add(BigInteger.TWO);
            } else {
                break;
            }
        } while (true);
        return new BigInteger[] {p, q};
    }

    BigInteger generateA(BigInteger p, BigInteger q) {
        BigInteger d;
        BigInteger f;
        do {
            do {
                d = new BigInteger(p.bitLength(), new Random());
            } while(d.compareTo(BigInteger.ONE) <= 0 || d.compareTo(p.subtract(BigInteger.ONE)) >= 0);
            f = d.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        } while (f.equals(BigInteger.ONE));
        return f;
    }
}


class LinearCongruentialGenerator {

    private long seed;
    private long a;
    private long c;
    private long m;

    LinearCongruentialGenerator(long seed, long a, long c, long m) {
        this.seed = seed;
        this.a = a;
        this.c = c;
        this.m = m;
    }

    LongStream rand() {
        return iterate(seed, x -> (a * x + c) % m).skip(1);
    }

    void setSeed(long seed) {
        this.seed = seed;
    }

    long getSeed() {
        return seed;
    }
}
class GOSTSignature {

    private BigInteger p;
    private BigInteger q;
    private BigInteger a;

    GOSTSignature(BigInteger p, BigInteger q, BigInteger a) {
        this.p = p;
        this.q = q;
        this.a = a;
    }

    BigInteger getPublicKey(BigInteger x) {
        return a.modPow(x, p);
    }

    BigInteger getRandomPrivateKey(int bitLength) {
        if(bitLength < 0 || bitLength < 128 || bitLength > q.bitLength()) {
            throw new IllegalArgumentException("Wrong key length");
        }
        Random r = new Random();
        BigInteger result;
        do {
            result = new BigInteger(bitLength, r);
        } while (result.compareTo(BigInteger.ZERO) < 0 || result.compareTo(q) > 0);
        return result;
    }

    BigInteger getPrivateKeyByPassword(String passwordHash) {
        return new BigInteger(passwordHash, 16);
    }

    String sign(String hash, BigInteger x) {
        BigInteger h = new BigInteger(hash, 16);
        if(h.mod(q).equals(BigInteger.ZERO)) {
            h = BigInteger.ONE;
        }
        BigInteger r;
        BigInteger s;
        BigInteger k;
        do {
            do {
                k = new BigInteger(q.bitLength(), new Random());
            } while (k.compareTo(BigInteger.ZERO) < 0 || k.compareTo(q) > 0);
            r = a.modPow(k, p).mod(q);
            s = x.multiply(r).add(k.multiply(h)).mod(q);
        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));
        String rString = addPadding(r.toString(16));
        String sString = addPadding(s.toString(16));
        return rString + sString;
    }

    boolean verify(String hash, String signature, BigInteger y) {
        String rString = signature.substring(0, signature.length() / 2);
        String sString = signature.substring(signature.length() / 2);
        BigInteger r = new BigInteger(rString, 16);
        BigInteger s = new BigInteger(sString, 16);
        if(r.compareTo(BigInteger.ZERO) <= 0) {
            return false;
        }
        BigInteger h = new BigInteger(hash, 16);
        if(h.mod(q).equals(BigInteger.ZERO)) {
            h = BigInteger.ONE;
        }
        BigInteger v = h.modPow(q.subtract(BigInteger.TWO), q);
        BigInteger z1 = s.multiply(v).mod(q);
        BigInteger z2 = q.subtract(r).multiply(v).mod(q);
        BigInteger u = a.modPow(z1, p).multiply(y.modPow(z2, p)).mod(p).mod(q);
        System.out.println("r = " + r);
        System.out.println("u = " + u);
        return r.equals(u);
    }

    private String addPadding(String input) {
        StringBuilder inputBuilder = new StringBuilder(input);
        for(int i = input.length(); i < 64; i++) {
            inputBuilder.insert(0, "0");
        }
        return inputBuilder.toString();
    }
}


  class MD4 extends MessageDigest {
// MD4 specific object variables
//...........................................................................

    /**
     * The size in bytes of the input block to the transformation algorithm.
     */
    private static final int BLOCK_LENGTH = 64;       //    = 512 / 8;

    /**
     * 4 32-bit words (interim result)
     */
    private int[] context = new int[4];

    /**
     * Number of bytes processed so far mod. 2 power of 64.
     */
    private long count;

    /**
     * 512 bits input buffer = 16 x 32-bit words holds until reaches 512 bits.
     */
    private byte[] buffer = new byte[BLOCK_LENGTH];

    /**
     * 512 bits work buffer = 16 x 32-bit words
     */
    private int[] X = new int[16];


// Constructors
//...........................................................................

    MD4 () {
        super("MD4");
        engineReset();
    }


// JCE methods
//...........................................................................

    /**
     * Resets this object disregarding any temporary data present at the
     * time of the invocation of this call.
     */
    public void engineReset () {
        // initial values of MD4 i.e. A, B, C, D
        // as per rfc-1320; they are low-order byte first
        context[0] = 0x67452301;
        context[1] = 0xEFCDAB89;
        context[2] = 0x98BADCFE;
        context[3] = 0x10325476;
        count = 0L;
        for (int i = 0; i < BLOCK_LENGTH; i++)
            buffer[i] = 0;
    }

    /**
     * Continues an MD4 message digest using the input byte.
     */
    public void engineUpdate (byte b) {
        // compute number of bytes still un hashed; ie. present in buffer
        int i = (int)(count % BLOCK_LENGTH);
        count++;                                        // update number of bytes
        buffer[i] = b;
        if (i == BLOCK_LENGTH - 1)
            transform(buffer, 0);
    }

    String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * MD4 block update operation.
     * <p>
     * Continues an MD4 message digest operation, by filling the buffer,
     * transform(ing) data in 512-bit message block(s), updating the variables
     * context and count, and leaving (buffering) the remaining bytes in buffer
     * for the next update or finish.
     *
     * @param    input    input block
     * @param    offset    start of meaningful bytes in input
     * @param    len        count of bytes in input block to consider
     */
    public void engineUpdate (byte[] input, int offset, int len) {
        // make sure we don't exceed input's allocated size/length
        if (offset < 0 || len < 0 || (long)offset + len > input.length)
            throw new ArrayIndexOutOfBoundsException();

        // compute number of bytes still un hashed; ie. present in buffer
        int bufferNdx = (int)(count % BLOCK_LENGTH);
        count += len;                                        // update number of bytes
        int partLen = BLOCK_LENGTH - bufferNdx;
        int i = 0;
        if (len >= partLen) {
            System.arraycopy(input, offset, buffer, bufferNdx, partLen);


            transform(buffer, 0);

            for (i = partLen; i + BLOCK_LENGTH - 1 < len; i+= BLOCK_LENGTH)
                transform(input, offset + i);
            bufferNdx = 0;
        }
        // buffer remaining input
        if (i < len)
            System.arraycopy(input, offset + i, buffer, bufferNdx, len - i);
    }

    /**
     * Completes the hash computation by performing final operations such
     * as padding. At the return of this engineDigest, the MD engine is
     * reset.
     *
     * @return the array of bytes for the resulting hash value.
     */
    public byte[] engineDigest () {
        // pad output to 56 mod 64; as RFC1320 puts it: congruent to 448 mod 512
        int bufferNdx = (int)(count % BLOCK_LENGTH);
        int padLen = (bufferNdx < 56) ? (56 - bufferNdx) : (120 - bufferNdx);

        // padding is always binary 1 followed by binary 0s
        byte[] tail = new byte[padLen + 8];
        tail[0] = (byte)0x80;

        // append length before final transform:
        // save number of bits, casting the long to an array of 8 bytes
        // save low-order byte first.
        for (int i = 0; i < 8; i++)
            tail[padLen + i] = (byte)((count * 8) >>> (8 * i));

        engineUpdate(tail, 0, tail.length);

        byte[] result = new byte[16];
        // cast this MD4's context (array of 4 ints) into an array of 16 bytes.
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result[i * 4 + j] = (byte)(context[i] >>> (8 * j));

        // reset the engine
        engineReset();
        return result;
    }


// own methods
//...........................................................................

    /**
     *    MD4 basic transformation.
     *    <p>
     *    Transforms context based on 512 bits from input block starting
     *    from the offset'th byte.
     *
     *    @param    block    input sub-array.
     *    @param    offset    starting position of sub-array.
     */
    private void transform (byte[] block, int offset) {

        // encodes 64 bytes from input block into an array of 16 32-bit
        // entities. Use A as a temp var.
        for (int i = 0; i < 16; i++)
            X[i] = (block[offset++] & 0xFF)       |
                    (block[offset++] & 0xFF) <<  8 |
                    (block[offset++] & 0xFF) << 16 |
                    (block[offset++] & 0xFF) << 24;
        int A = context[0];
        int B = context[1];
        int C = context[2];
        int D = context[3];
        A = FF(A, B, C, D, X[ 0],  3);
        D = FF(D, A, B, C, X[ 1],  7);
        C = FF(C, D, A, B, X[ 2], 11);
        B = FF(B, C, D, A, X[ 3], 19);
        A = FF(A, B, C, D, X[ 4],  3);
        D = FF(D, A, B, C, X[ 5],  7);
        C = FF(C, D, A, B, X[ 6], 11);
        B = FF(B, C, D, A, X[ 7], 19);
        A = FF(A, B, C, D, X[ 8],  3);
        D = FF(D, A, B, C, X[ 9],  7);
        C = FF(C, D, A, B, X[10], 11);
        B = FF(B, C, D, A, X[11], 19);
        A = FF(A, B, C, D, X[12],  3);
        D = FF(D, A, B, C, X[13],  7);
        C = FF(C, D, A, B, X[14], 11);
        B = FF(B, C, D, A, X[15], 19);
        A = GG(A, B, C, D, X[ 0],  3);
        D = GG(D, A, B, C, X[ 4],  5);
        C = GG(C, D, A, B, X[ 8],  9);
        B = GG(B, C, D, A, X[12], 13);
        A = GG(A, B, C, D, X[ 1],  3);
        D = GG(D, A, B, C, X[ 5],  5);
        C = GG(C, D, A, B, X[ 9],  9);
        B = GG(B, C, D, A, X[13], 13);
        A = GG(A, B, C, D, X[ 2],  3);
        D = GG(D, A, B, C, X[ 6],  5);
        C = GG(C, D, A, B, X[10],  9);
        B = GG(B, C, D, A, X[14], 13);
        A = GG(A, B, C, D, X[ 3],  3);
        D = GG(D, A, B, C, X[ 7],  5);
        C = GG(C, D, A, B, X[11],  9);
        B = GG(B, C, D, A, X[15], 13);
        A = HH(A, B, C, D, X[ 0],  3);
        D = HH(D, A, B, C, X[ 8],  9);
        C = HH(C, D, A, B, X[ 4], 11);
        B = HH(B, C, D, A, X[12], 15);
        A = HH(A, B, C, D, X[ 2],  3);
        D = HH(D, A, B, C, X[10],  9);
        C = HH(C, D, A, B, X[ 6], 11);
        B = HH(B, C, D, A, X[14], 15);
        A = HH(A, B, C, D, X[ 1],  3);
        D = HH(D, A, B, C, X[ 9],  9);
        C = HH(C, D, A, B, X[ 5], 11);
        B = HH(B, C, D, A, X[13], 15);
        A = HH(A, B, C, D, X[ 3],  3);
        D = HH(D, A, B, C, X[11],  9);
        C = HH(C, D, A, B, X[ 7], 11);
        B = HH(B, C, D, A, X[15], 15);
        context[0] += A;
        context[1] += B;
        context[2] += C;
        context[3] += D;
    }
    // The basic MD4 atomic functions.
    private int FF (int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & c) | (~b & d)) + x;
        return t << s | t >>> (32 - s);
    }
    private int GG (int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & (c | d)) | (c & d)) + x + 0x5A827999;
        return t << s | t >>> (32 - s);
    }
    private int HH (int a, int b, int c, int d, int x, int s) {
        int t = a + (b ^ c ^ d) + x + 0x6ED9EBA1;
        return t << s | t >>> (32 - s);
    }
}