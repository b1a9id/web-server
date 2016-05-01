import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by uchitate on 2016/04/30.
 */
public class Server01 {

	private static final String DOCUMENT_ROOT = "/Users/uchitate/IdeaProject/webserver/src";

	public static void main(String[] args) throws Exception {

		String fileName = null;
		try (ServerSocket listener = new ServerSocket()) {

			listener.setReuseAddress(true);
			listener.bind(new InetSocketAddress(8001));
			System.out.println("Server listening on port 8001");
			while (true) {
				Socket socket = listener.accept();
				Thread thread = ServerThread.generateThread();
				thread.start();

				InputStream inputStream = socket.getInputStream();
				String line;
				while ((line = readLine(inputStream)) != null) {
					if (line == "") {
						break;
					}
					if (line.startsWith("GET")) {
						fileName = line.split(" ")[1];
					}
				}

				OutputStream outputStream = socket.getOutputStream();
				setResponseHeader(outputStream, "HTTP/1.1 200 OK");
				setResponseHeader(outputStream, "Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME));
				setResponseHeader(outputStream, "Server: Server01.java");
				setResponseHeader(outputStream, "Connection: close");
				setResponseHeader(outputStream, "Content-type: text/html");
				setResponseHeader(outputStream, "");

				try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + fileName)) {
					int ch;
					while ((ch = fileInputStream.read()) != -1) {
						outputStream.write(ch);
					}
				}
				catch (FileNotFoundException e) {
					e.getStackTrace();
				}
				socket.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setResponseHeader(OutputStream outputStream, String str) throws Exception {
		for (char ch : str.toCharArray()) {
			outputStream.write((int) ch);
		}
		outputStream.write((int) '\r');
		outputStream.write((int) '\n');
	}

	private static String readLine(InputStream input) throws Exception {
		int ch;
		String ret = "";
		while ((ch = input.read()) != -1) {
			if (ch == '\r') {
				// 何もしない
			} else if (ch == '\n') {
				break;
			} else {
				ret += (char) ch;
			}
		}
		if (ch == -1) {
			return null;
		} else {
			return ret;
		}
	}
}
