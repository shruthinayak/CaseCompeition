package gsonExample;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import gsonExample.country;
import com.google.gson.*;

public class WriteJsonToFile {
	
	public static void main(String[] args)
	{
/*		country c1 = new country();
		c1.setName("United States");
		c1.setNoOfStates(52);*/
		flights f1 = new flights();
		f1.setCode("AA");
		f1.setNum("001");
		f1.setDep("2015-07-14T15:10:50+00:00");
		f1.setCheckin(false);
		f1.setLat("32.89");
		f1.setLon("97.03");
		crew c1 = new crew();
		c1.setcrewid("crw121");
		c1.setfname("Matt");
		c1.setlname("Collin");
		c1.setflight(f1);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(c1);
		try {
			FileWriter writer = new FileWriter("test.json");
			writer.write(json);
			writer.close();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(json);
	}
}
