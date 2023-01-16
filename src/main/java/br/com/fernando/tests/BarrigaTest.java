package br.com.fernando.tests;

import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest{
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
		.get("/contas")
		.then()
		.statusCode(401);
	}

}
