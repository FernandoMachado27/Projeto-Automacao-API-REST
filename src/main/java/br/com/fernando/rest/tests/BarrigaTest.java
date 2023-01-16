package br.com.fernando.rest.tests;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.fernando.rest.core.BaseTest;
import br.com.fernando.rest.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //executa em ordem alfabética
public class BarrigaTest extends BaseTest{
	
	private static String CONTA_NAME = "Conta " + System.nanoTime(); // static para o JUnit não zerar a variável, vamos pegar o tempo atual em nano seg 
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
	@BeforeClass // apenas uma vez para classe inteira
	public static void login () {
		Map<String, String> login = new HashMap<>();
		login.put("email", "fernandooo@gmail.com");
		login.put("senha", "Senha");
		
		String TOKEN = given()
		.body(login)
		.when()
		.post("/signin")
		.then()
		.statusCode(200)
		.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN); // token vai para todas requisições
	}
	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		CONTA_ID = given()
		.body("{\"nome\": \""+CONTA_NAME+"\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(201)
		.extract().path("id");
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {
		given()
		.body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
		.pathParam("id", CONTA_ID)
		.when()
		.put("/contas/{id}")
		.then()
		.statusCode(200)
		.body("nome", is(CONTA_NAME + " alterada"));
	}
	
	@Test
	public void t04_naoDeveInserirContaComMesmoNome() {
		given()
		.body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(400)
		.body("error", is("Já existe uma conta com esse nome!"));
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		
		MOV_ID = given()
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(201)
		.extract().path("id");
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosMovimentacao() {
		given()
		.body("{}")
		.when()
		.post("/transacoes")
		.then()
		.statusCode(400)
		.body("$", hasSize(8)) // as 8 mensagens que eu quero validar
		.body("msg", hasItems(
				"Data da Movimentação é obrigatório",
				"Data do pagamento é obrigatório",
				"Descrição é obrigatório",
				"Interessado é obrigatório",
				"Valor é obrigatório",
				"Valor deve ser um número",
				"Conta é obrigatório",
				"Situação é obrigatório"
			));
	}
	
	@Test
	public void t07_naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataComDiferencaDias(2));
		
		given()
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(400)
		.body("$", hasSize(1))
		.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
	}
	
	@Test
	public void t08_naoDeveRemoverContaComMovimentacao() {
		given()
		.pathParam("id", CONTA_ID)
		.when()
		.delete("/contas/{id}")
		.then()
		.statusCode(500)
		.body("constraint", is("transacoes_conta_id_foreign"));
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {
		given()
		.when()
		.get("/saldo")
		.then()
		.statusCode(200)
		.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00")); // pega o saldo de apenas esta conta
	}
	
	@Test
	public void t10_deveRemoverMovimentacao() {
		given()
		.pathParam("id", MOV_ID)
		.when()
		.delete("/transacoes/{id}")
		.then()
		.statusCode(204);
	}
	
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification; // para tirar o spec, que está mandando token
		req.removeHeader("Authorization");
		
		given()
		.when()
		.get("/contas")
		.then()
		.statusCode(401);
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
//		mov.getUsuario_id();
		mov.setDescricao("Descricao da movimentacao");
		mov.setEnvolvido("Envolvido na mov");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataComDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataComDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	
}
