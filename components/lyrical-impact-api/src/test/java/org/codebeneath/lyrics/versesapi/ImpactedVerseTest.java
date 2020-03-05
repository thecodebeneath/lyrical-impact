package org.codebeneath.lyrics.versesapi;

import io.github.benas.randombeans.api.EnhancedRandom;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class ImpactedVerseTest {

    static final String problematicText = "aaa\r\nbbb\r\nccc";
    static final String convertedText = "aaa\nbbb\nccc";
    ImpactedVerse defaultVerse;
    ImpactedVerse populatedVerse;

    @Before
    public void setUp() {
        defaultVerse = new ImpactedVerse();
        populatedVerse = EnhancedRandom.random(ImpactedVerse.class);
    }

    @Test
    public void testVerseTextConvertedToConsistantLineFeeds() {
        assertThat(defaultVerse.getText()).isNull();
        assertThat(defaultVerse.getReaction()).isNull();

        defaultVerse.setText("none");
        defaultVerse.setReaction("none");
        assertThat(defaultVerse.getText()).isEqualTo("none");
        assertThat(defaultVerse.getReaction()).isEqualTo("none");

        defaultVerse.setText("aaa\nbbb");
        defaultVerse.setReaction("aaa\nbbb");
        assertThat(defaultVerse.getText()).isEqualTo("aaa\nbbb");
        assertThat(defaultVerse.getReaction()).isEqualTo("aaa\nbbb");

        defaultVerse.setText("aaa\nbbb");
        defaultVerse.setReaction("aaa\nbbb");
        assertThat(defaultVerse.getText()).isEqualTo("aaa\nbbb");
        assertThat(defaultVerse.getReaction()).isEqualTo("aaa\nbbb");

        defaultVerse.setText(problematicText);
        defaultVerse.setReaction(problematicText);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);
    }

    @Test
    public void testVerseConstructorTextConvertedToConsistantLineFeeds() {
        defaultVerse = new ImpactedVerse(problematicText, null, null, problematicText);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);

        defaultVerse = new ImpactedVerse(problematicText, null, null, problematicText, null);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);

        defaultVerse = new ImpactedVerse(problematicText, null, null, problematicText, null, null);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);
    }

    @Test
    public void testVerseCanBeAnonymized() {
        ImpactedVerse cleaned = populatedVerse.anonymizeVerse();
        assertThat(cleaned.getTitle()).isEqualTo(populatedVerse.getTitle());
        assertThat(cleaned.getAuthor()).isEqualTo(populatedVerse.getAuthor());
        assertThat(cleaned.getText()).isEqualTo(populatedVerse.getText());
        assertThat(cleaned.getId()).isNull();
        assertThat(cleaned.getReaction()).isNull();
        assertThat(cleaned.getTags()).isEmpty();
    }
}
