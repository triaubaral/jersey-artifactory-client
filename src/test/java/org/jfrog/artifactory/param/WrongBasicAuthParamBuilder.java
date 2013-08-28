package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongBasicAuthParamBuilder extends DefautParameterBuilder {
	
	@Override
	public void buildUsername() {
		parameter.setUsername(settingsLoader.getSetting(SettingsKey.WRONG_LOGIN));
	}
	
	@Override
	public void buildPassword() {
		parameter.setPassword(settingsLoader.getSetting(SettingsKey.WRONG_PASSWORD));
	}

}
