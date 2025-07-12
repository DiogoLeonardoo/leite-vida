package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.service.criteria.ColetaCriteria;
import com.mycompany.myapp.service.dto.ColetaDTO;
import com.mycompany.myapp.service.mapper.ColetaMapper;
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
 * Service for executing complex queries for {@link Coleta} entities in the database.
 * The main input is a {@link ColetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ColetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ColetaQueryService extends QueryService<Coleta> {

    private static final Logger LOG = LoggerFactory.getLogger(ColetaQueryService.class);

    private final ColetaRepository coletaRepository;

    private final ColetaMapper coletaMapper;

    public ColetaQueryService(ColetaRepository coletaRepository, ColetaMapper coletaMapper) {
        this.coletaRepository = coletaRepository;
        this.coletaMapper = coletaMapper;
    }

    /**
     * Return a {@link Page} of {@link ColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ColetaDTO> findByCriteria(ColetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Coleta> specification = createSpecification(criteria);
        return coletaRepository.findAll(specification, page).map(coletaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ColetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Coleta> specification = createSpecification(criteria);
        return coletaRepository.count(specification);
    }

    /**
     * Function to convert {@link ColetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Coleta> createSpecification(ColetaCriteria criteria) {
        Specification<Coleta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Coleta_.id),
                buildRangeSpecification(criteria.getDataColeta(), Coleta_.dataColeta),
                buildRangeSpecification(criteria.getVolumeMl(), Coleta_.volumeMl),
                buildRangeSpecification(criteria.getTemperatura(), Coleta_.temperatura),
                buildStringSpecification(criteria.getLocalColeta(), Coleta_.localColeta),
                buildStringSpecification(criteria.getObservacoes(), Coleta_.observacoes),
                buildSpecification(criteria.getStatusColeta(), Coleta_.statusColeta),
                buildSpecification(criteria.getProcessamentoId(), root ->
                    root.join(Coleta_.processamento, JoinType.LEFT).get(Processamento_.id)
                ),
                buildSpecification(criteria.getDoadoraId(), root -> root.join(Coleta_.doadora, JoinType.LEFT).get(Doadora_.id))
            );
        }
        return specification;
    }
}
