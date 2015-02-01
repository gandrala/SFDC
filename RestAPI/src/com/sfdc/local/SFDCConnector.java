/**
 * 
 */
package com.sfdc.local;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

/**
 * @author gandrala 
 * This class stores the refresh_token URL and use it to get the valid access token using
 * factory pattern. Sys admin can generate the refresh tokens ahead of the time by following steps.
 * Step 1 , get the authorization code
 * https://login.salesforce.com/services/oauth2/authorize?response_type=code&client_id=3MVG9fMtCkV6eLhceT8vn9YMViUWHKfylY7IttVnkKwPqoeiVMcn_daTzTPNRwStYDmrrVRRBbdzZPfwjQ3Ex&redirect_uri=https%3A%2F%2Flogin.salesforce.com%2Fservices%2Foauth2%2Fsuccess&state=mystate
 * Step 2, scrape the authorization code from the redirect_uri and do a POST using the below URL
 * curl --data "grant_type=authorization_code&code=aPrxHLAg3XMmPXsgBJJDD2yp.QS2N3YGHq8EJiOzugsloIi_L1bplMrmaXPBteXQgS4ILz1bOw%3D%3D&client_id=3MVG9fMtCkV6eLhceT8vn9YMViUWHKfylY7IttVnkKwPqoeiVMcn_daTzTPNRwStYDmrrVRRBbdzZPfwjQ3Ex&client_secret=6912036742224700531&redirect_uri=https%3A%2F%2Flogin.salesforce.com%2Fservices%2Foauth2%2Fsuccess" https://login.salesforce.com/services/oauth2/token
 * Step 3, collect the refresh token and use it as needed by doing a post to the below URL to get the access_token
 * curl --data "grant_type=refresh_token&client_id=3MVG9fMtCkV6eLhceT8vn9YMViUWHKfylY7IttVnkKwPqoeiVMcn_daTzTPNRwStYDmrrVRRBbdzZPfwjQ3Ex&client_secret=6912036742224700531&refresh_token=5Aep861E3ECfhV22nYr7mCEhBNRsybM24ajSflS1eiX9aqy9bGdY2ZejZA8yvAs.yIkhSVtudOgymhOLQgp.aZN" https://login.salesforce.com/services/oauth2/token       
 * This is hack to get a valid refresh_token that never expires for API's to do a I/O with salesforce.com
 * 
 */
public enum SFDCConnector {

	//INSTANCE is same as public static final SFDCConnector INSTANCE = new SFDCConnector()
	INSTANCE;
	private static final String REFRESH_TOKEN = "5Aep861E3ECfhV22nYr7mCEhBNRsybM24ajSflS1eiX9aqy9bGdY2ZejZA8yvAs.yIkhSVtudOgymhOLQgp.aZN";
	private static final long REFRESH_INTERVAL = 6900000;
	private static final String REFRESH_TOKEN_URL = "https://login.salesforce.com/services/oauth2/token";
	private static final String CLIENT_ID = "3MVG9fMtCkV6eLhceT8vn9YMViUWHKfylY7IttVnkKwPqoeiVMcn_daTzTPNRwStYDmrrVRRBbdzZPfwjQ3Ex";
	private static final String CLIENT_SECRET ="6912036742224700531";
	private static final String GRANT_TYPE = "refresh_token";

	private String accessToken;
	private long tokenIssuedTS=0;
	private String instanceURL;

	private SFDCConnector() {

	}

	public String getAccessToken() {
		// If the token issued time crossed the refresh interval time then regenerate new token.
		// This is needed because the access token is valid for the period of session timeout set by admin.
		// This is a safety net to avoid invalid access token issues during run time.
		if (tokenIssuedTS+REFRESH_INTERVAL<System.currentTimeMillis())
		{
			refreshToken();
		}		
			
		return accessToken;
	}
	public String getInstanceURL()
	{
		return instanceURL;
	}

	private void refreshToken() {
		// Do http post and capture the accessToken and tokenIssuesTS;
		CloseableHttpClient httpClient = HttpClients.createDefault();		
		try {
			
			HttpPost httpPost = new HttpPost(REFRESH_TOKEN_URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
			params.add(new BasicNameValuePair("client_id", CLIENT_ID));
			params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			params.add(new BasicNameValuePair("refresh_token", REFRESH_TOKEN));
			StringEntity se = new UrlEncodedFormEntity(params);
			se.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(se);
			httpPost.addHeader("X-PrettyPrint", "1");
			HttpResponse response = httpClient.execute(httpPost);
			String body = EntityUtils.toString(response.getEntity());
			System.out.println("Refresh Token Response:"+body);
			JSONObject json = new JSONObject(body);
			accessToken = json.getString("access_token");
			tokenIssuedTS = json.getLong("issued_at");
			instanceURL = json.getString("instance_url");
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if(httpClient!=null)
			{
				try {
					httpClient.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
	}

}
