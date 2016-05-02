import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by uchitate on 2016/04/30.
 */
public class Server01 {

	private static final Path DOCUMENT_ROOT = Paths.get(System.getProperty("user.dir") + File.separator + "resources");

	private static String prefix = null;

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

				BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
				String line;
				while ((line = readLine(inputStream)) != null) {
					if (line == "") {
						break;
					}
					if (line.startsWith("GET")) {
						fileName = line.split(" ")[1];
						String[] tmp = fileName.split("\\.");
						prefix = tmp[tmp.length -1];
					}
				}

				OutputStream outputStream = socket.getOutputStream();
				setResponseHeader(outputStream);

				try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + fileName)) {
					int ch;
					while ((ch = fileInputStream.read()) != -1) {
						outputStream.write(ch);
					}
				} catch (FileNotFoundException e) {
					e.getStackTrace();
				}
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setResponseHeader(OutputStream outputStream) throws Exception {
		String[] header = {
				"HTTP/1.1 200 OK",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + getContentType(prefix),
				""
		};

		for (int i = 0; i < header.length; i++) {
			for (char ch : header[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

	private static String readLine(InputStream input) throws Exception {
		int readChar;
		String ret = "";
		while ((readChar = input.read()) != -1) {
			if (readChar == '\r') {
				// 何もしない
			} else if (readChar == '\n') {
				break;
			} else {
				ret += (char) readChar;
			}
		}
		return readChar == -1 ? null : ret;
	}

	public static String getContentType(String prefix) {
		HashMap<String, String> contentTypeMap = new HashMap<String, String>() {{
			put("html", "text/html");
			put("htm", "text/html");
			put("txt", "text/plain");
			put("css", "text/css");
			put("png", "image/png");
			put("jpg", "image/jpeg");
			put("jpeg", "image/jpeg");
			put("gif", "image/gif");
		}};

		return contentTypeMap.get(prefix);
	}
}
