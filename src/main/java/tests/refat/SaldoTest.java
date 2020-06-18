package tests.refat;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import core.BaseTest;
import utils.BarrigaUtils;

public class SaldoTest extends BaseTest{
	
	@Test
	public void deveCalcularSaldoContas() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPorNome("Conta para saldo");
		System.out.println(CONTA_ID);
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			//.body("find{it.conta_id = " + CONTA_ID + "}.saldo", is("534.00"))
		;
	}

}
