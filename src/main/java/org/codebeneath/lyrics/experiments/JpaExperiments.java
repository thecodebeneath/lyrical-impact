package org.codebeneath.lyrics.experiments;

import org.codebeneath.lyrics.Application;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class JpaExperiments {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    
    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public JpaExperiments(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        this.impactedRepo = impactedRepo;
        this.verseRepo = verseRepo;
        this.tagRepo = tagRepo;
    }

    public void experiment() {
        
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
//                LOG.info("...tag...." + t.toString());
//            });
//
//            LOG.info("Impacted found with findAll():");
//            LOG.info("-------------------------------");
//            impactedRepo.findAll().forEach(i -> {
//                LOG.info(i.toString());
//                LOG.info("-verses------------------------------");
//                i.getVerses().forEach(v -> {
//                    LOG.info(v.toString());
//                });
//            });
//
//            impactedRepo.findById(savedJeff.getId())
//                    .ifPresent(i -> {
//                        LOG.info("Impacted found with findById(1L):");
//                        LOG.info("--------------------------------");
//                        LOG.info(i.toString());
//                        LOG.info("");
//
//                        Set<Tag> tags = new HashSet<>();
//                        tags.add(happyTag);
//
//                        i.getVerses().addc);
////                        i.getVerses().add(new Verse("wild and free", "Shadows", "Metric", "", tags));
//                        impactedRepo.save(i);
//
//                        LOG.info("......tags for jeff... " + i.toString());
//                        tagRepo.findByImpacted(i).forEach(t -> {
//                            LOG.info(t.toString());
//                        });
//                    });
//
//            Impacted jeffAgain = impactedRepo.findById(savedJeff.getId()).get();
//
//            LOG.info("......tagging verses...");
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
//            LOG.info("All happy tagged verses");
//            LOG.info("-------------------------------");
//            verseRepo.findAllByTagsLabel("happy").forEach(v -> {
//                LOG.info(v.toString());
//            });
//            
//            LOG.info("Impacted found with findAll():");
//            LOG.info("-------------------------------");
//            impactedRepo.findAll().forEach(i -> {
//                LOG.info(i.toString());
//                LOG.info("-verses------------------------------");
//                i.getVerses().forEach(v -> {
//                    LOG.info(v.toString());
//                });
//            });

        Impacted i = impactedRepo.save(new Impacted("c@c.com", "Chloe", "O'Brian"));
        Tag sadTag = tagRepo.save(new Tag("sad", i));
        Verse vi1 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i));
        vi1.getTags().add(sadTag);
        verseRepo.save(vi1);

        Impacted i2 = impactedRepo.save(new Impacted("z@z.com", "Tex", "O'Brian"));
        Tag happyTag = tagRepo.save(new Tag("happy", i2));
        Tag sad2Tag = tagRepo.save(new Tag("sad", i2));
        Verse v1 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));
        Verse v2 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));
        Verse v3 = verseRepo.save(new Verse("wild and free", "Shadows", "Metric", "makes me happy", i2));

        v1.getTags().add(sad2Tag);
        v1 = verseRepo.save(v1);

        v2.getTags().add(happyTag);
        v2.getTags().add(sad2Tag);
        v2 = verseRepo.save(v2);

        LOG.info("...Tex verses 3?..." + verseRepo.findByImpactedId(i2.getId()).size());
        LOG.info("...Tex sads 2?..." + verseRepo.findByImpactedIdAndTagsLabel(i2.getId(), "sad").size());
        LOG.info("...All sads 3?..." + verseRepo.countByTagsLabel("sad"));
    }
}
