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
import tech.bytespot.harmony.domain.enumeration.BillingStatus;
import tech.bytespot.harmony.domain.enumeration.ClassType;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A Students.
 */
@Entity
@Table(name = "students")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Students implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "class_level", nullable = false)
    private ClassType classLevel;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @NotNull
    @Column(name = "home_address", nullable = false)
    private String homeAddress;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_status", nullable = false)
    private BillingStatus billingStatus;

    @NotNull
    @Column(name = "next_billing_cycle", nullable = false)
    private Instant nextBillingCycle;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @JsonIgnoreProperties(value = { "student", "trip" }, allowSetters = true)
    private Set<StudentTrips> studentTrips = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @JsonIgnoreProperties(value = { "student" }, allowSetters = true)
    private Set<StudentBillings> studentBillings = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "terminal", "drivers", "students", "school" }, allowSetters = true, allowGetters = true)
    private Fleet fleet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "students" }, allowSetters = true, allowGetters = true)
    private Guardians guardian;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Students id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Students name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Students dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public ClassType getClassLevel() {
        return this.classLevel;
    }

    public Students classLevel(ClassType classLevel) {
        this.setClassLevel(classLevel);
        return this;
    }

    public void setClassLevel(ClassType classLevel) {
        this.classLevel = classLevel;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public Students profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getHomeAddress() {
        return this.homeAddress;
    }

    public Students homeAddress(String homeAddress) {
        this.setHomeAddress(homeAddress);
        return this;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public Students longitude(String longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public Students latitude(String latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public BillingStatus getBillingStatus() {
        return this.billingStatus;
    }

    public Students billingStatus(BillingStatus billingStatus) {
        this.setBillingStatus(billingStatus);
        return this;
    }

    public void setBillingStatus(BillingStatus billingStatus) {
        this.billingStatus = billingStatus;
    }

    public Instant getNextBillingCycle() {
        return this.nextBillingCycle;
    }

    public Students nextBillingCycle(Instant nextBillingCycle) {
        this.setNextBillingCycle(nextBillingCycle);
        return this;
    }

    public void setNextBillingCycle(Instant nextBillingCycle) {
        this.nextBillingCycle = nextBillingCycle;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Students entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Students creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Students modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<StudentTrips> getStudentTrips() {
        return this.studentTrips;
    }

    public void setStudentTrips(Set<StudentTrips> studentTrips) {
        if (this.studentTrips != null) {
            this.studentTrips.forEach(i -> i.setStudent(null));
        }
        if (studentTrips != null) {
            studentTrips.forEach(i -> i.setStudent(this));
        }
        this.studentTrips = studentTrips;
    }

    public Students studentTrips(Set<StudentTrips> studentTrips) {
        this.setStudentTrips(studentTrips);
        return this;
    }

    public Students addStudentTrips(StudentTrips studentTrips) {
        this.studentTrips.add(studentTrips);
        studentTrips.setStudent(this);
        return this;
    }

    public Students removeStudentTrips(StudentTrips studentTrips) {
        this.studentTrips.remove(studentTrips);
        studentTrips.setStudent(null);
        return this;
    }

    public Set<StudentBillings> getStudentBillings() {
        return this.studentBillings;
    }

    public void setStudentBillings(Set<StudentBillings> studentBillings) {
        if (this.studentBillings != null) {
            this.studentBillings.forEach(i -> i.setStudent(null));
        }
        if (studentBillings != null) {
            studentBillings.forEach(i -> i.setStudent(this));
        }
        this.studentBillings = studentBillings;
    }

    public Students studentBillings(Set<StudentBillings> studentBillings) {
        this.setStudentBillings(studentBillings);
        return this;
    }

    public Students addStudentBillings(StudentBillings studentBillings) {
        this.studentBillings.add(studentBillings);
        studentBillings.setStudent(this);
        return this;
    }

    public Students removeStudentBillings(StudentBillings studentBillings) {
        this.studentBillings.remove(studentBillings);
        studentBillings.setStudent(null);
        return this;
    }

    public Fleet getFleet() {
        return this.fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public Students fleet(Fleet fleet) {
        this.setFleet(fleet);
        return this;
    }

    public Guardians getGuardian() {
        return this.guardian;
    }

    public void setGuardian(Guardians guardians) {
        this.guardian = guardians;
    }

    public Students guardian(Guardians guardians) {
        this.setGuardian(guardians);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Students)) {
            return false;
        }
        return getId() != null && getId().equals(((Students) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Students{" +
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
            "}";
    }
}
