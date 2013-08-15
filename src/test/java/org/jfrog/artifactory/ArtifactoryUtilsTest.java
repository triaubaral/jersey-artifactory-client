package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(ArtifactoryUtilsTest.class);
		
	private static File testArtifact;
	private String username;
	private String password;
	private ArtifactoryConfig config;
	private Properties settings; 
	private enum SettingsKey{
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
		config = new ArtifactoryConfig();		
		config.setHomeUrl(getSetting(SettingsKey.ARTIFACTORY_URL));		
		config.setRepository(getSetting(SettingsKey.REPOSITORY));
		config.setPath(getSetting(SettingsKey.PATH));
		config.setBasicAuth(username, password);		
		
		logger.debug(config);
		
	}	
	
	@Test
	public void testEmptyFileUpload(){		
		testUpload(null, config);
	}
	
	@Test	
	public void testWrongHomeUrlFileUpload(){	
		
		logger.info("testWrongHomeUrlFileUpload");
		
		try{
			config.setHomeUrl(getSetting(SettingsKey.WRONG_ARTIFACTORY_URL));		
			testUpload(testArtifact, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong ARTIFACTORY_URL");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(405,e.getStatus());
			logger.info("Expected message error :"+e.getStatus() +" "+ e.getMessage());
		}
		
	}
	
	@Test
	public void testWrongRepositoryFileUpload(){
		
		logger.info("testWrongRepositoryFileUpload");
		
		try{
			config.setRepository(getSetting(SettingsKey.WRONG_REPOSITORY));
			testUpload(testArtifact, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong REPOSITORY");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(403,e.getStatus());
			logger.info("Expected message error :"+e.getStatus() +" "+ e.getMessage());
		}
	}
	
	@Test
	public void testWrongPathFileUpload(){
		logger.info("testWrongPathFileUpload");
		config.setPath(getSetting(SettingsKey.WRONG_PATH));
		testUpload(testArtifact, config);		
		//A path is created at the wrong path and it does not throw any error.		
	}
	
	@Test
	public void testWrongBasicAuthFileUpload(){
		logger.info("testWrongBasicAuthFileUpload");
		try{
			config.setBasicAuth(getSetting(SettingsKey.WRONG_LOGIN),getSetting(SettingsKey.WRONG_PASSWORD));
			testUpload(testArtifact, config);
			fail("Should have thrown ArtifactoryUtilsException for wrong LOGIN/PASSWORD");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(401,e.getStatus());
			logger.info("Expected message error :"+e.getStatus() +" "+ e.getMessage());			
		}
	}
	
	@Test
	public void testEmptyConfigFileUpload(){
		logger.info("testEmptyConfigFileUpload");
		try{
			testUpload(testArtifact, null);
			fail("Should have thrown ArtifactoryUtilsException for EMPTY CONFIG FILE");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(0,e.getStatus());
			logger.info("Expected message error :"+e.getMessage());
		}
	}
	
	@Test
	public void testRegularUpload() {	
		logger.info("testRegularUpload");
		testUpload(testArtifact, config);
	}
	
	
	
	@Test
	public void testUnknwownArtifactDownload(){
		logger.info("testUnknwownArtifactDownload");
		try{
		
			doDownload(getWebResourcePath()+"/"+getSetting(SettingsKey.WRONG_PATH)+"*è/''");
			fail("Should have thrown ArtifactoryUtilsException for ARTIFACT NOT FOUND");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(404,e.getStatus());
			logger.info(e.getMessage());
		}
	}
	
	
	@Test
	public void testRegularDownload(){	
		logger.info("testRegularDownload");
		final File artifactDownloaded = doDownload(getWebResourcePath());
		assertNotNull(artifactDownloaded);
		assertEquals(testArtifact.length(), artifactDownloaded.length());
		FileUtils.deleteQuietly(artifactDownloaded);
	}
	
	@Test
	public void testRegularDelete(){
		logger.info("testRegularDelete");		
		final String workingUrl = getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+getSetting(SettingsKey.REPOSITORY)+"/";
		logger.debug("WorkingUrl ="+workingUrl);
		ClientResponse response1 = ArtifactoryUtils.delete(workingUrl+getSetting(SettingsKey.RESOURCE_TO_DELETE), config);
		ClientResponse response2 = ArtifactoryUtils.delete(workingUrl+getSetting(SettingsKey.WRONG_PATH), config);
		assertEquals(204,response1.getStatus());
		assertEquals(204,response2.getStatus());
		
	}
	
	private File doDownload(final String pWebResoucePath){
		final String pathWebResouce = pWebResoucePath;
		
		final File fileDestination = new File(getSetting(SettingsKey.OUTPUT_JAR));
		
		if(fileDestination.exists())
			FileUtils.deleteQuietly(fileDestination);
		
		return ArtifactoryUtils.download(pathWebResouce, fileDestination);	
		
	}	
	
	private void initFile(final File pFileToFill, final String pPathInputStream){
		
		try {
			FileUtils.write(pFileToFill, IOUtils.toString(this.getClass().getResourceAsStream(pPathInputStream)));
		} catch (IOException e) {			
			logger.error(e);
		}
	}
	
	private void testUpload(final File pfile, final ArtifactoryConfig pConfig){		
		
		ClientResponse response = ArtifactoryUtils.upload(pfile, pConfig);		
		assertEquals(201,response.getStatus());		
	}
	
	private String getSetting(final SettingsKey pKey){	
		logger.debug("Get setting for key :"+pKey);
		return settings.getProperty(pKey.name());
	}
	
	private String getWebResourcePath(){
		final String webResourcePath =  getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+getSetting(SettingsKey.REPOSITORY)+"/"+getSetting(SettingsKey.PATH);
		logger.debug("Get WebResourcePath :"+webResourcePath);
		return webResourcePath;
	}
	
	private void showParameters(){
		logger.debug("====== Properties from settings.properties =====");
		for(Entry<Object, Object> property : this.settings.entrySet()){
			logger.debug(property.getKey()+"="+property.getValue());
		}
		logger.debug("================================================");
	}
	
	@AfterClass
	public static void afterClass(){
		
		FileUtils.deleteQuietly(testArtifact);
		
	}
	
}
