package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.StudentBillings;
import tech.bytespot.harmony.repository.StudentBillingsRepository;
import tech.bytespot.harmony.service.StudentBillingsService;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;
import tech.bytespot.harmony.service.mapper.StudentBillingsMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.StudentBillings}.
 */
@Service
@Transactional
public class StudentBillingsServiceImpl implements StudentBillingsService {

    private final Logger log = LoggerFactory.getLogger(StudentBillingsServiceImpl.class);

    private final StudentBillingsRepository studentBillingsRepository;

    private final StudentBillingsMapper studentBillingsMapper;

    public StudentBillingsServiceImpl(StudentBillingsRepository studentBillingsRepository, StudentBillingsMapper studentBillingsMapper) {
        this.studentBillingsRepository = studentBillingsRepository;
        this.studentBillingsMapper = studentBillingsMapper;
    }

    @Override
    public StudentBillingsDTO save(StudentBillingsDTO studentBillingsDTO) {
        log.debug("Request to save StudentBillings : {}", studentBillingsDTO);
        StudentBillings studentBillings = studentBillingsMapper.toEntity(studentBillingsDTO);
        studentBillings = studentBillingsRepository.save(studentBillings);
        return studentBillingsMapper.toDto(studentBillings);
    }

    @Override
    public StudentBillingsDTO update(StudentBillingsDTO studentBillingsDTO) {
        log.debug("Request to update StudentBillings : {}", studentBillingsDTO);
        StudentBillings studentBillings = studentBillingsMapper.toEntity(studentBillingsDTO);
        studentBillings = studentBillingsRepository.save(studentBillings);
        return studentBillingsMapper.toDto(studentBillings);
    }

    @Override
    public Optional<StudentBillingsDTO> partialUpdate(StudentBillingsDTO studentBillingsDTO) {
        log.debug("Request to partially update StudentBillings : {}", studentBillingsDTO);

        return studentBillingsRepository
            .findById(studentBillingsDTO.getId())
            .map(existingStudentBillings -> {
                studentBillingsMapper.partialUpdate(existingStudentBillings, studentBillingsDTO);

                return existingStudentBillings;
            })
            .map(studentBillingsRepository::save)
            .map(studentBillingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentBillingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentBillings");
        return studentBillingsRepository.findAll(pageable).map(studentBillingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentBillingsDTO> findOne(Long id) {
        log.debug("Request to get StudentBillings : {}", id);
        return studentBillingsRepository.findById(id).map(studentBillingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentBillings : {}", id);
        studentBillingsRepository.deleteById(id);
    }
}
