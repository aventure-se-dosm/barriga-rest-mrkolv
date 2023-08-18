package rest.core.tests;

import static io.restassured.RestAssured.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.junit.Test;

import br.dev.marcelodeoliveira.rest.model.UserAuth;
import io.restassured.http.ContentType;
import org.junit.Assert;
import rest.core.BaseTest;
import rest.model.requests.ContaRequest;

public class BarrigaTest extends BaseTest  {
	
	private static final Object INVALID_TOKEN = "INVALID_TOKEN";
	private List<String> contasCriadasList;
	
	public  BarrigaTest() {
		this.contasCriadasList = new ArrayList<>();
	}
	
	

	@Test
	public void naoDeveAcessarApiSemToken() {
		//seubarriga
//		String firstAccountXMLPathLocation = "html.body.table.tbody.tr[0].td[0]";
				
		UserAuth userauth = new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq");
		given().log().all()
	
		.body(userauth)
		.header("token", INVALID_TOKEN)

			.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
			//.body(userauth)
			//token: .contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
		.when()
			.post("/contas")
		.then().log().all()
		//.body("error", is("Problemas com o login do usuário"))
		.statusCode(HttpStatus.SC_UNAUTHORIZED);
		
	}
	@Test
	public void DeveListarContas() {
		
		UserAuth userauth = new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq");
		given().log().all()
		.body(userauth)
		.header("Authorization", String.join(" ", "JWT", getToken()))
		.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
//			.body(new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq"))
		//token: .contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
		.when()
		.get("/contas")
		.then().log().all()
		//.body("error", emptyOrNullString())
		.statusCode(HttpStatus.SC_OK);

	}
	
	@Test
	public void DeveSalvarConta() {
		
		ContaRequest conta = new ContaRequest(String.join("_", "Nova Conta", LocalDateTime.now().toString()));

		 String idContaCriada = given().log().all()
		.body(conta)
		.header("Authorization", String.join(" ", "JWT", getToken()))
		.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
	
		.when()
		.post("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_CREATED)
		.extract().path("id").toString();
		;
		
		contasCriadasList.add(idContaCriada);
		System.out.println(contasCriadasList.get(0));
		Assert.assertTrue(contasCriadasList.size() > 0);
	}

	private String getCookieStringFromResponse(String email, String senha) {
	

		String cookie = given().log().all()
				.contentType(ContentType.URLENC.withCharset(CharEncoding.UTF_8))
				.formParam(email, "automation.dvmrkolv@gmail.com")
				.formParam(senha, "wXY2AUQXYy3gbeq")
				.when()
				.post("/signin")
				// - it doesn't work
				//.post("http://seubarriga.wcaquino.me/logar")
				.then().log().all()
				.extract().header("set-cookie");
		
	// if(cookie != null) 
		 return cookie;
	// throw new RuntimeException("cookie nulo");
	}
	
	private String getCookieValue(String cookie) {
		return cookie.split("=")[1].split(";")[0];
	}
}
