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

            oos.writeObject(mqv.getQ());
            oos.writeObject(mqv.getR());
            System.out.println("Qx:" + mqv.getQ().getX() + " y:" + mqv.getQ().getY());
            System.out.println("Rx:" + mqv.getR().getX() + " y:" + mqv.getR().getY());

            //read Server ECC Point
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());
            mqv.setQ_public((ECC.Point) ois.readObject());
            mqv.setR_public((ECC.Point) ois.readObject());
            System.out.println("Server Qx:" + mqv.getQ_public().getX() + " y:" + mqv.getQ_public().getY());
            System.out.println("Server Rx:" + mqv.getR_public().getX() + " y:" + mqv.getR_public().getY());


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
