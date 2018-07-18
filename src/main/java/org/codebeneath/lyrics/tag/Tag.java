package org.codebeneath.lyrics.tag;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codebeneath.lyrics.impacted.Impacted;

/**
 * Tags that an impacted user might use to remember or group verses.
 */
@Entity
@Getter
@Setter
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Impacted impacted;

    protected Tag() {
    }

    public Tag(String label, Impacted impacted) {
        this.label = label;
        this.impacted = impacted;
    }

}
