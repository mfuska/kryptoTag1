/**
 * Created by mike on 18.02.15.
 */
//import java.math.BigInteger.* ;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
//import java.security.cert.*;
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
                InputStream in_Stream = socket.getInputStream();
                OutputStream out_Stream = socket.getOutputStream();

                Scanner in_Scanner = new Scanner(in_Stream);
                PrintWriter out = new PrintWriter(out_Stream, true);

                while ( in_Scanner.hasNextLine() ) {
                    String line = in_Scanner.nextLine();
                    System.out.println("Client " + anzahlClient + ": " + line);
                    out.println("TEST: " + anzahlClient);
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
