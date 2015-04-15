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

    private String m;
    private Boolean debug = false;
    private String SHA = "SHA-512";

    // bereche y = g^x mod p
    // p,q,g,y veroeffentlicht
    // x geheimer schluessel
    public DSA() {
        this.m = new String();
        //this.initDSA();
        this.initDSA_2048();
    }

    private void initDSA(){
        this.p = new BigInteger("10393954077751214712499992780393199730031677231586309600049430943184888618160796113546515144759488157243203345348864934082041936783128349761732911530115149");
        this.q = new BigInteger("1157957959370050296211761000590584685845216338317");
        this.g = new BigInteger("4684289672848615558873935953266635411681311883478488045613882682100910591652849969973475805471524137342135214690620342668630077489775258912010033677983266");
    }
    private void initDSA_2048(){
        this.p = new BigInteger("24805908021494084663838025854973446005776420063478495877285033428377598647689579713677734194393584173310812217542013034650232066496250611366317174909691284009623817049645507190680856994199923845067785300715484473608245252611630775234904267014366005022947365051458802535171322265571606202987811999479586538506694794545499359474661588499014447533699006287983812971421640712967747381849458123539351941129918549347631193396568428388350571704812995701103884067691346617024280855835324110868764225498873758293564363570860883823642169333509797981899357535307762578152781193238101467914379783210724485309259777209484301979987");
        this.q = new BigInteger("1098600872742691959353687271146221102017102150737");
        this.g = new BigInteger("19902397698046578917676274242556371189843338894917213735264241566336883349290141711811266094473125241482140537369095716710289771094928669746489042362596057949470547123817043654124634729943483402530103036346749242975559824413972790777761907066078058252769197923017309487119100843958667498134933682321769000726892562978120046092867011967101624506549411429294322398588873089397030669979159266560437538491440034631876363734400618935033100295592109497429399935359387360244805727997961290515398387521671599700455384780033069952304292611739180140810957783137706961042543977109828966319050713005712923462404731440497956232536");
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
        //MessageDigest sha2= MessageDigest.getInstance("SHA-256");
        MessageDigest sha2= MessageDigest.getInstance(this.SHA);
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
        if (debug) System.out.println("sig bits:" + s1.bitLength() + s2.bitLength());
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

        //MessageDigest sha2= MessageDigest.getInstance("SHA-256");
        MessageDigest sha2= MessageDigest.getInstance(this.SHA);
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
            return v.compareTo(s1) == 0;
        }
        return false;
    }
}


