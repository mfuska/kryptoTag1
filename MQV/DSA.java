package MQV;

/**
 * Created by mike on 09.04.15.
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DSA {
    private BigInteger p;  // length L (p = 1 mod q) p = kq + 1
    private BigInteger q;  // length 160 (q teilt p-1)
    private BigInteger g;  // == h ^ (p-1/q) mod p
    private BigInteger x;  // 1 < x < q
    private BigInteger y;  // 1 < y < q
    private int BITLENGTH = 512;

    private String m;

    private Boolean debug = false;
    // bereche y = g^x mod p
    // p,q,g,y veroeffentlicht
    // x geheimer schluessel

    public DSA() {
        this.m = new String();
        this.initDSA();
    }

    private void initDSA(){
        this.p = new BigInteger("10393954077751214712499992780393199730031677231586309600049430943184888618160796113546515144759488157243203345348864934082041936783128349761732911530115149");
        this.q = new BigInteger("1157957959370050296211761000590584685845216338317");
        this.g = new BigInteger("4684289672848615558873935953266635411681311883478488045613882682100910591652849969973475805471524137342135214690620342668630077489775258912010033677983266");

    }
    private void generateY() {
        //y = g ^ x mod p
        this.y = this.g.modPow(this.x, this.p);
    }

    protected void setMessage(String number, String Q1, String Q2, String R1, String R2) {
        this.m = number + Q1 + Q2 + R1 + R2;
    }
    protected void setPrivateKey(String privateKey) {
        this.x = new BigInteger(privateKey,16);
        this.generateY();
    }
    protected BigInteger[] sign() throws NoSuchAlgorithmException {
        /*
            1< s <q
            s1 = (g^s mod p) mod q --> wenn s1 = 0 repeat
            s2 = s^-1 (SHA(m) + s1 . x) mod q -->  s2 = 0  s1 new calculate
         */
        MessageDigest sha2= MessageDigest.getInstance("SHA-256");
        BigInteger msg2sha2 = new BigInteger(sha2.digest(this.m.getBytes()));

        SecureRandom random = new SecureRandom();
        BigInteger s;
        BigInteger s1 = BigInteger.ZERO;
        BigInteger s2 = BigInteger.ZERO;
        do {
            s = new BigInteger(this.q.bitLength(), random);
            //compareTo:  // -1 less than // 0 equal // 1 greater than
            if (s.compareTo(BigInteger.ONE) == 1 && s.compareTo(this.q) == -1) {
                do {
                    s1 = (this.g.modPow(s, this.p)).mod(this.q);
                } while(s1.compareTo(BigInteger.ZERO) == 0);
                s2 = s.modInverse(this.q).multiply(msg2sha2.add(s1.multiply(this.x))).mod(this.q);
            }
        } while ( s2.compareTo(BigInteger.ZERO) == 0 );

        BigInteger[] signature = {s1, s2};
        System.out.println("DSA sign s1:" + s1.toString() + "---------------");
        System.out.println("DSA sign s2:" + s2.toString() + "----------------");
        System.out.println("DSA sign m:" + this.m + "---------------------");
        System.out.println("DSA sign x:" + this.x.toString() + "--------------");
        return signature;
    }

    protected boolean verify(BigInteger[] signatur) throws NoSuchAlgorithmException {
        /*
        geg: s1, s2 --> signatur[]
        m die nachricht
        Check:
            1. 0 < s1 < q  && 0 < s2 < q --> not True sig false

        Calulate:
            w = s2^-1 mod q
            u1 = SHA(m) mod q
            u2 = s1 mod q
            v = (g^u1 y^u2 mod p) mod q
            if (v == s1) --> sig valid
         */

        MessageDigest sha2= MessageDigest.getInstance("SHA-256");
        BigInteger sha2hash = new BigInteger(sha2.digest(this.m.getBytes()));

        BigInteger s1 = signatur[0];
        BigInteger s2 = signatur[1];


        // check 1: 0 < s1 < q  && 0 < s2 < q --> not True sig false
        if ( (s1.compareTo(BigInteger.ZERO) == 1 && s1.compareTo(this.q) == -1) &&
                (s2.compareTo(BigInteger.ZERO) == 1 && s2.compareTo(this.q) == -1) ) {
            BigInteger w = s2.modInverse(this.q);
            BigInteger u1 = sha2hash.multiply(w).mod(this.q);
            BigInteger u2 = s1.multiply(w).mod(this.q);
            BigInteger v = this.g.modPow(u1, this.p).multiply(this.y.modPow(u2, this.p)).mod(this.p).mod(this.q);
            System.out.println("DSA verify s1:" + s1.toString() + "-------------");
            System.out.println("DSA verify s2:" + s2.toString() + "-----------------");
            System.out.println("DSA verify m:" + this.m + "----------------");
            System.out.println("DSA verify x:" + this.x.toString() + "-----------------");
            System.out.println("v:" + v.toString() + " == s1:" + s1.toString());
            return v.compareTo(s1) == 0;
        }
        return false;
    }
}


