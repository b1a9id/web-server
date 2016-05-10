import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

/**
 * Created by uchitate on 2016/05/01.
 */
public class ServerThread implements Runnable {

	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
		new Thread(this).start();
	}

	@Override
	public void run() {

		try {
			BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
			String line;
			String fileName = null;
			String prefix = null;
			while ((line = readLine(inputStream)) != null) {
				if (line == "") {
					break;
				}
				if (line.startsWith("GET")) {
					fileName = line.split(" ")[1];
					String[] tmp = fileName.split("\\.");
					prefix = tmp[tmp.length - 1];
				}
			}

			String DOCUMENT_ROOT = Paths.get(System.getProperty("user.dir") + File.separator + "resources").toString();
			OutputStream outputStream = socket.getOutputStream();
			try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + fileName)) {
				ResponseHeader.setResponseHeader(outputStream, prefix);
				int byteData;
				while ((byteData = fileInputStream.read()) != -1) {
					outputStream.write(byteData);
				}
			} catch (FileNotFoundException e) {
				ResponseHeader.setNotFoundResponseHeader(outputStream);
				try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + "/404.html")) {
					int byteData;
					while ((byteData = fileInputStream.read()) != -1) {
						outputStream.write(byteData);
					}
				}
			}
			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String readLine(InputStream input) throws Exception {
		int byteData;
		String ret = "";
		while ((byteData = input.read()) != -1) {
			if (byteData == '\r') {
				// 何もしない
			} else if (byteData == '\n') {
				break;
			} else {
				ret += (char) byteData;
			}
		}
		return byteData == -1 ? null : ret;
	}
}
