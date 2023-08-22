package rest.core;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.dev.marcelodeoliveira.rest.model.UserAuth;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
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

	/**
	 * 
	 */
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
		resetDataMass();
	}
	
	protected boolean savedTestAccountIds (String accountId) {
		return getListAccountId().add(accountId);
	}
	protected boolean savedTestTransactionIds (String accountId) {
		return getListTransactionId().add(accountId);
	}
	
	@AfterClass
	public static void resetDataMass() {
		cleanUpApiCreatedTestDataMass();	
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
	
	public static void cleanUpApiCreatedTestDataMass() {
		cleanAllTestTransactions();
		cleanAllAllTestAccounts();
	}
	
	private static void cleanAllTestTransactions() {
		
		List<String> allApiTransactions = 
				RequestWithJwtToken()
				.get(APP_BASE_URL+"/transacoes")
				.then().log().all()
				.extract().jsonPath().getList("id");
		
		if (allApiTransactions.isEmpty()) return;
		
		
		for(Object ResourceId : allApiTransactions) { 
			deleteAPIResource("/transacoes", ResourceId);
		}
		
	}

	private static void cleanAllAllTestAccounts() {
		List<String> allApiAccounts = 
		RequestWithJwtToken()
			.get(APP_BASE_URL+"/contas")
		.then().log().all()
		.extract().jsonPath().getList("id");

				if (allApiAccounts.isEmpty()) return;
		
		for(Object ResourceId : allApiAccounts) {
			deleteAPIResource("/contas", ResourceId);
		}
		;
				
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
	
	private static void  deleteAPIResource(String basePath, Object value) {
		RequestWithJwtToken()
		.delete(String.join("/", basePath, value.toString()))
		.then().log().all()
		.statusCode(HttpStatus.SC_NO_CONTENT)
		;
	}

	

}
