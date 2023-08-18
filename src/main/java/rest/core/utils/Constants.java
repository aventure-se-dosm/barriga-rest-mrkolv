package rest.core.utils;

import io.restassured.http.ContentType;
public interface Constants {
	
	
	

	String APP_BASE_URL = "http://barrigarest.wcaquino.me";
	String APP_BASE_PATH = "";
	Integer APP_PORT_HTTPS = 443;
	Integer APP_PORT_HTTP = 80;
	Integer APP_PORT_DEFAULT = APP_PORT_HTTP;

	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 10000L;
}
