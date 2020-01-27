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
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(value = 1)
@Component
public class SeedData implements CommandLineRunner {

    private static final Random RND = new Random();
    private static final Lorem LOREM = LoremIpsum.getInstance();

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public SeedData(ImpactedRepository impactedRepo, VerseRepository verseRepo, TagRepository tagRepo) {
        this.impactedRepo = impactedRepo;
        this.verseRepo = verseRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        Tag happyTag = tagRepo.findByLabel("happy").get();
        Tag sadTag = tagRepo.findByLabel("sad").get();
        // IntStream.range(0, 10).forEach(x -> tagRepo.save(new Tag("tag-" + x)));

        Impacted jeff = impactedRepo.findByUserName("jeff").get();
        Impacted alan = impactedRepo.findByUserName("alan").get();
        Impacted sue = impactedRepo.findByUserName("sue").get();
        Impacted dan = impactedRepo.findByUserName("dan").get();

        List<String> numbTags = new ArrayList<>();
        numbTags.add(happyTag.getLabel());
        numbTags.add(sadTag.getLabel());

        Verse multiline = verseRepo.save(
                new Verse("There is no pain, you are receding\n"
                        + "A distant ship smoke on the horizon\n"
                        + "You are only coming through in waves\n"
                        + "Your lips move but I can't hear what you're saying",
                        "Comfortably Numb", "Pink Floyd",
                        "makes\n"
                        + "me\n"
                        + "happy!", jeff, numbTags));
        Verse scripts = verseRepo.save(
                new Verse("<b>ve-oops</b><script>alert('ve');</script>",
                        "<b>so-oops</b><script>alert('so');</script>", "<b>ar-oops</b><script>alert('ar');</script>",
                        "<b>re-oops</b><script>alert('re');</script>", jeff, numbTags));
        Verse i18n = verseRepo.save(
                new Verse("由 匿名 (未验证) 提交于\nThe façade pattern's a software-design \"£\" pattern.\n提交于",
                        "i18n 由", "i18n 由",
                        "由 匿名 (未验证) 提交于\n"
                        + "The façade pattern's a software-design \"£\" pattern.\n提交于", jeff, numbTags));

        List<Tag> allTags = (List<Tag>) tagRepo.findAll();
        createImpactedVersesFor(alan, allTags, 15);
        createImpactedVersesFor(sue, allTags, 3);
        createImpactedVersesFor(dan, allTags, 5);
    }

    private void createImpactedVersesFor(Impacted user, List<Tag> allTags, int count) {
        for (int v = 0; v < count; v++) {
            int tagId = RND.nextInt(allTags.size() - 1);
            List<String> verseTags = new ArrayList<>();
            verseTags.add(allTags.get(tagId).getLabel());
            verseRepo.save(
                    new Verse(createRandomVerse(),
                            LOREM.getWords(1, 3), LOREM.getWords(1, 3),
                            LOREM.getWords(1, 5), user, verseTags));
        }
    }

    private String createRandomVerse() {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < RND.nextInt(3) + 1; n++) {
            for (int v = 0; v < RND.nextInt(6) + 2; v++) {
                sb.append(LOREM.getWords(5, 10));
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
