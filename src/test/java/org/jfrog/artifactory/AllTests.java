package org.jfrog.artifactory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
{ 	UploadTest.class, 
	DownloadTest.class, 
	DeleteTest.class
})
public class AllTests {

}
