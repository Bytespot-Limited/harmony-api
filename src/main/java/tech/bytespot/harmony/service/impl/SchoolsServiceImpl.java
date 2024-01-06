package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.repository.SchoolsRepository;
import tech.bytespot.harmony.service.SchoolsService;
import tech.bytespot.harmony.service.dto.SchoolsDTO;
import tech.bytespot.harmony.service.mapper.SchoolsMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Schools}.
 */
@Service
@Transactional
public class SchoolsServiceImpl implements SchoolsService {

    private final Logger log = LoggerFactory.getLogger(SchoolsServiceImpl.class);

    private final SchoolsRepository schoolsRepository;

    private final SchoolsMapper schoolsMapper;

    public SchoolsServiceImpl(SchoolsRepository schoolsRepository, SchoolsMapper schoolsMapper) {
        this.schoolsRepository = schoolsRepository;
        this.schoolsMapper = schoolsMapper;
    }

    @Override
    public SchoolsDTO save(SchoolsDTO schoolsDTO) {
        log.debug("Request to save Schools : {}", schoolsDTO);
        Schools schools = schoolsMapper.toEntity(schoolsDTO);
        schools = schoolsRepository.save(schools);
        return schoolsMapper.toDto(schools);
    }

    @Override
    public SchoolsDTO update(SchoolsDTO schoolsDTO) {
        log.debug("Request to update Schools : {}", schoolsDTO);
        Schools schools = schoolsMapper.toEntity(schoolsDTO);
        schools = schoolsRepository.save(schools);
        return schoolsMapper.toDto(schools);
    }

    @Override
    public Optional<SchoolsDTO> partialUpdate(SchoolsDTO schoolsDTO) {
        log.debug("Request to partially update Schools : {}", schoolsDTO);

        return schoolsRepository
            .findById(schoolsDTO.getId())
            .map(existingSchools -> {
                schoolsMapper.partialUpdate(existingSchools, schoolsDTO);

                return existingSchools;
            })
            .map(schoolsRepository::save)
            .map(schoolsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchoolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        return schoolsRepository.findAll(pageable).map(schoolsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchoolsDTO> findOne(Long id) {
        log.debug("Request to get Schools : {}", id);
        return schoolsRepository.findById(id).map(schoolsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Schools : {}", id);
        schoolsRepository.deleteById(id);
    }
}
