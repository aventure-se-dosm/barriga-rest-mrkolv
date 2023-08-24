package rest.core.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import rest.core.BaseTest;
import rest.model.enums.TipoTransacao;
import rest.model.requests.ContaRequest;
import rest.model.requests.TransacaoRequest;
import rest.model.responses.ContaResponse;

public class BarrigaTest extends BaseTest {

	private static final Object INVALID_TOKEN = "INVALID_TOKEN";
	private static final DateTimeFormatter DATE_FORMATTER_DD_MM_YY = DateTimeFormatter.ofPattern("dd/MM/YYYY");

	private List<String> contasCriadasList;

	public BarrigaTest() {
		this.contasCriadasList = new ArrayList<>();
	}

	@Test
	public void naoDeveAcessarApiSemToken() {
		given().log().all()
			.contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8))
		.when().get("/contas")
		.then().log().all()
		.statusCode(HttpStatus.SC_UNAUTHORIZED);
	}

	@Test
	public void deveListarContas() {
		getAllApiResource("/contas");
	}

	@Test
	public void deveCriarConta() {

		Integer idContaCriada = createApiResource("/contas",
				new ContaRequest(getNameWithTimeStampdSuffix("Nova Conta"))).extract().path("id");
		Assert.assertNotNull(idContaCriada);
	}

	@Test
	public void deveAlterarUmaContaSalva() {

		ContaResponse contaInalterada, contaAlterada;
		ContaRequest contaReq = new ContaRequest(getNameWithTimeStampdSuffix("Conta Inalterada"));

		contaInalterada = createApiResource("/contas", contaReq).extract().body().as(ContaResponse.class);
		contaReq.setNome("ContaAlterada" + LocalDateTime.now().toString());
		contaAlterada = editApiResource("/contas", contaInalterada.getId(), contaReq).extract().body()
				.as(ContaResponse.class);
		;

		Assert.assertEquals(contaInalterada.getId(), contaAlterada.getId());
		Assert.assertNotEquals(contaInalterada.getNome(), contaAlterada.getNome());

	}

	private String getNameWithTimeStampdSuffix(String string) {
		return String.join("_", LocalDateTime.now().toString());
	}

	@Test
	public void N�oDeveCriarContaComNomeRepetido() {

		//'nome' e 'conta_id' s�o OS campo obrigat�rios
		
		ContaRequest conta = new ContaRequest(String.join("_", "Conta", LocalDateTime.now().toString()));
		
//		RequestWithJwtToken()
//			.body(conta)
//		.when().post("/contas")
//		.then().log().all()
		createApiResource("/contas", conta)
		.statusCode(HttpStatus.SC_CREATED);

		String erro = createApiResource("/contas", conta)
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.extract().path("error").toString();
		;
		Assert.assertEquals(erro, "J� existe uma conta com esse nome!");
	}

	@Test
	public void deveIncluirUmaMovimentacao() {
		TransacaoRequest transacaoReq;
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o"));

		String idContaCriada = RequestWithJwtToken().body(conta).when().post("/contas").then().log().all()
				.statusCode(HttpStatus.SC_CREATED).extract().path("id").toString();

		Assert.assertNotNull(idContaCriada);
		Assert.assertTrue(idContaCriada.length() > 0);

		transacaoReq = new TransacaoRequest();
		transacaoReq.setConta_id(Integer.parseInt(idContaCriada));
		transacaoReq.setDescricao("Descri��o de Movimenta��o");
		transacaoReq.setEnvolvido("Envolvido na Movimenta��o");
		transacaoReq.setTipo(TipoTransacao.REC);
		transacaoReq.setData_transacao(LocalDateTime.now().format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setData_pagamento(LocalDateTime.now().format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setValor(100f);
		transacaoReq.setStatus(true);

		createApiResource("/transacoes", transacaoReq);
	}
	
	@Test
	public void naoDeveIncluirUmaMovimentacaoComDataDeTransacaoFutura() {
		TransacaoRequest transacaoReq;
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));
		
		String idContaCriada = //RequestWithJwtToken().body(conta).when().post("/contas").then().log().all()
				createApiResource("/contas", conta)
				.statusCode(HttpStatus.SC_CREATED).extract().path("id").toString();
		
		Assert.assertNotNull(idContaCriada);
		Assert.assertTrue(idContaCriada.length() > 0);
		
		transacaoReq = new TransacaoRequest();
		transacaoReq.setConta_id(Integer.parseInt(idContaCriada));
		transacaoReq.setDescricao("Descri��o de Movimenta��o");
		transacaoReq.setEnvolvido("Envolvido na Movimenta��o");
		transacaoReq.setTipo(TipoTransacao.REC);
		transacaoReq.setData_transacao(LocalDateTime.now().plusYears(10).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setData_pagamento(LocalDateTime.now().format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setValor(100f);
		transacaoReq.setStatus(true);
		
		createApiResource("/transacoes", transacaoReq)
		.statusCode(HttpStatus.SC_BAD_REQUEST);
	}
	@Test
	public void naoDeveRemoverUmaContaComTransacao() {
		TransacaoRequest transacaoReq;
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));
		
		String idContaCriada = 
				createApiResource("/contas", conta)
				.statusCode(HttpStatus.SC_CREATED).extract().path("id").toString();
		
		Assert.assertNotNull(idContaCriada);
		Assert.assertTrue(idContaCriada.length() > 0);
		
		transacaoReq = new TransacaoRequest();
		transacaoReq.setConta_id(Integer.parseInt(idContaCriada));
		transacaoReq.setDescricao("Descri��o de Movimenta��o");
		transacaoReq.setEnvolvido("Envolvido na Movimenta��o");
		transacaoReq.setTipo(TipoTransacao.REC);
		transacaoReq.setData_transacao(LocalDateTime.now().minusDays(10).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setData_pagamento(LocalDateTime.now().plusDays(8).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq.setValor(100f);
		transacaoReq.setStatus(true);
		
		createApiResource("/transacoes", transacaoReq)
		.statusCode(getMethodExpectedStatusCode(Method.POST));
		deleteAPIResource("/contas", idContaCriada)
		.body("name", is("error"))
		.body("constraint", is("transacoes_conta_id_foreign"))
		.statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));

		createApiResource("/contas", conta);
		
//		TransacaoRequest transacaoReq = new TransacaoRequest();
//		//transacaoReq.setConta_id(Integer.parseInt(idContaCriada));
//		transacaoReq.setDescricao("Descri��o de Movimenta��o");
//		transacaoReq.setEnvolvido("Envolvido na Movimenta��o");
//		transacaoReq.setTipo(TipoTransacao.REC);
//		transacaoReq.setData_transacao(LocalDateTime.now().minusDays(10).format(DATE_FORMATTER_DD_MM_YY));
//		transacaoReq.setData_pagamento(LocalDateTime.now().plusDays(8).format(DATE_FORMATTER_DD_MM_YY));
//		transacaoReq.setValor(100f);
//		transacaoReq.setStatus(true);
		createApiResource("/transacoes", EMPTY_JSON)
		.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
		
		
		
	}

	@Test
	public void deveExcluirUmaConta() {
		ContaRequest contaRequest = new ContaRequest("Conta a ser Excuida");
		String newAccountId = createApiResource("/contas", contaRequest)
				.statusCode(getMethodExpectedStatusCode(Method.POST))
				.extract().path("id").toString();
		deleteAPIResource("/contas", newAccountId);
	}
	
	

}
