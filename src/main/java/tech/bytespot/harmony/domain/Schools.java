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

/**
 * A Schools.
 */
@Entity
@Table(name = "schools")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schools implements Serializable {

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
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "logo_image_url")
    private String logoImageUrl;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "terminal", "drivers", "students", "school" }, allowSetters = true)
    private Set<Fleet> fleets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Schools id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Schools name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public Schools location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogoImageUrl() {
        return this.logoImageUrl;
    }

    public Schools logoImageUrl(String logoImageUrl) {
        this.setLogoImageUrl(logoImageUrl);
        return this;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Schools emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Schools phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EntityStatus getEntityStatus() {
        return this.entityStatus;
    }

    public Schools entityStatus(EntityStatus entityStatus) {
        this.setEntityStatus(entityStatus);
        return this;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Schools creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Schools modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Fleet> getFleets() {
        return this.fleets;
    }

    public void setFleets(Set<Fleet> fleets) {
        if (this.fleets != null) {
            this.fleets.forEach(i -> i.setSchool(null));
        }
        if (fleets != null) {
            fleets.forEach(i -> i.setSchool(this));
        }
        this.fleets = fleets;
    }

    public Schools fleets(Set<Fleet> fleets) {
        this.setFleets(fleets);
        return this;
    }

    public Schools addFleet(Fleet fleet) {
        this.fleets.add(fleet);
        fleet.setSchool(this);
        return this;
    }

    public Schools removeFleet(Fleet fleet) {
        this.fleets.remove(fleet);
        fleet.setSchool(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schools)) {
            return false;
        }
        return getId() != null && getId().equals(((Schools) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schools{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", logoImageUrl='" + getLogoImageUrl() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
