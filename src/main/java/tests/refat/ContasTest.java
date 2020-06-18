package tests.refat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BaseTest;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest{
	
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
	
	public Integer getIdContaPorNome(String name){
		return given()
		.when()
			.get("/contas?name="+name)
		.then()
		.extract().path("id[0]")
		;
	}
}