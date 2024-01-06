package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.BillingStatus;
import tech.bytespot.harmony.domain.enumeration.ClassType;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Students} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.StudentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ClassType
     */
    public static class ClassTypeFilter extends Filter<ClassType> {

        public ClassTypeFilter() {}

        public ClassTypeFilter(ClassTypeFilter filter) {
            super(filter);
        }

        @Override
        public ClassTypeFilter copy() {
            return new ClassTypeFilter(this);
        }
    }

    /**
     * Class for filtering BillingStatus
     */
    public static class BillingStatusFilter extends Filter<BillingStatus> {

        public BillingStatusFilter() {}

        public BillingStatusFilter(BillingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BillingStatusFilter copy() {
            return new BillingStatusFilter(this);
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

    private StringFilter name;

    private LocalDateFilter dob;

    private ClassTypeFilter classLevel;

    private StringFilter profileImageUrl;

    private StringFilter homeAddress;

    private StringFilter longitude;

    private StringFilter latitude;

    private BillingStatusFilter billingStatus;

    private InstantFilter nextBillingCycle;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter studentTripsId;

    private LongFilter studentBillingsId;

    private LongFilter fleetId;

    private LongFilter guardianId;

    private Boolean distinct;

    public StudentsCriteria() {}

    public StudentsCriteria(StudentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.classLevel = other.classLevel == null ? null : other.classLevel.copy();
        this.profileImageUrl = other.profileImageUrl == null ? null : other.profileImageUrl.copy();
        this.homeAddress = other.homeAddress == null ? null : other.homeAddress.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.billingStatus = other.billingStatus == null ? null : other.billingStatus.copy();
        this.nextBillingCycle = other.nextBillingCycle == null ? null : other.nextBillingCycle.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.studentTripsId = other.studentTripsId == null ? null : other.studentTripsId.copy();
        this.studentBillingsId = other.studentBillingsId == null ? null : other.studentBillingsId.copy();
        this.fleetId = other.fleetId == null ? null : other.fleetId.copy();
        this.guardianId = other.guardianId == null ? null : other.guardianId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudentsCriteria copy() {
        return new StudentsCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getDob() {
        return dob;
    }

    public LocalDateFilter dob() {
        if (dob == null) {
            dob = new LocalDateFilter();
        }
        return dob;
    }

    public void setDob(LocalDateFilter dob) {
        this.dob = dob;
    }

    public ClassTypeFilter getClassLevel() {
        return classLevel;
    }

    public ClassTypeFilter classLevel() {
        if (classLevel == null) {
            classLevel = new ClassTypeFilter();
        }
        return classLevel;
    }

    public void setClassLevel(ClassTypeFilter classLevel) {
        this.classLevel = classLevel;
    }

    public StringFilter getProfileImageUrl() {
        return profileImageUrl;
    }

    public StringFilter profileImageUrl() {
        if (profileImageUrl == null) {
            profileImageUrl = new StringFilter();
        }
        return profileImageUrl;
    }

    public void setProfileImageUrl(StringFilter profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public StringFilter getHomeAddress() {
        return homeAddress;
    }

    public StringFilter homeAddress() {
        if (homeAddress == null) {
            homeAddress = new StringFilter();
        }
        return homeAddress;
    }

    public void setHomeAddress(StringFilter homeAddress) {
        this.homeAddress = homeAddress;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public StringFilter longitude() {
        if (longitude == null) {
            longitude = new StringFilter();
        }
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public StringFilter latitude() {
        if (latitude == null) {
            latitude = new StringFilter();
        }
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
    }

    public BillingStatusFilter getBillingStatus() {
        return billingStatus;
    }

    public BillingStatusFilter billingStatus() {
        if (billingStatus == null) {
            billingStatus = new BillingStatusFilter();
        }
        return billingStatus;
    }

    public void setBillingStatus(BillingStatusFilter billingStatus) {
        this.billingStatus = billingStatus;
    }

    public InstantFilter getNextBillingCycle() {
        return nextBillingCycle;
    }

    public InstantFilter nextBillingCycle() {
        if (nextBillingCycle == null) {
            nextBillingCycle = new InstantFilter();
        }
        return nextBillingCycle;
    }

    public void setNextBillingCycle(InstantFilter nextBillingCycle) {
        this.nextBillingCycle = nextBillingCycle;
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

    public LongFilter getStudentBillingsId() {
        return studentBillingsId;
    }

    public LongFilter studentBillingsId() {
        if (studentBillingsId == null) {
            studentBillingsId = new LongFilter();
        }
        return studentBillingsId;
    }

    public void setStudentBillingsId(LongFilter studentBillingsId) {
        this.studentBillingsId = studentBillingsId;
    }

    public LongFilter getFleetId() {
        return fleetId;
    }

    public LongFilter fleetId() {
        if (fleetId == null) {
            fleetId = new LongFilter();
        }
        return fleetId;
    }

    public void setFleetId(LongFilter fleetId) {
        this.fleetId = fleetId;
    }

    public LongFilter getGuardianId() {
        return guardianId;
    }

    public LongFilter guardianId() {
        if (guardianId == null) {
            guardianId = new LongFilter();
        }
        return guardianId;
    }

    public void setGuardianId(LongFilter guardianId) {
        this.guardianId = guardianId;
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
        final StudentsCriteria that = (StudentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(classLevel, that.classLevel) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(homeAddress, that.homeAddress) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(billingStatus, that.billingStatus) &&
            Objects.equals(nextBillingCycle, that.nextBillingCycle) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(studentTripsId, that.studentTripsId) &&
            Objects.equals(studentBillingsId, that.studentBillingsId) &&
            Objects.equals(fleetId, that.fleetId) &&
            Objects.equals(guardianId, that.guardianId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            dob,
            classLevel,
            profileImageUrl,
            homeAddress,
            longitude,
            latitude,
            billingStatus,
            nextBillingCycle,
            entityStatus,
            creationDate,
            modifiedDate,
            studentTripsId,
            studentBillingsId,
            fleetId,
            guardianId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (classLevel != null ? "classLevel=" + classLevel + ", " : "") +
            (profileImageUrl != null ? "profileImageUrl=" + profileImageUrl + ", " : "") +
            (homeAddress != null ? "homeAddress=" + homeAddress + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (billingStatus != null ? "billingStatus=" + billingStatus + ", " : "") +
            (nextBillingCycle != null ? "nextBillingCycle=" + nextBillingCycle + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (studentTripsId != null ? "studentTripsId=" + studentTripsId + ", " : "") +
            (studentBillingsId != null ? "studentBillingsId=" + studentBillingsId + ", " : "") +
            (fleetId != null ? "fleetId=" + fleetId + ", " : "") +
            (guardianId != null ? "guardianId=" + guardianId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
