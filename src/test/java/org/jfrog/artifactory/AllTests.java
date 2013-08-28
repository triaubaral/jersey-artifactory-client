package org.jfrog.artifactory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
{ 	ImportTest.class, 
	ExportTest.class, 
	DeleteTest.class
})
public class AllTests {

}
