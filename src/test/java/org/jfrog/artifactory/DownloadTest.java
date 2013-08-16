package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class DownloadTest extends ArtifactoryUtilsTest {

	private static Logger logger = Logger.getLogger(DownloadTest.class);
	
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
	
	
	private File doDownload(final String pWebResoucePath){
		final String pathWebResouce = pWebResoucePath;
		
		final File fileDestination = new File(getSetting(SettingsKey.OUTPUT_JAR));
		
		if(fileDestination.exists())
			FileUtils.deleteQuietly(fileDestination);
		
		return ArtifactoryUtils.download(pathWebResouce, fileDestination);	
		
	}
	
	private String getWebResourcePath(){
		final String webResourcePath =  getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+getSetting(SettingsKey.REPOSITORY)+"/"+getSetting(SettingsKey.PATH);
		logger.debug("Get WebResourcePath :"+webResourcePath);
		return webResourcePath;
	}

}
