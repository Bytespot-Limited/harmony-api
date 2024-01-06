package tech.bytespot.harmony.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class StudentTripsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentTrips.class);
        StudentTrips studentTrips1 = new StudentTrips();
        studentTrips1.setId(1L);
        StudentTrips studentTrips2 = new StudentTrips();
        studentTrips2.setId(studentTrips1.getId());
        assertThat(studentTrips1).isEqualTo(studentTrips2);
        studentTrips2.setId(2L);
        assertThat(studentTrips1).isNotEqualTo(studentTrips2);
        studentTrips1.setId(null);
        assertThat(studentTrips1).isNotEqualTo(studentTrips2);
    }
}
