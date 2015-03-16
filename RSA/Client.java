package RSA;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by mike on 18.02.15.
 */

public class Client {

    private static final int PORT = 5013;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            Socket c_socket = new Socket(Client.HOST, Client.PORT);
            InputStream in_Stream = c_socket.getInputStream();
            OutputStream out_Stream = c_socket.getOutputStream();

            Scanner in_Scanner = new Scanner(in_Stream);
            PrintWriter out = new PrintWriter(out_Stream, true);
            // Wait for Server INPUT n and e
            BigInteger n = in_Scanner.nextBigInteger();
            BigInteger e = in_Scanner.nextBigInteger();
            //create RSA object
            RSA rsa = new RSA(n, e);
            SHA256 sha2 = new SHA256();
            String s = "Client HELLO 1";
            // SHA2 Hash Wert berechnen
            String hash =  sha2.hex2String(sha2.calculateHash(s));
            String str_message = s + "," + hash;
            System.out.println("str_message:" + str_message);
            BigInteger message = new BigInteger(str_message.getBytes());
            BigInteger encrypt = rsa.encrypt(message);
            //send encrpted message to the server
            out.println(encrypt);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
