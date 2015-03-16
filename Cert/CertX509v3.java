package Cert;
/**
 * Created by mike on 06.03.15.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

// openssl req -newkey rsa:2048 -nodes -keyout domain.key -x509 -days 365 -out domain.crt -sha256
public class CertX509v3 {
        public static void main(String[] args) throws CertificateException, IOException {
            String filename = "/Users/mike/kryptoProto/UE1/src/Cert/domain.crt";
            FileInputStream fileInput = new FileInputStream(filename);

            CertificateFactory certFact = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFact.generateCertificate(fileInput);

            System.out.println(cert.toString());
            System.out.println(cert.getPublicKey().toString());

            try {
                cert.checkValidity();
                System.out.println("cert ist valid");
                cert.verify(cert.getPublicKey());
                System.out.println("cert verify with public key ok");
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } finally {
                fileInput.close();
            }
        }
}
