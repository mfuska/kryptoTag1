package MQV;
/**
 * Created by mike on 18.02.15.
 */
//import java.math.BigInteger.* ;

import java.io.*;
import java.math.BigInteger;
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
            //Client -- Qa,Ra --> Server
            MessageObj msgObj_read = (MessageObj) ois.readObject();
            //CHECK PUBLIC KEY FROM CLIENT
            mqv.checkValidyR_public(msgObj_read.getR());
            //SET CERT AND PUBLIC KEY FROM CLIENT
            mqv.setQ_public(msgObj_read.getQ());
            mqv.setR_public(msgObj_read.getR());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // Generate private and public Key
            mqv.generate2Key();
            //check tb = MAC(2,Qb,Qa,Rb,Ra)

            // Berechne HASH Wert (3,Qa,Qb,Ra,Rb)
            //Server -- Qb,Rb,tb --> Server
            MessageObj msgObj_write = new MessageObj(mqv.getQ(),mqv.getR(),new BigInteger("34242"));
            oos.writeObject(msgObj_write);

            //Client -- ta --> Server
            String ta = (String) ois.readObject();
            //check ta = MAC(3,Qa,Qb,Ra,ba)
            mqv.generateSemmetricKey();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
