package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.TripStatus;
import tech.bytespot.harmony.domain.enumeration.TripType;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Trips} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripsDTO implements Serializable {

    private Long id;

    @NotNull
    private TripType tripType;

    @NotNull
    private TripStatus tripStatus;

    @NotNull
    private Instant startTime;

    private Instant endTime;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public EntityStatus getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripsDTO)) {
            return false;
        }

        TripsDTO tripsDTO = (TripsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripsDTO{" +
            "id=" + getId() +
            ", tripType='" + getTripType() + "'" +
            ", tripStatus='" + getTripStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
