package org.codebeneath.lyrics.verses;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    static final String problematicText = "aaa\r\nbbb\r\nccc";
    static final String convertedText = "aaa\nbbb\nccc";
    static final Lorem lorem = LoremIpsum.getInstance();
    Validator validator;
    Verse defaultVerse;
    Verse populatedVerse;
    
    @Before
    public void setUp() {
        defaultVerse = new Verse();
        populatedVerse = EnhancedRandom.random(Verse.class);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void testInvalidTextShouldFailValidation() {
        Verse emptyVerse = new Verse();
        Set<ConstraintViolation<Verse>> violations = validator.validate(emptyVerse);
        assertThat(violations)
                .isNotEmpty()
                .hasSize(1);
        ConstraintViolation<Verse> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("must not be null");
        assertThat(violation.getInvalidValue()).isNull();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");   

        emptyVerse.setText("");
        violations = validator.validate(emptyVerse);
        violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("size must be between 1 and 1000");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");   

        emptyVerse.setText(lorem.getWords(1000));
        violations = validator.validate(emptyVerse);
        violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("size must be between 1 and 1000");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");   
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
        defaultVerse = new Verse(problematicText, null, null, problematicText);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);

        defaultVerse = new Verse(problematicText, null, null, problematicText, null);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);

        defaultVerse = new Verse(problematicText, null, null, problematicText, null, null);
        assertThat(defaultVerse.getText()).isEqualTo(convertedText);
        assertThat(defaultVerse.getReaction()).isEqualTo(convertedText);
    }

    @Test
    public void testVerseCanBeAnonymized() {
        Verse cleaned = populatedVerse.anonymizeVerse();
        assertThat(cleaned.getTitle()).isEqualTo(populatedVerse.getTitle());
        assertThat(cleaned.getAuthor()).isEqualTo(populatedVerse.getAuthor());
        assertThat(cleaned.getText()).isEqualTo(populatedVerse.getText());
        assertThat(cleaned.getId()).isNull();
        assertThat(cleaned.getReaction()).isNull();
        assertThat(cleaned.getTags()).isNull();        
    }
}
