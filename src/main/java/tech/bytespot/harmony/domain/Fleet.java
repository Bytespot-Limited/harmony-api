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
import tech.bytespot.harmony.domain.enumeration.VehicleType;

/**
 * A Fleet.
 */
@Entity
@Table(name = "fleet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fleet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number_plate", nullable = false, unique = true)
    private String numberPlate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @JsonIgnoreProperties(value = { "fleet" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Terminal terminal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fleet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fleet" }, allowSetters = true)
    private Set<Drivers> drivers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fleet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentTrips", "studentBillings", "fleet", "guardian" }, allowSetters = true)
    private Set<Students> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fleets" }, allowSetters = true)
    private Schools school;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fleet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return this.numberPlate;
    }

    public Fleet numberPlate(String numberPlate) {
        this.setNumberPlate(numberPlate);
        return this;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public Fleet vehicleType(VehicleType vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Fleet entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Fleet creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Fleet modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Fleet terminal(Terminal terminal) {
        this.setTerminal(terminal);
        return this;
    }

    public Set<Drivers> getDrivers() {
        return this.drivers;
    }

    public void setDrivers(Set<Drivers> drivers) {
        if (this.drivers != null) {
            this.drivers.forEach(i -> i.setFleet(null));
        }
        if (drivers != null) {
            drivers.forEach(i -> i.setFleet(this));
        }
        this.drivers = drivers;
    }

    public Fleet drivers(Set<Drivers> drivers) {
        this.setDrivers(drivers);
        return this;
    }

    public Fleet addDrivers(Drivers drivers) {
        this.drivers.add(drivers);
        drivers.setFleet(this);
        return this;
    }

    public Fleet removeDrivers(Drivers drivers) {
        this.drivers.remove(drivers);
        drivers.setFleet(null);
        return this;
    }

    public Set<Students> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Students> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setFleet(null));
        }
        if (students != null) {
            students.forEach(i -> i.setFleet(this));
        }
        this.students = students;
    }

    public Fleet students(Set<Students> students) {
        this.setStudents(students);
        return this;
    }

    public Fleet addStudents(Students students) {
        this.students.add(students);
        students.setFleet(this);
        return this;
    }

    public Fleet removeStudents(Students students) {
        this.students.remove(students);
        students.setFleet(null);
        return this;
    }

    public Schools getSchool() {
        return this.school;
    }

    public void setSchool(Schools schools) {
        this.school = schools;
    }

    public Fleet school(Schools schools) {
        this.setSchool(schools);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fleet)) {
            return false;
        }
        return getId() != null && getId().equals(((Fleet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fleet{" +
            "id=" + getId() +
            ", numberPlate='" + getNumberPlate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
