package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import com.mycompany.myapp.service.mapper.ProcessamentoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Processamento}.
 */
@Service
@Transactional
public class ProcessamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessamentoService.class);

    private final ProcessamentoRepository processamentoRepository;

    private final ProcessamentoMapper processamentoMapper;

    public ProcessamentoService(ProcessamentoRepository processamentoRepository, ProcessamentoMapper processamentoMapper) {
        this.processamentoRepository = processamentoRepository;
        this.processamentoMapper = processamentoMapper;
    }

    /**
     * Save a processamento.
     *
     * @param processamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessamentoDTO save(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to save Processamento : {}", processamentoDTO);
        Processamento processamento = processamentoMapper.toEntity(processamentoDTO);
        processamento = processamentoRepository.save(processamento);
        return processamentoMapper.toDto(processamento);
    }

    /**
     * Update a processamento.
     *
     * @param processamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessamentoDTO update(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to update Processamento : {}", processamentoDTO);
        Processamento processamento = processamentoMapper.toEntity(processamentoDTO);
        processamento = processamentoRepository.save(processamento);
        return processamentoMapper.toDto(processamento);
    }

    /**
     * Partially update a processamento.
     *
     * @param processamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProcessamentoDTO> partialUpdate(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to partially update Processamento : {}", processamentoDTO);

        return processamentoRepository
            .findById(processamentoDTO.getId())
            .map(existingProcessamento -> {
                processamentoMapper.partialUpdate(existingProcessamento, processamentoDTO);

                return existingProcessamento;
            })
            .map(processamentoRepository::save)
            .map(processamentoMapper::toDto);
    }

    /**
     *  Get all the processamentos where Coleta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessamentoDTO> findAllWhereColetaIsNull() {
        LOG.debug("Request to get all processamentos where Coleta is null");
        return StreamSupport.stream(processamentoRepository.findAll().spliterator(), false)
            .filter(processamento -> processamento.getColeta() == null)
            .map(processamentoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one processamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessamentoDTO> findOne(Long id) {
        LOG.debug("Request to get Processamento : {}", id);
        return processamentoRepository.findById(id).map(processamentoMapper::toDto);
    }

    /**
     * Delete the processamento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Processamento : {}", id);
        processamentoRepository.deleteById(id);
    }
}
