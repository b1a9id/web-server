import ResponseHeader.ResponseHeader2xx;
import ResponseHeader.ResponseHeader3xx;
import ResponseHeader.ResponseHeader4xx;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ServerThread implements Runnable {

	private Socket socket;

	private final String DOCUMENT_ROOT = "/Users/uchitate/IdeaProject/webserver/resources";

	private final String SERVER_NAME = "localhost:8002";

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
			String host = null;
			String modifiedDate = null;
			while ((line = readLine(inputStream)) != null) {
				if (line == "") {
					break;
				}
				if (line.startsWith("GET")) {
					fileName = line.split(" ")[1];
					String[] tmp = fileName.split("\\.");
					prefix = tmp[tmp.length - 1];
				} else if (line.startsWith("Host:")) {
					host = line.substring("Host: ".length());
				} else if (line.startsWith("If-Modified-Since:")) {
					modifiedDate = line.substring("If-Modified-Since: ".length());
				}
			}

			OutputStream outputStream = socket.getOutputStream();
			try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + fileName)) {
				FileSystem fileSystem = FileSystems.getDefault();
				Path path = fileSystem.getPath(DOCUMENT_ROOT + fileName);
				String fileTime = Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS)
						.toInstant()
						.atZone(ZoneId.of("GMT"))
						.format(DateTimeFormatter.RFC_1123_DATE_TIME);

				if (modifiedDate != null && modifiedDate.equals(fileTime)) {
					ResponseHeader3xx.sendNotModified(outputStream, prefix, fileTime);
				} else {
					ResponseHeader2xx.sendOkResponse(outputStream, prefix);
					int byteData;
					while ((byteData = fileInputStream.read()) != -1) {
						outputStream.write(byteData);
					}
				}
			} catch (FileNotFoundException e) {
				FileSystem fileSystem = FileSystems.getDefault();
				Path path = fileSystem.getPath(DOCUMENT_ROOT + fileName);
				if (Files.isDirectory(path)) {
					String url = "http://" + ((host != null ? host : SERVER_NAME)) + "/index.html" + File.separator;
					ResponseHeader3xx.sendMovedPermanentlyResponse(outputStream, url);
				} else {
					ResponseHeader4xx.sendNotFoundResponse(outputStream);
					try (FileInputStream fileInputStream = new FileInputStream(DOCUMENT_ROOT + File.separator + "404.html")) {
						int byteData;
						while ((byteData = fileInputStream.read()) != -1) {
							outputStream.write(byteData);
						}
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
