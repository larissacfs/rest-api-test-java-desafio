package tests.refat.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import core.BaseTest;
import io.restassured.RestAssured;
import tests.refat.AuthTest;
import tests.refat.ContasTest;
import tests.refat.MovimentacaoTest;
import tests.refat.SaldoTest;

@RunWith(Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class MySuite extends BaseTest{
	
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
}
