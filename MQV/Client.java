package MQV;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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
            MQV mqv = new MQV();
            oos.writeObject(mqv.getPublicKey());
            System.out.println("Clients public key x:" + mqv.getPublicKey().getX() + " y:" + mqv.getPublicKey().getY());

            //read Server ECC Point
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());
            mqv.setForeign_public_key((ECC.Point) ois.readObject());
            System.out.println("Clients get foreigen key x:" + mqv.getForeign_public_key().getX() + " y:" + mqv.getForeign_public_key().getY());
            mqv.generateClientKey();
            System.out.println("x:" + mqv.getSemmetric_key().getX().toString() + " y:" + mqv.getSemmetric_key().getY().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
