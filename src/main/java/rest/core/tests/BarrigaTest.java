package rest.core.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

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

	private static final DateTimeFormatter DATE_FORMATTER_DD_MM_YY = DateTimeFormatter.ofPattern("dd/MM/YYYY");

	public BarrigaTest() {
	}

	@Test
	public void naoDeveAcessarApiSemToken() {
		given().log().all().contentType(ContentType.JSON.withCharset(CharEncoding.UTF_8)).when().get("/contas").then()
				.log().all().statusCode(HttpStatus.SC_UNAUTHORIZED);
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

		contaAlterada = editApiResource("/contas", contaInalterada.getId(), contaReq)
				.statusCode(getMethodExpectedStatusCode(Method.PUT)).extract().body().as(ContaResponse.class);
		;

		Assert.assertEquals(contaInalterada.getId(), contaAlterada.getId());
		Assert.assertNotEquals(contaInalterada.getNome(), contaAlterada.getNome());

	}

	private String getNameWithTimeStampdSuffix(String string) {
		return String.join("_", string, LocalDateTime.now().toString());
	}

	@Test
	public void N�oDeveCriarContaComNomeRepetido() {
		ContaRequest conta = new ContaRequest(String.join("_", "Conta", LocalDateTime.now().toString()));
		createApiResource("/contas", conta).statusCode(HttpStatus.SC_CREATED);

		String erro = createApiResource("/contas", conta).statusCode(HttpStatus.SC_BAD_REQUEST).extract().path("error")
				.toString();
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

		createApiResource("/transacoes", transacaoReq)
		.statusCode(getMethodExpectedStatusCode(Method.POST));
	}

	@Test
	public void naoDeveIncluirUmaMovimentacaoComDataDeTransacaoFutura() {
		TransacaoRequest transacaoReq;
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));

		String idContaCriada = createApiResource("/contas", conta)
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
		transacaoReq.setValor(200f);
		transacaoReq.setStatus(true);

		createApiResource("/transacoes", transacaoReq).statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void naoDeveRemoverUmaContaComTransacao() {
		TransacaoRequest transacaoReq;
		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));

		String idContaCriada = createApiResource("/contas", conta).statusCode(HttpStatus.SC_CREATED).extract()
				.path("id").toString();

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

		createApiResource("/transacoes", transacaoReq).statusCode(getMethodExpectedStatusCode(Method.POST));
		deleteAPIResource("/contas", idContaCriada).body("name", is("error"))
				.body("constraint", is("transacoes_conta_id_foreign")).statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}

	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {

		ContaRequest conta = new ContaRequest(getNameWithTimeStampdSuffix("Conta Para Movimenta��o com Data Atrasada"));
		
		createApiResource("/contas", conta);
		List<Object> listaMsg = createApiResource("/transacoes", EMPTY_JSON).statusCode(HttpStatus.SC_BAD_REQUEST).log()
				.all().statusCode(400).extract().jsonPath().getList("msg");

		Assert.assertTrue(listaMsg.containsAll(Arrays.asList("Data da Movimenta��o � obrigat�rio",
				"Data do pagamento � obrigat�rio", "Descri��o � obrigat�rio", "Interessado � obrigat�rio",
				"Valor � obrigat�rio", "Valor deve ser um n�mero", "Conta � obrigat�rio", "Situa��o � obrigat�rio")));
		listaMsg.forEach(o -> System.out.println(o));
	}

	@Test
	public void deveExcluirUmaConta() {
		ContaRequest contaRequest = new ContaRequest("Conta a ser Excuida");
		String newAccountId = createApiResource("/contas", contaRequest)
				.statusCode(getMethodExpectedStatusCode(Method.POST)).extract().path("id").toString();
		deleteAPIResource("/contas", newAccountId).statusCode(getMethodExpectedStatusCode(Method.DELETE));
	}

	@Test 
	public void deveCalcularSaldoTotal() {
		
		cleanUpApiCreatedTestDataMass();
		
		TransacaoRequest transacaoReq1, transacaoReq2;
		
		ContaRequest conta1 = new ContaRequest(getNameWithTimeStampdSuffix("Conta Jos�"));
		ContaRequest conta2 = new ContaRequest(getNameWithTimeStampdSuffix("Conta Maria"));
		ContaResponse ContaCriada1 = createApiResource("/contas", conta1)
				.extract().body().as(ContaResponse.class);
		
		ContaResponse ContaCriada2 = createApiResource("/contas", conta2)
				.extract().body().as(ContaResponse.class);
		
		
		
//				;;createApiResource("/contas", conta2);
		
		transacaoReq1 = new TransacaoRequest();
		transacaoReq1.setConta_id(ContaCriada1.getId());
		transacaoReq1.setDescricao("Maria Silva");
		transacaoReq1.setEnvolvido("Corporativo");
		transacaoReq1.setTipo(TipoTransacao.REC);
		transacaoReq1.setData_transacao(LocalDateTime.now().minusDays(10).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq1.setData_pagamento(LocalDateTime.now().plusDays(8).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq1.setValor(5000.00f);
		transacaoReq1.setStatus(true);
		
		transacaoReq2 = new TransacaoRequest();
		transacaoReq2.setConta_id(ContaCriada2.getId());
		transacaoReq2.setDescricao("Pens�o Jos�");
		transacaoReq2.setEnvolvido("Jos� Herculano");
		transacaoReq2.setTipo(TipoTransacao.REC);
		transacaoReq2.setData_transacao(LocalDateTime.now().minusDays(10).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq2.setData_pagamento(LocalDateTime.now().plusDays(8).format(DATE_FORMATTER_DD_MM_YY));
		transacaoReq2.setValor(5000.00f);
		transacaoReq2.setStatus(true);
		
		createApiResource("/contas", conta1);
		createApiResource("/contas", conta2);

		createApiResource("/transacoes", transacaoReq1);
		createApiResource("/transacoes", transacaoReq2);

		 float total = getAllApiResource("/saldo").extract().jsonPath().getList("saldo").stream().map(o -> Float.parseFloat(o.toString()))
				 .reduce(0f, (x,y) -> x+y).floatValue();
		Assert.assertNotNull(total);
		Assert.assertTrue("Total: "+total, total == 10000f);
		
	}

}
