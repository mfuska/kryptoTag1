package MQV;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by mike on 18.02.15.
 */

public class Client {

    private static final int PORT = 50130;
    private static final String HOST = "localhost";

    private static Socket c_socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        try {
            SHA256 sha256 = new SHA256();
            DSA dsa = new DSA();

            //INIT: Socket
            c_socket = new Socket(HOST, PORT);

            //INIT: MQV
            MQV_Client mqv = new MQV_Client();

            //SEND: Client --- Qa,Ra ---> Server
            oos = new ObjectOutputStream(c_socket.getOutputStream());
            MessageObj msgObj_write = new MessageObj(mqv.getQ(), mqv.getR());
            oos.writeObject(msgObj_write);

            //RECEIVE: Server --- Qb,Rb,tb ---> Client
            ois = new ObjectInputStream(c_socket.getInputStream());
            MessageObj msgObj_read = (MessageObj) ois.readObject();
            mqv.setQ_public(msgObj_read.getQ());

            //CHECK: VALIDITY of R --- PUBLIC KEY FROM SERVER
            mqv.checkValidyR_public(msgObj_read.getR());
            mqv.setR_public(msgObj_read.getR());

            //{k1,k2} <- KDF(Zx);
            mqv.generateSemmetricKey();
            sha256.calculateKeyPair(mqv.getZ().getX());

            // Berechne HASH Wert tb = (2,Qb,Qa,Rb,Ra)
            dsa.setPrivateKey(sha256.getHashKey());
            System.out.println("Client (2,Qb,Qa,Rb,Ra)");
            dsa.setMessage("2", mqv.getQ_public().getX().toString(), mqv.getQ().getX().toString(), mqv.getR_public().getX().toString(), mqv.getR().getX().toString());
            if ( dsa.verify(msgObj_read.getHashWert()) ) {
                //SEND: Client -- ta = MAC(3,Qa,Qb,Ra,Rb) --> Server
                dsa.setMessage("3", mqv.getQ().getX().toString(), mqv.getQ_public().getX().toString(), mqv.getR().getX().toString(), mqv.getR_public().getX().toString());
                System.out.println("Client (3,Qa,Qb,Ra,Rb)");
                oos.writeObject(dsa.sign());
            } else {
                throw new MQVException("CLIENT DSA SIG(tb = (2,Qb,Qa,Rb,Ra) VERIFY WRONG");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MQVException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
                ois.close();
                c_socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
