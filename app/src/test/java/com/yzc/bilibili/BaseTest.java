package com.yzc.bilibili;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BaseTest extends TestCase {

    @Rule
    public BaseTestRule test = new BaseTestRule();

    @Before
    public void setUp() throws Exception {
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void main() {
        System.out.println("dfasdf");
    }
}
