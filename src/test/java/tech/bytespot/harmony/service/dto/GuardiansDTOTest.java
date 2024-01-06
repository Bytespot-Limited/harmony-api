package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class GuardiansDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuardiansDTO.class);
        GuardiansDTO guardiansDTO1 = new GuardiansDTO();
        guardiansDTO1.setId(1L);
        GuardiansDTO guardiansDTO2 = new GuardiansDTO();
        assertThat(guardiansDTO1).isNotEqualTo(guardiansDTO2);
        guardiansDTO2.setId(guardiansDTO1.getId());
        assertThat(guardiansDTO1).isEqualTo(guardiansDTO2);
        guardiansDTO2.setId(2L);
        assertThat(guardiansDTO1).isNotEqualTo(guardiansDTO2);
        guardiansDTO1.setId(null);
        assertThat(guardiansDTO1).isNotEqualTo(guardiansDTO2);
    }
}
