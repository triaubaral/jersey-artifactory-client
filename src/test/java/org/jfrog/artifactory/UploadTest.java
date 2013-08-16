package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class UploadTest extends ArtifactoryUtilsTest{
	
	private static Logger logger = Logger.getLogger(UploadTest.class);	
	
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
	
	@AfterClass
	public static void afterClass(){
		
		FileUtils.deleteQuietly(testArtifact);
		
	}
	
	private void testUpload(final File pfile, final ArtifactoryConfig pConfig){		
		
		ClientResponse response = ArtifactoryUtils.upload(pfile, pConfig);		
		assertEquals(201,response.getStatus());		
	}	
	
}
