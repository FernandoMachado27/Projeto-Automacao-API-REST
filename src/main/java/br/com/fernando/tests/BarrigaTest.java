package br.com.fernando.tests;

import org.junit.Before;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class BarrigaTest extends BaseTest{
	
	private String TOKEN;
	
	@Before
	public void login () {
		Map<String, String> login = new HashMap<>();
		login.put("email", "fernandooo@gmail.com");
		login.put("senha", "Senha");
		
		TOKEN = given()
		.body(login)
		.when()
		.post("/signin")
		.then()
		.statusCode(200)
		.extract().path("token");
	}
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
		.get("/contas")
		.then()
		.statusCode(401);
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.body("{\"nome\": \"conta qualquer\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(201);
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.body("{\"nome\": \"conta alterada\"}")
		.when()
		.put("/contas/1557472")
		.then()
		.statusCode(200)
		.body("nome", is("conta alterada"));
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.body("{\"nome\": \"conta alterada\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(400)
		.body("error", is("Já existe uma conta com esse nome!"));
	}
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		
		given()
		.header("Authorization", "JWT " + TOKEN)
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(201);
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
		given()
		.header("Authorization", "JWT " + TOKEN)
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
	public void naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao("18/01/2023");
		
		given()
		.header("Authorization", "JWT " + TOKEN)
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(400)
		.body("$", hasSize(1))
		.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.when()
		.delete("/contas/1557472")
		.then()
		.statusCode(500)
		.body("constraint", is("transacoes_conta_id_foreign"));
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.when()
		.get("/saldo")
		.then()
		.statusCode(200)
		.body("find{it.conta_id == 1557472}.saldo", is("100.00")); // pega o saldo de apenas esta conta
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		given()
		.header("Authorization", "JWT " + TOKEN)
		.when()
		.delete("/transacoes/1456032")
		.then()
		.statusCode(204);
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(1557472);
//		mov.getUsuario_id();
		mov.setDescricao("Descricao da movimentacao");
		mov.setEnvolvido("Envolvido na mov");
		mov.setTipo("REC");
		mov.setData_transacao("01/01/2000");
		mov.setData_pagamento("10/05/2010");
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	
}
