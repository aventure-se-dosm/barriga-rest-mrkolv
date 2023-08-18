package rest.core;

import org.apache.commons.codec.CharEncoding;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import rest.core.utils.Constants;

public class BaseTest implements Constants {

	static private RequestSpecBuilder reqBuilder;
	static private ResponseSpecBuilder resBuilder;
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NDA1NzN9.TVUD2XK-pMxp6crPVLymwG-WQNU4GyzD4JGdjrWLG_g";

	protected String getToken() {
		return this.token;
	}

	/**
	 * 
	 */
	@BeforeClass

	public static void setupTest() {
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT_DEFAULT;
		RestAssured.basePath = APP_BASE_PATH;

		reqBuilder = new RequestSpecBuilder();
		resBuilder = new ResponseSpecBuilder();

		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		reqBuilder.setContentType(APP_CONTENT_TYPE.withCharset(CharEncoding.UTF_8));

		RestAssured.requestSpecification = reqBuilder.build();
		RestAssured.responseSpecification = resBuilder.build();

		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	private String getCookieValue(String cookie) {
		return cookie.split("=")[1].split(";")[0];
	}

}