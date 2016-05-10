import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

	public static void setResponseHeader(OutputStream outputStream, String prefix) throws Exception {
		String[] successHeader = {
				"HTTP/1.1 200 OK",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + getContentType(prefix),
				""
		};

		for (int i = 0; i < successHeader.length; i++) {
			for (char ch : successHeader[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

	public static void setNotFoundResponseHeader(OutputStream outputStream) throws Exception {
		String[] notFoundHeader = {
				"HTTP/1.1 404 Not Found",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: text/html",
				""
		};

		for (int i = 0; i < notFoundHeader.length; i++) {
			for (char ch : notFoundHeader[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}
}
//statuss code 304対応
