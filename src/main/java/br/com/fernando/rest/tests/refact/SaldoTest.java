package br.com.fernando.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import br.com.fernando.rest.core.BaseTest;
import br.com.fernando.rest.utils.BarrigaUtils;

public class SaldoTest extends BaseTest{ // Classe usando o reset da API
	
	@Test
	public void deveCalcularSaldoContas() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para saldo");
		
		given()
		.when()
		.get("/saldo")
		.then()
		.statusCode(200)
		.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00")); // pega o saldo de apenas esta conta
	}

}
