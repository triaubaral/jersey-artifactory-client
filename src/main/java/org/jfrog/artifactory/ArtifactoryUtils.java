package org.jfrog.artifactory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * Allow to use artifactory REST API
 * with jersey behind the scene.
 * @author Aurélien Tricoire
 *
 */
public final class ArtifactoryUtils {	
	
	private ArtifactoryUtils(){}
	
	static{	
		//Remove all jersey logging strategy with java.util.logging 
		//then it will be manage by log4j.
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
	
	private static Logger logger = Logger.getLogger(ArtifactoryUtils.class);
	
	/**
	 * Download a file from artifactory
	 * @param pPathToWebResource path to artifact to download
	 * @param pFileDestination path to store the downloaded artifact
	 * @return File downloaded
	 */
	public static File download(final String pPathToWebResource, final File pFileDestination){
		
		final Client client = Client.create();
		
		if(logger.isDebugEnabled()){
			client.addFilter(new LoggingFilter());
		}
		
		final WebResource webResource = client.resource(pPathToWebResource);
 
		final ClientResponse response = webResource.accept("application/zip").get(ClientResponse.class);
 
		final int status = response.getStatus();
		
		if (status != 200) {
		   throw new ArtifactoryUtilsException(status,"The download does not succeed. Please check if the url : "+pPathToWebResource+" is correct.");
		}
		
		try {
			FileUtils.writeByteArrayToFile(pFileDestination, IOUtils.toByteArray(response.getEntityInputStream()));
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
		
		return pFileDestination;
	}
	
	/**
	 * Upload a file in artifactory
	 * @param pFileToUpload file to upload in artifactory.
	 * @param pConfig ArtifactoryConfig that contains artifactory configuration.
	 * @return A jersey ClientResponse object
	 */
	public static ClientResponse upload(final File pFileToUpload, final ArtifactoryConfig pConfig){
		
		if(pConfig == null){
			throw new ArtifactoryUtilsException();
		}
			
		final Client client = Client.create();
		
		if(logger.isDebugEnabled())
			client.addFilter(new LoggingFilter());
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(pConfig.getUsername(), pConfig.getPassword()));
 	
		final WebResource webResource = client.resource(pConfig.getWebResourcePath());			
				
		final ClientResponse clientResponse =  webResource.put(ClientResponse.class, pFileToUpload);
		
		final int status = clientResponse.getStatus();
		
		if(status != 201){
			throw new ArtifactoryUtilsException(status,pConfig);
		}
		
		return clientResponse;
	}
	
	/**
	 * Delete a file in artifactory
	 * @param pWebResourcePath artifact's path to delete
	 * @param pConfig ArtifactoryConfig that contains artifactory configuration.
	 * @return A jersey ClientResponse object
	 */
	public static ClientResponse delete(final String pWebResourcePath, final ArtifactoryConfig pConfig){
			
		final Client client = Client.create();
		
		if(logger.isDebugEnabled())
			client.addFilter(new LoggingFilter());
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(pConfig.getUsername(), pConfig.getPassword()));			
					
		final WebResource webResource = client.resource(pWebResourcePath);			
					
		return webResource.delete(ClientResponse.class);
		
		
	}

}
