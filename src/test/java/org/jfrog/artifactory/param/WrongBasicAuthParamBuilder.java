package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongBasicAuthParamBuilder extends DefautParameterBuilder {
	
	@Override
	public void buildUsername() {
		parameter.setUsername("wrongusername");
	}
	
	@Override
	public void buildPassword() {
		parameter.setPassword("wrongpassword");
	}

}
