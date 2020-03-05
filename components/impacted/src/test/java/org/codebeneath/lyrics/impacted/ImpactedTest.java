package org.codebeneath.lyrics.impacted;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ImpactedTest {
    
    @Test
    public void newImpactedAreGrantedOnlyUserRole() {
        Impacted impacted = new Impacted();
        assertThat(impacted.getRoles()).isEqualTo(Impacted.ROLE_USER);

        impacted = new Impacted("", "", "");
        assertThat(impacted.getRoles()).isEqualTo(Impacted.ROLE_USER);
    }

}
