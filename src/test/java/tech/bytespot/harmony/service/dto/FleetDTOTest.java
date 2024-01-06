package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class FleetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FleetDTO.class);
        FleetDTO fleetDTO1 = new FleetDTO();
        fleetDTO1.setId(1L);
        FleetDTO fleetDTO2 = new FleetDTO();
        assertThat(fleetDTO1).isNotEqualTo(fleetDTO2);
        fleetDTO2.setId(fleetDTO1.getId());
        assertThat(fleetDTO1).isEqualTo(fleetDTO2);
        fleetDTO2.setId(2L);
        assertThat(fleetDTO1).isNotEqualTo(fleetDTO2);
        fleetDTO1.setId(null);
        assertThat(fleetDTO1).isNotEqualTo(fleetDTO2);
    }
}
