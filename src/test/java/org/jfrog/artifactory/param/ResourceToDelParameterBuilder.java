package org.jfrog.artifactory.param;

public class ResourceToDelParameterBuilder extends DefautParameterBuilder {
	
	@Override
	public void buildPath() {
		parameter.setPath(settingsLoader.getSetting(SettingsKey.RESOURCE_TO_DELETE));
	}

}
