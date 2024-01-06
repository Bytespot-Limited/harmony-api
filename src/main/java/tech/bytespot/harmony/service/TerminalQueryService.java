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
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.repository.TerminalRepository;
import tech.bytespot.harmony.service.criteria.TerminalCriteria;
import tech.bytespot.harmony.service.dto.TerminalDTO;
import tech.bytespot.harmony.service.mapper.TerminalMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Terminal} entities in the database.
 * The main input is a {@link TerminalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TerminalDTO} or a {@link Page} of {@link TerminalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TerminalQueryService extends QueryService<Terminal> {

    private final Logger log = LoggerFactory.getLogger(TerminalQueryService.class);

    private final TerminalRepository terminalRepository;

    private final TerminalMapper terminalMapper;

    public TerminalQueryService(TerminalRepository terminalRepository, TerminalMapper terminalMapper) {
        this.terminalRepository = terminalRepository;
        this.terminalMapper = terminalMapper;
    }

    /**
     * Return a {@link List} of {@link TerminalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TerminalDTO> findByCriteria(TerminalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Terminal> specification = createSpecification(criteria);
        return terminalMapper.toDto(terminalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TerminalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TerminalDTO> findByCriteria(TerminalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Terminal> specification = createSpecification(criteria);
        return terminalRepository.findAll(specification, page).map(terminalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TerminalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Terminal> specification = createSpecification(criteria);
        return terminalRepository.count(specification);
    }

    /**
     * Function to convert {@link TerminalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Terminal> createSpecification(TerminalCriteria criteria) {
        Specification<Terminal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Terminal_.id));
            }
            if (criteria.getDevideId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDevideId(), Terminal_.devideId));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Terminal_.phoneNumber));
            }
            if (criteria.getManufacturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacturer(), Terminal_.manufacturer));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Terminal_.model));
            }
            if (criteria.getLastPing() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastPing(), Terminal_.lastPing));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), Terminal_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), Terminal_.latitude));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Terminal_.status));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), Terminal_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Terminal_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Terminal_.modifiedDate));
            }
            if (criteria.getFleetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFleetId(), root -> root.join(Terminal_.fleet, JoinType.LEFT).get(Fleet_.id))
                    );
            }
        }
        return specification;
    }
}
