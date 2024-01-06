package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Schools} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.SchoolsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schools?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchoolsCriteria implements Serializable, Criteria {

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

    private StringFilter location;

    private StringFilter logoImageUrl;

    private StringFilter emailAddress;

    private StringFilter phoneNumber;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter fleetId;

    private Boolean distinct;

    public SchoolsCriteria() {}

    public SchoolsCriteria(SchoolsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.logoImageUrl = other.logoImageUrl == null ? null : other.logoImageUrl.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.fleetId = other.fleetId == null ? null : other.fleetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SchoolsCriteria copy() {
        return new SchoolsCriteria(this);
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

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getLogoImageUrl() {
        return logoImageUrl;
    }

    public StringFilter logoImageUrl() {
        if (logoImageUrl == null) {
            logoImageUrl = new StringFilter();
        }
        return logoImageUrl;
    }

    public void setLogoImageUrl(StringFilter logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
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
        final SchoolsCriteria that = (SchoolsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(location, that.location) &&
            Objects.equals(logoImageUrl, that.logoImageUrl) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
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
            name,
            location,
            logoImageUrl,
            emailAddress,
            phoneNumber,
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
        return "SchoolsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (logoImageUrl != null ? "logoImageUrl=" + logoImageUrl + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (fleetId != null ? "fleetId=" + fleetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
