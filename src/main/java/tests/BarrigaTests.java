package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import utils.DataUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTests extends BaseTest{
	
	private static String CONTA_NAME = "Conta " + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
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
	}
	
	@Test
	public void t02_deveIncluirUmaContaComSucesso() {
		CONTA_ID = given()
			.body("{ \"nome\": \"" + CONTA_NAME + "\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.extract().path("id")// created
		;
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {
		given()
			.body("{ \"nome\": \"" + CONTA_NAME + " alterada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.statusCode(200) // ok
			.body("nome", is(CONTA_NAME + " alterada"))
		;
	}
	
	@Test
	public void t04_naoDeveIncluirContaComNomeRepetido() {
		given()
			.body("{ \"nome\": \"" + CONTA_NAME + " alterada\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(400) // bad request
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void t05_deveInserirMovivemtacaoComSucesso() {
		Movimentacao mov = this.getMovimentacaoValida();
		MOV_ID = given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201) //created
			.extract().path("id")// created
		;
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosNaMovimentacao() {
		given()
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems("Data da Movimentação é obrigatório", 
					"Data do pagamento é obrigatório", 
					"Descrição é obrigatório", 
					"Interessado é obrigatório", 
					"Valor é obrigatório", 
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"))
		;
	}
	
	@Test
	public void t07_naoDeveCadastrarMovimentacaoFutura() {
		Movimentacao mov = this.getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(2));
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400) // bad request
			.body("$", hasSize(1))
			.body("param", hasItem("data_transacao"))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	
	@Test
	public void t08_naoDeveRemoverContaComMovimentacao() {
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500) // Internal Server Error
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id = " + CONTA_ID + "}.saldo", is("200.00"))
		;
	}
	
	@Test
	public void t10_deveRemoverUmaMovimentacao() {
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401) //Unauthorized
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
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
