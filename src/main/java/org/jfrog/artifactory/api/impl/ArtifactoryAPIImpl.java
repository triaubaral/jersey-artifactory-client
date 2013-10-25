package org.jfrog.artifactory.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jfrog.artifactory.ArtifactoryUtilsException;
import org.jfrog.artifactory.api.ArtifactoryAPI;
import org.jfrog.artifactory.api.StatusCode;
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
public final class ArtifactoryAPIImpl implements ArtifactoryAPI<File> {	
	
	private Parameter params;

	@SuppressWarnings("unused")
	private ArtifactoryAPIImpl(){}
	
	public ArtifactoryAPIImpl(final ParameterBuilder pParameterBuilder){
		pParameterBuilder.createNewParameterProduct();
		pParameterBuilder.buildHomeUrl();
		pParameterBuilder.buildRepository();
		pParameterBuilder.buildPath();
		pParameterBuilder.buildUsername();
		pParameterBuilder.buildPassword();	
		pParameterBuilder.buildHeaders();
		this.params = pParameterBuilder.getParameter();
	}
		
	static{
		
		//Remove all jersey logging strategy with java.util.logging 
		//then it will be manage by log4j.
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
	
	private static Logger logger = Logger.getLogger(ArtifactoryAPIImpl.class);
	
	/**
	 * Download a file from artifactory
	 * @param pFileDestination path to store the downloaded artifact
	 * @return File downloaded
	 */
	public File exportTo(final File pFileDestination){
		
		final Client client = initLogIfDebugEnabled();		
		
		final WebResource webResource = client.resource(params.getWebResourcePath());
 
		final ClientResponse response = webResource.accept("application/zip").get(ClientResponse.class);
 
		final int status = response.getStatus();
		
		if (status != StatusCode.OK.getCode()) {
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
		
		final Client client = initLogIfDebugEnabled();		
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(this.params.getUsername(), this.params.getPassword()));
 	
		final WebResource webResource = client.resource(this.params.getWebResourcePath());
		
		for(Entry<String,String> header : params.getHeaders().entrySet()){
			webResource.header(header.getKey().trim(), header.getValue().trim());			
		}
				
		final ClientResponse clientResponse =  webResource.put(ClientResponse.class, pFileToUpload);
		
		final int status = clientResponse.getStatus();
		
		if(status != StatusCode.CREATED.getCode()){
			throw new ArtifactoryUtilsException(status,this.params);
		}		
	}
	
	
	/**
	 * Delete a file in artifactory
	 * @return A jersey ClientResponse object
	 */
	public void delete(){
		
		final Client client = initLogIfDebugEnabled();
		
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(this.params.getUsername(), this.params.getPassword()));			
					
		final WebResource webResource = client.resource(this.params.getWebResourcePath());			
					
		final ClientResponse clientResponse =webResource.delete(ClientResponse.class);
		
		final int status = clientResponse.getStatus();
		
		if(status != StatusCode.NO_CONTENT.getCode()){
			throw new ArtifactoryUtilsException(status,this.params);
		}
		
	}
	
	public Parameter getParams() {
		return params;
	}
	
	private Client initLogIfDebugEnabled(){
		final Client client = Client.create();
		if(logger.isDebugEnabled()){
			client.addFilter(new LoggingFilter());
		}
		
		return client;
	}

}
