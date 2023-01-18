package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import br.com.fernando.rest.utils.BarrigaUtils;

public class ContasTest extends BaseTest{ // Classe usando o reset da API
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
		.body("{\"nome\": \"Conta inserida\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(201)
		.extract().path("id");
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
		.body("{\"nome\": \"Conta alterada\"}")
		.pathParam("id", CONTA_ID)
		.when()
		.put("/contas/{id}")
		.then()
		.statusCode(200)
		.body("nome", is("Conta alterada"));
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
		.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(400)
		.body("error", is("Já existe uma conta com esse nome!"));
	}

}
