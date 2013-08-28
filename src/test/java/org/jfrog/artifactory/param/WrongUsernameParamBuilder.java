package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongUsernameParamBuilder extends DefautParameterBuilder {
	@Override
	public void buildUsername() {
		parameter.setUsername(settingsLoader.getSetting(SettingsKey.WRONG_LOGIN));
	}

}
