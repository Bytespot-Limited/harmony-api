package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class SchoolsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schools.class);
        Schools schools1 = new Schools();
        schools1.setId(1L);
        Schools schools2 = new Schools();
        schools2.setId(schools1.getId());
        assertThat(schools1).isEqualTo(schools2);
        schools2.setId(2L);
        assertThat(schools1).isNotEqualTo(schools2);
        schools1.setId(null);
        assertThat(schools1).isNotEqualTo(schools2);
    }
}
