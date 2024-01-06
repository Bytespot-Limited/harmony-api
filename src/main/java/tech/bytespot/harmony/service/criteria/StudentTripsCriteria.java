package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.StudentTrips} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.StudentTripsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentTripsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BoardingStatus
     */
    public static class BoardingStatusFilter extends Filter<BoardingStatus> {

        public BoardingStatusFilter() {}

        public BoardingStatusFilter(BoardingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BoardingStatusFilter copy() {
            return new BoardingStatusFilter(this);
        }
    }

    /**
     * Class for filtering EntityStatus
     */
    public static class EntityStatusFilter extends Filter<EntityStatus> {

        public EntityStatusFilter() {}

        public EntityStatusFilter(EntityStatusFilter filter) {
            super(filter);
        }

        @Override
        public EntityStatusFilter copy() {
            return new EntityStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BoardingStatusFilter status;

    private InstantFilter pickupTime;

    private InstantFilter dropOffTime;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter studentId;

    private LongFilter tripId;

    private Boolean distinct;

    public StudentTripsCriteria() {}

    public StudentTripsCriteria(StudentTripsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.pickupTime = other.pickupTime == null ? null : other.pickupTime.copy();
        this.dropOffTime = other.dropOffTime == null ? null : other.dropOffTime.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudentTripsCriteria copy() {
        return new StudentTripsCriteria(this);
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

    public BoardingStatusFilter getStatus() {
        return status;
    }

    public BoardingStatusFilter status() {
        if (status == null) {
            status = new BoardingStatusFilter();
        }
        return status;
    }

    public void setStatus(BoardingStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getPickupTime() {
        return pickupTime;
    }

    public InstantFilter pickupTime() {
        if (pickupTime == null) {
            pickupTime = new InstantFilter();
        }
        return pickupTime;
    }

    public void setPickupTime(InstantFilter pickupTime) {
        this.pickupTime = pickupTime;
    }

    public InstantFilter getDropOffTime() {
        return dropOffTime;
    }

    public InstantFilter dropOffTime() {
        if (dropOffTime == null) {
            dropOffTime = new InstantFilter();
        }
        return dropOffTime;
    }

    public void setDropOffTime(InstantFilter dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public EntityStatusFilter getEntityStatus() {
        return entityStatus;
    }

    public EntityStatusFilter entityStatus() {
        if (entityStatus == null) {
            entityStatus = new EntityStatusFilter();
        }
        return entityStatus;
    }

    public void setEntityStatus(EntityStatusFilter entityStatus) {
        this.entityStatus = entityStatus;
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

    public InstantFilter getModifiedDate() {
        return modifiedDate;
    }

    public InstantFilter modifiedDate() {
        if (modifiedDate == null) {
            modifiedDate = new InstantFilter();
        }
        return modifiedDate;
    }

    public void setModifiedDate(InstantFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getTripId() {
        return tripId;
    }

    public LongFilter tripId() {
        if (tripId == null) {
            tripId = new LongFilter();
        }
        return tripId;
    }

    public void setTripId(LongFilter tripId) {
        this.tripId = tripId;
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
        final StudentTripsCriteria that = (StudentTripsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(pickupTime, that.pickupTime) &&
            Objects.equals(dropOffTime, that.dropOffTime) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, pickupTime, dropOffTime, entityStatus, creationDate, modifiedDate, studentId, tripId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentTripsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (pickupTime != null ? "pickupTime=" + pickupTime + ", " : "") +
            (dropOffTime != null ? "dropOffTime=" + dropOffTime + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
