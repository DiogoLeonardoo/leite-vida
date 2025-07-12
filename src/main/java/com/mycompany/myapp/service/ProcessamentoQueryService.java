package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.criteria.ProcessamentoCriteria;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import com.mycompany.myapp.service.mapper.ProcessamentoMapper;
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
 * Service for executing complex queries for {@link Processamento} entities in the database.
 * The main input is a {@link ProcessamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProcessamentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcessamentoQueryService extends QueryService<Processamento> {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessamentoQueryService.class);

    private final ProcessamentoRepository processamentoRepository;

    private final ProcessamentoMapper processamentoMapper;

    public ProcessamentoQueryService(ProcessamentoRepository processamentoRepository, ProcessamentoMapper processamentoMapper) {
        this.processamentoRepository = processamentoRepository;
        this.processamentoMapper = processamentoMapper;
    }

    /**
     * Return a {@link Page} of {@link ProcessamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessamentoDTO> findByCriteria(ProcessamentoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Processamento> specification = createSpecification(criteria);
        return processamentoRepository.findAll(specification, page).map(processamentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessamentoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Processamento> specification = createSpecification(criteria);
        return processamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Processamento> createSpecification(ProcessamentoCriteria criteria) {
        Specification<Processamento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Processamento_.id),
                buildRangeSpecification(criteria.getDataProcessamento(), Processamento_.dataProcessamento),
                buildStringSpecification(criteria.getTecnicoResponsavel(), Processamento_.tecnicoResponsavel),
                buildRangeSpecification(criteria.getValorAcidezDornic(), Processamento_.valorAcidezDornic),
                buildRangeSpecification(criteria.getValorCaloricoKcal(), Processamento_.valorCaloricoKcal),
                buildSpecification(criteria.getResultadoAnalise(), Processamento_.resultadoAnalise),
                buildSpecification(criteria.getStatusProcessamento(), Processamento_.statusProcessamento),
                buildSpecification(criteria.getEstoqueId(), root -> root.join(Processamento_.estoque, JoinType.LEFT).get(Estoque_.id)),
                buildSpecification(criteria.getColetaId(), root -> root.join(Processamento_.coleta, JoinType.LEFT).get(Coleta_.id))
            );
        }
        return specification;
    }
}
