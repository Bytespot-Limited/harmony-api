package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.repository.GuardiansRepository;
import tech.bytespot.harmony.service.GuardiansService;
import tech.bytespot.harmony.service.dto.GuardiansDTO;
import tech.bytespot.harmony.service.mapper.GuardiansMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Guardians}.
 */
@Service
@Transactional
public class GuardiansServiceImpl implements GuardiansService {

    private final Logger log = LoggerFactory.getLogger(GuardiansServiceImpl.class);

    private final GuardiansRepository guardiansRepository;

    private final GuardiansMapper guardiansMapper;

    public GuardiansServiceImpl(GuardiansRepository guardiansRepository, GuardiansMapper guardiansMapper) {
        this.guardiansRepository = guardiansRepository;
        this.guardiansMapper = guardiansMapper;
    }

    @Override
    public GuardiansDTO save(GuardiansDTO guardiansDTO) {
        log.debug("Request to save Guardians : {}", guardiansDTO);
        Guardians guardians = guardiansMapper.toEntity(guardiansDTO);
        guardians = guardiansRepository.save(guardians);
        return guardiansMapper.toDto(guardians);
    }

    @Override
    public GuardiansDTO update(GuardiansDTO guardiansDTO) {
        log.debug("Request to update Guardians : {}", guardiansDTO);
        Guardians guardians = guardiansMapper.toEntity(guardiansDTO);
        guardians = guardiansRepository.save(guardians);
        return guardiansMapper.toDto(guardians);
    }

    @Override
    public Optional<GuardiansDTO> partialUpdate(GuardiansDTO guardiansDTO) {
        log.debug("Request to partially update Guardians : {}", guardiansDTO);

        return guardiansRepository
            .findById(guardiansDTO.getId())
            .map(existingGuardians -> {
                guardiansMapper.partialUpdate(existingGuardians, guardiansDTO);

                return existingGuardians;
            })
            .map(guardiansRepository::save)
            .map(guardiansMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuardiansDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Guardians");
        return guardiansRepository.findAll(pageable).map(guardiansMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuardiansDTO> findOne(Long id) {
        log.debug("Request to get Guardians : {}", id);
        return guardiansRepository.findById(id).map(guardiansMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guardians : {}", id);
        guardiansRepository.deleteById(id);
    }
}
