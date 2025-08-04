package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Distribuicao;
import com.mycompany.myapp.repository.DistribuicaoRepository;
import com.mycompany.myapp.service.DistribuicaoQueryService;
import com.mycompany.myapp.service.DistribuicaoService;
import com.mycompany.myapp.service.criteria.DistribuicaoCriteria;
import com.mycompany.myapp.service.dto.DistribuicaoDTO;
import com.mycompany.myapp.service.dto.DistribuicaoDetalhesDTO;
import com.mycompany.myapp.service.dto.DistribuicaoRequestDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Distribuicao}.
 */
@RestController
@RequestMapping("/api/distribuicaos")
public class DistribuicaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DistribuicaoResource.class);

    private static final String ENTITY_NAME = "distribuicao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistribuicaoService distribuicaoService;

    private final DistribuicaoRepository distribuicaoRepository;

    private final DistribuicaoQueryService distribuicaoQueryService;

    public DistribuicaoResource(
        DistribuicaoService distribuicaoService,
        DistribuicaoRepository distribuicaoRepository,
        DistribuicaoQueryService distribuicaoQueryService
    ) {
        this.distribuicaoService = distribuicaoService;
        this.distribuicaoRepository = distribuicaoRepository;
        this.distribuicaoQueryService = distribuicaoQueryService;
    }

    /**
     * {@code POST  /distribuicaos} : Create a new distribuicao.
     *
     * @param distribuicaoDTO the distribuicaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new distribuicaoDTO, or with status {@code 400 (Bad Request)} if the distribuicao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DistribuicaoDTO> createDistribuicao(@Valid @RequestBody DistribuicaoDTO distribuicaoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Distribuicao : {}", distribuicaoDTO);
        if (distribuicaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new distribuicao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        distribuicaoDTO = distribuicaoService.save(distribuicaoDTO);
        return ResponseEntity.created(new URI("/api/distribuicaos/" + distribuicaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, distribuicaoDTO.getId().toString()))
            .body(distribuicaoDTO);
    }

    /**
     * {@code PUT  /distribuicaos/:id} : Updates an existing distribuicao.
     *
     * @param id the id of the distribuicaoDTO to save.
     * @param distribuicaoDTO the distribuicaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distribuicaoDTO,
     * or with status {@code 400 (Bad Request)} if the distribuicaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the distribuicaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DistribuicaoDTO> updateDistribuicao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DistribuicaoDTO distribuicaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Distribuicao : {}, {}", id, distribuicaoDTO);
        if (distribuicaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distribuicaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distribuicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        distribuicaoDTO = distribuicaoService.update(distribuicaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distribuicaoDTO.getId().toString()))
            .body(distribuicaoDTO);
    }

    /**
     * {@code PATCH  /distribuicaos/:id} : Partial updates given fields of an existing distribuicao, field will ignore if it is null
     *
     * @param id the id of the distribuicaoDTO to save.
     * @param distribuicaoDTO the distribuicaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distribuicaoDTO,
     * or with status {@code 400 (Bad Request)} if the distribuicaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the distribuicaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the distribuicaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DistribuicaoDTO> partialUpdateDistribuicao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DistribuicaoDTO distribuicaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Distribuicao partially : {}, {}", id, distribuicaoDTO);
        if (distribuicaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distribuicaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distribuicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DistribuicaoDTO> result = distribuicaoService.partialUpdate(distribuicaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distribuicaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /distribuicaos} : get all the distribuicaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of distribuicaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DistribuicaoDTO>> getAllDistribuicaos(
        DistribuicaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Distribuicaos by criteria: {}", criteria);

        Page<DistribuicaoDTO> page = distribuicaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /distribuicaos/count} : count all the distribuicaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDistribuicaos(DistribuicaoCriteria criteria) {
        LOG.debug("REST request to count Distribuicaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(distribuicaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /distribuicaos/:id} : get the "id" distribuicao.
     *
     * @param id the id of the distribuicaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the distribuicaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistribuicaoDTO> getDistribuicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Distribuicao : {}", id);
        Optional<DistribuicaoDTO> distribuicaoDTO = distribuicaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(distribuicaoDTO);
    }

    /**
     * {@code DELETE  /distribuicaos/:id} : delete the "id" distribuicao.
     *
     * @param id the id of the distribuicaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribuicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Distribuicao : {}", id);
        distribuicaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/realizar-distribuicao")
    public ResponseEntity<String> realizarDistribuicao(@RequestBody DistribuicaoRequestDTO dto) {
        boolean sucesso = distribuicaoService.realizarDistribuicao(dto);

        if (sucesso) {
            return ResponseEntity.ok("Distribuição realizada com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Erro ao realizar a distribuição. Verifique os dados.");
        }
    }

    /**
     * {@code GET  /distribuicaos/detalhes/:id} : get details of the "id" distribuicao.
     *
     * @param id the id of the distribuicao to retrieve details.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the details DTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DistribuicaoDetalhesDTO> getDetalhesDistribuicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get details for Distribuicao : {}", id);
        DistribuicaoDetalhesDTO detalhes = distribuicaoService.buscarDetalhesDistribuicao(id);

        if (detalhes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detalhes);
    }

    /**
     * {@code GET  /distribuicaos/buscar} : search for distribuicaos.
     *
     * @param searchTerm the term to search for.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the page of distribuicaos in body.
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<Distribuicao>> buscarDistribuicoesComFiltros(
        @RequestParam(required = false) String searchTerm,
        Pageable pageable
    ) {
        Page<Distribuicao> page = distribuicaoService.buscarDistribuicoesComFiltros(searchTerm, pageable);
        return ResponseEntity.ok().body(page);
    }
}
