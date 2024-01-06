package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.TripStatus;
import tech.bytespot.harmony.domain.enumeration.TripType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Trips} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.TripsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TripType
     */
    public static class TripTypeFilter extends Filter<TripType> {

        public TripTypeFilter() {}

        public TripTypeFilter(TripTypeFilter filter) {
            super(filter);
        }

        @Override
        public TripTypeFilter copy() {
            return new TripTypeFilter(this);
        }
    }

    /**
     * Class for filtering TripStatus
     */
    public static class TripStatusFilter extends Filter<TripStatus> {

        public TripStatusFilter() {}

        public TripStatusFilter(TripStatusFilter filter) {
            super(filter);
        }

        @Override
        public TripStatusFilter copy() {
            return new TripStatusFilter(this);
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

    private TripTypeFilter tripType;

    private TripStatusFilter tripStatus;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter studentTripsId;

    private Boolean distinct;

    public TripsCriteria() {}

    public TripsCriteria(TripsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tripType = other.tripType == null ? null : other.tripType.copy();
        this.tripStatus = other.tripStatus == null ? null : other.tripStatus.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.studentTripsId = other.studentTripsId == null ? null : other.studentTripsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TripsCriteria copy() {
        return new TripsCriteria(this);
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

    public TripTypeFilter getTripType() {
        return tripType;
    }

    public TripTypeFilter tripType() {
        if (tripType == null) {
            tripType = new TripTypeFilter();
        }
        return tripType;
    }

    public void setTripType(TripTypeFilter tripType) {
        this.tripType = tripType;
    }

    public TripStatusFilter getTripStatus() {
        return tripStatus;
    }

    public TripStatusFilter tripStatus() {
        if (tripStatus == null) {
            tripStatus = new TripStatusFilter();
        }
        return tripStatus;
    }

    public void setTripStatus(TripStatusFilter tripStatus) {
        this.tripStatus = tripStatus;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
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

    public LongFilter getStudentTripsId() {
        return studentTripsId;
    }

    public LongFilter studentTripsId() {
        if (studentTripsId == null) {
            studentTripsId = new LongFilter();
        }
        return studentTripsId;
    }

    public void setStudentTripsId(LongFilter studentTripsId) {
        this.studentTripsId = studentTripsId;
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
        final TripsCriteria that = (TripsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tripType, that.tripType) &&
            Objects.equals(tripStatus, that.tripStatus) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(studentTripsId, that.studentTripsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tripType,
            tripStatus,
            startTime,
            endTime,
            entityStatus,
            creationDate,
            modifiedDate,
            studentTripsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tripType != null ? "tripType=" + tripType + ", " : "") +
            (tripStatus != null ? "tripStatus=" + tripStatus + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (studentTripsId != null ? "studentTripsId=" + studentTripsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
