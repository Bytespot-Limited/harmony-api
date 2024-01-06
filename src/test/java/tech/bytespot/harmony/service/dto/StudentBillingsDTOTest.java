package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class StudentBillingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentBillingsDTO.class);
        StudentBillingsDTO studentBillingsDTO1 = new StudentBillingsDTO();
        studentBillingsDTO1.setId(1L);
        StudentBillingsDTO studentBillingsDTO2 = new StudentBillingsDTO();
        assertThat(studentBillingsDTO1).isNotEqualTo(studentBillingsDTO2);
        studentBillingsDTO2.setId(studentBillingsDTO1.getId());
        assertThat(studentBillingsDTO1).isEqualTo(studentBillingsDTO2);
        studentBillingsDTO2.setId(2L);
        assertThat(studentBillingsDTO1).isNotEqualTo(studentBillingsDTO2);
        studentBillingsDTO1.setId(null);
        assertThat(studentBillingsDTO1).isNotEqualTo(studentBillingsDTO2);
    }
}
