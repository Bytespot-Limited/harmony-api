package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class StudentBillingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentBillings.class);
        StudentBillings studentBillings1 = new StudentBillings();
        studentBillings1.setId(1L);
        StudentBillings studentBillings2 = new StudentBillings();
        studentBillings2.setId(studentBillings1.getId());
        assertThat(studentBillings1).isEqualTo(studentBillings2);
        studentBillings2.setId(2L);
        assertThat(studentBillings1).isNotEqualTo(studentBillings2);
        studentBillings1.setId(null);
        assertThat(studentBillings1).isNotEqualTo(studentBillings2);
    }
}
