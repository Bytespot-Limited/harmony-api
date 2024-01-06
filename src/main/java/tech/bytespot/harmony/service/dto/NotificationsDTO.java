package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.NotificationChannel;
import tech.bytespot.harmony.domain.enumeration.NotificationStatus;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Notifications} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationsDTO implements Serializable {

    private Long id;

    @NotNull
    private NotificationChannel channel;

    @NotNull
    private String channelId;

    @NotNull
    private String message;

    private String errorMessage;

    private NotificationStatus status;

    private Instant creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationsDTO)) {
            return false;
        }

        NotificationsDTO notificationsDTO = (NotificationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationsDTO{" +
            "id=" + getId() +
            ", channel='" + getChannel() + "'" +
            ", channelId='" + getChannelId() + "'" +
            ", message='" + getMessage() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
