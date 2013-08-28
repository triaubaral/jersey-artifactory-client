package org.jfrog.artifactory.param;

import org.jfrog.artifactory.param.DefautParameterBuilder;

public class WrongPathParamBuilder extends DefautParameterBuilder {

	@Override
	public void buildPath() {
		parameter.setPath(settingsLoader.getSetting(SettingsKey.WRONG_PATH));
	}
}
