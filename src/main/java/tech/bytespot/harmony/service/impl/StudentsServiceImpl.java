package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.repository.StudentsRepository;
import tech.bytespot.harmony.service.StudentsService;
import tech.bytespot.harmony.service.dto.StudentsDTO;
import tech.bytespot.harmony.service.mapper.StudentsMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Students}.
 */
@Service
@Transactional
public class StudentsServiceImpl implements StudentsService {

    private final Logger log = LoggerFactory.getLogger(StudentsServiceImpl.class);

    private final StudentsRepository studentsRepository;

    private final StudentsMapper studentsMapper;

    public StudentsServiceImpl(StudentsRepository studentsRepository, StudentsMapper studentsMapper) {
        this.studentsRepository = studentsRepository;
        this.studentsMapper = studentsMapper;
    }

    @Override
    public StudentsDTO save(StudentsDTO studentsDTO) {
        log.debug("Request to save Students : {}", studentsDTO);
        Students students = studentsMapper.toEntity(studentsDTO);
        students = studentsRepository.save(students);
        return studentsMapper.toDto(students);
    }

    @Override
    public StudentsDTO update(StudentsDTO studentsDTO) {
        log.debug("Request to update Students : {}", studentsDTO);
        Students students = studentsMapper.toEntity(studentsDTO);
        students = studentsRepository.save(students);
        return studentsMapper.toDto(students);
    }

    @Override
    public Optional<StudentsDTO> partialUpdate(StudentsDTO studentsDTO) {
        log.debug("Request to partially update Students : {}", studentsDTO);

        return studentsRepository
            .findById(studentsDTO.getId())
            .map(existingStudents -> {
                studentsMapper.partialUpdate(existingStudents, studentsDTO);

                return existingStudents;
            })
            .map(studentsRepository::save)
            .map(studentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentsRepository.findAll(pageable).map(studentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentsDTO> findOne(Long id) {
        log.debug("Request to get Students : {}", id);
        var student =  studentsRepository.findById(id);
        return student.map(studentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Students : {}", id);
        studentsRepository.deleteById(id);
    }
}
