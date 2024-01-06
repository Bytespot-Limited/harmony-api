package tech.bytespot.harmony.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link tech.bytespot.harmony.domain.StudentBillings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentBillingsDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentChannel;

    @NotNull
    private String paymentReference;

    private Instant subscriptionStart;

    private Instant subscriptionEnd;

    private StudentsDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public Instant getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(Instant subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public Instant getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public void setSubscriptionEnd(Instant subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public StudentsDTO getStudent() {
        return student;
    }

    public void setStudent(StudentsDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentBillingsDTO)) {
            return false;
        }

        StudentBillingsDTO studentBillingsDTO = (StudentBillingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentBillingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentBillingsDTO{" +
            "id=" + getId() +
            ", paymentChannel='" + getPaymentChannel() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", subscriptionStart='" + getSubscriptionStart() + "'" +
            ", subscriptionEnd='" + getSubscriptionEnd() + "'" +
            ", student=" + getStudent() +
            "}";
    }
}
