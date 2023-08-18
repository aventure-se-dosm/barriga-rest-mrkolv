package rest.core.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.oneOf;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import rest.core.BaseTest;

public class BarrigaTest extends BaseTest  {
	
	
	@Test
	public void deveAcessarAplicacaoWebExtraindoStringXmlPathECookies() {
		//It doesn't work if we're using the API 'barrigarest' instead of 'seubarriga'
		
		
		//seubarriga
		String email = "email"; // <input id='email' ... name='email'>
		String senha = "senha"; // <input id='senha' ... name='senha'>
		String firstAccountXMLPathLocation = "html.body.table.tbody.tr[0].td[0]";
		
		String cookie = getCookieStringFromResponse(email, senha);
		
		String body = given().log().all()
				.cookie("connect.sid", getCookieValue(cookie))
			.when()
				.get("http://seubarriga.wcaquino.me/contas")
			.then().log().all()
				.statusCode(HttpStatus.SC_OK)
				.body(firstAccountXMLPathLocation, is("Conta de Teste Jwt"))	
				//.body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste Jwt"))	
				//.extract().path(firstAccountXMLPathLocation)	
				.extract().body().asString()	
			;
		XmlPath xmlcontaPathPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlcontaPathPath.getString(firstAccountXMLPathLocation));
	}

	private String getCookieStringFromResponse(String email, String senha) {
	

		String cookie = given().log().all()
				.contentType(ContentType.URLENC.withCharset(CharEncoding.UTF_8))
				.formParam(email, "automation.dvmrkolv@gmail.com")
				.formParam(senha, "wXY2AUQXYy3gbeq")
				.when()
				//.post("http://barrigarest.wcaquino.me/signin")
				// - it doesn't work
				.post("http://seubarriga.wcaquino.me/logar")
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
