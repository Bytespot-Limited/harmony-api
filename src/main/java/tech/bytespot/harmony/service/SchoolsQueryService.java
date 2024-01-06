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
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.repository.SchoolsRepository;
import tech.bytespot.harmony.service.criteria.SchoolsCriteria;
import tech.bytespot.harmony.service.dto.SchoolsDTO;
import tech.bytespot.harmony.service.mapper.SchoolsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Schools} entities in the database.
 * The main input is a {@link SchoolsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolsDTO} or a {@link Page} of {@link SchoolsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolsQueryService extends QueryService<Schools> {

    private final Logger log = LoggerFactory.getLogger(SchoolsQueryService.class);

    private final SchoolsRepository schoolsRepository;

    private final SchoolsMapper schoolsMapper;

    public SchoolsQueryService(SchoolsRepository schoolsRepository, SchoolsMapper schoolsMapper) {
        this.schoolsRepository = schoolsRepository;
        this.schoolsMapper = schoolsMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolsDTO> findByCriteria(SchoolsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Schools> specification = createSpecification(criteria);
        return schoolsMapper.toDto(schoolsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolsDTO> findByCriteria(SchoolsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Schools> specification = createSpecification(criteria);
        return schoolsRepository.findAll(specification, page).map(schoolsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Schools> specification = createSpecification(criteria);
        return schoolsRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Schools> createSpecification(SchoolsCriteria criteria) {
        Specification<Schools> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Schools_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Schools_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Schools_.location));
            }
            if (criteria.getLogoImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoImageUrl(), Schools_.logoImageUrl));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Schools_.emailAddress));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Schools_.phoneNumber));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Schools_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Schools_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Schools_.modifiedDate));
            }
            if (criteria.getFleetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFleetId(), root -> root.join(Schools_.fleets, JoinType.LEFT).get(Fleet_.id))
                    );
            }
        }
        return specification;
    }
}
