package org.codebeneath.lyrics.experiments;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.codebeneath.lyrics.versesapi.VersesClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(value = 1)
@Component
public class SeedData implements CommandLineRunner {

    private static final int USERS_TO_SEED = 5;
    private static final int MAX_VERSES_PER_USER = 15;
    private static final int MAX_TAGS_PER_VERSE = 3;

    private static final Random RND = new Random();
    private static final Lorem LOREM = LoremIpsum.getInstance();

    private final ImpactedClient impactedClient;
    private final VersesClient versesClient;
    private final TagsClient tagsClient;
    
    public SeedData(ImpactedClient impactedClient, VersesClient versesClient, TagsClient tagsClient) {
        this.impactedClient = impactedClient;
        this.versesClient = versesClient;
        this.tagsClient = tagsClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // for real
        //createImpactedVersesForJeff();
        // for testing
        //createImpactedVersesForRandomUsers();
    }

    private void createImpactedVersesForRandomUsers() {
        List<VerseTag> tags = tagsClient.getVerseTags();
        IntStream.rangeClosed(1, USERS_TO_SEED).forEach(u -> {
            long randomUserId = RND.nextInt(USERS_TO_SEED + 1) + 2;
            ImpactedUser randomUser = impactedClient.findById(randomUserId).get();
            int randomNumberOfVerses = RND.nextInt(MAX_VERSES_PER_USER) + 1;
            createImpactedVersesFor(randomUser, tags, randomNumberOfVerses);
        });        
    }

    private void createImpactedVersesFor(ImpactedUser user, List<VerseTag> allTags, int randomNumberOfVerses) {
        IntStream.rangeClosed(1, randomNumberOfVerses+1).forEach(v -> {
            List<String> randomTags = new ArrayList<>();
            if (RND.nextBoolean()) {
                IntStream.range(0, RND.nextInt(MAX_TAGS_PER_VERSE+1)).forEach(t -> {
                    int tagId = RND.nextInt(allTags.size() - 1);
                    randomTags.add(allTags.get(tagId).getLabel());
                });
            }
            versesClient.save(
                    new ImpactedVerse(createRandomVerse(),
                            LOREM.getWords(1, 3), LOREM.getWords(1, 3),
                            LOREM.getWords(1, 5), user.getId(), randomTags));
        });
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

    private void createImpactedVersesForJeff() {
        ImpactedUser jeff = impactedClient.findByRolesContains(ImpactedUser.ROLE_ADMIN).get(0);

        versesClient.save(
                new ImpactedVerse("<b>ve-oops</b><script>alert('versetext');</script>",
                        "<b>so-oops</b><script>alert('versetitle');</script>", "<b>ar-oops</b><script>alert('verseauthor');</script>",
                        "<b>re-oops</b><script>alert('versereaction');</script>", jeff.getId(), List.of("confusion", "horror")));
        versesClient.save(
                new ImpactedVerse("由 匿名 (未验证) 提交于\nThe façade pattern's a software-design \"£\" pattern.\n提交于",
                        "i18n 由", "i18n 由",
                        "由 匿名 (未验证) 提交于\n"
                        + "The façade pattern's a software-design \"£\" pattern &amp; <b>FUN FUN FUN</b>.\n提交于", jeff.getId(), List.of("joy", "sexy", "funny")));
        versesClient.save(
                new ImpactedVerse("There is no pain, you are receding\n"
                        + "A distant ship smoke on the horizon\n"
                        + "You are only coming through in waves\n"
                        + "Your lips move but I can't hear what you're saying",
                        "Comfortably Numb", "Pink Floyd",
                        "Makes\n"
                        + "me\n"
                        + "happy!", jeff.getId(), List.of("calmness", "joy")));

        // more verses to test scrolling
        // createImpactedVersesFor(jeff, (List<Tag>) tagRepo.findAll(), 100);
    }
}
