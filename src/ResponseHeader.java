import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseHeader {

	private static final String NOT_FOUND_PREFIX = "NotFound";

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
		List list = new ArrayList<>();
		String statusLine = prefix.equals(NOT_FOUND_PREFIX) ? "HTTP/1.1 404 Not Found" : "HTTP/1.1 200 OK";
		list.add(statusLine);
		list.add("Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME));
		list.add("Server: Server01.java");
		list.add("Connection: close");
		prefix = prefix.equals(NOT_FOUND_PREFIX) ? "html" : prefix;
		list.add("Content-type: " + getContentType(prefix));
		list.add("");

		String[] responseMsg = new String[list.size()];
		list.toArray(responseMsg);

		for (int i = 0; i < responseMsg.length; i++) {
			for (char ch : responseMsg[i].toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}

	public static void setNotFoundResponseHeader(OutputStream outputStream) throws Exception {
		setResponseHeader(outputStream, NOT_FOUND_PREFIX);
	}
}
//statuss code 304対応
