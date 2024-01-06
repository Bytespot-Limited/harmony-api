package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.VehicleType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Fleet} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.FleetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fleets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FleetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering VehicleType
     */
    public static class VehicleTypeFilter extends Filter<VehicleType> {

        public VehicleTypeFilter() {}

        public VehicleTypeFilter(VehicleTypeFilter filter) {
            super(filter);
        }

        @Override
        public VehicleTypeFilter copy() {
            return new VehicleTypeFilter(this);
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

    private StringFilter numberPlate;

    private VehicleTypeFilter vehicleType;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter terminalId;

    private LongFilter driversId;

    private LongFilter studentsId;

    private LongFilter schoolId;

    private Boolean distinct;

    public FleetCriteria() {}

    public FleetCriteria(FleetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numberPlate = other.numberPlate == null ? null : other.numberPlate.copy();
        this.vehicleType = other.vehicleType == null ? null : other.vehicleType.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.terminalId = other.terminalId == null ? null : other.terminalId.copy();
        this.driversId = other.driversId == null ? null : other.driversId.copy();
        this.studentsId = other.studentsId == null ? null : other.studentsId.copy();
        this.schoolId = other.schoolId == null ? null : other.schoolId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FleetCriteria copy() {
        return new FleetCriteria(this);
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

    public StringFilter getNumberPlate() {
        return numberPlate;
    }

    public StringFilter numberPlate() {
        if (numberPlate == null) {
            numberPlate = new StringFilter();
        }
        return numberPlate;
    }

    public void setNumberPlate(StringFilter numberPlate) {
        this.numberPlate = numberPlate;
    }

    public VehicleTypeFilter getVehicleType() {
        return vehicleType;
    }

    public VehicleTypeFilter vehicleType() {
        if (vehicleType == null) {
            vehicleType = new VehicleTypeFilter();
        }
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeFilter vehicleType) {
        this.vehicleType = vehicleType;
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

    public LongFilter getTerminalId() {
        return terminalId;
    }

    public LongFilter terminalId() {
        if (terminalId == null) {
            terminalId = new LongFilter();
        }
        return terminalId;
    }

    public void setTerminalId(LongFilter terminalId) {
        this.terminalId = terminalId;
    }

    public LongFilter getDriversId() {
        return driversId;
    }

    public LongFilter driversId() {
        if (driversId == null) {
            driversId = new LongFilter();
        }
        return driversId;
    }

    public void setDriversId(LongFilter driversId) {
        this.driversId = driversId;
    }

    public LongFilter getStudentsId() {
        return studentsId;
    }

    public LongFilter studentsId() {
        if (studentsId == null) {
            studentsId = new LongFilter();
        }
        return studentsId;
    }

    public void setStudentsId(LongFilter studentsId) {
        this.studentsId = studentsId;
    }

    public LongFilter getSchoolId() {
        return schoolId;
    }

    public LongFilter schoolId() {
        if (schoolId == null) {
            schoolId = new LongFilter();
        }
        return schoolId;
    }

    public void setSchoolId(LongFilter schoolId) {
        this.schoolId = schoolId;
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
        final FleetCriteria that = (FleetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numberPlate, that.numberPlate) &&
            Objects.equals(vehicleType, that.vehicleType) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(terminalId, that.terminalId) &&
            Objects.equals(driversId, that.driversId) &&
            Objects.equals(studentsId, that.studentsId) &&
            Objects.equals(schoolId, that.schoolId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numberPlate,
            vehicleType,
            entityStatus,
            creationDate,
            modifiedDate,
            terminalId,
            driversId,
            studentsId,
            schoolId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FleetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numberPlate != null ? "numberPlate=" + numberPlate + ", " : "") +
            (vehicleType != null ? "vehicleType=" + vehicleType + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (terminalId != null ? "terminalId=" + terminalId + ", " : "") +
            (driversId != null ? "driversId=" + driversId + ", " : "") +
            (studentsId != null ? "studentsId=" + studentsId + ", " : "") +
            (schoolId != null ? "schoolId=" + schoolId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
