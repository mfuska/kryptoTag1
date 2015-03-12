/**
 * Created by mike on 03.03.15.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    public static void main(String[] args) {
        try {
            String base = "TEST";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            hex2String(hash);
            System.out.println("byte2String:" + hash.toString());
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private static void hex2String(byte[] hashValue) {
        StringBuffer hexString = new StringBuffer();

        System.out.println("hashlength:" + hashValue.length );
        for (int i = 0; i < hashValue.length; i++) {
            System.out.println("hash["+ i +"]:" + hashValue[i]);
            String hex = Integer.toHexString(0xff & hashValue[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("hexString:" + hexString);
    }
}
