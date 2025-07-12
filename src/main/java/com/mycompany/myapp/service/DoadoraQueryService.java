package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.repository.DoadoraRepository;
import com.mycompany.myapp.service.criteria.DoadoraCriteria;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import com.mycompany.myapp.service.mapper.DoadoraMapper;
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
 * Service for executing complex queries for {@link Doadora} entities in the database.
 * The main input is a {@link DoadoraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DoadoraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoadoraQueryService extends QueryService<Doadora> {

    private static final Logger LOG = LoggerFactory.getLogger(DoadoraQueryService.class);

    private final DoadoraRepository doadoraRepository;

    private final DoadoraMapper doadoraMapper;

    public DoadoraQueryService(DoadoraRepository doadoraRepository, DoadoraMapper doadoraMapper) {
        this.doadoraRepository = doadoraRepository;
        this.doadoraMapper = doadoraMapper;
    }

    /**
     * Return a {@link Page} of {@link DoadoraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoadoraDTO> findByCriteria(DoadoraCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doadora> specification = createSpecification(criteria);
        return doadoraRepository.findAll(specification, page).map(doadoraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoadoraCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Doadora> specification = createSpecification(criteria);
        return doadoraRepository.count(specification);
    }

    /**
     * Function to convert {@link DoadoraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Doadora> createSpecification(DoadoraCriteria criteria) {
        Specification<Doadora> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Doadora_.id),
                buildStringSpecification(criteria.getNome(), Doadora_.nome),
                buildStringSpecification(criteria.getCartaoSUS(), Doadora_.cartaoSUS),
                buildStringSpecification(criteria.getCpf(), Doadora_.cpf),
                buildRangeSpecification(criteria.getDataNascimento(), Doadora_.dataNascimento),
                buildStringSpecification(criteria.getCep(), Doadora_.cep),
                buildStringSpecification(criteria.getEstado(), Doadora_.estado),
                buildStringSpecification(criteria.getCidade(), Doadora_.cidade),
                buildStringSpecification(criteria.getEndereco(), Doadora_.endereco),
                buildStringSpecification(criteria.getTelefone(), Doadora_.telefone),
                buildStringSpecification(criteria.getProfissao(), Doadora_.profissao),
                buildSpecification(criteria.getTipoDoadora(), Doadora_.tipoDoadora),
                buildSpecification(criteria.getLocalPreNatal(), Doadora_.localPreNatal),
                buildSpecification(criteria.getResultadoVDRL(), Doadora_.resultadoVDRL),
                buildSpecification(criteria.getResultadoHBsAg(), Doadora_.resultadoHBsAg),
                buildSpecification(criteria.getResultadoFTAabs(), Doadora_.resultadoFTAabs),
                buildSpecification(criteria.getResultadoHIV(), Doadora_.resultadoHIV),
                buildSpecification(criteria.getTransfusaoUltimos5Anos(), Doadora_.transfusaoUltimos5Anos),
                buildRangeSpecification(criteria.getDataRegistro(), Doadora_.dataRegistro),
                buildSpecification(criteria.getColetasId(), root -> root.join(Doadora_.coletas, JoinType.LEFT).get(Coleta_.id))
            );
        }
        return specification;
    }
}
