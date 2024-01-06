package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class GuardiansTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guardians.class);
        Guardians guardians1 = new Guardians();
        guardians1.setId(1L);
        Guardians guardians2 = new Guardians();
        guardians2.setId(guardians1.getId());
        assertThat(guardians1).isEqualTo(guardians2);
        guardians2.setId(2L);
        assertThat(guardians1).isNotEqualTo(guardians2);
        guardians1.setId(null);
        assertThat(guardians1).isNotEqualTo(guardians2);
    }
}
