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
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.repository.StudentsRepository;
import tech.bytespot.harmony.service.criteria.StudentsCriteria;
import tech.bytespot.harmony.service.dto.StudentsDTO;
import tech.bytespot.harmony.service.mapper.StudentsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Students} entities in the database.
 * The main input is a {@link StudentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentsDTO} or a {@link Page} of {@link StudentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentsQueryService extends QueryService<Students> {

    private final Logger log = LoggerFactory.getLogger(StudentsQueryService.class);

    private final StudentsRepository studentsRepository;

    private final StudentsMapper studentsMapper;

    public StudentsQueryService(StudentsRepository studentsRepository, StudentsMapper studentsMapper) {
        this.studentsRepository = studentsRepository;
        this.studentsMapper = studentsMapper;
    }

    /**
     * Return a {@link List} of {@link StudentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentsDTO> findByCriteria(StudentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsMapper.toDto(studentsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentsDTO> findByCriteria(StudentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsRepository.findAll(specification, page).map(studentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Students> createSpecification(StudentsCriteria criteria) {
        Specification<Students> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Students_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Students_.name));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Students_.dob));
            }
            if (criteria.getClassLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getClassLevel(), Students_.classLevel));
            }
            if (criteria.getProfileImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImageUrl(), Students_.profileImageUrl));
            }
            if (criteria.getHomeAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomeAddress(), Students_.homeAddress));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), Students_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), Students_.latitude));
            }
            if (criteria.getBillingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getBillingStatus(), Students_.billingStatus));
            }
            if (criteria.getNextBillingCycle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextBillingCycle(), Students_.nextBillingCycle));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Students_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Students_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Students_.modifiedDate));
            }
            if (criteria.getStudentTripsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentTripsId(),
                            root -> root.join(Students_.studentTrips, JoinType.LEFT).get(StudentTrips_.id)
                        )
                    );
            }
            if (criteria.getStudentBillingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentBillingsId(),
                            root -> root.join(Students_.studentBillings, JoinType.LEFT).get(StudentBillings_.id)
                        )
                    );
            }
            if (criteria.getFleetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFleetId(), root -> root.join(Students_.fleet, JoinType.LEFT).get(Fleet_.id))
                    );
            }
            if (criteria.getGuardianId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGuardianId(),
                            root -> root.join(Students_.guardian, JoinType.LEFT).get(Guardians_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
