package org.jfrog.artifactory.param;

import java.util.HashMap;
import java.util.Map;

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

	@Override
	public void buildHeaders() {
		String headerStr = settingsLoader.getSetting(SettingsKey.HEADERS);
		
		Map<String,String> params = new HashMap<String,String>();
		
		String[] paramHeader = headerStr.split(",");
		
		for(String param : paramHeader){
			
			String[] keyValue = param.split(":");
			
			params.put(keyValue[0], keyValue[1]);
			
		}
		
		parameter.setHeaders(params);
		
	}

	

}
