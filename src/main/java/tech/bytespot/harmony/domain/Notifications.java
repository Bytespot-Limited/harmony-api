package tech.bytespot.harmony.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.NotificationChannel;
import tech.bytespot.harmony.domain.enumeration.NotificationStatus;

/**
 * A Notifications.
 */
@Entity
@Table(name = "notifications")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private String channelId;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "error_message")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @Column(name = "creation_date")
    private Instant creationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notifications id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationChannel getChannel() {
        return this.channel;
    }

    public Notifications channel(NotificationChannel channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public Notifications channelId(String channelId) {
        this.setChannelId(channelId);
        return this;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return this.message;
    }

    public Notifications message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Notifications errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public Notifications status(NotificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Notifications creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notifications)) {
            return false;
        }
        return getId() != null && getId().equals(((Notifications) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notifications{" +
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
