package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import io.restassured.RestAssured;

public class SaldoTest extends BaseTest{ // Classe usando o reset da API
	
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
	public void deveCalcularSaldoContas() {
		Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");
		
		given()
		.when()
		.get("/saldo")
		.then()
		.statusCode(200)
		.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00")); // pega o saldo de apenas esta conta
	}
	
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]"); // retorna o ID pelo nome que passei
	}

}
