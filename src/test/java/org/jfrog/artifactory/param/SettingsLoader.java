package org.jfrog.artifactory.param;

import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public final class SettingsLoader {
	
	private static Logger logger = Logger.getLogger(SettingsLoader.class);
	
	private Properties settings;
	
	public SettingsLoader(){
		
		settings = new Properties();
		try {
			settings.load(getClass().getResourceAsStream("/settings.properties"));
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
	}
	
	public String getSetting(final SettingsKey pKey){	
		logger.debug("Get setting for key :"+pKey);
		return settings.getProperty(pKey.name());
	}
	
	public void showParameters(){
		logger.debug("====== Properties from settings.properties =====");
		for(Entry<Object, Object> property : this.settings.entrySet()){
			logger.debug(property.getKey()+"="+property.getValue());
		}
		logger.debug("================================================");
	}

}
