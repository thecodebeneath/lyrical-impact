package org.codebeneath.lyrics.versesapi.seed;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.impactedapi.ImpactedUserDto;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author black
 */
@Component
public class VersesFixtures {

    private static final int USERS_TO_SEED = 5;
    private static final int FIRST_SEED_USER_ID = 5;
    private static final int MAX_VERSES_PER_USER = 15;
    private static final int MAX_TAGS_PER_VERSE = 3;

    private static final Random RND = new Random();
    private static final Lorem LOREM = LoremIpsum.getInstance();
    private final ModelMapper modelMapper = new ModelMapper();

    private final ImpactedClient impactedClient;
    private final VersesService versesService;
    private final VerseTagsService tagsService;

    public VersesFixtures(ImpactedClient impactedClient, VersesService versesService, VerseTagsService tagsService) {
        this.impactedClient = impactedClient;
        this.versesService = versesService;
        this.tagsService = tagsService;
    }

    public void loadMy(ImpactedUser impactedUser) {
        createImpactedVersesForLoggedInUser(impactedUser);
    }

    public void loadRandom() {
        createFixtureUsers();
        createImpactedVersesForFixtureUsers();
    }

    private void createImpactedVersesForFixtureUsers() {
        List<VerseTag> tags = tagsService.getVerseTags();
        IntStream.rangeClosed(1, USERS_TO_SEED).forEach(u -> {
            long randomUserId = RND.nextInt(USERS_TO_SEED + 1) + FIRST_SEED_USER_ID;
            ImpactedUser randomUser = impactedClient.findById(randomUserId).get();
            int randomNumberOfVerses = RND.nextInt(MAX_VERSES_PER_USER) + 1;
            createImpactedVersesFor(randomUser, tags, randomNumberOfVerses);
        });
    }

    private void createImpactedVersesFor(ImpactedUser user, List<VerseTag> allTags, int randomNumberOfVerses) {
        IntStream.rangeClosed(1, randomNumberOfVerses + 1).forEach(v -> {
            List<String> randomTags = new ArrayList<>();
            if (RND.nextBoolean()) {
                IntStream.range(0, RND.nextInt(MAX_TAGS_PER_VERSE + 1)).forEach(t -> {
                    int tagId = RND.nextInt(allTags.size() - 1);
                    randomTags.add(allTags.get(tagId).getLabel());
                });
            }
            versesService.create(
                    new ImpactedVerse(createRandomVerse(),
                            LOREM.getWords(1, 3), LOREM.getWords(1, 3),
                            LOREM.getWords(1, 5), user.getId(), randomTags), false);
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

    private void createFixtureUsers() {
        ImpactedUser fUser = new ImpactedUser("alan", "fixture", "由匿名未验证 提交于");
        impactedClient.save(mapToDto(fUser));
        fUser = new ImpactedUser("alan", "fixture", "Alan Smithe");
        impactedClient.save(mapToDto(fUser));
        fUser = new ImpactedUser("sue", "fixture", "Sue Z.");
        impactedClient.save(mapToDto(fUser));
        fUser = new ImpactedUser("jenn", "fixture", "Jennifer Doe");
        impactedClient.save(mapToDto(fUser));
        fUser = new ImpactedUser("chloe", "fixture", "Chloe O'Brian");
        impactedClient.save(mapToDto(fUser));
    }
    
    private ImpactedUserDto mapToDto(ImpactedUser impactedUser) {
        return modelMapper.map(impactedUser, ImpactedUserDto.class);
    }
    
    private void createImpactedVersesForLoggedInUser(ImpactedUser impactedUser) {
        versesService.create(
                new ImpactedVerse("<b>ve-oops</b><script>alert('versetext');</script>",
                        "<b>so-oops</b><script>alert('versetitle');</script>", "<b>ar-oops</b><script>alert('verseauthor');</script>",
                        "<b>re-oops</b><script>alert('versereaction');</script>", impactedUser.getId(), List.of("confusion", "horror")), false);
        versesService.create(
                new ImpactedVerse("由 匿名 (未验证) 提交于\nThe façade pattern's a software-design \"£\" pattern.\n提交于",
                        "i18n 由", "i18n 由",
                        "由 匿名 (未验证) 提交于\n"
                        + "The façade pattern's a software-design \"£\" pattern &amp; <b>FUN FUN FUN</b>.\n提交于", impactedUser.getId(), List.of("confusion", "funny")), false);
        versesService.create(
                new ImpactedVerse("There is no pain, you are receding\n"
                        + "A distant ship smoke on the horizon\n"
                        + "You are only coming through in waves\n"
                        + "Your lips move but I can't hear what you're saying",
                        "Comfortably Numb", "Pink Floyd",
                        "Makes\n"
                        + "me\n"
                        + "happy!", impactedUser.getId(), List.of("calmness", "sadness")), false);
        versesService.create(
                new ImpactedVerse("Wide awake in bed, words in my brain,\n"
                        + "\"Secretly you love this, do you even wanna go free?\"\n"
                        + "Let me in the ring, I'll show you what that big word means",
                        "Glory and Gore", "Lorde", "", impactedUser.getId(), List.of("triumph", "sexy")), false);
        versesService.create(
                new ImpactedVerse("The best lack all conviction, while the worst\n"
                        + "Are full of passionate intensity.",
                        "The Second Coming", "W. B. Yeats", "", impactedUser.getId(), List.of("pain")), false);
        versesService.create(
                new ImpactedVerse("If you can trust yourself when all men doubt you,\n"
                        + "But make allowance for their doubting too;\n"
                        + "If you can wait and not be tired by waiting,\n"
                        + "Or being lied about, don’t deal in lies,\n"
                        + "Or being hated, don’t give way to hating",
                        "If", "Rudyard Kipling", "", impactedUser.getId(), List.of("triumph")), false);

        // more verses to test scrolling
        // createImpactedVersesFor(jeff, (List<Tag>) tagRepo.findAll(), 100);
    }

}
