package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import com.mycompany.myapp.repository.EstoqueRepository;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.dto.EstoqueDoadoraDTO;
import com.mycompany.myapp.service.mapper.EstoqueMapper;
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

/**
 * Service Implementation for managing
 * {@link com.mycompany.myapp.domain.Estoque}.
 */
@Service
@Transactional
public class EstoqueService {

    private static final Logger LOG = LoggerFactory.getLogger(EstoqueService.class);

    private final EstoqueRepository estoqueRepository;

    private final EstoqueMapper estoqueMapper;

    public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    /**
     * Save a estoque.
     *
     * @param estoqueDTO the entity to save.
     * @return the persisted entity.
     */
    public EstoqueDTO save(EstoqueDTO estoqueDTO) {
        LOG.debug("Request to save Estoque : {}", estoqueDTO);
        Estoque estoque = estoqueMapper.toEntity(estoqueDTO);
        estoque = estoqueRepository.save(estoque);
        return estoqueMapper.toDto(estoque);
    }

    /**
     * Update a estoque.
     *
     * @param estoqueDTO the entity to save.
     * @return the persisted entity.
     */
    public EstoqueDTO update(EstoqueDTO estoqueDTO) {
        LOG.debug("Request to update Estoque : {}", estoqueDTO);
        Estoque estoque = estoqueMapper.toEntity(estoqueDTO);
        estoque = estoqueRepository.save(estoque);
        return estoqueMapper.toDto(estoque);
    }

    /**
     * Partially update a estoque.
     *
     * @param estoqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstoqueDTO> partialUpdate(EstoqueDTO estoqueDTO) {
        LOG.debug("Request to partially update Estoque : {}", estoqueDTO);

        return estoqueRepository
            .findById(estoqueDTO.getId())
            .map(existingEstoque -> {
                estoqueMapper.partialUpdate(existingEstoque, estoqueDTO);

                return existingEstoque;
            })
            .map(estoqueRepository::save)
            .map(estoqueMapper::toDto);
    }

    /**
     * Get all the estoques where Processamento is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EstoqueDTO> findAllWhereProcessamentoIsNull() {
        LOG.debug("Request to get all estoques where Processamento is null");
        return StreamSupport.stream(estoqueRepository.findAll().spliterator(), false)
            .filter(estoque -> estoque.getProcessamento() == null)
            .map(estoqueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one estoque by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstoqueDTO> findOne(Long id) {
        LOG.debug("Request to get Estoque : {}", id);
        return estoqueRepository.findById(id).map(estoqueMapper::toDto);
    }

    /**
     * Delete the estoque by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Estoque : {}", id);
        estoqueRepository.deleteById(id);
    }

    public Long somarVolumeDisponivelMl() {
        return estoqueRepository.somarVolumeDisponivelMl();
    }

    public Page<Estoque> buscarTodos(TipoLeite tipoLeite, ClassificacaoLeite classificacao, StatusLote statusLote, Pageable pageable) {
        return estoqueRepository.buscarComFiltros(tipoLeite, classificacao, statusLote, pageable);
    }

    public EstoqueDoadoraDTO buscarDetalhesEstoque(Long estoqueId) {
        List<Object[]> results = estoqueRepository.buscarDoadoraEstoque(estoqueId);
        if (results.isEmpty()) {
            return null;
        }
        Object[] result = results.get(0);
        return new EstoqueDoadoraDTO(
            ((Number) result[0]).longValue(),
            ((java.sql.Date) result[1]).toLocalDate(),
            (String) result[2],
            (String) result[3],
            ((Number) result[4]).doubleValue(),
            (String) result[5],
            (String) result[6],
            (String) result[7],
            (String) result[8],
            ((java.sql.Date) result[9]).toLocalDate(),
            ((Number) result[10]).doubleValue(),
            ((Number) result[11]).doubleValue()
        );
    }
}
