package MQV;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * Created by mike on 18.02.15.
 */

public class Client {

    private static final int PORT = 50130;
    private static final String HOST = "localhost";


    public static void main(String[] args) {
        try {
            SHA256 sha256 = new SHA256();
            DSA dsa = new DSA();

            //INIT: Socket
            Socket c_socket = new Socket(HOST, PORT);

            //INIT: MQV
            MQV_Client mqv = new MQV_Client();

            //SEND: Client --- Qa,Ra ---> Server
            ObjectOutputStream oos = new ObjectOutputStream(c_socket.getOutputStream());
            MessageObj msgObj_write = new MessageObj(mqv.getQ(), mqv.getR());
            oos.writeObject(msgObj_write);

            //RECEIVE: Server --- Qb,Rb,tb ---> Client
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());
            MessageObj msgObj_read = (MessageObj) ois.readObject();
            mqv.setQ_public(msgObj_read.getQ());

            //CHECK: VALIDITY of R --- PUBLIC KEY FROM SERVER
            mqv.checkValidyR_public(msgObj_read.getR());
            mqv.setR_public(msgObj_read.getR());

            //{k1,k2} <- KDF(Zx);
            mqv.generateSemmetricKey();
            sha256.calulateKeyPair(mqv.getZ().getX());

            // Berechne HASH Wert tb = (2,Qb,Qa,Rb,Ra)
            dsa.setPrivateKey(sha256.getHashKey());
            dsa.setMessage("2", mqv.getQ(), mqv.getQ_public(), mqv.getR(), mqv.getR_public());
            if ( ! dsa.verify(msgObj_read.getHashWert()) ) {
                System.err.println("Signatur ist falsch");
            } else {
                //SEND: Client -- ta = MAC(3,Qa,Qb,Ra,Rb) --> Server
                dsa.setMessage("3", mqv.getQ_public(), mqv.getQ(), mqv.getR_public(), mqv.getR());
                oos.writeObject(dsa.sign());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
