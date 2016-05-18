package ResponseHeader;

import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseHeader3xx {

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

	public static void sendNotModified(OutputStream outputStream, String prefix, String fileTime) throws Exception {
		String[] response = {
				"HTTP/1.1 304 Not Modified",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + ContentType.getContentType(prefix),
				"Last-Modified: " + fileTime,
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
