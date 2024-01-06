package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.repository.TripsRepository;
import tech.bytespot.harmony.service.TripsService;
import tech.bytespot.harmony.service.dto.TripsDTO;
import tech.bytespot.harmony.service.mapper.TripsMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Trips}.
 */
@Service
@Transactional
public class TripsServiceImpl implements TripsService {

    private final Logger log = LoggerFactory.getLogger(TripsServiceImpl.class);

    private final TripsRepository tripsRepository;

    private final TripsMapper tripsMapper;

    public TripsServiceImpl(TripsRepository tripsRepository, TripsMapper tripsMapper) {
        this.tripsRepository = tripsRepository;
        this.tripsMapper = tripsMapper;
    }

    @Override
    public TripsDTO save(TripsDTO tripsDTO) {
        log.debug("Request to save Trips : {}", tripsDTO);
        Trips trips = tripsMapper.toEntity(tripsDTO);
        trips = tripsRepository.save(trips);
        return tripsMapper.toDto(trips);
    }

    @Override
    public TripsDTO update(TripsDTO tripsDTO) {
        log.debug("Request to update Trips : {}", tripsDTO);
        Trips trips = tripsMapper.toEntity(tripsDTO);
        trips = tripsRepository.save(trips);
        return tripsMapper.toDto(trips);
    }

    @Override
    public Optional<TripsDTO> partialUpdate(TripsDTO tripsDTO) {
        log.debug("Request to partially update Trips : {}", tripsDTO);

        return tripsRepository
            .findById(tripsDTO.getId())
            .map(existingTrips -> {
                tripsMapper.partialUpdate(existingTrips, tripsDTO);

                return existingTrips;
            })
            .map(tripsRepository::save)
            .map(tripsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripsRepository.findAll(pageable).map(tripsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripsDTO> findOne(Long id) {
        log.debug("Request to get Trips : {}", id);
        return tripsRepository.findById(id).map(tripsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trips : {}", id);
        tripsRepository.deleteById(id);
    }
}
