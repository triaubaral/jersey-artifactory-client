package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.api.impl.ArtifactoryAPIImpl;
import org.jfrog.artifactory.param.WrongBasicAuthParamBuilder;
import org.jfrog.artifactory.param.WrongHomeUrlParamBuilder;
import org.jfrog.artifactory.param.WrongPathParamBuilder;
import org.jfrog.artifactory.param.WrongRepositoryParamBuilder;
import org.junit.AfterClass;
import org.junit.Test;

public class ImportTest extends ArtifactoryAPITest{
	
	private static Logger logger = Logger.getLogger(ImportTest.class);	
	
	@Test
	public void testEmptyFileUpload(){		
		testUpload(null, artifactoryAPI);
	}
	
	@Test(expected=com.sun.jersey.api.client.ClientHandlerException.class)	
	public void testWrongHomeUrlFileUpload(){	
		
		logger.info("testWrongHomeUrlFileUpload");		
							
		testUpload(testArtifact, new ArtifactoryAPIImpl(new WrongHomeUrlParamBuilder()));
		fail("Should have thrown com.sun.jersey.api.client.ClientHandlerException for wrong ARTIFACTORY_URL");
			
	}
	
	@Test
	public void testWrongRepositoryFileUpload(){
		
		logger.info("testWrongRepositoryFileUpload");
		
		try{			
			testUpload(testArtifact, new ArtifactoryAPIImpl(new WrongRepositoryParamBuilder()));
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
		testUpload(testArtifact, new ArtifactoryAPIImpl(new WrongPathParamBuilder()));		
		//A path is created at the wrong path and it does not throw any error.		
	}
	
	@Test
	public void testWrongBasicAuthFileUpload(){
		logger.info("testWrongBasicAuthFileUpload");
		try{			
			testUpload(testArtifact, new ArtifactoryAPIImpl(new WrongBasicAuthParamBuilder()));
			fail("Should have thrown ArtifactoryUtilsException for wrong LOGIN/PASSWORD");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(401,e.getStatus());
			logger.info("Expected message error :"+e.getStatus() +" "+ e.getMessage());			
		}
	}
	
	@Test
	public void testRegularImport() {	
		logger.info("testRegularImport");
		testUpload(testArtifact, artifactoryAPI);
	}	
	
	
	@AfterClass
	public static void afterClass(){
		
		FileUtils.deleteQuietly(testArtifact);
		
	}
	
	private void testUpload(final File pfile, final ArtifactoryAPIImpl pPartifactoryAPI){		
		pPartifactoryAPI.importFrom(pfile);		
	}	
	
}
