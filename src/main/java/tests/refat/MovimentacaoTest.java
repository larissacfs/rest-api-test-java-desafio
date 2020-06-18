package tests.refat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static utils.BarrigaUtils.*;
import static utils.DataUtils.getDataDiferencaDias;

import org.junit.Test;

import core.BaseTest;
import tests.Movimentacao;

public class MovimentacaoTest extends BaseTest{

	@Test
	public void deveInserirMovivemtacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201) //created
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {
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
	public void naoDeveCadastrarMovimentacaoFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(getDataDiferencaDias(2));
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
	public void naoDeveRemoverContaComMovimentacao() {
		given()
			.pathParam("id", getIdContaPorNome("Conta para movimentacoes"))
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500) // Internal Server Error
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void deveRemoverUmaMovimentacao() {
		given()
			.pathParam("id", getIdMovimentacaoPorDescricao("Movimentacao para exclusao"))
		.when()
			.delete("/transacoes/{id}")
		.then()
			.statusCode(204)
		;
	}
	
	public Integer getIdContaPorNome(String name){
		return given()
		.when()
			.get("/contas?name="+name)
		.then()
		.extract().path("id[0]")
		;
	}
	
	
}
