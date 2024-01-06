package tech.bytespot.harmony.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.TripStatus;
import tech.bytespot.harmony.domain.enumeration.TripType;

/**
 * A Trips.
 */
@Entity
@Table(name = "trips")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trips implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false)
    private TripType tripType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", nullable = false)
    private TripStatus tripStatus;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "student", "trip" }, allowSetters = true)
    private Set<StudentTrips> studentTrips = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trips id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TripType getTripType() {
        return this.tripType;
    }

    public Trips tripType(TripType tripType) {
        this.setTripType(tripType);
        return this;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public TripStatus getTripStatus() {
        return this.tripStatus;
    }

    public Trips tripStatus(TripStatus tripStatus) {
        this.setTripStatus(tripStatus);
        return this;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Trips startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Trips endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Trips entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Trips creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Trips modifiedDate(Instant modifiedDate) {
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
            this.studentTrips.forEach(i -> i.setTrip(null));
        }
        if (studentTrips != null) {
            studentTrips.forEach(i -> i.setTrip(this));
        }
        this.studentTrips = studentTrips;
    }

    public Trips studentTrips(Set<StudentTrips> studentTrips) {
        this.setStudentTrips(studentTrips);
        return this;
    }

    public Trips addStudentTrips(StudentTrips studentTrips) {
        this.studentTrips.add(studentTrips);
        studentTrips.setTrip(this);
        return this;
    }

    public Trips removeStudentTrips(StudentTrips studentTrips) {
        this.studentTrips.remove(studentTrips);
        studentTrips.setTrip(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trips)) {
            return false;
        }
        return getId() != null && getId().equals(((Trips) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trips{" +
            "id=" + getId() +
            ", tripType='" + getTripType() + "'" +
            ", tripStatus='" + getTripStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
