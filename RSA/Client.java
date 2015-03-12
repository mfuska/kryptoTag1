package RSA;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
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
            // Wait for Server INPUT n and d
            String n = in_Scanner.nextLine();
            String e = in_Scanner.nextLine();
            //create RSA object
            RSA rsa = new RSA(n,e);
            String s = "Client HELLO 1";
            BigInteger message = new BigInteger(s.getBytes());
            BigInteger encrypt = rsa.encrypt(message);
            //send encrpted message to the server
            out.println(encrypt);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
