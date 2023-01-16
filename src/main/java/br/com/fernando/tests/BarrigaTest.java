package br.com.fernando.tests;

import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class BarrigaTest extends BaseTest{
	
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
		Map<String, String> login = new HashMap<>();
		login.put("email", "fernandooo@gmail.com");
		login.put("senha", "Senha");
		
		String token = given()
		.body(login)
		.when()
		.post("/signin")
		.then()
		.statusCode(200)
		.extract().path("token");
		
		given()
		.header("Authorization", "JWT " + token)
		.body("{\"nome\": \"conta qualquer\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(201);
	}
	
}
