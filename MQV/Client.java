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
            // Input und Output Sockets werden angelegt ...
            Socket c_socket = new Socket(HOST, PORT);

            ObjectOutputStream oos = new ObjectOutputStream(c_socket.getOutputStream());

            //MQV wird initialisiert
            MQV_Client mqv = new MQV_Client();
            //Client --- Qa,Ra ---> Server
            MessageObj msgObj_write = new MessageObj(mqv.getQ(), mqv.getR());
            oos.writeObject(msgObj_write);

            //read Server ECC Point
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());
            //Server --- Qb,Rb,tb ---> Client
            MessageObj msgObj_read = (MessageObj) ois.readObject();
            mqv.setQ_public(msgObj_read.getQ());
            mqv.checkValidyR_public(msgObj_read.getR());
            mqv.setR_public(msgObj_read.getR());
            //check tb == MAC(2,Qb,Qa,Rb,Ra)
            //Client -- ta = MAC(3,Qa,Qb,Ra,Rb) --> Server
            String ta = new String("13131313123");
            oos.writeObject(ta);

            mqv.generateSemmetricKey();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
