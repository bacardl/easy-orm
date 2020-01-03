package com.softserve.easy.meta;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses( { DirectedGraphTest.class, DependencyGraphTest.class } )
public class MetaSuiteTests {

}
