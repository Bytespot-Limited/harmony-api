package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class SchoolsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolsDTO.class);
        SchoolsDTO schoolsDTO1 = new SchoolsDTO();
        schoolsDTO1.setId(1L);
        SchoolsDTO schoolsDTO2 = new SchoolsDTO();
        assertThat(schoolsDTO1).isNotEqualTo(schoolsDTO2);
        schoolsDTO2.setId(schoolsDTO1.getId());
        assertThat(schoolsDTO1).isEqualTo(schoolsDTO2);
        schoolsDTO2.setId(2L);
        assertThat(schoolsDTO1).isNotEqualTo(schoolsDTO2);
        schoolsDTO1.setId(null);
        assertThat(schoolsDTO1).isNotEqualTo(schoolsDTO2);
    }
}
