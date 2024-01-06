package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.VehicleType;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.Fleet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FleetDTO implements Serializable {

    private Long id;

    @NotNull
    private String numberPlate;

    @NotNull
    private VehicleType vehicleType;

    private EntityStatus entityStatus;

    private Instant creationDate;

    private Instant modifiedDate;

    private TerminalDTO terminal;

    private SchoolsDTO school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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

    public TerminalDTO getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDTO terminal) {
        this.terminal = terminal;
    }

    public SchoolsDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolsDTO school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FleetDTO)) {
            return false;
        }

        FleetDTO fleetDTO = (FleetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fleetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FleetDTO{" +
            "id=" + getId() +
            ", numberPlate='" + getNumberPlate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", entityStatus='" + getEntityStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", terminal=" + getTerminal() +
            ", school=" + getSchool() +
            "}";
    }
}
