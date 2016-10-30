package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {
    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(8001);
            FileOutputStream fileOutputStream = new FileOutputStream("resources/text/server/server_receive.txt");
            FileInputStream fileInputStream = new FileInputStream("resources/text/server/server_send.txt")) {

            System.out.println("クライアントからの接続を待ちます。");
            Socket socket = serverSocket.accept();
            System.out.println("クライアント接続");

            int ch;
            InputStream inputStream = socket.getInputStream();
            while ((ch = inputStream.read()) != 0) {
                fileOutputStream.write(ch);
            }

            OutputStream outputStream = socket.getOutputStream();
            while ((ch = fileInputStream.read()) != -1) {
                outputStream.write(ch);
            }

            outputStream.write(0);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

  }
    }
}
