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
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.repository.StudentTripsRepository;
import tech.bytespot.harmony.service.criteria.StudentTripsCriteria;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;
import tech.bytespot.harmony.service.mapper.StudentTripsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentTrips} entities in the database.
 * The main input is a {@link StudentTripsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentTripsDTO} or a {@link Page} of {@link StudentTripsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentTripsQueryService extends QueryService<StudentTrips> {

    private final Logger log = LoggerFactory.getLogger(StudentTripsQueryService.class);

    private final StudentTripsRepository studentTripsRepository;

    private final StudentTripsMapper studentTripsMapper;

    public StudentTripsQueryService(StudentTripsRepository studentTripsRepository, StudentTripsMapper studentTripsMapper) {
        this.studentTripsRepository = studentTripsRepository;
        this.studentTripsMapper = studentTripsMapper;
    }

    /**
     * Return a {@link List} of {@link StudentTripsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentTripsDTO> findByCriteria(StudentTripsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentTrips> specification = createSpecification(criteria);
        return studentTripsMapper.toDto(studentTripsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentTripsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentTripsDTO> findByCriteria(StudentTripsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentTrips> specification = createSpecification(criteria);
        return studentTripsRepository.findAll(specification, page).map(studentTripsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentTripsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentTrips> specification = createSpecification(criteria);
        return studentTripsRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentTripsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentTrips> createSpecification(StudentTripsCriteria criteria) {
        Specification<StudentTrips> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentTrips_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), StudentTrips_.status));
            }
            if (criteria.getPickupTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPickupTime(), StudentTrips_.pickupTime));
            }
            if (criteria.getDropOffTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDropOffTime(), StudentTrips_.dropOffTime));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), StudentTrips_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), StudentTrips_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), StudentTrips_.modifiedDate));
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentId(),
                            root -> root.join(StudentTrips_.student, JoinType.LEFT).get(Students_.id)
                        )
                    );
            }
            if (criteria.getTripId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTripId(), root -> root.join(StudentTrips_.trip, JoinType.LEFT).get(Trips_.id))
                    );
            }
        }
        return specification;
    }
}
