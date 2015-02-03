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

	public JSONObject CreateAccounts(Account acc) {
		JSONObject accountsJSON = new JSONObject();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(
					SFDCConnector.INSTANCE.getInstanceURL()
							+ "/services/data/v29.0/sobjects/Account");
			httpPost.addHeader("Authorization", "Bearer "
					+ SFDCConnector.INSTANCE.getAccessToken());
			httpPost.addHeader("X-PrettyPrint", "1");
			JSONObject account = new JSONObject();
			account.put("Name", acc.getName());
			account.put("Phone", acc.getPhone());
			//System.out.println(JsonWriter.formatJson(account.toString()));
			StringEntity se = new StringEntity(account.toString());
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			//System.out.println(response.toString());
			if (response.getStatusLine().getStatusCode() == 201) {
				String body = EntityUtils.toString(response.getEntity());
				System.out.println(JsonWriter.formatJson(body));
				accountsJSON.put("status", body);
			} else {
				System.out.println("Error while creating the record:"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
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

//	public static void main(String[] args) {
//		Account a = new Account();
//		a.setName("3 Account");
//		a.setPhone("+1 925 679 7899");
//		System.out.println(new SFDCPostData().CreateAccounts(a).toString());
//
//	}

}
