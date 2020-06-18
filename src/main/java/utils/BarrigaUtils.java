package utils;

import static io.restassured.RestAssured.given;

import tests.Movimentacao;

public class BarrigaUtils {

	public static Integer getIdContaPorNome(String name){
		return given()
		.when()
			.get("/contas?name="+name)
		.then()
		.extract().path("id[0]")
		;
	}
	
	public static Integer getIdMovimentacaoPorDescricao(String des){
		return given()
		.when()
			.get("/transacoes?descricao="+des)
		.then()
		.extract().path("id[0]")
		;
	}
	
	public static Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(getIdContaPorNome("Conta para movimentacoes"));
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
