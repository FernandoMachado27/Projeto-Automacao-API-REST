package br.com.fernando.rest.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {
	
	public static Integer getIdContaPeloNome(String nome) { // estático para utilizar sem precisar instanciar
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]"); // retorna o ID pelo nome que passei
	}
	
	public static Integer getIdMovPelaDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]"); // retorna o ID pelo nome que passei
	}

}
