import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

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

				try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + fileName)) {
					ResponseHeader.setResponseHeader(outputStream, prefix);
					int ch;
					while ((ch = fileInputStream.read()) != -1) {
						outputStream.write(ch);
					}
				} catch (FileNotFoundException e) {
					ResponseHeader.setNotFoundResponseHeader(outputStream);
					try(FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + "/404.html")) {
						int ch;
						while ((ch = fileInputStream.read()) != -1) {
							outputStream.write(ch);
						}
					}
				}
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
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


}
