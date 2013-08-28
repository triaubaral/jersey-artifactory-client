package org.jfrog.artifactory;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.api.impl.ArtifactoryAPI;
import org.jfrog.artifactory.param.DefautParameterBuilder;
import org.jfrog.artifactory.param.Parameter;
import org.junit.Before;

public abstract class ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(ArtifactoryUtilsTest.class);
	
	protected static File testArtifact;
	
	protected String username;
	protected String password;
	protected ArtifactoryAPI artifactoryAPI;
	protected Properties settings; 
	protected enum SettingsKey{
		ARTIFACTORY_URL,
		INPUT_JAR,
		USERNAME,
		PASSWORD,
		REPOSITORY,
		PATH,
		WRONG_ARTIFACTORY_URL,
		WRONG_REPOSITORY,
		WRONG_PATH,
		WRONG_LOGIN,
		WRONG_PASSWORD,
		RESOURCE_TO_DELETE,
		OUTPUT_JAR;		
	}
	
	@Before
	public void init(){
		
		logger.info("Settings intialisation");
		
		settings = new Properties();
		try {
			settings.load(this.getClass().getResourceAsStream("/settings.properties"));
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
		
		if(logger.isDebugEnabled()){
			showParameters();
		}
		
		testArtifact = new File(getSetting(SettingsKey.OUTPUT_JAR));		
		
		initFile(testArtifact, getSetting(SettingsKey.INPUT_JAR));
		  
		username = getSetting(SettingsKey.USERNAME);
		password = getSetting(SettingsKey.PASSWORD);
		
		logger.info("ArtifactoryConfig object intialisation");
		
		
		//Parameter params = ;		
		artifactoryAPI = new ArtifactoryAPI(new DefautParameterBuilder());		
		
		logger.debug(artifactoryAPI);
		
	}
	
	protected String getSetting(final SettingsKey pKey){	
		logger.debug("Get setting for key :"+pKey);
		return settings.getProperty(pKey.name());
	}

	protected void showParameters(){
		logger.debug("====== Properties from settings.properties =====");
		for(Entry<Object, Object> property : this.settings.entrySet()){
			logger.debug(property.getKey()+"="+property.getValue());
		}
		logger.debug("================================================");
	}
	
	private void initFile(final File pFileToFill, final String pPathInputStream){
		
		try {
			FileUtils.write(pFileToFill, IOUtils.toString(this.getClass().getResourceAsStream(pPathInputStream)));
		} catch (IOException e) {			
			logger.error(e);
		}
	}

}
