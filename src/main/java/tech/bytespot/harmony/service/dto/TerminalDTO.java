package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.DeviceStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Terminal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TerminalDTO implements Serializable {

    private Long id;

    @NotNull
    private String devideId;

    private String phoneNumber;

    @NotNull
    private String manufacturer;

    @NotNull
    private String model;

    private Instant lastPing;

    private String longitude;

    private String latitude;

    private DeviceStatus status;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevideId() {
        return devideId;
    }

    public void setDevideId(String devideId) {
        this.devideId = devideId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
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
        if (!(o instanceof TerminalDTO)) {
            return false;
        }

        TerminalDTO terminalDTO = (TerminalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terminalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalDTO{" +
            "id=" + getId() +
            ", devideId='" + getDevideId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", model='" + getModel() + "'" +
            ", lastPing='" + getLastPing() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", status='" + getStatus() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
