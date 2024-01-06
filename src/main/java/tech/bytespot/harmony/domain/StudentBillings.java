package tech.bytespot.harmony.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentBillings.
 */
@Entity
@Table(name = "student_billings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentBillings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "payment_channel", nullable = false)
    private String paymentChannel;

    @NotNull
    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "subscription_start")
    private Instant subscriptionStart;

    @Column(name = "subscription_end")
    private Instant subscriptionEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "studentTrips", "studentBillings", "fleet", "guardian" }, allowSetters = true)
    private Students student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentBillings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentChannel() {
        return this.paymentChannel;
    }

    public StudentBillings paymentChannel(String paymentChannel) {
        this.setPaymentChannel(paymentChannel);
        return this;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public StudentBillings paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public Instant getSubscriptionStart() {
        return this.subscriptionStart;
    }

    public StudentBillings subscriptionStart(Instant subscriptionStart) {
        this.setSubscriptionStart(subscriptionStart);
        return this;
    }

    public void setSubscriptionStart(Instant subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public Instant getSubscriptionEnd() {
        return this.subscriptionEnd;
    }

    public StudentBillings subscriptionEnd(Instant subscriptionEnd) {
        this.setSubscriptionEnd(subscriptionEnd);
        return this;
    }

    public void setSubscriptionEnd(Instant subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public Students getStudent() {
        return this.student;
    }

    public void setStudent(Students students) {
        this.student = students;
    }

    public StudentBillings student(Students students) {
        this.setStudent(students);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentBillings)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentBillings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentBillings{" +
            "id=" + getId() +
            ", paymentChannel='" + getPaymentChannel() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", subscriptionStart='" + getSubscriptionStart() + "'" +
            ", subscriptionEnd='" + getSubscriptionEnd() + "'" +
            "}";
    }
}
