package tech.bytespot.harmony.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A SchoolStaff.
 */
@Entity
@Table(name = "school_staff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchoolStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "role_description", nullable = false)
    private String roleDescription;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @NotNull
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @NotNull
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SchoolStaff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public SchoolStaff userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public SchoolStaff name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleDescription() {
        return this.roleDescription;
    }

    public SchoolStaff roleDescription(String roleDescription) {
        this.setRoleDescription(roleDescription);
        return this;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public SchoolStaff dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public SchoolStaff nationalId(String nationalId) {
        this.setNationalId(nationalId);
        return this;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public SchoolStaff profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public SchoolStaff emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public SchoolStaff phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public SchoolStaff entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public SchoolStaff creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public SchoolStaff modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolStaff)) {
            return false;
        }
        return getId() != null && getId().equals(((SchoolStaff) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolStaff{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", name='" + getName() + "'" +
            ", roleDescription='" + getRoleDescription() + "'" +
            ", dob='" + getDob() + "'" +
            ", nationalId='" + getNationalId() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
