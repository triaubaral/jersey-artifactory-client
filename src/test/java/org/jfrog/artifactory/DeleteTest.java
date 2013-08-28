package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.jfrog.artifactory.param.SettingsKey;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class DeleteTest extends ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(DeleteTest.class);
	
	@Test
	public void testRegularDelete(){
		logger.info("testRegularDelete");		
		final String workingUrl = settingsLoader.getSetting(SettingsKey.ARTIFACTORY_URL)+"/"+settingsLoader.getSetting(SettingsKey.REPOSITORY)+"/";
		logger.debug("WorkingUrl ="+workingUrl);
		ClientResponse response1 = artifactoryAPI.delete(workingUrl+settingsLoader.getSetting(SettingsKey.RESOURCE_TO_DELETE));
		ClientResponse response2 = artifactoryAPI.delete(workingUrl+settingsLoader.getSetting(SettingsKey.WRONG_PATH));
		assertEquals(204,response1.getStatus());
		assertEquals(204,response2.getStatus());
		
	}

}
