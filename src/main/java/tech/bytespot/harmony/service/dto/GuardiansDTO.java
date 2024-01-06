package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.GuardianRelationshipType;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Guardians} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuardiansDTO implements Serializable {

    private Long id;

    private Integer userId;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dob;

    @NotNull
    private String nationalId;

    private String profileImageUrl;

    @NotNull
    private GuardianRelationshipType guardianType;

    @NotNull
    private String emailAddress;

    @NotNull
    private String phoneNumber;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public GuardianRelationshipType getGuardianType() {
        return guardianType;
    }

    public void setGuardianType(GuardianRelationshipType guardianType) {
        this.guardianType = guardianType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        if (!(o instanceof GuardiansDTO)) {
            return false;
        }

        GuardiansDTO guardiansDTO = (GuardiansDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guardiansDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuardiansDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", nationalId='" + getNationalId() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", guardianType='" + getGuardianType() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
