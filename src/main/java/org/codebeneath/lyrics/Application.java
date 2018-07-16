package org.codebeneath.lyrics;

import java.util.HashSet;
import java.util.Set;
import org.codebeneath.lyrics.impacted.Impacted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;

@SpringBootApplication
/**
 * A webapp that allows people to remember the lyrical verses that have impacted them in some way. 
 */
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        return (args) -> {

            Set<Verse> verses = new HashSet<>();
            verses.add(new Verse("once I was young", "Busted", "Pink", ""));
            verses.add(new Verse("round and round", "Round and Round", "Ratt", "makes me dizzy!"));

            Impacted jeff = new Impacted("Jeff", "Black", verses);
            Impacted savedJeff = impactedRepo.save(jeff);
            impactedRepo.save(new Impacted("Chloe", "O'Brian"));
            impactedRepo.save(new Impacted("Kim", "Bauer", new HashSet<>()));
            impactedRepo.save(new Impacted("David", "Palmer", new HashSet<>()));

            Tag sadTag = tagRepo.save(new Tag("sad", savedJeff));
            Tag happyTag = tagRepo.save(new Tag("happy", savedJeff));
            Tag otherTag = tagRepo.save(new Tag("other", savedJeff));
            tagRepo.findAll().forEach(t -> {
                LOG.info("...tag...." + t.toString());
            });

            LOG.info("Impacted found with findAll():");
            LOG.info("-------------------------------");
            impactedRepo.findAll().forEach(i -> {
                LOG.info(i.toString());
                LOG.info("-verses------------------------------");
                i.getVerses().forEach(v -> {
                    LOG.info(v.toString());
                });
            });

            impactedRepo.findById(savedJeff.getId())
                    .ifPresent(i -> {
                        LOG.info("Impacted found with findById(1L):");
                        LOG.info("--------------------------------");
                        LOG.info(i.toString());
                        LOG.info("");

                        Set<Tag> tags = new HashSet<>();
                        tags.add(happyTag);

                        i.getVerses().add(new Verse("fucked up as they say", "Circles", "Metric", "yup..."));
//                        i.getVerses().add(new Verse("wild and free", "Shadows", "Metric", "", tags));
                        impactedRepo.save(i);

                        LOG.info("......tags for jeff... " + i.toString());
                        tagRepo.findByImpacted(i).forEach(t -> {
                            LOG.info(t.toString());
                        });
                    });

            Impacted jeffAgain = impactedRepo.findById(savedJeff.getId()).get();

            LOG.info("......tagging verses...");
            jeffAgain.getVerses().forEach(v -> {
                if (v.getArtist().equals("Metric")) {
                    v.getTags().add(sadTag);
                    v.getTags().add(happyTag);
                } else if (v.getArtist().equals("Pink")) {
                    v.getTags().add(otherTag);
                } else {
                    v.getTags().add(happyTag);
                }
                verseRepo.save(v);
            });

            LOG.info("All happy tagged verses");
            LOG.info("-------------------------------");
            verseRepo.findAllByTagsLabel("happy").forEach(v -> {
                LOG.info(v.toString());
            });
            
            LOG.info("Impacted found with findAll():");
            LOG.info("-------------------------------");
            impactedRepo.findAll().forEach(i -> {
                LOG.info(i.toString());
                LOG.info("-verses------------------------------");
                i.getVerses().forEach(v -> {
                    LOG.info(v.toString());
                });
            });

        };
    }
}
