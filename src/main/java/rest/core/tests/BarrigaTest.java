package rest.core.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import br.dev.marcelodeoliveira.rest.model.UserAuth;
import io.restassured.http.ContentType;
import rest.core.BaseTest;
import rest.model.enums.TipoTransacao;
import rest.model.requests.ContaRequest;
import rest.model.requests.TransacaoRequest;
import rest.model.responses.ContaResponse;

public class BarrigaTest extends BaseTest  {
	
	private static final Object INVALID_TOKEN = "INVALID_TOKEN";
	private List<String> contasCriadasList;
	
	public  BarrigaTest() {
		this.contasCriadasList = new ArrayList<>();
	}

	@Test
	public void naoDeveAcessarApiSemToken() {
		//seubarriga

				
		//UserAuth userauth = new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq");
		given().log().all()
	
		//.body(userauth)
		//.header("token", INVALID_TOKEN)

			.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
			//.body(userauth)
			//token: .contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
		.when()
			.get("/contas")
		.then().log().all()
		//.body("error", is("Problemas com o login do usuário"))
		.statusCode(HttpStatus.SC_UNAUTHORIZED);
		
	}
	@Test
	public void DeveListarContas() {
		
		UserAuth userauth = new UserAuth("automation.dvmrkolv@gmail.com", "wXY2AUQXYy3gbeq");
	
		RequestWithJwtToken()
		.body(userauth)
	
		//.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))

		//token: .contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
	
		.get("/contas")
		.then().log().all()
		//.body("error", emptyOrNullString())
		.statusCode(HttpStatus.SC_OK);

	}
	 
	@Test
	public void DeveCriarConta() {
		
		ContaRequest conta = new ContaRequest(String.join("_", "Nova Conta", LocalDateTime.now().toString()));

		 String idContaCriada = RequestWithJwtToken()
		.body(conta)	
		.accept(ContentType.JSON)
		.post("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_CREATED)
		.extract().path("id").toString();
		;
		
		contasCriadasList.add(idContaCriada);
		System.out.println(contasCriadasList.get(0));
		Assert.assertTrue(contasCriadasList.size() > 0);
	}
	
	@Test
	public void DeveAlterarUmaContaSalva() {
		
		
//melhor usar desserialização para compararmos mais campos
		
		ContaRequest conta = new ContaRequest(String.join("_", "Conta não-alterada", LocalDateTime.now().toString()));
		ContaResponse contaCriada =
				
		RequestWithJwtToken()
			.body(conta)	
		.when()
			.post("/contas")
		.then().log().all()
			.statusCode(HttpStatus.SC_CREATED)
			.extract().body().as(ContaResponse.class)
		;
		
			
		conta.setNome("ContaAlterada"+LocalDateTime.now().toString());
		
		ContaResponse contaAlterada = 
		RequestWithJwtToken()
			.body(conta)
		.when()
			.put("/contas/"+contaCriada.getId().toString())
		.then().log().all()
			.statusCode(HttpStatus.SC_OK)
			.extract().body().as(ContaResponse.class)
		;
		
		Assert.assertEquals(contaCriada.getId(), contaAlterada.getId());
		Assert.assertNotEquals(contaCriada.getNome(), contaAlterada.getNome());

	}
	
	@Test
	public void NãoDeveCriarContaComNomeRepetido() {
		
		ContaRequest conta = new ContaRequest(String.join("_", "Conta", LocalDateTime.now().toString()));
		RequestWithJwtToken()
		.body(conta)
	
		.when()
		.post("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_CREATED);

		
		//TODO: Isolate too much repeated req codes on your right-ancestor class!
		String erro = RequestWithJwtToken()
		.body(conta)
		.post("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_BAD_REQUEST)
		.extract().path("error").toString();
		;
		Assert.assertEquals(erro, "Já existe uma conta com esse nome!");
	}

	@Test 
	public void deveIncluirUmaMovimentacao() {
		
		ContaRequest conta = new ContaRequest(
				String.join("_", "Conta Transferidor", LocalDateTime.now().toString())
		);

		String idContaCriada  = 
	
				RequestWithJwtToken()
			.body(conta)
	
		.when()
			.post("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_CREATED)
		.extract().path("id").toString();
		
		Assert.assertNotNull(idContaCriada);
		Assert.assertTrue(idContaCriada.length() > 0);
		
		TransacaoRequest transacaoReq = new TransacaoRequest (
				Integer.parseInt(idContaCriada),
				"Depoósito para conta",
				"Receptor",
				TipoTransacao.DES,
				LocalDateTime.now().minusDays(10),
				LocalDateTime.now(),
				145.50f,
				true
		);
		
		System.out.println(transacaoReq);
	}
	

}
