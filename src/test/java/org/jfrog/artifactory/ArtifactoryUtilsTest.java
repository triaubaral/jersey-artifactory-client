package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(ArtifactoryUtilsTest.class);
		
	private File artifactToDownload;
	private File artifactToUpload;
	private String username;
	private String password;
	private ArtifactoryConfig config;
	private Properties settings; 
	private enum SettingsKey{
		ARTIFACTORY_URL,
		PATH_LOCAL_TEST_JAR,
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
		PATH_TO_DOWNLOADED_RESOURCE;		
	}
			
	@Before
	public void init(){
		
		settings = new Properties();
		try {
			settings.load(this.getClass().getResourceAsStream("/settings.properties"));
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
		
		artifactToDownload = new File(getSetting(SettingsKey.PATH_LOCAL_TEST_JAR));
		artifactToUpload = new File(getSetting(SettingsKey.PATH_LOCAL_TEST_JAR));
		
		username = getSetting(SettingsKey.USERNAME);
		password = getSetting(SettingsKey.PASSWORD);
		
		config = new ArtifactoryConfig();		
		config.setHomeUrl(getSetting(SettingsKey.ARTIFACTORY_URL));		
		config.setRepository(getSetting(SettingsKey.REPOSITORY));
		config.setPath(getSetting(SettingsKey.PATH));
		config.setBasicAuth(username, password);
	}	
	
	private String getSetting(final SettingsKey pKey){		
		return settings.getProperty(pKey.name());
	}
	
	private String getWebResourcePath(){
		
		return getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+getSetting(SettingsKey.REPOSITORY)+"/"+getSetting(SettingsKey.PATH);
	}
	
	@Test
	public void testEmptyFileUpload(){		
		testUpload(null, config);
	}
	
	@Test	
	public void testWrongHomeUrlFileUpload(){	
		
		try{
			config.setHomeUrl(getSetting(SettingsKey.WRONG_ARTIFACTORY_URL));		
			testUpload(artifactToUpload, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong ARTIFACTORY_URL");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(405,e.getStatus());
			logger.error(e.getStatus() +" "+ e.getMessage());
		}
		
	}
	
	@Test
	public void testWrongRepositoryFileUpload(){
		try{
			config.setRepository(getSetting(SettingsKey.WRONG_REPOSITORY));
			testUpload(artifactToUpload, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong REPOSITORY");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(403,e.getStatus());
			logger.error(e.getStatus() +" "+ e.getMessage());
		}
	}
	
	@Test
	public void testWrongPathFileUpload(){			
		config.setPath(getSetting(SettingsKey.WRONG_PATH));
		testUpload(artifactToUpload, config);		
		//A path is created at the wrong path and it does not throw any error.		
	}
	
	@Test
	public void testWrongBasicAuthFileUpload(){
		try{
			config.setBasicAuth(getSetting(SettingsKey.WRONG_LOGIN),getSetting(SettingsKey.WRONG_PASSWORD));
			testUpload(artifactToUpload, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong LOGIN/PASSWORD");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(401,e.getStatus());
			logger.error(e.getStatus() +" "+ e.getMessage());			
		}
	}
	
	@Test
	public void testEmptyConfigFileUpload(){
		try{
			testUpload(artifactToUpload, null);
			fail("Should have thrown ArtifactoryUtilsException for EMPTY CONFIG FILE");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(0,e.getStatus());
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void testRegularUpload() {		
		testUpload(artifactToUpload, config);
	}
	
	private void testUpload(final File pfile, final ArtifactoryConfig pConfig){		
						
		ClientResponse response = ArtifactoryUtils.upload(pfile, pConfig);
		
		assertEquals(201,response.getStatus());		
	}
	
	@Test
	public void testUnknwownArtifactDownload(){
		
		try{
		
			doDownload(getWebResourcePath()+"/"+getSetting(SettingsKey.WRONG_PATH)+"*è/''");
			fail("Should have thrown ArtifactoryUtilsException for ARTIFACT NOT FOUND");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(404,e.getStatus());
			logger.error(e.getMessage());
		}
	}
	
	
	@Test
	public void testRegularDownload(){		
		final File artifactDownloaded = doDownload(getWebResourcePath());
		assertNotNull(artifactDownloaded);
		assertEquals(artifactToDownload.length(), artifactDownloaded.length());
		FileUtils.deleteQuietly(artifactDownloaded);
	}
	
	private File doDownload(final String pWebResoucePath){
		final String pathWebResouce = pWebResoucePath;
		
		final File fileDestination = new File(getSetting(SettingsKey.PATH_TO_DOWNLOADED_RESOURCE));	
		
		if(fileDestination.exists())
			FileUtils.deleteQuietly(fileDestination);
		
		return ArtifactoryUtils.download(pathWebResouce, fileDestination);	
		
	}	
	
	
	@Test
	public void testRegularDelete(){
		final String workingUrl = getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+getSetting(SettingsKey.REPOSITORY)+"/";
		ClientResponse response1 = ArtifactoryUtils.delete(workingUrl+getSetting(SettingsKey.RESOURCE_TO_DELETE), config);
		ClientResponse response2 = ArtifactoryUtils.delete(workingUrl+getSetting(SettingsKey.WRONG_PATH), config);
		assertEquals(204,response1.getStatus());
		assertEquals(204,response2.getStatus());
		
	}	
	

	
}
