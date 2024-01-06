package tech.bytespot.harmony.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.*; // for static metamodels
import tech.bytespot.harmony.domain.SchoolStaff;
import tech.bytespot.harmony.repository.SchoolStaffRepository;
import tech.bytespot.harmony.service.criteria.SchoolStaffCriteria;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;
import tech.bytespot.harmony.service.mapper.SchoolStaffMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SchoolStaff} entities in the database.
 * The main input is a {@link SchoolStaffCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolStaffDTO} or a {@link Page} of {@link SchoolStaffDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolStaffQueryService extends QueryService<SchoolStaff> {

    private final Logger log = LoggerFactory.getLogger(SchoolStaffQueryService.class);

    private final SchoolStaffRepository schoolStaffRepository;

    private final SchoolStaffMapper schoolStaffMapper;

    public SchoolStaffQueryService(SchoolStaffRepository schoolStaffRepository, SchoolStaffMapper schoolStaffMapper) {
        this.schoolStaffRepository = schoolStaffRepository;
        this.schoolStaffMapper = schoolStaffMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolStaffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolStaffDTO> findByCriteria(SchoolStaffCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SchoolStaff> specification = createSpecification(criteria);
        return schoolStaffMapper.toDto(schoolStaffRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolStaffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolStaffDTO> findByCriteria(SchoolStaffCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolStaff> specification = createSpecification(criteria);
        return schoolStaffRepository.findAll(specification, page).map(schoolStaffMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolStaffCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolStaff> specification = createSpecification(criteria);
        return schoolStaffRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolStaffCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolStaff> createSpecification(SchoolStaffCriteria criteria) {
        Specification<SchoolStaff> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolStaff_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), SchoolStaff_.userId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SchoolStaff_.name));
            }
            if (criteria.getRoleDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoleDescription(), SchoolStaff_.roleDescription));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), SchoolStaff_.dob));
            }
            if (criteria.getNationalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalId(), SchoolStaff_.nationalId));
            }
            if (criteria.getProfileImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImageUrl(), SchoolStaff_.profileImageUrl));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), SchoolStaff_.emailAddress));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SchoolStaff_.phoneNumber));
            }
            if (criteria.getEntityStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityStatus(), SchoolStaff_.entityStatus));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), SchoolStaff_.creationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), SchoolStaff_.modifiedDate));
            }
        }
        return specification;
    }
}
