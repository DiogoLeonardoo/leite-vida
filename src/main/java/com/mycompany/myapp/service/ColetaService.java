package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.repository.DoadoraRepository;
import com.mycompany.myapp.service.dto.ColetaDTO;
import com.mycompany.myapp.service.mapper.ColetaMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.mycompany.myapp.domain.Coleta}.
 */
@Service
@Transactional
public class ColetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ColetaService.class);

    private final ColetaRepository coletaRepository;

    private final DoadoraRepository doadoraRepository;

    private final ColetaMapper coletaMapper;

    public ColetaService(ColetaRepository coletaRepository, ColetaMapper coletaMapper, DoadoraRepository doadoraRepository) {
        this.coletaRepository = coletaRepository;
        this.coletaMapper = coletaMapper;
        this.doadoraRepository = doadoraRepository;
    }

    /**
     * Save a coleta.
     *
     * @param coletaDTO the entity to save.
     * @return the persisted entity.
     */
    public ColetaDTO save(ColetaDTO coletaDTO) {
        LOG.debug("Request to save Coleta : {}", coletaDTO);
        Coleta coleta = coletaMapper.toEntity(coletaDTO);

        if (coletaDTO.getDoadora() != null && coletaDTO.getDoadora().getId() != null) {
            Doadora doadora = doadoraRepository
                .findById(coletaDTO.getDoadora().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doadora não encontrada"));

            coleta.setDoadora(doadora);
        }

        coleta = coletaRepository.save(coleta);
        return coletaMapper.toDto(coleta);
    }

    /**
     * Update a coleta.
     *
     * @param coletaDTO the entity to save.
     * @return the persisted entity.
     */
    public ColetaDTO update(ColetaDTO coletaDTO) {
        LOG.debug("Request to update Coleta : {}", coletaDTO);
        Coleta coleta = coletaMapper.toEntity(coletaDTO);
        coleta = coletaRepository.save(coleta);
        return coletaMapper.toDto(coleta);
    }

    /**
     * Partially update a coleta.
     *
     * @param coletaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ColetaDTO> partialUpdate(ColetaDTO coletaDTO) {
        LOG.debug("Request to partially update Coleta : {}", coletaDTO);

        return coletaRepository
            .findById(coletaDTO.getId())
            .map(existingColeta -> {
                coletaMapper.partialUpdate(existingColeta, coletaDTO);

                return existingColeta;
            })
            .map(coletaRepository::save)
            .map(coletaMapper::toDto);
    }

    /**
     * Get one coleta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ColetaDTO> findOne(Long id) {
        LOG.debug("Request to get Coleta : {}", id);
        return coletaRepository.findById(id).map(coletaMapper::toDto);
    }

    /**
     * Delete the coleta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Coleta : {}", id);
        coletaRepository.deleteById(id);
    }

    public Double getVolumeAguardandoProcessamento() {
        return coletaRepository.somarVolumeMlPorStatus();
    }

    public Page<Coleta> buscarColetasFiltradas(StatusColeta status, Long id, Pageable pageable) {
        return coletaRepository.findByStatusAndIdOptional(status, id, pageable);
    }
}
