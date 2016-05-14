package ResponseHeader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Content Typeを返すクラス
 */
public class ContentType {

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
}
