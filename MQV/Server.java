package MQV;
/**
 * Created by mike on 18.02.15.
 */
//import java.math.BigInteger.* ;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 50130;

    public static void main(String[] args) {
        ServerSocket s_Socket = null;
        try {
            s_Socket = new ServerSocket(PORT);
            //MQV wird initialisiert
            MQV_Server mqv = new MQV_Server();

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
    private int anzahlClient;

    public ServerThread(Socket s, int i, MQV mqv) {
        this.socket = s;
        this.anzahlClient = i;
        this.mqv = mqv;

    }
    public void run() {
        try {

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //Server get public Key from CLient
            //mqv.setQ_public((ECC.Point) ois.readObject());
            //mqv.setR_public((ECC.Point) ois.readObject());
            MessageObj msgObj = (MessageObj) ois.readObject();
            mqv.setQ_public(msgObj.getQ());

            mqv.setR_public(msgObj.getR());
            // check R
            System.out.println("Client Qx:" + mqv.getQ_public().getX() + " y:" + mqv.getQ_public().getY());
            System.out.println("Client Rx:" + mqv.getR_public().getX() + " y:" + mqv.getR_public().getY());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            mqv.generate2Key();
            oos.writeObject(mqv.getQ());
            oos.writeObject(mqv.getR());

            System.out.println("Qx:" + mqv.getQ().getX() + " y:" + mqv.getQ().getY());
            System.out.println("Rx:" + mqv.getR().getX() + " y:" + mqv.getR().getY());


            mqv.generateSemmetricKey();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
