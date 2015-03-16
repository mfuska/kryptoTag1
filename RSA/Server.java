package RSA;
/**
 * Created by mike on 18.02.15.
 */
//import java.math.BigInteger.* ;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Server {
    private static final int PORT = 5013;

    public static void main(String[] args) {
        try {
            ServerSocket s_Socket = new ServerSocket(Server.PORT);

            int i = 1;
            while (true) {
                Socket s_incoming = s_Socket.accept();
                Runnable r = new ServerThread(s_incoming, i);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerThread implements Runnable {
    private Socket socket;

    private int anzahlClient;

    public ServerThread(Socket s, int i) {
        socket = s;
        anzahlClient = i;
    }
    public void run() {
        try {
            try {
                RSA rsa = new RSA();
                SHA256 sha2 = new SHA256();

                InputStream in_Stream = socket.getInputStream();
                OutputStream out_Stream = socket.getOutputStream();

                Scanner in_Scanner = new Scanner(in_Stream);
                PrintWriter out = new PrintWriter(out_Stream, true);
                out.println(rsa.getN());
                out.println(rsa.getE());

                BigInteger encrytedMsg = in_Scanner.nextBigInteger();
                String message[] = rsa.decrypt(encrytedMsg).split(",");
                String hash =  sha2.hex2String( sha2.calculateHash(message[0]) );
                if (hash.equals(message[1])) {
                    System.out.println("Hash Wert ist equal");
                    System.out.println("message:" + message[0]);
                    System.out.println("hash:" + hash + " == sha2:" + message[1]);
                } else {
                    System.out.print("Hash Wert ist nicht gleich");
                    System.out.println("message:" + message[0]);
                    System.out.println("hash:" + hash + " =! sha2:" + message[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
