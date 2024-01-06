package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.StudentTrips} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentTripsDTO implements Serializable {

    private Long id;

    @NotNull
    private BoardingStatus status;

    private Instant pickupTime;

    private Instant dropOffTime;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    private StudentsDTO student;

    private TripsDTO trip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardingStatus getStatus() {
        return status;
    }

    public void setStatus(BoardingStatus status) {
        this.status = status;
    }

    public Instant getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Instant pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Instant getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(Instant dropOffTime) {
        this.dropOffTime = dropOffTime;
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

    public StudentsDTO getStudent() {
        return student;
    }

    public void setStudent(StudentsDTO student) {
        this.student = student;
    }

    public TripsDTO getTrip() {
        return trip;
    }

    public void setTrip(TripsDTO trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentTripsDTO)) {
            return false;
        }

        StudentTripsDTO studentTripsDTO = (StudentTripsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentTripsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentTripsDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", pickupTime='" + getPickupTime() + "'" +
            ", dropOffTime='" + getDropOffTime() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", student=" + getStudent() +
            ", trip=" + getTrip() +
            "}";
    }
}
