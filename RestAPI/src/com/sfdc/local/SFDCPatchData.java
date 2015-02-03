/**
 * 
 */
package com.sfdc.local;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

/**
 * @author gandrala This class prepares the http patch payload to update the
 *         account data. if update is successful then status true is returned.
 * 
 */
public class SFDCPatchData {

	/**
	 * @param args
	 */

	public boolean updateAccount(Account acc) {
		boolean status = false;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {

			HttpPatch httpPatch = new HttpPatch(
					SFDCConnector.INSTANCE.getInstanceURL()
							+ "/services/data/v29.0/sobjects/Account/"
							+ acc.getId());
			httpPatch.addHeader("Authorization", "Bearer "
					+ SFDCConnector.INSTANCE.getAccessToken());
			httpPatch.addHeader("X-PrettyPrint", "1");
			JSONObject account = new JSONObject();
			// In prod do null checks
			account.put("Name", acc.getName());
			account.put("Region__c", acc.getRegion());
			StringEntity se = new StringEntity(account.toString());
			se.setContentType("application/json");
			httpPatch.setEntity(se);
			HttpResponse response = httpClient.execute(httpPatch);
			System.out.println(response.toString());
			if (response.getStatusLine().getStatusCode() == 204) {
				status = true;
			} else {
				System.out.println("Updated Failed:"
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
		return status;
	}

	// private static class HttpPatch extends HttpPost {
	// public HttpPatch(String uri) {
	// super(uri);
	// }
	//
	// public String getMethod() {
	// return "PATCH";
	// }
	// }
	public static void main(String[] args) {
		Account acc = new Account();
		acc.setId("001j000000FWkqCAAT");
		acc.setName("Third Account");
		acc.setRegion("Australia");
		System.out.println(new SFDCPatchData().updateAccount(acc));

	}

}
