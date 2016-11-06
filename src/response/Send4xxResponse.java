package response;

import java.io.FileInputStream;
import java.io.OutputStream;

import static support.Util.*;

/**
 * Created by ryosuke on 2016/11/06.
 */
public class Send4xxResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";

    public static void Send404Response(OutputStream outputStream, String errorDocumentRoot) throws Exception{
        // レスポンスヘッダを返す
        writeLine(outputStream, HTTP_VERSION + " 404 Not Found");
        writeLine(outputStream, "Date: " + getDateStringUtc());
        writeLine(outputStream, "Server: Sever04.java");
        writeLine(outputStream, "Connection: close");
        writeLine(outputStream, "Content-type: " + getContentType("html"));
        writeLine(outputStream, "");

        try(FileInputStream fileInputStream = new FileInputStream(errorDocumentRoot + "/404.html")) {
            int ch;
            while ((ch = fileInputStream.read()) != -1) {
                outputStream.write(ch);
            }
        }
    }
}
