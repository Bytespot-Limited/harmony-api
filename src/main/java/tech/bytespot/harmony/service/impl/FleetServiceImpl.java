package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.repository.FleetRepository;
import tech.bytespot.harmony.service.FleetService;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.service.mapper.FleetMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Fleet}.
 */
@Service
@Transactional
public class FleetServiceImpl implements FleetService {

    private final Logger log = LoggerFactory.getLogger(FleetServiceImpl.class);

    private final FleetRepository fleetRepository;

    private final FleetMapper fleetMapper;

    public FleetServiceImpl(FleetRepository fleetRepository, FleetMapper fleetMapper) {
        this.fleetRepository = fleetRepository;
        this.fleetMapper = fleetMapper;
    }

    @Override
    public FleetDTO save(FleetDTO fleetDTO) {
        log.debug("Request to save Fleet : {}", fleetDTO);
        Fleet fleet = fleetMapper.toEntity(fleetDTO);
        fleet = fleetRepository.save(fleet);
        return fleetMapper.toDto(fleet);
    }

    @Override
    public FleetDTO update(FleetDTO fleetDTO) {
        log.debug("Request to update Fleet : {}", fleetDTO);
        Fleet fleet = fleetMapper.toEntity(fleetDTO);
        fleet = fleetRepository.save(fleet);
        return fleetMapper.toDto(fleet);
    }

    @Override
    public Optional<FleetDTO> partialUpdate(FleetDTO fleetDTO) {
        log.debug("Request to partially update Fleet : {}", fleetDTO);

        return fleetRepository
            .findById(fleetDTO.getId())
            .map(existingFleet -> {
                fleetMapper.partialUpdate(existingFleet, fleetDTO);

                return existingFleet;
            })
            .map(fleetRepository::save)
            .map(fleetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FleetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fleets");
        return fleetRepository.findAll(pageable).map(fleetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FleetDTO> findOne(Long id) {
        log.debug("Request to get Fleet : {}", id);
        return fleetRepository.findById(id).map(fleetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fleet : {}", id);
        fleetRepository.deleteById(id);
    }
}
