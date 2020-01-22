package org.codebeneath.lyrics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Ignore;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ApplicationTest {

    @Ignore("The @SpringBootTest will find the @SpringBootApplication and load the full Spring context")
    @Test
    public void contextLoads() throws Exception {
        // sanity check for the context loading...
    }

}