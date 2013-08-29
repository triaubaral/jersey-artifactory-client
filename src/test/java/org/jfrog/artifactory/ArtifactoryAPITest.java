package org.jfrog.artifactory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.api.impl.ArtifactoryAPIImpl;
import org.jfrog.artifactory.param.DefautParameterBuilder;
import org.jfrog.artifactory.param.SettingsKey;
import org.jfrog.artifactory.param.SettingsLoader;
import org.junit.Before;

public abstract class ArtifactoryAPITest {
	
	private static Logger logger = Logger.getLogger(ArtifactoryAPITest.class);
	
	protected static File testArtifact;
	
	protected String username;
	protected String password;
	protected ArtifactoryAPIImpl artifactoryAPI;
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
				
		artifactoryAPI = new ArtifactoryAPIImpl(new DefautParameterBuilder());		
		
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
