package gsonExample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import gsonExample.country;
import com.google.gson.*;

public class jsonParser {
	public static void main(String[] args)
	{
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader("test.json"));
			crew c2 = gson.fromJson(br, crew.class);
			System.out.println(c2.getcrewid());
			System.out.println(c2.getfname());
			System.out.println(c2.getlname());
			System.out.println(c2.getflights().getCode());
			System.out.println(c2.getflights().getNum());
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
