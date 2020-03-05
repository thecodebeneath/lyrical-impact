package org.codebeneath.lyrics.impactedapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ImpactedUserTest {

    @Test
    public void newImpactedAreGrantedOnlyUserRole() {
        ImpactedUser impacted = new ImpactedUser();
        assertThat(impacted.getRoles()).isEqualTo(ImpactedUser.ROLE_USER);

        impacted = new ImpactedUser("", "", "");
        assertThat(impacted.getRoles()).isEqualTo(ImpactedUser.ROLE_USER);
    }

}
