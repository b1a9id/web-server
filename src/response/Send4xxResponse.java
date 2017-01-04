package response;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import static support.Util.*;

/**
 * Created by ryosuke on 2016/11/06.
 */
public class Send4xxResponse {
    public static void send404Response(OutputStream outputStream, String errorDocumentRoot) throws Exception{
        ResourceBundle rb = ResourceBundle.getBundle("application");

        // レスポンスヘッダを返す
        writeLine(outputStream, rb.getString("http-version") + " 404 Not Found");
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
