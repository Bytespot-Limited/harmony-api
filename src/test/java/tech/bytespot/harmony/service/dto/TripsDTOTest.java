package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class TripsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripsDTO.class);
        TripsDTO tripsDTO1 = new TripsDTO();
        tripsDTO1.setId(1L);
        TripsDTO tripsDTO2 = new TripsDTO();
        assertThat(tripsDTO1).isNotEqualTo(tripsDTO2);
        tripsDTO2.setId(tripsDTO1.getId());
        assertThat(tripsDTO1).isEqualTo(tripsDTO2);
        tripsDTO2.setId(2L);
        assertThat(tripsDTO1).isNotEqualTo(tripsDTO2);
        tripsDTO1.setId(null);
        assertThat(tripsDTO1).isNotEqualTo(tripsDTO2);
    }
}
