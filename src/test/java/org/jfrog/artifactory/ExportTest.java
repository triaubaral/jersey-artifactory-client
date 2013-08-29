package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.api.impl.ArtifactoryAPIImpl;
import org.jfrog.artifactory.param.SettingsKey;
import org.jfrog.artifactory.param.WrongPathParamBuilder;
import org.junit.Test;

public class ExportTest extends ArtifactoryAPITest {

	private static Logger logger = Logger.getLogger(ExportTest.class);
	
	@Test
	public void testUnknwownArtifactDownload(){
		logger.info("testUnknwownArtifactDownload");
		try{
			final ArtifactoryAPIImpl artifactoryAPI = new ArtifactoryAPIImpl(new WrongPathParamBuilder());
			artifactoryAPI.delete();									
			doDownload(artifactoryAPI);
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
		final File artifactDownloaded = doDownload(artifactoryAPI);
		assertNotNull(artifactDownloaded);
		assertEquals(testArtifact.length(), artifactDownloaded.length());
		FileUtils.deleteQuietly(artifactDownloaded);
	}
	
	
	private File doDownload(final ArtifactoryAPIImpl pArtifactoryAPI){
		
		
		final File fileDestination = new File(settingsLoader.getSetting(SettingsKey.OUTPUT_JAR));
		
		if(fileDestination.exists())
			FileUtils.deleteQuietly(fileDestination);
		
		return pArtifactoryAPI.exportTo(fileDestination);	
		
	}	

}
