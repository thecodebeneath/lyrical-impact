package org.codebeneath.lyrics.tag;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.codebeneath.lyrics.impacted.Impacted;

/**
 * Tags that an impacted user might use to remember or group verses.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"label", "impacted_id"}))
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String label;

    @ManyToOne
    @NotNull
    private Impacted impacted;

    protected Tag() {
    }

    public Tag(String label, Impacted impacted) {
        this.label = label;
        this.impacted = impacted;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("Tag[id=%d, label=%s, impacted=%d]", id, label, impacted.getId());
    }
}
