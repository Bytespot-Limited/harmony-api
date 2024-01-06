package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class DriversTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Drivers.class);
        Drivers drivers1 = new Drivers();
        drivers1.setId(1L);
        Drivers drivers2 = new Drivers();
        drivers2.setId(drivers1.getId());
        assertThat(drivers1).isEqualTo(drivers2);
        drivers2.setId(2L);
        assertThat(drivers1).isNotEqualTo(drivers2);
        drivers1.setId(null);
        assertThat(drivers1).isNotEqualTo(drivers2);
    }
}
