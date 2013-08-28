package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongPasswordParamBuilder extends DefautParameterBuilder {

	@Override
	public void buildPassword() {
		parameter.setPassword(settingsLoader.getSetting(SettingsKey.WRONG_PASSWORD));
	}
}
