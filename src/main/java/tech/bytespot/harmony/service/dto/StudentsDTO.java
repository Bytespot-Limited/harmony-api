package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.BillingStatus;
import tech.bytespot.harmony.domain.enumeration.ClassType;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Students} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dob;

    @NotNull
    private ClassType classLevel;

    private String profileImageUrl;

    @NotNull
    private String homeAddress;

    private String longitude;

    private String latitude;

    @NotNull
    private BillingStatus billingStatus;

    @NotNull
    private Instant nextBillingCycle;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    private FleetDTO fleet;

    private GuardiansDTO guardian;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public ClassType getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(ClassType classLevel) {
        this.classLevel = classLevel;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
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

    public BillingStatus getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(BillingStatus billingStatus) {
        this.billingStatus = billingStatus;
    }

    public Instant getNextBillingCycle() {
        return nextBillingCycle;
    }

    public void setNextBillingCycle(Instant nextBillingCycle) {
        this.nextBillingCycle = nextBillingCycle;
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

    public FleetDTO getFleet() {
        return fleet;
    }

    public void setFleet(FleetDTO fleet) {
        this.fleet = fleet;
    }

    public GuardiansDTO getGuardian() {
        return guardian;
    }

    public void setGuardian(GuardiansDTO guardian) {
        this.guardian = guardian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentsDTO)) {
            return false;
        }

        StudentsDTO studentsDTO = (StudentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", classLevel='" + getClassLevel() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", homeAddress='" + getHomeAddress() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", billingStatus='" + getBillingStatus() + "'" +
            ", nextBillingCycle='" + getNextBillingCycle() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", fleet=" + getFleet() +
            ", guardian=" + getGuardian() +
            "}";
    }
}
