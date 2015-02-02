/**
 * 
 */
package com.sfdc.local;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.cedarsoftware.util.io.JsonWriter;

/**
 * @author gandrala
 *
 */
public class SFDCPostData {

	/**
	 * @param args
	 */
	//List<Account> accounts
	public JSONObject CreateAccounts()
	{
		JSONObject accountsJSON = new JSONObject();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try
		{
			HttpPost httpPost = new HttpPost(SFDCConnector.INSTANCE.getInstanceURL()+ "/services/data/v29.0/sobjects/Account");
			httpPost.addHeader("Authorization", "Bearer "+SFDCConnector.INSTANCE.getAccessToken());
			httpPost.addHeader("X-PrettyPrint", "1");
			JSONObject account = new JSONObject();
			account.put("Name", "x-Account1");
			account.put("Phone", "1-408-907-9090");
			System.out.println(JsonWriter.formatJson(account.toString()));
			StringEntity se = new StringEntity(account.toString()); 
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			String body = EntityUtils.toString(response.getEntity());
			System.out.println(JsonWriter.formatJson(body));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (httpClient!=null)
			{
				try {
					httpClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return accountsJSON;
	}
	
	public static void main(String[] args) {
		new SFDCPostData().CreateAccounts();

	}

}
