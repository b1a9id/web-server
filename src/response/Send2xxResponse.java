package response;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import static support.Util.*;

/**
 * Created by ryosuke on 2016/11/06.
 */
public class Send2xxResponse {
    public static void send200Response(OutputStream outputStream,
                                FileInputStream fileInputStream,
                                String ext) throws Exception{
        ResourceBundle rb = ResourceBundle.getBundle("application");

        // レスポンスヘッダを返す
        writeLine(outputStream, rb.getString("http-version") + " 200 OK");
        writeLine(outputStream, "Date: " + getDateStringUtc());
        writeLine(outputStream, "Server: Sever04.java");
        writeLine(outputStream, "Connection: close");
        writeLine(outputStream, "Content-type: " + getContentType(ext));
        writeLine(outputStream, "");

        int ch;
        while ((ch = fileInputStream.read()) != -1) {
            outputStream.write(ch);
        }
    }
}
