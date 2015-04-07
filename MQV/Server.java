package MQV;
/**
 * Created by mike on 18.02.15.
 */
//import java.math.BigInteger.* ;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 50130;

    public static void main(String[] args) {
        ServerSocket s_Socket = null;
        try {
            s_Socket = new ServerSocket(PORT);
            MQV mqv = new MQV();

            int i = 1;
            while (true) {
                Socket s_incoming = s_Socket.accept();
                Runnable r = new ServerThread(s_incoming, i, mqv);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s_Socket.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
}

class ServerThread implements Runnable {
    private Socket socket;
    private MQV mqv;


    public ServerThread(Socket s, int i, MQV mqv) {
        this.socket = s;
        this.anzahlClient = i;
        this.mqv = mqv;

    }
    public void run() {
        try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //MQV wird initialisiert
                // Server get public Key from CLient
                mqv.setForeign_public_key((ECC.Point) ois.readObject());
                System.out.println("Server get foreigen key x:" + mqv.getForeign_public_key().getX() + " y:" + mqv.getForeign_public_key().getY());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(mqv.getPublicKey());
                System.out.println("Server public key x:" + mqv.getPublicKey().getX() + " y:" + mqv.getPublicKey().getY());
                mqv.generateServerKey();
                System.out.println("x:" + mqv.getSemmetric_key().getX().toString() + " y:" + mqv.getSemmetric_key().getY().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
