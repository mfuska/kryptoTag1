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
                InputStream in_Stream = socket.getInputStream();
                OutputStream out_Stream = socket.getOutputStream();

                Scanner in_Scanner = new Scanner(in_Stream);
                PrintWriter out = new PrintWriter(out_Stream, true);

                out.println(rsa.getN());
                out.println(rsa.getE());
                BigInteger encrytedMsg = new BigInteger(in_Scanner.nextLine());
                System.out.println("Client:" + anzahlClient + ": " + rsa.decrypt(encrytedMsg) );
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
