package server;

import response.Send2xxResponse;
import response.Send3xxResponse;
import response.Send4xxResponse;
import support.Util;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/";
    private static final String ERROR_DOCUMENT = "html/error/";
    private static final String SERVER_NAME = "localhost:8001";
    private Socket socket;

    @Override
    public void run() {
        OutputStream outputStream;
        try {
            InputStream inputStream = socket.getInputStream();

            String line;
            String path = null;
            String ext = null;
            String host = null;
            while ((line = Util.readLine(inputStream)) != null) {
                if (line.equals("")) {
                    break;
                }
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                    String[] tmp = path.split("\\.");
                    ext = tmp[tmp.length - 1];
                } else if (line.startsWith("Host:")) {
                    host = line.substring("Host: ".length());
                }
            }

            if (path == null) {
                return;
            }

            if (path.endsWith("/") ) {
                path += "index.html";
                ext = "html";
            }

            outputStream = socket.getOutputStream();

            // レスポンスボディを返す
            try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + path)) {
                Send2xxResponse.send200Response(outputStream, fileInputStream, ext);
            } catch (FileNotFoundException e) {
                FileSystem fs = FileSystems.getDefault();
                Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
                if (Files.isDirectory(pathObj)) {
                    String location = "http://"
                            + ((host != null) ? host : SERVER_NAME)
                            + path + File.separator;
                    Send3xxResponse.send301Response(outputStream, location);
                } else {
                    Send4xxResponse.send404Response(outputStream, DOCUMENT_ROOT + ERROR_DOCUMENT);
                }
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
