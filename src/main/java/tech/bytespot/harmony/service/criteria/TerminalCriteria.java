package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.bytespot.harmony.domain.enumeration.DeviceStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.Terminal} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.TerminalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /terminals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TerminalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DeviceStatus
     */
    public static class DeviceStatusFilter extends Filter<DeviceStatus> {

        public DeviceStatusFilter() {}

        public DeviceStatusFilter(DeviceStatusFilter filter) {
            super(filter);
        }

        @Override
        public DeviceStatusFilter copy() {
            return new DeviceStatusFilter(this);
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

    private StringFilter devideId;

    private StringFilter phoneNumber;

    private StringFilter manufacturer;

    private StringFilter model;

    private InstantFilter lastPing;

    private StringFilter longitude;

    private StringFilter latitude;

    private DeviceStatusFilter status;

    private EntityStatusFilter entityStatus;

    private InstantFilter creationDate;

    private InstantFilter modifiedDate;

    private LongFilter fleetId;

    private Boolean distinct;

    public TerminalCriteria() {}

    public TerminalCriteria(TerminalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.devideId = other.devideId == null ? null : other.devideId.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.manufacturer = other.manufacturer == null ? null : other.manufacturer.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.lastPing = other.lastPing == null ? null : other.lastPing.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.entityStatus = other.entityStatus == null ? null : other.entityStatus.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.fleetId = other.fleetId == null ? null : other.fleetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TerminalCriteria copy() {
        return new TerminalCriteria(this);
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

    public StringFilter getDevideId() {
        return devideId;
    }

    public StringFilter devideId() {
        if (devideId == null) {
            devideId = new StringFilter();
        }
        return devideId;
    }

    public void setDevideId(StringFilter devideId) {
        this.devideId = devideId;
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

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public StringFilter manufacturer() {
        if (manufacturer == null) {
            manufacturer = new StringFilter();
        }
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public InstantFilter getLastPing() {
        return lastPing;
    }

    public InstantFilter lastPing() {
        if (lastPing == null) {
            lastPing = new InstantFilter();
        }
        return lastPing;
    }

    public void setLastPing(InstantFilter lastPing) {
        this.lastPing = lastPing;
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

    public DeviceStatusFilter getStatus() {
        return status;
    }

    public DeviceStatusFilter status() {
        if (status == null) {
            status = new DeviceStatusFilter();
        }
        return status;
    }

    public void setStatus(DeviceStatusFilter status) {
        this.status = status;
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
        final TerminalCriteria that = (TerminalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(devideId, that.devideId) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(manufacturer, that.manufacturer) &&
            Objects.equals(model, that.model) &&
            Objects.equals(lastPing, that.lastPing) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(status, that.status) &&
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
            devideId,
            phoneNumber,
            manufacturer,
            model,
            lastPing,
            longitude,
            latitude,
            status,
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
        return "TerminalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (devideId != null ? "devideId=" + devideId + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (manufacturer != null ? "manufacturer=" + manufacturer + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (lastPing != null ? "lastPing=" + lastPing + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (entityStatus != null ? "entityStatus=" + entityStatus + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (fleetId != null ? "fleetId=" + fleetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
