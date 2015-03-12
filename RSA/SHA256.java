package RSA;
/**
 * Created by mike on 03.03.15.
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.lang.String;

public class SHA256 {
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
