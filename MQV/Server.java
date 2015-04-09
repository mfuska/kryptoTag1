package MQV;
/**
 * Created by mike on 18.02.15.
 */

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

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
            SHA256 sha256 = new SHA256();
            DSA dsa = new DSA();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //RECEIVE: Client -- Qa,Ra --> Server
            MessageObj msgObj_read = (MessageObj) ois.readObject();

            //CHECK: VALIDITY of R --- PUBLIC KEY FROM CLIENT
            mqv.checkValidyR_public(msgObj_read.getR());

            //SET: CERT AND PUBLIC KEY FROM CLIENT
            mqv.setQ_public(msgObj_read.getQ());
            mqv.setR_public(msgObj_read.getR());

            //CALCULATE: private and public Key SERVER
            mqv.generate2Key();
            //{k1,k2} <- KDF(Zx);
            mqv.generateSemmetricKey();
            sha256.calculateKeyPair(mqv.getZ().getX());

            //CALULATE: HASH Wert tb = (2,Qb,Qa,Rb,Ra)
            dsa.setPrivateKey(sha256.getHashKey());
            dsa.setMessage("2", mqv.getQ().toString(), mqv.getQ_public().toString(), mqv.getR().toString(), mqv.getR_public().toString());

            //SEND: Server -- Qb,Rb,tb --> Client
            MessageObj msgObj_write = new MessageObj(mqv.getQ(), mqv.getR(), dsa.sign());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msgObj_write);

            //RECEIVE: Client -- ta --> Server
            BigInteger[] ta = (BigInteger[]) ois.readObject();

            //CHECK: ta = MAC(3,Qa,Qb,Ra,ba)
            dsa.setMessage("3", mqv.getQ_public().toString(), mqv.getQ().toString(), mqv.getR_public().toString(), mqv.getR().toString());
            if (!dsa.verify(ta)) {
                System.err.println("Signatur ist falsch");
            } else {
                System.out.println("Signatur ist richtig");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
