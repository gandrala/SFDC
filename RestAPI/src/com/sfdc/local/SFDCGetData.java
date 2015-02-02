package com.sfdc.local;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author gandrala This class prepares the http get pay load and returns accounts up to 100 records
 * in JSON format. This internally uses the SFDCConnector to get the access_token and the instance URI.
 * This framework can be extended by adding more parameters like batchsize and recordoffset to support pagination.  
 * 
 */
public class SFDCGetData {

	public JSONObject getAccounts() {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject accountsJSON = new JSONObject();
		try {
			URI uri = new URIBuilder(SFDCConnector.INSTANCE.getInstanceURL()
					+ "/services/data/v29.0/sobjects/Account/")
			.addParameter("q","SELECT Id,Name FROM Account LIMIT 100")			
			.build();			
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("Authorization", "Bearer "+SFDCConnector.INSTANCE.getAccessToken());
			httpGet.addHeader("X-PrettyPrint", "1");
			HttpResponse response = httpClient.execute(httpGet);
			String body = EntityUtils.toString(response.getEntity());						
			JSONObject json = new JSONObject(body);			
			JSONArray records = json.getJSONArray("recentItems");			
			List<JSONObject> accountRecordsList = new ArrayList<>();
			for(int i=0;i<records.length();i++)
			{
				JSONObject account = new JSONObject();
				account.put("Id", records.getJSONObject(i).get("Id"));
				account.put("Name", records.getJSONObject(i).get("Name"));
				accountRecordsList.add(account);				
			}
			accountsJSON.put("accounts", accountRecordsList);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return accountsJSON;
	}

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject accountJSON = new SFDCGetData().getAccounts();
		
		try {
			System.out.println(JsonWriter.formatJson(accountJSON.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
