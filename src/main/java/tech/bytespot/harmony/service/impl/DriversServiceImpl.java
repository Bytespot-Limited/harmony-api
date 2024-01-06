package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Drivers;
import tech.bytespot.harmony.repository.DriversRepository;
import tech.bytespot.harmony.service.DriversService;
import tech.bytespot.harmony.service.dto.DriversDTO;
import tech.bytespot.harmony.service.mapper.DriversMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Drivers}.
 */
@Service
@Transactional
public class DriversServiceImpl implements DriversService {

    private final Logger log = LoggerFactory.getLogger(DriversServiceImpl.class);

    private final DriversRepository driversRepository;

    private final DriversMapper driversMapper;

    public DriversServiceImpl(DriversRepository driversRepository, DriversMapper driversMapper) {
        this.driversRepository = driversRepository;
        this.driversMapper = driversMapper;
    }

    @Override
    public DriversDTO save(DriversDTO driversDTO) {
        log.debug("Request to save Drivers : {}", driversDTO);
        Drivers drivers = driversMapper.toEntity(driversDTO);
        drivers = driversRepository.save(drivers);
        return driversMapper.toDto(drivers);
    }

    @Override
    public DriversDTO update(DriversDTO driversDTO) {
        log.debug("Request to update Drivers : {}", driversDTO);
        Drivers drivers = driversMapper.toEntity(driversDTO);
        drivers = driversRepository.save(drivers);
        return driversMapper.toDto(drivers);
    }

    @Override
    public Optional<DriversDTO> partialUpdate(DriversDTO driversDTO) {
        log.debug("Request to partially update Drivers : {}", driversDTO);

        return driversRepository
            .findById(driversDTO.getId())
            .map(existingDrivers -> {
                driversMapper.partialUpdate(existingDrivers, driversDTO);

                return existingDrivers;
            })
            .map(driversRepository::save)
            .map(driversMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriversDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drivers");
        return driversRepository.findAll(pageable).map(driversMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriversDTO> findOne(Long id) {
        log.debug("Request to get Drivers : {}", id);
        return driversRepository.findById(id).map(driversMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Drivers : {}", id);
        driversRepository.deleteById(id);
    }
}
