package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.SchoolStaff;
import tech.bytespot.harmony.repository.SchoolStaffRepository;
import tech.bytespot.harmony.service.SchoolStaffService;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;
import tech.bytespot.harmony.service.mapper.SchoolStaffMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.SchoolStaff}.
 */
@Service
@Transactional
public class SchoolStaffServiceImpl implements SchoolStaffService {

    private final Logger log = LoggerFactory.getLogger(SchoolStaffServiceImpl.class);

    private final SchoolStaffRepository schoolStaffRepository;

    private final SchoolStaffMapper schoolStaffMapper;

    public SchoolStaffServiceImpl(SchoolStaffRepository schoolStaffRepository, SchoolStaffMapper schoolStaffMapper) {
        this.schoolStaffRepository = schoolStaffRepository;
        this.schoolStaffMapper = schoolStaffMapper;
    }

    @Override
    public SchoolStaffDTO save(SchoolStaffDTO schoolStaffDTO) {
        log.debug("Request to save SchoolStaff : {}", schoolStaffDTO);
        SchoolStaff schoolStaff = schoolStaffMapper.toEntity(schoolStaffDTO);
        schoolStaff = schoolStaffRepository.save(schoolStaff);
        return schoolStaffMapper.toDto(schoolStaff);
    }

    @Override
    public SchoolStaffDTO update(SchoolStaffDTO schoolStaffDTO) {
        log.debug("Request to update SchoolStaff : {}", schoolStaffDTO);
        SchoolStaff schoolStaff = schoolStaffMapper.toEntity(schoolStaffDTO);
        schoolStaff = schoolStaffRepository.save(schoolStaff);
        return schoolStaffMapper.toDto(schoolStaff);
    }

    @Override
    public Optional<SchoolStaffDTO> partialUpdate(SchoolStaffDTO schoolStaffDTO) {
        log.debug("Request to partially update SchoolStaff : {}", schoolStaffDTO);

        return schoolStaffRepository
            .findById(schoolStaffDTO.getId())
            .map(existingSchoolStaff -> {
                schoolStaffMapper.partialUpdate(existingSchoolStaff, schoolStaffDTO);

                return existingSchoolStaff;
            })
            .map(schoolStaffRepository::save)
            .map(schoolStaffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchoolStaffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolStaffs");
        return schoolStaffRepository.findAll(pageable).map(schoolStaffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchoolStaffDTO> findOne(Long id) {
        log.debug("Request to get SchoolStaff : {}", id);
        return schoolStaffRepository.findById(id).map(schoolStaffMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchoolStaff : {}", id);
        schoolStaffRepository.deleteById(id);
    }
}
