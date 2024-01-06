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
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.repository.GuardiansRepository;
import tech.bytespot.harmony.service.criteria.GuardiansCriteria;
import tech.bytespot.harmony.service.dto.GuardiansDTO;
import tech.bytespot.harmony.service.mapper.GuardiansMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Guardians} entities in the database.
 * The main input is a {@link GuardiansCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuardiansDTO} or a {@link Page} of {@link GuardiansDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuardiansQueryService extends QueryService<Guardians> {

    private final Logger log = LoggerFactory.getLogger(GuardiansQueryService.class);

    private final GuardiansRepository guardiansRepository;

    private final GuardiansMapper guardiansMapper;

    public GuardiansQueryService(GuardiansRepository guardiansRepository, GuardiansMapper guardiansMapper) {
        this.guardiansRepository = guardiansRepository;
        this.guardiansMapper = guardiansMapper;
    }

    /**
     * Return a {@link List} of {@link GuardiansDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuardiansDTO> findByCriteria(GuardiansCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Guardians> specification = createSpecification(criteria);
        return guardiansMapper.toDto(guardiansRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GuardiansDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuardiansDTO> findByCriteria(GuardiansCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Guardians> specification = createSpecification(criteria);
        return guardiansRepository.findAll(specification, page).map(guardiansMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuardiansCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Guardians> specification = createSpecification(criteria);
        return guardiansRepository.count(specification);
    }

    /**
     * Function to convert {@link GuardiansCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Guardians> createSpecification(GuardiansCriteria criteria) {
        Specification<Guardians> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Guardians_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Guardians_.userId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Guardians_.name));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Guardians_.dob));
            }
            if (criteria.getNationalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalId(), Guardians_.nationalId));
            }
            if (criteria.getProfileImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImageUrl(), Guardians_.profileImageUrl));
            }
            if (criteria.getGuardianType() != null) {
                specification = specification.and(buildSpecification(criteria.getGuardianType(), Guardians_.guardianType));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Guardians_.emailAddress));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Guardians_.phoneNumber));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Guardians_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Guardians_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Guardians_.modifiedDate));
            }
            if (criteria.getStudentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentsId(),
                            root -> root.join(Guardians_.students, JoinType.LEFT).get(Students_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
