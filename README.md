#jersey-artifactory-client

Simple jersey client for artifactory

You can only use it to download (exportTo), update(importFrom) or delete an artifact.

Here a simple example in order to show you how it works

First you need to create an object that extends ParameterBuilder object like this :

	public class DefautParameterBuilder extends ParameterBuilder {

	@Override
	public void buildHomeUrl() {
		parameter.setHomeUrl("http://localhost:8081/artifactory");
		
	}

	@Override
	public void buildPath() {
		parameter.setPath("path/to/artifact");		
	}

	@Override
	public void buildRepository() {
		parameter.setRepository("libs-release-local");		
	}

	@Override
	public void buildUsername() {
		parameter.setUsername("admin");		
	}

	@Override
	public void buildPassword() {
		parameter.setPassword("password");
	}

	}


Then you need to set an ArtifactoryAPI object :

	ArtifactoryAPI artifactoryAPI = new ArtifactoryAPI(new DefautParameterBuilder());		
	
	
Then you can use the ArtifactoryAPI to :

	//dowload :
	artifactoryAPI.exportTo(new File("path/to/downloaded/file"));
	
	
	//upload :
	artifactoryAPI.importFrom(new File("path/to/file/to/upload"));
	
	
	//delete resource defined in buildPath from DefautParameterBuilder:
	artifactoryAPI.delete();
* * *
*Have a look to the unit test to get more example.*

