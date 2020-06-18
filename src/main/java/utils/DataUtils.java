package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtils {
	
	public static String getDataDiferencaDias(int dias) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, dias);
		return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
	}
	
	
}
