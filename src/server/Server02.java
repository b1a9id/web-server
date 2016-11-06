package server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Server02 {
    private static final String DOCUMENT_ROOT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";

    // InputStreamからバイト配列を、行単位で読み込むメソッド
    private static String readLine(InputStream inputStream) throws Exception {
        int ch;
        String ret = "";
        while ((ch = inputStream.read()) != -1) {
            if (ch == '\n') {
                break;
            } else {
                ret += ch != '\r' ? (char) ch : "";
            }
        }
        return ch == -1 ? null : ret;
    }

    // 1行の文字列を、バイト列としてOutputStreamに書き込むユーティリティメソッド
    private static void writeLine(OutputStream outputStream, String value) throws Exception {
        for (char ch : value.toCharArray()) {
            outputStream.write((int)ch);
        }
        outputStream.write((int)'\r');
        outputStream.write((int)'\n');
    }

    // 現在時刻からHTTP標準に合わせてフォーマットされた日付文字列を返す
    private static String getDateStringUtc() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        return now.format(formatter);
    }

    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(8001)) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            String line;
            String path = null;
            while ((line = readLine(inputStream)) != null) {
                if (line.equals("")) {
                    break;
                }
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                }
            }

            OutputStream outputStream = socket.getOutputStream();
            // レスポンスヘッダを返す
            writeLine(outputStream, HTTP_VERSION + " 200 OK");
            writeLine(outputStream,"Date: " + getDateStringUtc());
            writeLine(outputStream, "Server: Server02.java");
            writeLine(outputStream, "Connection: close");
            writeLine(outputStream, "Content-type : " + CONTENT_TYPE_TEXT_HTML);
            writeLine(outputStream, "");

            // レスポンスボディを返す
            try(FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + path)) {
                int ch;
                while ((ch = fileInputStream.read()) != -1) {
                    outputStream.write(ch);
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
