package tech.bytespot.harmony.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.DeviceStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A Terminal.
 */
@Entity
@Table(name = "terminal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Terminal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "devide_id", nullable = false, unique = true)
    private String devideId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @NotNull
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "last_ping")
    private Instant lastPing;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @JsonIgnoreProperties(value = { "terminal", "drivers", "students", "school" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "terminal")
    private Fleet fleet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Terminal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevideId() {
        return this.devideId;
    }

    public Terminal devideId(String devideId) {
        this.setDevideId(devideId);
        return this;
    }

    public void setDevideId(String devideId) {
        this.devideId = devideId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Terminal phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public Terminal manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public Terminal model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Instant getLastPing() {
        return this.lastPing;
    }

    public Terminal lastPing(Instant lastPing) {
        this.setLastPing(lastPing);
        return this;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public Terminal longitude(String longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public Terminal latitude(String latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public DeviceStatus getStatus() {
        return this.status;
    }

    public Terminal status(DeviceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Terminal entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Terminal creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Terminal modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Fleet getFleet() {
        return this.fleet;
    }

    public void setFleet(Fleet fleet) {
        if (this.fleet != null) {
            this.fleet.setTerminal(null);
        }
        if (fleet != null) {
            fleet.setTerminal(this);
        }
        this.fleet = fleet;
    }

    public Terminal fleet(Fleet fleet) {
        this.setFleet(fleet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Terminal)) {
            return false;
        }
        return getId() != null && getId().equals(((Terminal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Terminal{" +
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
