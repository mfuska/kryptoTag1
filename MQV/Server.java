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
            System.out.println("SERVER UP AND RUNNING");
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
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private static int countClient = 0;
    public ServerThread(Socket s, int i, MQV mqv) {
        this.socket = s;
        this.mqv = mqv;
        this.countClient = i;
    }

    public void run() {
        try {
            System.out.println("Client " + countClient);
            SHA256 sha256 = new SHA256();
            DSA dsa = new DSA();

            this.ois = new ObjectInputStream(socket.getInputStream());
            //RECEIVE: Client -- Qa,Ra --> Server
            System.out.println("SERVER receive: Qa,Ra");
            MessageObj msgObj_read = (MessageObj) ois.readObject();

            //CHECK: VALIDITY of R --- PUBLIC KEY FROM CLIENT
            System.out.println("SERVER verify validity of Ra");
            mqv.checkValidyR_public(msgObj_read.getR());
            System.out.println("SERVER verify validity of Ra: STATUS OK");

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
            dsa.setMessage("2", mqv.getQ().getX().toString(), mqv.getQ_public().getX().toString(), mqv.getR().getX().toString(), mqv.getR_public().getX().toString());

            //SEND: Server -- Qb,Rb,tb --> Client
            System.out.println("SERVER send: Qb,Rb,tb");
            MessageObj msgObj_write = new MessageObj(mqv.getQ(), mqv.getR(), dsa.sign());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.oos.writeObject(msgObj_write);

            //RECEIVE: Client -- ta --> Server
            BigInteger[] ta = (BigInteger[]) ois.readObject();

            //CHECK: ta = MAC(3,Qa,Qb,Ra,ba)
            System.out.println("SERVER receive: sig(3,Qa,Qb,Ra,Rb)");
            System.out.println("SERVER verify sig: sig(3,Qa,Qb,Ra,Rb)");
            dsa.setMessage("3", mqv.getQ_public().getX().toString(), mqv.getQ().getX().toString(), mqv.getR_public().getX().toString(), mqv.getR().getX().toString());
            if (!dsa.verify(ta)) {
                throw new MQVException("SERVER verify sig(3,Qa,Qb,Ra,ba): STATUS NOT");
            }
            System.out.println("SERVER verify sig: sig(3,Qa,Qb,Ra,Rb): STATUS OK");
        }catch (MQVException e) {
            e.printStackTrace();
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
                this.ois.close();
                this.oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
