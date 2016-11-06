package support;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by ryosuke on 2016/11/06.
 */
public class Util {
    private static final String DOCUMENT_ROOT = "/Users/ryosuke/IdeProjectIntelliJ/web-server/resources/";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private Socket socket;

    // InputStreamからバイト配列を、行単位で読み込むメソッド
    public static String readLine(InputStream inputStream) throws Exception {
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
    public static void writeLine(OutputStream outputStream, String value) throws Exception {
        for (char ch : value.toCharArray()) {
            outputStream.write((int)ch);
        }
        outputStream.write((int)'\r');
        outputStream.write((int)'\n');
    }

    // 現在時刻からHTTP標準に合わせてフォーマットされた日付文字列を返す
    public static String getDateStringUtc() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        return now.format(formatter);
    }

    // 拡張子とContent-Typeの対応表
    public static final HashMap<String, String> contentTypeMap = new HashMap<String, String>() {{
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
    public static String getContentType(String ext) {
        String value = contentTypeMap.get(ext.toLowerCase());
        return value == null ? "application/octet-stream" : value;
    }
}
