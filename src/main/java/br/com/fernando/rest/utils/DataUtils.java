package br.com.fernando.rest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {
	
	public static String getDataComDiferencaDias(Integer qtdeDias) {
		Calendar cal = Calendar.getInstance(); // instancia que retorna data atual
		cal.add(Calendar.DAY_OF_MONTH, qtdeDias); 
		// formatar em String
		return getDataFormatada(cal.getTime());
	}
	
	public static String getDataFormatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

}
