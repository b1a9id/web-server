package client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by ryosuke on 2016/10/30.
 */
public class Client01 {
    public static void main(String[] args) throws Exception {
        try(Socket socket = new Socket("localhost", 80);
            FileOutputStream fileOutputStream = new FileOutputStream("resources/text/client/client_receive.txt");
            FileInputStream fileInputStream = new FileInputStream("resources/text/client/client_send.txt")) {

            int ch;
            OutputStream outputStream = socket.getOutputStream();
            while ((ch = fileInputStream.read()) != -1) {
                outputStream.write(ch);
            }

//            outputStream.write(0);

            InputStream inputStream = socket.getInputStream();
            while ((ch = inputStream.read()) != -1) {
                fileOutputStream.write(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
