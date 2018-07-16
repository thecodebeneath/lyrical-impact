package org.codebeneath.lyrics.impacted;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.codebeneath.lyrics.verse.Verse;

/**
 * The person that was impacted by a verse and wants to express why.
 */
@Entity
public class Impacted {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Verse> verses = new HashSet<>();

    protected Impacted() {
    }

    public Impacted(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Impacted(String firstName, String lastName, Set<Verse> verses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.verses = verses;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Verse> getVerses() {
        return verses;
    }

    public void setVerses(Set<Verse> verses) {
        this.verses = verses;
    }

    @Override
    public String toString() {
        return String.format("Impacted[id=%d, firstName='%s', lastName='%s']",
                id, getFirstName(), getLastName());
    }
}
