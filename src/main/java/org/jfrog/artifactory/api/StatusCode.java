package org.jfrog.artifactory.api;

/**
 * Keeps track of a subset of status code defined by the w3c.
 * To get to the complete list go to :
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
 *
 */
public enum StatusCode {
	
	 OK(200),
	 CREATED(201),
	 NO_CONTENT(204);
	 
	 private int code;

	 StatusCode(final int pCode ){
		 this.code = pCode;
	 }
	 
	 public int getCode(){
		 return this.code;
	 }
}
