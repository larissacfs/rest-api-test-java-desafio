package core;

import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class BaseTest implements Constantes {

	@BeforeClass
	public static void setUp() {
		RestAssured.baseURI = APP_URL_BASE;
		RestAssured.port = APP_PORT;
		RestAssured.basePath = APP_BASE_PATH;
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(APP_CONTENT_TYPE); //definindo que o content type sempre sera json
		RestAssured.requestSpecification = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectResponseTime(lessThan(MAX_TIMEOUT)); //definindo timeout de resposta
		RestAssured.responseSpecification = resBuilder.build();
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // log aparece apenas quando da erro
	}
}
