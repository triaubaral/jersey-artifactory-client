package org.jfrog.artifactory.param;

public class DefautParameterBuilder extends ParameterBuilder {

	@Override
	public void buildHomeUrl() {
		parameter.setHomeUrl("http://localhost:8081/artifactory");
		
	}

	@Override
	public void buildPath() {
		parameter.setPath("org/jfrog/hello/helloworld.jar");		
	}

	@Override
	public void buildRepository() {
		parameter.setRepository("libs-release-local");		
	}

	@Override
	public void buildUsername() {
		parameter.setUsername("admin");		
	}

	@Override
	public void buildPassword() {
		parameter.setPassword("password");
	}

	

}
