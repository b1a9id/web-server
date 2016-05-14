import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseHeader {

	public static final String getContentType(String prefix) {
		Map<String, String> contentTypeMap = new ConcurrentHashMap<String, String>() {{
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

	public static void sendOkResponse(OutputStream outputStream, String prefix) throws Exception {
		String[] response = {
				"HTTP/1.1 200 OK",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + getContentType(prefix),
				""
		};

		for (int i = 0; i < response.length; i++) {
			for (char ch : response[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

	public static void sendNotFoundResponse(OutputStream outputStream) throws Exception {
		String[] response = {
				"HTTP/1.1 404 Not Found",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + getContentType("html"),
				""
		};

		for (int i = 0; i < response.length; i++) {
			for (char ch : response[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

	public static void sendMovedPermanentlyResponse(OutputStream outputStream, String url) throws Exception {
		String[] response = {
				"HTTP/1.1 301 Moved Permanently",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Location: " + url,
				"Connection: close",
				""
		};

		for (String responseLine : response) {
			for (char ch : responseLine.toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

}
