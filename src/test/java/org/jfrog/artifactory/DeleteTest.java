package org.jfrog.artifactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.jfrog.artifactory.api.impl.ArtifactoryAPIImpl;
import org.jfrog.artifactory.param.ResourceToDelParameterBuilder;
import org.jfrog.artifactory.param.WrongResourceToDelParameterBuilder;
import org.junit.Test;

public class DeleteTest extends ArtifactoryAPITest {
	
	private static Logger logger = Logger.getLogger(DeleteTest.class);
	
	@Test
	public void testRegularDelete(){		
		try{
			
			logger.info("testRegularDelete");		
			doDelete(new ArtifactoryAPIImpl(new ResourceToDelParameterBuilder()));			
		}
		catch(Exception e){
			fail("Exception for regular download has been thrown.");			
		}		
	}
	
	@Test
	public void testWrongArtifactDelete(){		
		try{
			
			logger.info("testWrongArtifactDelete");		
			doDelete(new ArtifactoryAPIImpl(new WrongResourceToDelParameterBuilder()));
			fail("Artifact has not been deleted");
		}
		catch(ArtifactoryUtilsException e){
			assertEquals(404,e.getStatus());			
		}		
	}
	
	private void doDelete(final ArtifactoryAPIImpl artifactoryAPI){
		
		artifactoryAPI.delete();
	}
	

}
