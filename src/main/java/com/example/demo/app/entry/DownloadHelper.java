package com.example.demo.app.entry;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;


/**
 * @author kasuda
 *@return
 */
@Component
public class DownloadHelper {

	/*日本語化*/
	private static final String CONTENT_DISPOSITION_FORMAT= "attachment; filename=\"%s\"; filename*=UTF-8''%s";

	/**
	 * @param headers
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 * @return csvファイルの日本語化用メソッド
	 */
	public void addContentDisposition(HttpHeaders headers, String fileName) throws UnsupportedEncodingException {
		String headerValue = String.format(CONTENT_DISPOSITION_FORMAT,
			fileName, UriUtils.encode(fileName, StandardCharsets.UTF_8.name()));
		headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
	}
}
