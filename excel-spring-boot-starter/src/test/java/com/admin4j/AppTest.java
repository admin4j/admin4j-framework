package com.admin4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws UnsupportedEncodingException {

        String encode = URLEncoder.encode("测+试", "UTF-8");
        System.out.println("encode = " + encode);
        String s = encode.replaceAll("\\+", "%20");
        System.out.println("s = " + s);
    }
}
