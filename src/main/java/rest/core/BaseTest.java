package rest.core;

import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.dev.marcelodeoliveira.rest.model.UserAuth;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import rest.core.utils.Constants;

public class BaseTest implements Constants {

	static private RequestSpecBuilder reqBuilder;
	static private ResponseSpecBuilder resBuilder;
	private static String token = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NDA1NzN9.TVUD2XK-pMxp6crPVLymwG-WQNU4GyzD4JGdjrWLG_g";
	

	private static List<String> accountIdList;
	private static List<String> transactionIdList;

	private static List<String> getListAccountId() {
		return BaseTest.accountIdList;
	}
	private static List<String> getListTransactionId() {
		return BaseTest.transactionIdList;
	}
	protected static String getToken() {
		return token;
	}

	@BeforeClass
	public static void setupTest() {
		
		accountIdList = new ArrayList<>();
		transactionIdList = new ArrayList<>();
		
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT_DEFAULT;	
		RestAssured.basePath = APP_BASE_PATH;

		reqBuilder = new RequestSpecBuilder();
		resBuilder = new ResponseSpecBuilder();

		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		reqBuilder.setContentType(APP_CONTENT_TYPE.withCharset(CharEncoding.UTF_8));

		RestAssured.requestSpecification = reqBuilder.build();
		RestAssured.responseSpecification = resBuilder.build();
		cleanUpApiCreatedTestDataMass();
	}
	
	protected boolean savedTestAccountIds (String accountId) {
		return getListAccountId().add(accountId);
	}
	protected boolean savedTestTransactionIds (String accountId) {
		return getListTransactionId().add(accountId);
	}
	
	
	protected static RequestSpecification loginWithFormParams() {
		String email = "email"; // <input id='email' ... name='email'>
		String senha = "senha"; // <input id='senha' ... name='senha'>
		String cookie = getCookieStringFromResponse(email, senha);

		return given().log().all()
				.contentType(APP_CONTENT_TYPE).cookie("connect.sid", getCookieValue(cookie))
			.when();
	}
	
	
	protected static RequestSpecification RequestWithJwtToken() {
		return  given().log().all()
		 .contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
		.when()
		.header(new Header("Authorization", getToken()));

	}
	
	private static void applyStaticallyRequestMethodForAllResourcesId(Method method, String basepath ) {
		List<String> allApiResourceId = 
				RequestWithJwtToken()
				.get(basepath)
				.then().log().all()
				.extract().jsonPath().getList("id");
		
		if (allApiResourceId.isEmpty()) return;
		
		for(Object apiResourceId : allApiResourceId) {
			applyStaticallyAPIMethodToResource(method, basepath, apiResourceId);
		}
		;
	}
	
	@AfterClass
	public static void finishTest() {
		cleanUpApiCreatedTestDataMass();
	}
	
	public static  boolean cleanUpApiCreatedTestDataMass() {
		applyStaticallyRequestMethodForAllResourcesId(Method.DELETE, "/transacoes");
		applyStaticallyRequestMethodForAllResourcesId(Method.DELETE, "/contas");
		return true;
	}


	
	private static ValidatableResponse applyStaticallyAPIMethodToResource(Method method, String basepath, Object resourceId) {
		return RequestWithJwtToken()
		.request(method, String.join("/", basepath, resourceId.toString()))
		.then().statusCode(getMethodExpectedStatusCode(method));
	}
	protected static ValidatableResponse applyAPIMethodToResource(Method method, String basepath, Object resourceId) {
		return RequestWithJwtToken()
				
				.request(method, String.join("/", basepath, method.equals(Method.GET)?"\r":resourceId.toString()))
				.then().statusCode(getMethodExpectedStatusCode(method));
	}
	
	private static int getMethodExpectedStatusCode(Method method) {
		switch(method) {
		case GET: return HttpStatus.SC_OK;
		case DELETE: return HttpStatus.SC_NO_CONTENT;
		//case HEAD: return HttpStatus.SC_OK;
		//case OPTIONS: return HttpStatus.SC_OK;
		//case PATCH: return HttpStatus.SC_OK;
		case POST: return HttpStatus.SC_CREATED;
		case PUT: return HttpStatus.SC_ACCEPTED;
		//case TRACE: return HttpStatus.SC_OK;
		default: throw new InvalidParameterException(String.format("M�todo n�o reconhecido: [%s]", method.toString()));
		
		}
	}

	private static String getCookieStringFromResponse(String email, String senha) {
		String cookie = given().log().all()
				.body(new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq"))
			.post("/signin")
				.then().log().all().extract().header("set-cookie");
		return cookie;
	}
	

	private static String getCookieValue(String cookie) {
		return cookie.split("=")[1].split(";")[0];
	}
	
	protected  ValidatableResponse  deleteAPIResource(String basePath, Object resourceId) {
		return RequestWithJwtToken()		
				.request(Method.DELETE, String.join("/", basePath, resourceId.toString()))
				.then().statusCode(getMethodExpectedStatusCode(Method.DELETE));
	}
	
	protected ValidatableResponse createApiResource(String basePath,  Object requestBody) {
		return RequestWithJwtToken()
				.body(requestBody)
				.request(Method.POST, String.join("/", basePath))
				.then()
				.statusCode(getMethodExpectedStatusCode(Method.POST))
				;
	}
	protected ValidatableResponse editApiResource(String basePath,  Object requestBody) {
		return RequestWithJwtToken()
				.body(requestBody)
				.request(Method.PUT, String.join("/", basePath))
				.then().statusCode(getMethodExpectedStatusCode(Method.PUT));
	}
	protected ValidatableResponse getApiResource(String basePath,  String resourceId) {
		return RequestWithJwtToken()
				.request(Method.GET, String.join("/", basePath, resourceId.toString()))
				.then().statusCode(getMethodExpectedStatusCode(Method.PUT));
	}
	protected ValidatableResponse getAllApiResource(String basePath,  String resourceId) {
		return RequestWithJwtToken()
				.request(Method.GET, String.join("/", basePath))
				.then().statusCode(getMethodExpectedStatusCode(Method.PUT));
	}


	

	

}
