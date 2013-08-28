package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.jfrog.artifactory.param.SettingsKey;
import org.junit.Test;

public class DeleteTest extends ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(DeleteTest.class);
	
	@Test
	public void testRegularDelete(){		
		try{
			
			logger.info("testRegularDelete");		
			final String workingUrl = settingsLoader.getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+settingsLoader.getSetting(SettingsKey.REPOSITORY)+"/";
			logger.debug("WorkingUrl ="+workingUrl);
			artifactoryAPI.delete(workingUrl+settingsLoader.getSetting(SettingsKey.RESOURCE_TO_DELETE));			
		}
		catch(Exception e){
			fail("Exception for regular download has been thrown.");			
		}		
	}
	
	@Test
	public void testWrongArtifactDelete(){		
		try{
			
			logger.info("testWrongArtifactDelete");		
			final String workingUrl = settingsLoader.getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+settingsLoader.getSetting(SettingsKey.REPOSITORY)+"/";
			logger.debug("WorkingUrl ="+workingUrl);
			artifactoryAPI.delete(workingUrl+settingsLoader.getSetting(SettingsKey.RESOURCE_TO_DELETE)+"iioi");
			fail("Artifact has not been deleted");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(404,e.getStatus());			
		}		
	}
	

}
