package org.codebeneath.lyrics.impacted;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class ImpactedServiceApplicationTest {

    @Disabled("The @SpringBootTest will find the @SpringBootApplication and load the full Spring context")
    @Test
    public void contextLoads() throws Exception {
        // sanity check for the context loading...
    }

}
