package tools.string;

import java.util.Date;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonDateTest {

	@Test
	public void test() {
		
		String json = "1.01.2001";
		
		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
		
		Date date = gson.fromJson(json, Date.class);
		
		System.out.println(date);
		

	}

}
