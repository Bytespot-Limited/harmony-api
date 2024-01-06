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
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.repository.FleetRepository;
import tech.bytespot.harmony.service.criteria.FleetCriteria;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.service.mapper.FleetMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Fleet} entities in the database.
 * The main input is a {@link FleetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FleetDTO} or a {@link Page} of {@link FleetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FleetQueryService extends QueryService<Fleet> {

    private final Logger log = LoggerFactory.getLogger(FleetQueryService.class);

    private final FleetRepository fleetRepository;

    private final FleetMapper fleetMapper;

    public FleetQueryService(FleetRepository fleetRepository, FleetMapper fleetMapper) {
        this.fleetRepository = fleetRepository;
        this.fleetMapper = fleetMapper;
    }

    /**
     * Return a {@link List} of {@link FleetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FleetDTO> findByCriteria(FleetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fleet> specification = createSpecification(criteria);
        return fleetMapper.toDto(fleetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FleetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FleetDTO> findByCriteria(FleetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fleet> specification = createSpecification(criteria);
        return fleetRepository.findAll(specification, page).map(fleetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FleetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fleet> specification = createSpecification(criteria);
        return fleetRepository.count(specification);
    }

    /**
     * Function to convert {@link FleetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fleet> createSpecification(FleetCriteria criteria) {
        Specification<Fleet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fleet_.id));
            }
            if (criteria.getNumberPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumberPlate(), Fleet_.numberPlate));
            }
            if (criteria.getVehicleType() != null) {
                specification = specification.and(buildSpecification(criteria.getVehicleType(), Fleet_.vehicleType));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Fleet_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Fleet_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Fleet_.modifiedDate));
            }
            if (criteria.getTerminalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTerminalId(), root -> root.join(Fleet_.terminal, JoinType.LEFT).get(Terminal_.id))
                    );
            }
            if (criteria.getDriversId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDriversId(), root -> root.join(Fleet_.drivers, JoinType.LEFT).get(Drivers_.id))
                    );
            }
            if (criteria.getStudentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStudentsId(), root -> root.join(Fleet_.students, JoinType.LEFT).get(Students_.id))
                    );
            }
            if (criteria.getSchoolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSchoolId(), root -> root.join(Fleet_.school, JoinType.LEFT).get(Schools_.id))
                    );
            }
        }
        return specification;
    }
}
