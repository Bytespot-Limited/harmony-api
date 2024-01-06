package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class SchoolStaffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolStaff.class);
        SchoolStaff schoolStaff1 = new SchoolStaff();
        schoolStaff1.setId(1L);
        SchoolStaff schoolStaff2 = new SchoolStaff();
        schoolStaff2.setId(schoolStaff1.getId());
        assertThat(schoolStaff1).isEqualTo(schoolStaff2);
        schoolStaff2.setId(2L);
        assertThat(schoolStaff1).isNotEqualTo(schoolStaff2);
        schoolStaff1.setId(null);
        assertThat(schoolStaff1).isNotEqualTo(schoolStaff2);
    }
}
