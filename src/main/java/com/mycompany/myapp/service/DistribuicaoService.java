package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Distribuicao;
import com.mycompany.myapp.repository.DistribuicaoRepository;
import com.mycompany.myapp.service.dto.DistribuicaoDTO;
import com.mycompany.myapp.service.mapper.DistribuicaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Distribuicao}.
 */
@Service
@Transactional
public class DistribuicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(DistribuicaoService.class);

    private final DistribuicaoRepository distribuicaoRepository;

    private final DistribuicaoMapper distribuicaoMapper;

    public DistribuicaoService(DistribuicaoRepository distribuicaoRepository, DistribuicaoMapper distribuicaoMapper) {
        this.distribuicaoRepository = distribuicaoRepository;
        this.distribuicaoMapper = distribuicaoMapper;
    }

    /**
     * Save a distribuicao.
     *
     * @param distribuicaoDTO the entity to save.
     * @return the persisted entity.
     */
    public DistribuicaoDTO save(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to save Distribuicao : {}", distribuicaoDTO);
        Distribuicao distribuicao = distribuicaoMapper.toEntity(distribuicaoDTO);
        distribuicao = distribuicaoRepository.save(distribuicao);
        return distribuicaoMapper.toDto(distribuicao);
    }

    /**
     * Update a distribuicao.
     *
     * @param distribuicaoDTO the entity to save.
     * @return the persisted entity.
     */
    public DistribuicaoDTO update(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to update Distribuicao : {}", distribuicaoDTO);
        Distribuicao distribuicao = distribuicaoMapper.toEntity(distribuicaoDTO);
        distribuicao = distribuicaoRepository.save(distribuicao);
        return distribuicaoMapper.toDto(distribuicao);
    }

    /**
     * Partially update a distribuicao.
     *
     * @param distribuicaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DistribuicaoDTO> partialUpdate(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to partially update Distribuicao : {}", distribuicaoDTO);

        return distribuicaoRepository
            .findById(distribuicaoDTO.getId())
            .map(existingDistribuicao -> {
                distribuicaoMapper.partialUpdate(existingDistribuicao, distribuicaoDTO);

                return existingDistribuicao;
            })
            .map(distribuicaoRepository::save)
            .map(distribuicaoMapper::toDto);
    }

    /**
     * Get one distribuicao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DistribuicaoDTO> findOne(Long id) {
        LOG.debug("Request to get Distribuicao : {}", id);
        return distribuicaoRepository.findById(id).map(distribuicaoMapper::toDto);
    }

    /**
     * Delete the distribuicao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Distribuicao : {}", id);
        distribuicaoRepository.deleteById(id);
    }
}
