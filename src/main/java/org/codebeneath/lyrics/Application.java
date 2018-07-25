package org.codebeneath.lyrics;

import org.codebeneath.lyrics.experiments.JpaExperiments;
import org.codebeneath.lyrics.experiments.RestTemplateExperiments;
import org.codebeneath.lyrics.experiments.SeedData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * A webapp that allows people to remember the lyrical verses that have impacted them in some way.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner experiments(SeedData seedData, JpaExperiments jpaEx, RestTemplateExperiments restEx) {
        return (args) -> {
            seedData.load();
//            jpaEx.experiment();
            restEx.experiment();
        };
    }

}
