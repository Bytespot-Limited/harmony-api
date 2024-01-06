package tech.bytespot.harmony.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;

/**
 * A StudentTrips.
 */
@Entity
@Table(name = "student_trips")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentTrips implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BoardingStatus status;

    @Column(name = "pickup_time")
    private Instant pickupTime;

    @Column(name = "drop_off_time")
    private Instant dropOffTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "studentTrips", "studentBillings", "fleet", "guardian" }, allowSetters = true)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "studentTrips" }, allowSetters = true)
    private Trips trip;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentTrips id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardingStatus getStatus() {
        return this.status;
    }

    public StudentTrips status(BoardingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BoardingStatus status) {
        this.status = status;
    }

    public Instant getPickupTime() {
        return this.pickupTime;
    }

    public StudentTrips pickupTime(Instant pickupTime) {
        this.setPickupTime(pickupTime);
        return this;
    }

    public void setPickupTime(Instant pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Instant getDropOffTime() {
        return this.dropOffTime;
    }

    public StudentTrips dropOffTime(Instant dropOffTime) {
        this.setDropOffTime(dropOffTime);
        return this;
    }

    public void setDropOffTime(Instant dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public StudentTrips entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public StudentTrips creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public StudentTrips modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Students getStudent() {
        return this.student;
    }

    public void setStudent(Students students) {
        this.student = students;
    }

    public StudentTrips student(Students students) {
        this.setStudent(students);
        return this;
    }

    public Trips getTrip() {
        return this.trip;
    }

    public void setTrip(Trips trips) {
        this.trip = trips;
    }

    public StudentTrips trip(Trips trips) {
        this.setTrip(trips);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentTrips)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentTrips) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentTrips{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", pickupTime='" + getPickupTime() + "'" +
            ", dropOffTime='" + getDropOffTime() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
