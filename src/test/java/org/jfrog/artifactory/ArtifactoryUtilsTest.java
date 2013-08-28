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
import org.jfrog.artifactory.param.SettingsKey;
import org.jfrog.artifactory.param.SettingsLoader;
import org.junit.Before;

public abstract class ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(ArtifactoryUtilsTest.class);
	
	protected static File testArtifact;
	
	protected String username;
	protected String password;
	protected ArtifactoryAPI artifactoryAPI;
	//protected Properties settings; 
	protected static SettingsLoader settingsLoader = new SettingsLoader();
	
	
	@Before
	public void init(){
		
		logger.info("Settings intialisation");		
		
		if(logger.isDebugEnabled()){
			settingsLoader.showParameters();
		}
		
		testArtifact = new File(settingsLoader.getSetting(SettingsKey.OUTPUT_JAR));		
		
		initFile(testArtifact, settingsLoader.getSetting(SettingsKey.INPUT_JAR));
		  
		username = settingsLoader.getSetting(SettingsKey.USERNAME);
		password = settingsLoader.getSetting(SettingsKey.PASSWORD);
		
		logger.info("ArtifactoryConfig object intialisation");
		
		
		//Parameter params = ;		
		artifactoryAPI = new ArtifactoryAPI(new DefautParameterBuilder());		
		
		logger.debug(artifactoryAPI);
		
	}
	
	
	private void initFile(final File pFileToFill, final String pPathInputStream){
		
		try {
			FileUtils.write(pFileToFill, IOUtils.toString(this.getClass().getResourceAsStream(pPathInputStream)));
		} catch (IOException e) {			
			logger.error(e);
		}
	}

}
