package response;

import java.io.OutputStream;
import java.util.ResourceBundle;

import static support.Util.getDateStringUtc;
import static support.Util.writeLine;

public class Send3xxResponse {
    public static void send301Response(OutputStream outputStream,
                                       String location) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("application");

        // レスポンスヘッダを返す
        writeLine(outputStream, rb.getString("http-version") + " 301 MovedPermanently");
        writeLine(outputStream, "Date: " + getDateStringUtc());
        writeLine(outputStream, "Server: Sever05.java");
        writeLine(outputStream, "Location: " + location);
        writeLine(outputStream, "Connection: close");
        writeLine(outputStream, "");
    }
}
