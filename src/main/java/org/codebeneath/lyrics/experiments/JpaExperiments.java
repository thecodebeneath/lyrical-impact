package org.codebeneath.lyrics.experiments;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Order(value = 2)
@Component
public class JpaExperiments implements CommandLineRunner {
    
    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public JpaExperiments(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        this.impactedRepo = impactedRepo;
        this.verseRepo = verseRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public void run(String... args) throws Exception {        
//            Set<Verse> verses = new HashSet<>();
//            verses.add(new Verse("once I was young", "Busted", "Pink", ""));
//            verses.add(new Verse("round and round", "Round and Round", "Ratt", "makes me dizzy!"));
//
//            Impacted jeff = new Impacted("j@j.com", "Jeff", "Black", verses);
//            Impacted savedJeff = impactedRepo.save(jeff);
//            impactedRepo.save(new Impacted("c@c.com", "Chloe", "O'Brian"));
//            impactedRepo.save(new Impacted("k@k.com", "Kim", "Bauer", new HashSet<>()));
//            impactedRepo.save(new Impacted("d@d.com", "David", "Palmer", new HashSet<>()));
//
//            Tag sadTag = tagRepo.save(new Tag("sad", savedJeff));
//            Tag happyTag = tagRepo.save(new Tag("happy", savedJeff));
//            Tag otherTag = tagRepo.save(new Tag("other", savedJeff));
//            tagRepo.findAll().forEach(t -> {
//                log.info("...tag.... {}", t.toString());
//            });
//
//            log.info("Impacted found with findAll():");
//            log.info("-------------------------------");
//            impactedRepo.findAll().forEach(i -> {
//                log.info(i.toString());
//                log.info("-verses------------------------------");
//                i.getVerses().forEach(v -> {
//                    log.info(v.toString());
//                });
//            });
//
//            impactedRepo.findById(savedJeff.getId())
//                    .ifPresent(i -> {
//                        log.info("Impacted found with findById(1L):");
//                        log.info("--------------------------------");
//                        log.info(i.toString());
//                        log.info("");
//
//                        Set<Tag> tags = new HashSet<>();
//                        tags.add(happyTag);
//
//                        i.getVerses().addc);
////                        i.getVerses().add(new Verse("wild and free", "Shadows", "Metric", "", tags));
//                        impactedRepo.save(i);
//
//                        log.info("......tags for jeff... {}", i.toString());
//                        tagRepo.findByImpacted(i).forEach(t -> {
//                            log.info(t.toString());
//                        });
//                    });
//
//            Impacted jeffAgain = impactedRepo.findById(savedJeff.getId()).get();
//
//            log.info("......tagging verses...");
//            jeffAgain.getVerses().forEach(v -> {
//                if (v.getArtist().equals("Metric")) {
//                    v.getTags().add(sadTag);
//                    v.getTags().add(happyTag);
//                } else if (v.getArtist().equals("Pink")) {
//                    v.getTags().add(otherTag);
//                } else {
//                    v.getTags().add(happyTag);
//                }
//                verseRepo.save(v);
//            });
//
//            log.info("All happy tagged verses");
//            log.info("-------------------------------");
//            verseRepo.findAllByTagsLabel("happy").forEach(v -> {
//                log.info(v.toString());
//            });
//            
//            log.info("Impacted found with findAll():");
//            log.info("-------------------------------");
//            impactedRepo.findAll().forEach(i -> {
//                log.info(i.toString());
//                log.info("-verses------------------------------");
//                i.getVerses().forEach(v -> {
//                    log.info(v.toString());
//                });
//            });

        Tag sadTag = tagRepo.save(new Tag("jpasad"));
        Tag happyTag = tagRepo.save(new Tag("jpahappy"));

        Impacted i = impactedRepo.save(new Impacted("chloe", "c@c.com", "Chloe", "O'Brian"));
        Verse vi1 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i));
        vi1.getTags().add("sad");
        verseRepo.save(vi1);

        Impacted i2 = impactedRepo.save(new Impacted("tex", "z@z.com", "Tex", "O'Brian"));
        Verse v1 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));
        Verse v2 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));
        Verse v3 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));

        v1.getTags().add("sad");
        v1 = verseRepo.save(v1);

        v2.getTags().add("happy");
        v2.getTags().add("sad");
        v2 = verseRepo.save(v2);

        log.info("...Tex verses 3?... {}", verseRepo.findByImpactedId(i2.getId()).size());
        log.info("...Tex sads 2?... {}", verseRepo.findByImpactedIdAndTags(i2.getId(), "sad").size());
        log.info("...All sads 3?... {}", verseRepo.countByTags("sad"));
    }
}
