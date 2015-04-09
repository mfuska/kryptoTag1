package MQV;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mike on 08.04.15.
 */
public class SHA256 {
    private String hashValue = new String();

    public SHA256(String number, String Q1, String Q2, String R1, String R2) {
       this.hashValue.concat(number).concat(Q1).concat(Q2).concat(R1).concat(R2);
    }
    public byte[] calculateHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest dig = null;
        dig = MessageDigest.getInstance("SHA-256");
        dig.reset();
        dig.update(str.getBytes("UTF-8"));
        return dig.digest();
    }
    public String hex2String(byte[] hashValue) {
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
}

