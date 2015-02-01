package com.sfdc.local;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SFDCLoginReponse {

	private String id,issued_at,token_type,instance_url,signature,access_token;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(String issued_at) {
		this.issued_at = issued_at;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getInstance_url() {
		return instance_url;
	}

	public void setInstance_url(String instance_url) {
		this.instance_url = instance_url;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String toString() {
		   return ToStringBuilder.reflectionToString(this);
		 }
	
}
