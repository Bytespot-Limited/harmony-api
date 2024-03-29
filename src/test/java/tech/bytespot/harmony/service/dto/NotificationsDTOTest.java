package tech.bytespot.harmony.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.bytespot.harmony.web.rest.TestUtil;

class NotificationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationsDTO.class);
        NotificationsDTO notificationsDTO1 = new NotificationsDTO();
        notificationsDTO1.setId(1L);
        NotificationsDTO notificationsDTO2 = new NotificationsDTO();
        assertThat(notificationsDTO1).isNotEqualTo(notificationsDTO2);
        notificationsDTO2.setId(notificationsDTO1.getId());
        assertThat(notificationsDTO1).isEqualTo(notificationsDTO2);
        notificationsDTO2.setId(2L);
        assertThat(notificationsDTO1).isNotEqualTo(notificationsDTO2);
        notificationsDTO1.setId(null);
        assertThat(notificationsDTO1).isNotEqualTo(notificationsDTO2);
    }
}
