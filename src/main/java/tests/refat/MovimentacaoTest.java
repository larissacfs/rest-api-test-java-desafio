package tests.refat;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;

import io.restassured.RestAssured;
import tests.Movimentacao;
import utils.DataUtils;

public class MovimentacaoTest {
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "larissacfdasilva2@gmail.com");
		login.put("senha", "123456ui");
		String token = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200) // ok
			.extract().path("token")
		;
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + token); // apis mais recentes usam o bearer
		RestAssured.get("/reset").then().statusCode(200);
	}
	
	public Integer getIdContaPorNome(String name){
		return given()
		.when()
			.get("/contas?name="+name)
		.then()
		.extract().path("id[0]")
		;
	}
	
	private Movimentacao getMovimentacaoValida(Integer id) {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(id);
		mov.setDescricao("Descricao");
		mov.setEnvolvido("Envolvido");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
}
