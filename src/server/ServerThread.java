package server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private Socket socket;

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

    // 拡張子とContent-Typeの対応表
    private static final HashMap<String, String> contentTypeMap = new HashMap<String, String>() {{
        put("html", "text/html");
        put("htm", "text/html");
        put("txt", "text/plain");
        put("css", "text/css");
        put("png", "image/png");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("gig", "image/gif");
    }};

    // 拡張子を受け取りContent-Typeを返す
    private static String getContentType(String ext) {
        String value = contentTypeMap.get(ext.toLowerCase());
        return value == null ? "application/octet-stream" : value;
    }

    @Override
    public void run() {
        OutputStream outputStream;
        try {
            InputStream inputStream = socket.getInputStream();

            String line;
            String path = null;
            String ext = null;
            while ((line = readLine(inputStream)) != null) {
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
            // レスポンスヘッダを返す
            writeLine(outputStream, HTTP_VERSION + " 200 OK");
            writeLine(outputStream, "Date: " + getDateStringUtc());
            writeLine(outputStream, "Server: Sever03.java");
            writeLine(outputStream, "Connection: close");
            writeLine(outputStream, "Content-type: " + getContentType(ext));
            writeLine(outputStream, "");

            // レスポンスボディを返す
            try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + path)) {
                int ch;
                while ((ch = fileInputStream.read()) != -1) {
                    outputStream.write(ch);
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
