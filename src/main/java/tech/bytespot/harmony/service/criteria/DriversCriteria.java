package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.DriverAssignmentStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Drivers} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.DriversResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /drivers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DriversCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DriverAssignmentStatus
     */
    public static class DriverAssignmentStatusFilter extends Filter<DriverAssignmentStatus> {

        public DriverAssignmentStatusFilter() {}

        public DriverAssignmentStatusFilter(DriverAssignmentStatusFilter filter) {
            super(filter);
        }

        @Override
        public DriverAssignmentStatusFilter copy() {
            return new DriverAssignmentStatusFilter(this);
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

    private IntegerFilter userId;

    private StringFilter name;

    private LocalDateFilter dob;

    private StringFilter nationalId;

    private StringFilter profileImageUrl;

    private StringFilter emailAddress;

    private StringFilter phoneNumber;

    private DriverAssignmentStatusFilter assignmentStatus;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter fleetId;

    private Boolean distinct;

    public DriversCriteria() {}

    public DriversCriteria(DriversCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.nationalId = other.nationalId == null ? null : other.nationalId.copy();
        this.profileImageUrl = other.profileImageUrl == null ? null : other.profileImageUrl.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.assignmentStatus = other.assignmentStatus == null ? null : other.assignmentStatus.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.fleetId = other.fleetId == null ? null : other.fleetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DriversCriteria copy() {
        return new DriversCriteria(this);
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

    public IntegerFilter getUserId() {
        return userId;
    }

    public IntegerFilter userId() {
        if (userId == null) {
            userId = new IntegerFilter();
        }
        return userId;
    }

    public void setUserId(IntegerFilter userId) {
        this.userId = userId;
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

    public StringFilter getNationalId() {
        return nationalId;
    }

    public StringFilter nationalId() {
        if (nationalId == null) {
            nationalId = new StringFilter();
        }
        return nationalId;
    }

    public void setNationalId(StringFilter nationalId) {
        this.nationalId = nationalId;
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

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            emailAddress = new StringFilter();
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DriverAssignmentStatusFilter getAssignmentStatus() {
        return assignmentStatus;
    }

    public DriverAssignmentStatusFilter assignmentStatus() {
        if (assignmentStatus == null) {
            assignmentStatus = new DriverAssignmentStatusFilter();
        }
        return assignmentStatus;
    }

    public void setAssignmentStatus(DriverAssignmentStatusFilter assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
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
        final DriversCriteria that = (DriversCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(nationalId, that.nationalId) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(assignmentStatus, that.assignmentStatus) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(fleetId, that.fleetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            name,
            dob,
            nationalId,
            profileImageUrl,
            emailAddress,
            phoneNumber,
            assignmentStatus,
            entityStatus,
            creationDate,
            modifiedDate,
            fleetId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriversCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (nationalId != null ? "nationalId=" + nationalId + ", " : "") +
            (profileImageUrl != null ? "profileImageUrl=" + profileImageUrl + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (assignmentStatus != null ? "assignmentStatus=" + assignmentStatus + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (fleetId != null ? "fleetId=" + fleetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
