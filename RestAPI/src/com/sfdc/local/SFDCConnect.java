/**
 * 
 */
package com.sfdc.local;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author gandrala This class prepares the http post payload and returns all
 *         the login parameters. Call oauth2Login() method which returns a java bean
 *         of type SFDCLoginResponse. To print all bean property values use the
 *         toString() method of SFDCLoginResponse. The method toString() is
 *         overwritten in bean class to output values using reflection.
 * 
 *         To close the session call the oauth2Loout() method by passing the
 *         access token. This method calls the revoke URL to close the session.
 *         
 */
public class SFDCConnect {

	/*
	 * Define constants that are needed to pass as login parameters to HTTP
	 * post. Another approach could be load from properties file or move all the
	 * constants to a separate class.
	 */

	private static final String LOGIN_URL = "https://login.salesforce.com/services/oauth2/token";
	private static final String LOGOUT_URL = "https://login.salesforce.com/services/oauth2/revoke";
	private static final String USER_NAME = "sfdc_gandrala@yahoo.com";
	private static final String PASSWORD = "subbu123";
	private static final String CONSUMER_KEY = "3MVG9fMtCkV6eLhceT8vn9YMViUWHKfylY7IttVnkKwPqoeiVMcn_daTzTPNRwStYDmrrVRRBbdzZPfwjQ3Ex";
	private static final String CONSUMER_SECRET = "6912036742224700531";

	public SFDCLoginReponse oauth2Login() {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		SFDCLoginReponse loginResponse = new SFDCLoginReponse();
		try {
			System.out.println("Building the request payload");
			HttpPost httpPost = new HttpPost(LOGIN_URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", "password"));
			params.add(new BasicNameValuePair("username", USER_NAME));
			params.add(new BasicNameValuePair("password", PASSWORD));
			params.add(new BasicNameValuePair("client_id", CONSUMER_KEY));
			params.add(new BasicNameValuePair("client_secret", CONSUMER_SECRET));
			StringEntity se = new UrlEncodedFormEntity(params);
			se.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(se);
			httpPost.addHeader("X-PrettyPrint", "1");
			System.out.println("Before executing post");
			HttpResponse response = httpClient.execute(httpPost);
			String body = EntityUtils.toString(response.getEntity());
			//System.out.println(body);
			
			JSONObject json = (JSONObject)new JSONParser().parse(body);
			loginResponse.setId((String) json.get("id"));
			loginResponse.setAccess_token((String) json.get("access_token"));
			loginResponse.setInstance_url((String) json.get("instance_url"));
			loginResponse.setIssued_at((String) json.get("issued_at"));
			loginResponse.setToken_type((String) json.get("token_type"));
			loginResponse.setSignature((String) json.get("signature"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return loginResponse;

	}

	public Boolean oauth2Loout(String token) {
		Boolean status = false;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// Build httpget Uri
			URI uri = new URIBuilder(LOGOUT_URL).addParameter("token", token)
					.build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("X-PrettyPrint", "1");
			HttpResponse response = httpClient.execute(httpGet);
			System.out.println("Logout Get response" + response.toString());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				status = true;
			} else {
				System.err.println("failue");
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

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		SFDCConnect conn = new SFDCConnect();
		SFDCLoginReponse response = conn.oauth2Login();
//		Boolean logout = conn.oauth2Loout(response.getAccess_token());
//		System.out.println("Logout status:" + logout);

	}

}
