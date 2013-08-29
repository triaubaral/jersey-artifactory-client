package org.jfrog.artifactory.param;

public abstract class ParameterBuilder {

	protected Parameter parameter;
	
	public Parameter getParameter(){		
		return parameter;
	}
	
	public void createNewParameterProduct(){
		parameter = new Parameter();
	}
	
	public abstract void buildHomeUrl();
	public abstract void buildPath();
	public abstract void buildRepository();
	public abstract void buildUsername();
	public abstract void buildPassword();
	public abstract void buildHeaders();

}
