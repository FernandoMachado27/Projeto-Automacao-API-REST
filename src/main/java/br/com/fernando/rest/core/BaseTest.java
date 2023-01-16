package br.com.fernando.rest.core;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class BaseTest implements Constantes {
	
	@BeforeClass
	public static void setup() {
		System.out.println("Iniciando...");
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT;
		RestAssured.basePath = APP_BASE_PATH;
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder(); // vai chamar em todas requisições
		reqBuilder.setContentType(APP_CONTENT_TYPE);
		RestAssured.requestSpecification = reqBuilder.build(); // atributo estático
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder(); // vai chamar em todas responses
		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		RestAssured.responseSpecification = resBuilder.build(); // atributo estático
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // add log().all quando tiver erro
	}

}
