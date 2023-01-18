package br.com.fernando.rest.tests.refact.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.fernando.rest.core.BaseTest;
import br.com.fernando.rest.tests.refact.AuthTest;
import br.com.fernando.rest.tests.refact.ContasTest;
import br.com.fernando.rest.tests.refact.MovimentacaoTest;
import br.com.fernando.rest.tests.refact.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class) // para o JUnit entender a classe como suite, conjunto de testes
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest{
	
	@BeforeClass // apenas uma vez para classe inteira, vai fazer nas 4 classes
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

}
