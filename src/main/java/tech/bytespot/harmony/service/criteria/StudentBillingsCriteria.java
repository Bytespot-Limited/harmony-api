package tech.bytespot.harmony.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.bytespot.harmony.domain.StudentBillings} entity. This class is used
 * in {@link tech.bytespot.harmony.web.rest.StudentBillingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-billings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentBillingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentChannel;

    private StringFilter paymentReference;

    private InstantFilter subscriptionStart;

    private InstantFilter subscriptionEnd;

    private LongFilter studentId;

    private Boolean distinct;

    public StudentBillingsCriteria() {}

    public StudentBillingsCriteria(StudentBillingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentChannel = other.paymentChannel == null ? null : other.paymentChannel.copy();
        this.paymentReference = other.paymentReference == null ? null : other.paymentReference.copy();
        this.subscriptionStart = other.subscriptionStart == null ? null : other.subscriptionStart.copy();
        this.subscriptionEnd = other.subscriptionEnd == null ? null : other.subscriptionEnd.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudentBillingsCriteria copy() {
        return new StudentBillingsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPaymentChannel() {
        return paymentChannel;
    }

    public StringFilter paymentChannel() {
        if (paymentChannel == null) {
            paymentChannel = new StringFilter();
        }
        return paymentChannel;
    }

    public void setPaymentChannel(StringFilter paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public StringFilter getPaymentReference() {
        return paymentReference;
    }

    public StringFilter paymentReference() {
        if (paymentReference == null) {
            paymentReference = new StringFilter();
        }
        return paymentReference;
    }

    public void setPaymentReference(StringFilter paymentReference) {
        this.paymentReference = paymentReference;
    }

    public InstantFilter getSubscriptionStart() {
        return subscriptionStart;
    }

    public InstantFilter subscriptionStart() {
        if (subscriptionStart == null) {
            subscriptionStart = new InstantFilter();
        }
        return subscriptionStart;
    }

    public void setSubscriptionStart(InstantFilter subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public InstantFilter getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public InstantFilter subscriptionEnd() {
        if (subscriptionEnd == null) {
            subscriptionEnd = new InstantFilter();
        }
        return subscriptionEnd;
    }

    public void setSubscriptionEnd(InstantFilter subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentBillingsCriteria that = (StudentBillingsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentChannel, that.paymentChannel) &&
            Objects.equals(paymentReference, that.paymentReference) &&
            Objects.equals(subscriptionStart, that.subscriptionStart) &&
            Objects.equals(subscriptionEnd, that.subscriptionEnd) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentChannel, paymentReference, subscriptionStart, subscriptionEnd, studentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentBillingsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentChannel != null ? "paymentChannel=" + paymentChannel + ", " : "") +
            (paymentReference != null ? "paymentReference=" + paymentReference + ", " : "") +
            (subscriptionStart != null ? "subscriptionStart=" + subscriptionStart + ", " : "") +
            (subscriptionEnd != null ? "subscriptionEnd=" + subscriptionEnd + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
