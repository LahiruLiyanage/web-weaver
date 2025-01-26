package lk.ijse.dep13.server.webweaver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebWeaverServerApp {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("Server started on port 80");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
