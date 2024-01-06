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
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.repository.TripsRepository;
import tech.bytespot.harmony.service.criteria.TripsCriteria;
import tech.bytespot.harmony.service.dto.TripsDTO;
import tech.bytespot.harmony.service.mapper.TripsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Trips} entities in the database.
 * The main input is a {@link TripsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TripsDTO} or a {@link Page} of {@link TripsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripsQueryService extends QueryService<Trips> {

    private final Logger log = LoggerFactory.getLogger(TripsQueryService.class);

    private final TripsRepository tripsRepository;

    private final TripsMapper tripsMapper;

    public TripsQueryService(TripsRepository tripsRepository, TripsMapper tripsMapper) {
        this.tripsRepository = tripsRepository;
        this.tripsMapper = tripsMapper;
    }

    /**
     * Return a {@link List} of {@link TripsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TripsDTO> findByCriteria(TripsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Trips> specification = createSpecification(criteria);
        return tripsMapper.toDto(tripsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TripsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripsDTO> findByCriteria(TripsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trips> specification = createSpecification(criteria);
        return tripsRepository.findAll(specification, page).map(tripsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Trips> specification = createSpecification(criteria);
        return tripsRepository.count(specification);
    }

    /**
     * Function to convert {@link TripsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trips> createSpecification(TripsCriteria criteria) {
        Specification<Trips> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trips_.id));
            }
            if (criteria.getTripType() != null) {
                specification = specification.and(buildSpecification(criteria.getTripType(), Trips_.tripType));
            }
            if (criteria.getTripStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTripStatus(), Trips_.tripStatus));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Trips_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Trips_.endTime));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Trips_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Trips_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Trips_.modifiedDate));
            }
            if (criteria.getStudentTripsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentTripsId(),
                            root -> root.join(Trips_.studentTrips, JoinType.LEFT).get(StudentTrips_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
