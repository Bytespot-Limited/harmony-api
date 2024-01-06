package tech.bytespot.harmony.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.GuardianRelationshipType;

/**
 * A Guardians.
 */
@Entity
@Table(name = "guardians")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Guardians implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "guardian_type", nullable = false)
    private GuardianRelationshipType guardianType;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "guardian")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentTrips", "studentBillings", "fleet", "guardian" }, allowSetters = true)
    private Set<Students> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Guardians id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Guardians userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public Guardians name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Guardians dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public Guardians nationalId(String nationalId) {
        this.setNationalId(nationalId);
        return this;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public Guardians profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public GuardianRelationshipType getGuardianType() {
        return this.guardianType;
    }

    public Guardians guardianType(GuardianRelationshipType guardianType) {
        this.setGuardianType(guardianType);
        return this;
    }

    public void setGuardianType(GuardianRelationshipType guardianType) {
        this.guardianType = guardianType;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Guardians emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Guardians phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Guardians entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Guardians creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Guardians modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Students> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Students> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setGuardian(null));
        }
        if (students != null) {
            students.forEach(i -> i.setGuardian(this));
        }
        this.students = students;
    }

    public Guardians students(Set<Students> students) {
        this.setStudents(students);
        return this;
    }

    public Guardians addStudents(Students students) {
        this.students.add(students);
        students.setGuardian(this);
        return this;
    }

    public Guardians removeStudents(Students students) {
        this.students.remove(students);
        students.setGuardian(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Guardians)) {
            return false;
        }
        return getId() != null && getId().equals(((Guardians) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Guardians{" +
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
