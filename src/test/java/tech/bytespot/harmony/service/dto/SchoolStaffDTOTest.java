package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class SchoolStaffDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolStaffDTO.class);
        SchoolStaffDTO schoolStaffDTO1 = new SchoolStaffDTO();
        schoolStaffDTO1.setId(1L);
        SchoolStaffDTO schoolStaffDTO2 = new SchoolStaffDTO();
        assertThat(schoolStaffDTO1).isNotEqualTo(schoolStaffDTO2);
        schoolStaffDTO2.setId(schoolStaffDTO1.getId());
        assertThat(schoolStaffDTO1).isEqualTo(schoolStaffDTO2);
        schoolStaffDTO2.setId(2L);
        assertThat(schoolStaffDTO1).isNotEqualTo(schoolStaffDTO2);
        schoolStaffDTO1.setId(null);
        assertThat(schoolStaffDTO1).isNotEqualTo(schoolStaffDTO2);
    }
}
