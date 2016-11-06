package response;

import java.io.FileInputStream;
import java.io.OutputStream;

import static support.Util.*;

/**
 * Created by ryosuke on 2016/11/06.
 */
public class Send2xxResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";

    public static void Send200Response(OutputStream outputStream,
                                FileInputStream fileInputStream,
                                String ext) throws Exception{
        // レスポンスヘッダを返す
        writeLine(outputStream, HTTP_VERSION + " 200 OK");
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
