package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongRepositoryParamBuilder extends DefautParameterBuilder {
	
	@Override
	public void buildRepository() {
		parameter.setRepository(settingsLoader.getSetting(SettingsKey.WRONG_REPOSITORY));
	}

}
