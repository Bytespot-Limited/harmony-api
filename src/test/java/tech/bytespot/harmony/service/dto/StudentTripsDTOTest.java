package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class StudentTripsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentTripsDTO.class);
        StudentTripsDTO studentTripsDTO1 = new StudentTripsDTO();
        studentTripsDTO1.setId(1L);
        StudentTripsDTO studentTripsDTO2 = new StudentTripsDTO();
        assertThat(studentTripsDTO1).isNotEqualTo(studentTripsDTO2);
        studentTripsDTO2.setId(studentTripsDTO1.getId());
        assertThat(studentTripsDTO1).isEqualTo(studentTripsDTO2);
        studentTripsDTO2.setId(2L);
        assertThat(studentTripsDTO1).isNotEqualTo(studentTripsDTO2);
        studentTripsDTO1.setId(null);
        assertThat(studentTripsDTO1).isNotEqualTo(studentTripsDTO2);
    }
}
