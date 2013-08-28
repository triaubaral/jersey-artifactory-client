package org.jfrog.artifactory.param;

public class DefautParameterBuilder extends ParameterBuilder {
	
	protected SettingsLoader settingsLoader = new SettingsLoader();	
	
	@Override
	public void buildHomeUrl() {
		parameter.setHomeUrl(settingsLoader.getSetting(SettingsKey.ARTIFACTORY_URL));
		
	}

	@Override
	public void buildPath() {
		parameter.setPath(settingsLoader.getSetting(SettingsKey.PATH));		
	}

	@Override
	public void buildRepository() {
		parameter.setRepository(settingsLoader.getSetting(SettingsKey.REPOSITORY));		
	}

	@Override
	public void buildUsername() {
		parameter.setUsername(settingsLoader.getSetting(SettingsKey.USERNAME));		
	}

	@Override
	public void buildPassword() {
		parameter.setPassword(settingsLoader.getSetting(SettingsKey.PASSWORD));
	}

	

}
