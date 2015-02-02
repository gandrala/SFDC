package com.sfdc.local;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.cedarsoftware.util.io.JsonWriter;


public class ExtractJSON {

	public static void main(String[] args) throws Exception {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(new File("").getAbsolutePath()+"/src/com/sfdc/local/accounts.json"));		
		JSONObject json = new JSONObject(JSONValue.toJSONString(obj));		
		System.out.println(JsonWriter.formatJson(json.toString()));
		JSONObject urls = (JSONObject) json.getJSONObject("objectDescribe").get("urls");
		System.out.println(JsonWriter.formatJson(urls.toString()));
		JSONArray records = json.getJSONArray("recentItems");
		JSONObject accountRecords = new JSONObject();		
		List<JSONObject> accountList = new ArrayList<>();
		for (int i=0;i<records.length();i++)
		{
			JSONObject r = records.getJSONObject(i);
			System.out.println(i+") Name:"+r.getString("Name")+", Id:"+r.getString("Id"));
			JSONObject account = new JSONObject();			
			account.put("Id",r.getString("Id"));
			account.put("Name",r.getString("Name"));
			accountList.add(account);
		}			
		accountRecords.put("accounts", accountList);
		System.out.println(JsonWriter.formatJson(accountRecords.toString()));
				

	}

}
