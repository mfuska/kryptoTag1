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
    //private BigInteger h;  // 1 < h < p-1 und (h ^ (p-1/q) mod p) != 1
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

        this.p = new BigInteger("13368566116231306037753538667530794234070322008103834359916472098579949466335308811083106456468528616528934653914585390214751316132736733292434587083074973");
        this.q = new BigInteger("1058041536992883176168589017564102759170726601011");
        this.g = new BigInteger("4065538879919081344892522714009301371109641606765011481813708499044101940541468364776997558143717102226898683951822286932267966396959001853309045202737397");
        this.y = new BigInteger("7345708114268162505390765315538857990129990315565204085483387026439469147183314431119351935434514617270714043730068664910346831924016426664376909243283346");
        //this.h = new BigInteger("h:636049788692830196229800425974387688720309131500983590655095967538490623861171084795531944402618819388784342650503934387411754572399682122560562499598947");

    }
    protected void setMessage(String number, String Q1, String Q2, String R1, String R2) {
        this.m.concat(number).concat(Q1).concat(Q2).concat(R1).concat(R2);
    }
    protected void setPrivateKey(String privateKey) {
        this.x = new BigInteger(privateKey,16);
    }
    protected BigInteger[] sign() throws NoSuchAlgorithmException {
        /*
            1< s <q
            s1 = (g^s mod p) mod q --> wenn s1 = 0 repeat
            s2 = s^-1 (SHA(m) + s1 . x) mod q -->  s2 = 0  s1 new calculate
         */
        MessageDigest sha2= MessageDigest.getInstance("SHA-256");
        BigInteger msg2sha2 = new BigInteger(sha2.digest(m.getBytes()));

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
        BigInteger sha2hash = new BigInteger(sha2.digest(m.getBytes()));

        BigInteger s1 = signatur[0];
        BigInteger s2 = signatur[1];

        // check 1: 0 < s1 < q  && 0 < s2 < q --> not True sig false
        if ( (s1.compareTo(BigInteger.ZERO) == 1 && s1.compareTo(this.q) == -1) &&
                (s2.compareTo(BigInteger.ZERO) == 1 && s2.compareTo(this.q) == -1) ) {
            BigInteger w = s2.modInverse(this.q);
            BigInteger u1 = sha2hash.multiply(w).mod(this.q);
            BigInteger u2 = s1.multiply(w).mod(this.q);
            BigInteger v = g.modPow(u1, this.p).multiply(y.modPow(u2, this.p)).mod(this.p).mod(this.q);
            return v.compareTo(s1) == 0;
        }
        return false;
    }
}


