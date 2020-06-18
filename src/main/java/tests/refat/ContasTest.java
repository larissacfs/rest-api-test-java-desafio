package tests.refat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static utils.BarrigaUtils.*;

import org.junit.Test;

import core.BaseTest;

public class ContasTest extends BaseTest{
	
	@Test
	public void deveIncluirUmaContaComSucesso() {
		given()
			.body("{ \"nome\": \"Conta inserida\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201) // created
			.extract().path("id")
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		given()
			.body("{ \"nome\": \"Conta alterada\"}")
			.pathParam("id", getIdContaPorNome("Conta para alterar"))
		.when()
			.put("/contas/{id}")
		.then()
			.statusCode(200) // ok
			.body("nome", is("Conta alterada"))
		;
	}
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {
		given()
			.body("{ \"nome\": \"Conta mesmo nome\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(400) // bad request
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
}