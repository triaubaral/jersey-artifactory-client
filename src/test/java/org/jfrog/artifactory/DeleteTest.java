package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class DeleteTest extends ArtifactoryUtilsTest {
	
	private static Logger logger = Logger.getLogger(DeleteTest.class);
	
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

}
