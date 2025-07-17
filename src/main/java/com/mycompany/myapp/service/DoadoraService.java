package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.repository.DoadoraRepository;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import com.mycompany.myapp.service.mapper.DoadoraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Doadora}.
 */
@Service
@Transactional
public class DoadoraService {

    private static final Logger LOG = LoggerFactory.getLogger(DoadoraService.class);

    private final DoadoraRepository doadoraRepository;

    private final DoadoraMapper doadoraMapper;

    public DoadoraService(DoadoraRepository doadoraRepository, DoadoraMapper doadoraMapper) {
        this.doadoraRepository = doadoraRepository;
        this.doadoraMapper = doadoraMapper;
    }

    /**
     * Save a doadora.
     *
     * @param doadoraDTO the entity to save.
     * @return the persisted entity.
     */
    public DoadoraDTO save(DoadoraDTO doadoraDTO) {
        LOG.debug("Request to save Doadora : {}", doadoraDTO);
        Doadora doadora = doadoraMapper.toEntity(doadoraDTO);
        doadora = doadoraRepository.save(doadora);
        return doadoraMapper.toDto(doadora);
    }

    /**
     * Update a doadora.
     *
     * @param doadoraDTO the entity to save.
     * @return the persisted entity.
     */
    public DoadoraDTO update(DoadoraDTO doadoraDTO) {
        LOG.debug("Request to update Doadora : {}", doadoraDTO);
        Doadora doadora = doadoraMapper.toEntity(doadoraDTO);
        doadora = doadoraRepository.save(doadora);
        return doadoraMapper.toDto(doadora);
    }

    /**
     * Partially update a doadora.
     *
     * @param doadoraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoadoraDTO> partialUpdate(DoadoraDTO doadoraDTO) {
        LOG.debug("Request to partially update Doadora : {}", doadoraDTO);

        return doadoraRepository
            .findById(doadoraDTO.getId())
            .map(existingDoadora -> {
                doadoraMapper.partialUpdate(existingDoadora, doadoraDTO);

                return existingDoadora;
            })
            .map(doadoraRepository::save)
            .map(doadoraMapper::toDto);
    }

    /**
     * Get one doadora by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoadoraDTO> findOne(Long id) {
        LOG.debug("Request to get Doadora : {}", id);
        return doadoraRepository.findById(id).map(doadoraMapper::toDto);
    }

    /**
     * Delete the doadora by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Doadora : {}", id);
        doadoraRepository.deleteById(id);
    }

    public long contarDoadoras() {
        return doadoraRepository.count();
    }

    public boolean existsByCpf(String cpf) {
        return doadoraRepository.existsByCpf(cpf);
    }
}
