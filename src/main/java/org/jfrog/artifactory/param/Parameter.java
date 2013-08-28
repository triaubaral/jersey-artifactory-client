package org.jfrog.artifactory.param;

public class Parameter {

	private String homeUrl;
	private String path;
	private String repository;
	private String username;
	private String password;
	
	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getWebResourcePath(){
		
		return this.homeUrl+"/"+this.repository+"/"+this.path;
	}
	
}
