package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class DriversDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriversDTO.class);
        DriversDTO driversDTO1 = new DriversDTO();
        driversDTO1.setId(1L);
        DriversDTO driversDTO2 = new DriversDTO();
        assertThat(driversDTO1).isNotEqualTo(driversDTO2);
        driversDTO2.setId(driversDTO1.getId());
        assertThat(driversDTO1).isEqualTo(driversDTO2);
        driversDTO2.setId(2L);
        assertThat(driversDTO1).isNotEqualTo(driversDTO2);
        driversDTO1.setId(null);
        assertThat(driversDTO1).isNotEqualTo(driversDTO2);
    }
}
