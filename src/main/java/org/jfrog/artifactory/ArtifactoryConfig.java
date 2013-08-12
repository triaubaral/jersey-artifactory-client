package org.jfrog.artifactory;


public class ArtifactoryConfig {
	
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
	
	public String getPassword() {
		return password;
	}
		
	public void setBasicAuth(final String pUsername, final String pPassword){		
		this.username = pUsername;
		this.password = pPassword;
	}
	
	public String getWebResourcePath(){
		
		return this.homeUrl+"/"+this.repository+"/"+this.path;
	}

}
