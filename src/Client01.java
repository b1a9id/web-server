import java.io.*;
import java.net.Socket;

/**
 * Created by uchitate on 2016/05/01.
 */
public class Client01 {

	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("localhost", 8001);
			 FileOutputStream fileOutputStream = new FileOutputStream("/Users/uchitate/IdeaProject/webserver/resources/client_receive");
			 FileInputStream fileInputStream = new FileInputStream("/Users/uchitate/IdeaProject/webserver/resources/client_send")
		) {
			int ch;
			OutputStream outputStream = socket.getOutputStream();
			while ((ch = fileInputStream.read()) != -1) {
				outputStream.write(ch);
			}
//			outputStream.write(0);
			InputStream inputStream = socket.getInputStream();
			while ((ch = inputStream.read()) != -1) {
				fileOutputStream.write(ch);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
