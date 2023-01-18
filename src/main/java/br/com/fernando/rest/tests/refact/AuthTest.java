package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{ // Classe usando o reset da API
	
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
