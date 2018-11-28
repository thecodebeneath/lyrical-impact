package org.codebeneath.lyrics.experiments;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SeedData {
    private static final Random rnd = new Random();
    private static final Lorem lorem = LoremIpsum.getInstance();

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public SeedData(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        this.impactedRepo = impactedRepo;
        this.verseRepo = verseRepo;
        this.tagRepo = tagRepo;
    }

    public void load() {
        Impacted jeff = impactedRepo.save(new Impacted("jeff@codebeneath.com", "Jeff", "Black"));
        log.info("======= Seed Jeff Id is: {}", jeff.getId());

        Tag happyTag = tagRepo.save(new Tag("happy", jeff));
        Tag sad2Tag = tagRepo.save(new Tag("sad", jeff));
        IntStream.range(0, 10).forEach(x -> tagRepo.save(new Tag("tag-" + x, jeff)));
        List<Tag> numbTags = new ArrayList<>();
        numbTags.add(happyTag);
        numbTags.add(sad2Tag);
        List<Tag> allTags = tagRepo.findByImpactedId(jeff.getId());

        Verse v1 = verseRepo.save(
                new Verse("There is no pain, you are receding\n" +
                        "A distant ship smoke on the horizon\n" +
                        "You are only coming through in waves\n" +
                        "Your lips move but I can't hear what you're saying",
                        "Comfortably Numb", "Pink Floyd", "This song makes me happy!", jeff, numbTags));

        for (int v = 0; v < 20; v++) {
            int tagId = rnd.nextInt(allTags.size() - 1);
            List<Tag> verseTags = allTags.subList(tagId, tagId + 1);
            verseRepo.save(
                    new Verse(createRandomVerse(), lorem.getWords(1, 3), lorem.getWords(1, 3),
                            "", jeff, verseTags));
        }
    }

    private String createRandomVerse() {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < rnd.nextInt(3)+1; n++) {
            for (int v = 0; v < rnd.nextInt(6)+2; v++) {
                sb.append(lorem.getWords(5, 10));
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
