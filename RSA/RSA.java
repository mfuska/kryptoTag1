/**
 * Created by mike on 18.02.15.
 */
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {

    private final static Boolean DEBUG = false;
    private BigInteger d;
    private BigInteger e;
    private BigInteger n;
    private Boolean SERVER = false;

    private RSA() {
        SecureRandom random = new SecureRandom();

        int BITLENGTH = 1024;

        BigInteger p = BigInteger.probablePrime(BITLENGTH, random);
        BigInteger q = BigInteger.probablePrime(BITLENGTH, random);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        n = p.multiply(q);
        e = new BigInteger("65537"); // 2^16 + 1
        d = e.modInverse(phi);

        if (DEBUG) {
            System.out.println("p:" + p);
            System.out.println("q:" + q);
            System.out.println("n:" + n);
            System.out.println("e:" + e);
            System.out.println("d:" + d);
        }
    }
    private RSA(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
    }
    BigInteger encrypt(BigInteger message) {
           return message.modPow(e, n);
    }

    BigInteger getD() {
        return this.d;
    }
    BigInteger getN() {
        return  this.n;
    }
    String decrypt(BigInteger encrypted) {
        return new String((encrypted.modPow(d, n)).toByteArray());
    }

    public static void main(String[] args) {
        RSA key = new RSA();

        String s = "test";
        BigInteger message = new BigInteger(s.getBytes());
        BigInteger encrypt = key.encrypt(message);

        String decrypt = key.decrypt(encrypt);

        //System.out.println("encrypted message= " + new String(encrypt.toByteArray()));
        System.out.println("decrypted message= " + decrypt);
    }
}
