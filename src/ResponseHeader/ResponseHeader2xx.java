package ResponseHeader;

import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResponseHeader2xx {

	public static void sendOkResponse(OutputStream outputStream, String prefix) throws Exception {
		List<String> responses = new ArrayList<>();
		responses.add("HTTP/1.1 200 OK");
		responses.add("Date: " + ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME));
		responses.add("Server: Server01.java");
		responses.add("Connection: close");
		responses.add("Content-type: " + ContentType.getContentType(prefix));
		responses.add("");

		for (String response : responses) {
			for (char ch : response.toCharArray()) {
				outputStream.write((int) ch);
			}
			outputStream.write((int) '\r');
			outputStream.write((int) '\n');
		}
	}
}
