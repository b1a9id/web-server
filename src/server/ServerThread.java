package server;

import response.Send2xxResponse;
import response.Send4xxResponse;
import support.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/";
    private static final String ERROR_DOCUMENT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/html/error/";
    private Socket socket;

    @Override
    public void run() {
        OutputStream outputStream;
        try {
            InputStream inputStream = socket.getInputStream();

            String line;
            String path = null;
            String ext = null;
            while ((line = Util.readLine(inputStream)) != null) {
                if (line.equals("")) {
                    break;
                }
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                    String[] tmp = path.split("\\.");
                    ext = tmp[tmp.length - 1];
                }
            }
            outputStream = socket.getOutputStream();

            // レスポンスボディを返す
            try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + path)) {
                Send2xxResponse.Send200Response(outputStream, fileInputStream, ext);
            } catch (FileNotFoundException e) {
                Send4xxResponse.Send404Response(outputStream, ERROR_DOCUMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
    }
}
