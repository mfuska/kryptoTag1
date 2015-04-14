package MQV;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mike on 08.04.15.
 */
public class SHA256 {
    private String[] strArray;

    public SHA256() {
    }
    public SHA256(BigInteger Zx) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //System.out.println("Zx bitlength:" + Zx.bitLength() );
        this.KDF(this.hex2String(this.calculateHash(Zx.toString())));
    }
    protected void calculateKeyPair(BigInteger Zx) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.KDF(this.hex2String(this.calculateHash(Zx.toString())));
    }
    private byte[] calculateHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest dig = null;
        dig = MessageDigest.getInstance("SHA-256");
        dig.reset();
        dig.update(str.getBytes("UTF-8"));
        return dig.digest();
    }
    private String hex2String(byte[] hashValue) {
        StringBuffer hexString = new StringBuffer();

        //System.out.println("hashlength:" + hashValue.length );
        for (int i = 0; i < hashValue.length; i++) {
            //System.out.println("hash["+ i +"]:" + hashValue[i]);
            String hex = Integer.toHexString(0xff & hashValue[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private void KDF(String hashValue) {
        String k1 = hashValue.substring(0,hashValue.length()/2);
        String k2 = hashValue.substring(hashValue.length()/2,(hashValue.length()-1));
        //System.out.println("hashValue:" + hashValue + " bitlength:" + new BigInteger(hashValue,16).bitLength());
        //System.out.println("k1:" + k1 + " bitlength:" + new BigInteger(k1,16).bitLength());
        //System.out.println("k2:" + k1 + " bitlength:" + new BigInteger(k2,16).bitLength());
        this.strArray = new String[] {k1, k2};
    }
    protected String[] getKeyPair() {
        return this.strArray;
    }
    protected String getHashKey() {
        return this.strArray[0];
    }
    protected String getSessionKey() {
        return this.strArray[1];
    }
}


