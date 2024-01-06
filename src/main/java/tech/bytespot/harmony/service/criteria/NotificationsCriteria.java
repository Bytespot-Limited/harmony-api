package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.NotificationChannel;
import tech.bytespot.harmony.domain.enumeration.NotificationStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Notifications} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.NotificationsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationChannel
     */
    public static class NotificationChannelFilter extends Filter<NotificationChannel> {

        public NotificationChannelFilter() {}

        public NotificationChannelFilter(NotificationChannelFilter filter) {
            super(filter);
        }

        @Override
        public NotificationChannelFilter copy() {
            return new NotificationChannelFilter(this);
        }
    }

    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {}

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private NotificationChannelFilter channel;

    private StringFilter channelId;

    private StringFilter message;

    private StringFilter errorMessage;

    private NotificationStatusFilter status;

    private InstantFilter creationDate;

    private Boolean distinct;

    public NotificationsCriteria() {}

    public NotificationsCriteria(NotificationsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.channel = other.channel == null ? null : other.channel.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.errorMessage = other.errorMessage == null ? null : other.errorMessage.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationsCriteria copy() {
        return new NotificationsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public NotificationChannelFilter getChannel() {
        return channel;
    }

    public NotificationChannelFilter channel() {
        if (channel == null) {
            channel = new NotificationChannelFilter();
        }
        return channel;
    }

    public void setChannel(NotificationChannelFilter channel) {
        this.channel = channel;
    }

    public StringFilter getChannelId() {
        return channelId;
    }

    public StringFilter channelId() {
        if (channelId == null) {
            channelId = new StringFilter();
        }
        return channelId;
    }

    public void setChannelId(StringFilter channelId) {
        this.channelId = channelId;
    }

    public StringFilter getMessage() {
        return message;
    }

    public StringFilter message() {
        if (message == null) {
            message = new StringFilter();
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getErrorMessage() {
        return errorMessage;
    }

    public StringFilter errorMessage() {
        if (errorMessage == null) {
            errorMessage = new StringFilter();
        }
        return errorMessage;
    }

    public void setErrorMessage(StringFilter errorMessage) {
        this.errorMessage = errorMessage;
    }

    public NotificationStatusFilter getStatus() {
        return status;
    }

    public NotificationStatusFilter status() {
        if (status == null) {
            status = new NotificationStatusFilter();
        }
        return status;
    }

    public void setStatus(NotificationStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationsCriteria that = (NotificationsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(message, that.message) &&
            Objects.equals(errorMessage, that.errorMessage) &&
            Objects.equals(status, that.status) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channel, channelId, message, errorMessage, status, creationDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (channel != null ? "channel=" + channel + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            (errorMessage != null ? "errorMessage=" + errorMessage + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
