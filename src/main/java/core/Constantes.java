package core;

import io.restassured.http.ContentType;

public interface Constantes {
	
	String APP_URL_BASE = "https://barrigarest.wcaquino.me";
	Integer APP_PORT = 443; // para http seria 80
	String APP_BASE_PATH = ""; // versao
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 5000L;
}
