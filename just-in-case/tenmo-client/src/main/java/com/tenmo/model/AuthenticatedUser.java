package com.tenmo.model;

public class AuthenticatedUser {
	
	private String token;
	private ClientUser clientUser;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ClientUser getUser() {
		return clientUser;
	}
	public void setUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}
}
