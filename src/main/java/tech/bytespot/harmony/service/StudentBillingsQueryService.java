package tech.bytespot.harmony.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.*; // for static metamodels
import tech.bytespot.harmony.domain.StudentBillings;
import tech.bytespot.harmony.repository.StudentBillingsRepository;
import tech.bytespot.harmony.service.criteria.StudentBillingsCriteria;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;
import tech.bytespot.harmony.service.mapper.StudentBillingsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentBillings} entities in the database.
 * The main input is a {@link StudentBillingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentBillingsDTO} or a {@link Page} of {@link StudentBillingsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentBillingsQueryService extends QueryService<StudentBillings> {

    private final Logger log = LoggerFactory.getLogger(StudentBillingsQueryService.class);

    private final StudentBillingsRepository studentBillingsRepository;

    private final StudentBillingsMapper studentBillingsMapper;

    public StudentBillingsQueryService(StudentBillingsRepository studentBillingsRepository, StudentBillingsMapper studentBillingsMapper) {
        this.studentBillingsRepository = studentBillingsRepository;
        this.studentBillingsMapper = studentBillingsMapper;
    }

    /**
     * Return a {@link List} of {@link StudentBillingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentBillingsDTO> findByCriteria(StudentBillingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentBillings> specification = createSpecification(criteria);
        return studentBillingsMapper.toDto(studentBillingsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentBillingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentBillingsDTO> findByCriteria(StudentBillingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentBillings> specification = createSpecification(criteria);
        return studentBillingsRepository.findAll(specification, page).map(studentBillingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentBillingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentBillings> specification = createSpecification(criteria);
        return studentBillingsRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentBillingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentBillings> createSpecification(StudentBillingsCriteria criteria) {
        Specification<StudentBillings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentBillings_.id));
            }
            if (criteria.getPaymentChannel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentChannel(), StudentBillings_.paymentChannel));
            }
            if (criteria.getPaymentReference() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentReference(), StudentBillings_.paymentReference));
            }
            if (criteria.getSubscriptionStart() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSubscriptionStart(), StudentBillings_.subscriptionStart));
            }
            if (criteria.getSubscriptionEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubscriptionEnd(), StudentBillings_.subscriptionEnd));
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentId(),
                            root -> root.join(StudentBillings_.student, JoinType.LEFT).get(Students_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
