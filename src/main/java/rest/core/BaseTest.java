package rest.core;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.CharSet;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.AfterClass;

import groovyjarjarantlr.LexerSharedInputState;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import rest.core.utils.Constants;

public class BaseTest implements Constants {

	
	static private RequestSpecBuilder reqBuilder;
	static private ResponseSpecBuilder resBuilder;
	
	/**
	 * 
	 */
	@AfterClass
	public static void setupTest() {

		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT_HTTP;
		RestAssured.basePath = APP_BASE_PATH;
		
		
		reqBuilder = new RequestSpecBuilder();
		resBuilder = new ResponseSpecBuilder();
		
		reqBuilder.setContentType(APP_CONTENT_TYPE.withCharset(CharEncoding.UTF_8));
		RestAssured.requestSpecification = reqBuilder.build();
		
		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		RestAssured.responseSpecification = resBuilder.build();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	;
}
