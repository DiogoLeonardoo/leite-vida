package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.repository.EstoqueRepository;
import com.mycompany.myapp.service.criteria.EstoqueCriteria;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.mapper.EstoqueMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Estoque} entities in the database.
 * The main input is a {@link EstoqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EstoqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstoqueQueryService extends QueryService<Estoque> {

    private static final Logger LOG = LoggerFactory.getLogger(EstoqueQueryService.class);

    private final EstoqueRepository estoqueRepository;

    private final EstoqueMapper estoqueMapper;

    public EstoqueQueryService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    /**
     * Return a {@link Page} of {@link EstoqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstoqueDTO> findByCriteria(EstoqueCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Estoque> specification = createSpecification(criteria);
        return estoqueRepository.findAll(specification, page).map(estoqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstoqueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Estoque> specification = createSpecification(criteria);
        return estoqueRepository.count(specification);
    }

    /**
     * Function to convert {@link EstoqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Estoque> createSpecification(EstoqueCriteria criteria) {
        Specification<Estoque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Estoque_.id),
                buildRangeSpecification(criteria.getDataProducao(), Estoque_.dataProducao),
                buildRangeSpecification(criteria.getDataValidade(), Estoque_.dataValidade),
                buildSpecification(criteria.getTipoLeite(), Estoque_.tipoLeite),
                buildSpecification(criteria.getClassificacao(), Estoque_.classificacao),
                buildRangeSpecification(criteria.getVolumeTotalMl(), Estoque_.volumeTotalMl),
                buildRangeSpecification(criteria.getVolumeDisponivelMl(), Estoque_.volumeDisponivelMl),
                buildStringSpecification(criteria.getLocalArmazenamento(), Estoque_.localArmazenamento),
                buildRangeSpecification(criteria.getTemperaturaArmazenamento(), Estoque_.temperaturaArmazenamento),
                buildSpecification(criteria.getStatusLote(), Estoque_.statusLote),
                buildSpecification(criteria.getDistribuicoesId(), root ->
                    root.join(Estoque_.distribuicoes, JoinType.LEFT).get(Distribuicao_.id)
                ),
                buildSpecification(criteria.getProcessamentoId(), root ->
                    root.join(Estoque_.processamento, JoinType.LEFT).get(Processamento_.id)
                )
            );
        }
        return specification;
    }
}
