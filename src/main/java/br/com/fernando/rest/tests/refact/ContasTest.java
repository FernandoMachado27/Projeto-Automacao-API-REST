package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest{ // Classe usando o reset da API
	
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
		
		RestAssured.get("/reset").then().statusCode(200); // sempre reseta as contas na API
	}
	
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
		Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");
		
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
	
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]"); // retorna o ID pelo nome que passei
	}

}
