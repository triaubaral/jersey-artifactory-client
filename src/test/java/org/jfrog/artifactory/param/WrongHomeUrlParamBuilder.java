package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongHomeUrlParamBuilder extends DefautParameterBuilder {

	@Override
	public void buildHomeUrl() {
		parameter.setHomeUrl(settingsLoader.getSetting(SettingsKey.WRONG_ARTIFACTORY_URL));
	}
}
