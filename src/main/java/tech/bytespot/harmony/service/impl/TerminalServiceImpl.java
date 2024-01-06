package tech.bytespot.harmony.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.repository.TerminalRepository;
import tech.bytespot.harmony.service.TerminalService;
import tech.bytespot.harmony.service.dto.TerminalDTO;
import tech.bytespot.harmony.service.mapper.TerminalMapper;

/**
 * Service Implementation for managing {@link tech.bytespot.harmony.domain.Terminal}.
 */
@Service
@Transactional
public class TerminalServiceImpl implements TerminalService {

    private final Logger log = LoggerFactory.getLogger(TerminalServiceImpl.class);

    private final TerminalRepository terminalRepository;

    private final TerminalMapper terminalMapper;

    public TerminalServiceImpl(TerminalRepository terminalRepository, TerminalMapper terminalMapper) {
        this.terminalRepository = terminalRepository;
        this.terminalMapper = terminalMapper;
    }

    @Override
    public TerminalDTO save(TerminalDTO terminalDTO) {
        log.debug("Request to save Terminal : {}", terminalDTO);
        Terminal terminal = terminalMapper.toEntity(terminalDTO);
        terminal = terminalRepository.save(terminal);
        return terminalMapper.toDto(terminal);
    }

    @Override
    public TerminalDTO update(TerminalDTO terminalDTO) {
        log.debug("Request to update Terminal : {}", terminalDTO);
        Terminal terminal = terminalMapper.toEntity(terminalDTO);
        terminal = terminalRepository.save(terminal);
        return terminalMapper.toDto(terminal);
    }

    @Override
    public Optional<TerminalDTO> partialUpdate(TerminalDTO terminalDTO) {
        log.debug("Request to partially update Terminal : {}", terminalDTO);

        return terminalRepository
            .findById(terminalDTO.getId())
            .map(existingTerminal -> {
                terminalMapper.partialUpdate(existingTerminal, terminalDTO);

                return existingTerminal;
            })
            .map(terminalRepository::save)
            .map(terminalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Terminals");
        return terminalRepository.findAll(pageable).map(terminalMapper::toDto);
    }

    /**
     *  Get all the terminals where Fleet is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TerminalDTO> findAllWhereFleetIsNull() {
        log.debug("Request to get all terminals where Fleet is null");
        return StreamSupport
            .stream(terminalRepository.findAll().spliterator(), false)
            .filter(terminal -> terminal.getFleet() == null)
            .map(terminalMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalDTO> findOne(Long id) {
        log.debug("Request to get Terminal : {}", id);
        return terminalRepository.findById(id).map(terminalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Terminal : {}", id);
        terminalRepository.deleteById(id);
    }
}
