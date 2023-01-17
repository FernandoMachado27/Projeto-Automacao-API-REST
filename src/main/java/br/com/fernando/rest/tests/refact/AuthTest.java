package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{ // Classe usando o reset da API
	
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
	public void naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification; // para tirar o spec, que está mandando token
		req.removeHeader("Authorization");
		
		given()
		.when()
		.get("/contas")
		.then()
		.statusCode(401);
	}
}
