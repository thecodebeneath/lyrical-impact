package org.codebeneath.lyrics.experiments;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class SeedData {
    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    private static final Random rnd = new Random();

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public SeedData(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        this.impactedRepo = impactedRepo;
        this.verseRepo = verseRepo;
        this.tagRepo = tagRepo;
    }

    public void load() {
        Lorem lorem = LoremIpsum.getInstance();
        Impacted jeff = impactedRepo.save(new Impacted("jeff@codebeneath.com", "Jeff", "Black"));
        LOG.info("======= Seed Jeff Id is: " + jeff.getId());

        Tag happyTag = tagRepo.save(new Tag("happy", jeff));
        Tag sad2Tag = tagRepo.save(new Tag("sad", jeff));
        IntStream.range(0, 10).forEach(x -> tagRepo.save(new Tag("tag-" + x, jeff)));
        List<Tag> numbTags = new ArrayList<>();
        numbTags.add(happyTag);
        numbTags.add(sad2Tag);
        List<Tag> allTags = tagRepo.findByImpactedId(jeff.getId());

        Verse multiline = verseRepo.save(
                new Verse("There is no pain, you are receding\n" +
                        "A distant ship smoke on the horizon\n" +
                        "You are only coming through in waves\n" +
                        "Your lips move but I can't hear what you're saying",
                        "Comfortably Numb", "Pink Floyd",
                        "makes\n" +
                        "me\n" +
                        "happy!", jeff, numbTags));
        Verse scripts = verseRepo.save(
                new Verse("<b>oops</b><script>alert('ve');<script>",
                        "<b>oops</b><script>alert('so');<script>", "<b>oops</b><script>alert('ar');<script>",
                        "<b>oops</b><script>alert('re');<script>", jeff, numbTags));
        Verse i18n = verseRepo.save(
                new Verse("由 匿名 (未验证) 提交于 \n The façade pattern's a software-design \"£\" pattern. \n 提交于",
                        "i18n 由", "i18n 由",
                        "由 匿名 (未验证) 提交于 \n" +
                        "The façade pattern's a software-design \"£\" pattern. \n 提交于", jeff, numbTags));
        
        for (int v = 0; v < 3; v++) {
            int tagId = rnd.nextInt(allTags.size() - 1);
            List<Tag> verseTags = allTags.subList(tagId, tagId + 1);
            verseRepo.save(
                    new Verse(lorem.getWords(1, 8) + "\n" + lorem.getWords(1, 4) + "\n" + lorem.getWords(1, 5) + "\n" + lorem.getWords(1, 6),
                            lorem.getWords(1, 3), lorem.getWords(1, 3),
                            lorem.getWords(1, 5), jeff, verseTags));
        }

    }
}