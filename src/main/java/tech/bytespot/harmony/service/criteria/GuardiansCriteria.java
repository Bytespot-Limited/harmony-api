package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.GuardianRelationshipType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Guardians} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.GuardiansResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guardians?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuardiansCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GuardianRelationshipType
     */
    public static class GuardianRelationshipTypeFilter extends Filter<GuardianRelationshipType> {

        public GuardianRelationshipTypeFilter() {}

        public GuardianRelationshipTypeFilter(GuardianRelationshipTypeFilter filter) {
            super(filter);
        }

        @Override
        public GuardianRelationshipTypeFilter copy() {
            return new GuardianRelationshipTypeFilter(this);
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

    private GuardianRelationshipTypeFilter guardianType;

    private StringFilter emailAddress;

    private StringFilter phoneNumber;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter studentsId;

    private Boolean distinct;

    public GuardiansCriteria() {}

    public GuardiansCriteria(GuardiansCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.nationalId = other.nationalId == null ? null : other.nationalId.copy();
        this.profileImageUrl = other.profileImageUrl == null ? null : other.profileImageUrl.copy();
        this.guardianType = other.guardianType == null ? null : other.guardianType.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.studentsId = other.studentsId == null ? null : other.studentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GuardiansCriteria copy() {
        return new GuardiansCriteria(this);
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

    public GuardianRelationshipTypeFilter getGuardianType() {
        return guardianType;
    }

    public GuardianRelationshipTypeFilter guardianType() {
        if (guardianType == null) {
            guardianType = new GuardianRelationshipTypeFilter();
        }
        return guardianType;
    }

    public void setGuardianType(GuardianRelationshipTypeFilter guardianType) {
        this.guardianType = guardianType;
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
        final GuardiansCriteria that = (GuardiansCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(nationalId, that.nationalId) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(guardianType, that.guardianType) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(entityStatus, that.entityStatus) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(studentsId, that.studentsId) &&
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
            guardianType,
            emailAddress,
            phoneNumber,
            entityStatus,
            creationDate,
            modifiedDate,
            studentsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuardiansCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (nationalId != null ? "nationalId=" + nationalId + ", " : "") +
            (profileImageUrl != null ? "profileImageUrl=" + profileImageUrl + ", " : "") +
            (guardianType != null ? "guardianType=" + guardianType + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (studentsId != null ? "studentsId=" + studentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
