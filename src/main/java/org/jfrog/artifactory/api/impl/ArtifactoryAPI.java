package org.jfrog.artifactory.api.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.ArtifactoryUtilsException;
import org.jfrog.artifactory.param.Parameter;
import org.jfrog.artifactory.param.ParameterBuilder;
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
public final class ArtifactoryAPI {	
	
	private Parameter params;
	
	public Parameter getParams() {
		return params;
	}

	@SuppressWarnings("unused")
	private ArtifactoryAPI(){}
	
	public ArtifactoryAPI(final ParameterBuilder pParameterBuilder){
		pParameterBuilder.createNewParameterProduct();
		pParameterBuilder.buildHomeUrl();
		pParameterBuilder.buildRepository();
		pParameterBuilder.buildPath();
		pParameterBuilder.buildUsername();
		pParameterBuilder.buildPassword();		
		this.params = pParameterBuilder.getParameter();
	}
	
	
	private static Client client;
	
	static{
		client = Client.create();
		//Remove all jersey logging strategy with java.util.logging 
		//then it will be manage by log4j.
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
	
	private static Logger logger = Logger.getLogger(ArtifactoryAPI.class);
	
	/**
	 * Download a file from artifactory
	 * @param pFileDestination path to store the downloaded artifact
	 * @return File downloaded
	 */
	public File exportTo(final File pFileDestination){
		
		if(logger.isDebugEnabled()){
			client.addFilter(new LoggingFilter());
		}
		
		final WebResource webResource = client.resource(params.getWebResourcePath());
 
		final ClientResponse response = webResource.accept("application/zip").get(ClientResponse.class);
 
		final int status = response.getStatus();
		
		if (status != 200) {
		   throw new ArtifactoryUtilsException(status,"The download does not succeed. Please check if the url : "+this.params.getPath()+" is correct.");
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
	 * @return A jersey ClientResponse object
	 */
	public void importFrom(final File pFileToUpload){
		
		if(logger.isDebugEnabled()){
			client.addFilter(new LoggingFilter());
		}
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(this.params.getUsername(), this.params.getPassword()));
 	
		final WebResource webResource = client.resource(this.params.getWebResourcePath());			
				
		final ClientResponse clientResponse =  webResource.put(ClientResponse.class, pFileToUpload);
		
		final int status = clientResponse.getStatus();
		
		if(status != 201){
			throw new ArtifactoryUtilsException(status,this.params);
		}		
	}

	public void importFromWithChecksum(final File pFileToUpload){
		
		if(logger.isDebugEnabled()){
			client.addFilter(new LoggingFilter());
		}
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(this.params.getUsername(), this.params.getPassword()));
 	
		//client.addFilter(new com.sun.jersey.api.client.filter);
		
		final WebResource webResource = client.resource(this.params.getWebResourcePath());			
				
		final ClientResponse clientResponse =  webResource.put(ClientResponse.class, pFileToUpload);
		
		final int status = clientResponse.getStatus();
		
		if(status != 201){
			throw new ArtifactoryUtilsException(status,this.params);
		}		
	}
	
	
	/**
	 * Delete a file in artifactory
	 * @param pWebResourcePath artifact's path to delete
	 * @return A jersey ClientResponse object
	 */
	public void delete(final String pWebResourcePath){
		
		if(logger.isDebugEnabled())
			client.addFilter(new LoggingFilter());
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(this.params.getUsername(), this.params.getPassword()));			
					
		final WebResource webResource = client.resource(pWebResourcePath);			
					
		final ClientResponse clientResponse =webResource.delete(ClientResponse.class);
		
		final int status = clientResponse.getStatus();
		
		if(status != 204){
			throw new ArtifactoryUtilsException(status,this.params);
		}
		
	}

}
