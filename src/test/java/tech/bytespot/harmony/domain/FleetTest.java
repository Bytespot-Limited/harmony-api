package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class FleetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fleet.class);
        Fleet fleet1 = new Fleet();
        fleet1.setId(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId(fleet1.getId());
        assertThat(fleet1).isEqualTo(fleet2);
        fleet2.setId(2L);
        assertThat(fleet1).isNotEqualTo(fleet2);
        fleet1.setId(null);
        assertThat(fleet1).isNotEqualTo(fleet2);
    }
}
