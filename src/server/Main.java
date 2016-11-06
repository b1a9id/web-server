package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(8001)) {
            for (;;) {
                Socket socket = serverSocket.accept();

                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}
