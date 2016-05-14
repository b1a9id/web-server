package ResponseHeader;

import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseHeader2xx {

	public static void sendOkResponse(OutputStream outputStream, String prefix) throws Exception {
		String[] response = {
				"HTTP/1.1 200 OK",
				"Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
				"Server: Server01.java",
				"Connection: close",
				"Content-type: " + ContentType.getContentType(prefix),
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
}
