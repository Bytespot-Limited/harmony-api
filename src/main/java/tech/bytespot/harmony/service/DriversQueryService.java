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
import tech.bytespot.harmony.domain.Drivers;
import tech.bytespot.harmony.repository.DriversRepository;
import tech.bytespot.harmony.service.criteria.DriversCriteria;
import tech.bytespot.harmony.service.dto.DriversDTO;
import tech.bytespot.harmony.service.mapper.DriversMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Drivers} entities in the database.
 * The main input is a {@link DriversCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DriversDTO} or a {@link Page} of {@link DriversDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DriversQueryService extends QueryService<Drivers> {

    private final Logger log = LoggerFactory.getLogger(DriversQueryService.class);

    private final DriversRepository driversRepository;

    private final DriversMapper driversMapper;

    public DriversQueryService(DriversRepository driversRepository, DriversMapper driversMapper) {
        this.driversRepository = driversRepository;
        this.driversMapper = driversMapper;
    }

    /**
     * Return a {@link List} of {@link DriversDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DriversDTO> findByCriteria(DriversCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Drivers> specification = createSpecification(criteria);
        return driversMapper.toDto(driversRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DriversDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DriversDTO> findByCriteria(DriversCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Drivers> specification = createSpecification(criteria);
        return driversRepository.findAll(specification, page).map(driversMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DriversCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Drivers> specification = createSpecification(criteria);
        return driversRepository.count(specification);
    }

    /**
     * Function to convert {@link DriversCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Drivers> createSpecification(DriversCriteria criteria) {
        Specification<Drivers> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Drivers_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Drivers_.userId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Drivers_.name));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Drivers_.dob));
            }
            if (criteria.getNationalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalId(), Drivers_.nationalId));
            }
            if (criteria.getProfileImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImageUrl(), Drivers_.profileImageUrl));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Drivers_.emailAddress));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Drivers_.phoneNumber));
            }
            if (criteria.getAssignmentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentStatus(), Drivers_.assignmentStatus));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Drivers_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Drivers_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Drivers_.modifiedDate));
            }
            if (criteria.getFleetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFleetId(), root -> root.join(Drivers_.fleet, JoinType.LEFT).get(Fleet_.id))
                    );
            }
        }
        return specification;
    }
}
