package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.service.ColetaQueryService;
import com.mycompany.myapp.service.ColetaService;
import com.mycompany.myapp.service.criteria.ColetaCriteria;
import com.mycompany.myapp.service.dto.ColetaDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Coleta}.
 */
@RestController
@RequestMapping("/api/coletas")
public class ColetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ColetaResource.class);

    private static final String ENTITY_NAME = "coleta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColetaService coletaService;

    private final ColetaRepository coletaRepository;

    private final ColetaQueryService coletaQueryService;

    public ColetaResource(ColetaService coletaService, ColetaRepository coletaRepository, ColetaQueryService coletaQueryService) {
        this.coletaService = coletaService;
        this.coletaRepository = coletaRepository;
        this.coletaQueryService = coletaQueryService;
    }

    /**
     * {@code POST  /coletas} : Create a new coleta.
     *
     * @param coletaDTO the coletaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new coletaDTO, or with status {@code 400 (Bad Request)} if
     *         the coleta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ColetaDTO> createColeta(@Valid @RequestBody ColetaDTO coletaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Coleta : {}", coletaDTO);
        if (coletaDTO.getId() != null) {
            throw new BadRequestAlertException("A new coleta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coletaDTO = coletaService.save(coletaDTO);
        return ResponseEntity.created(new URI("/api/coletas/" + coletaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coletaDTO.getId().toString()))
            .body(coletaDTO);
    }

    /**
     * {@code PUT  /coletas/:id} : Updates an existing coleta.
     *
     * @param id        the id of the coletaDTO to save.
     * @param coletaDTO the coletaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated coletaDTO,
     *         or with status {@code 400 (Bad Request)} if the coletaDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the coletaDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColetaDTO> updateColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ColetaDTO coletaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Coleta : {}, {}", id, coletaDTO);
        if (coletaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coletaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coletaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coletaDTO = coletaService.update(coletaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coletaDTO.getId().toString()))
            .body(coletaDTO);
    }

    /**
     * {@code PATCH  /coletas/:id} : Partial updates given fields of an existing
     * coleta, field will ignore if it is null
     *
     * @param id        the id of the coletaDTO to save.
     * @param coletaDTO the coletaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated coletaDTO,
     *         or with status {@code 400 (Bad Request)} if the coletaDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the coletaDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the coletaDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ColetaDTO> partialUpdateColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ColetaDTO coletaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Coleta partially : {}, {}", id, coletaDTO);
        if (coletaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coletaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coletaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ColetaDTO> result = coletaService.partialUpdate(coletaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coletaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coletas} : get all the coletas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of coletas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ColetaDTO>> getAllColetas(
        ColetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Coletas by criteria: {}", criteria);

        Page<ColetaDTO> page = coletaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coletas/count} : count all the coletas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countColetas(ColetaCriteria criteria) {
        LOG.debug("REST request to count Coletas by criteria: {}", criteria);
        return ResponseEntity.ok().body(coletaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /coletas/:id} : get the "id" coleta.
     *
     * @param id the id of the coletaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the coletaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColetaDTO> getColeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Coleta : {}", id);
        Optional<ColetaDTO> coletaDTO = coletaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coletaDTO);
    }

    /**
     * {@code DELETE  /coletas/:id} : delete the "id" coleta.
     *
     * @param id the id of the coletaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Coleta : {}", id);
        coletaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/volume-aguardando-processamento")
    public Double getVolumeAguardandoProcessamento() {
        return coletaService.getVolumeAguardandoProcessamento();
    }

    @GetMapping("/buscar-coletas")
    public ResponseEntity<Page<Coleta>> listarColetas(
        @RequestParam(required = false) StatusColeta status,
        @RequestParam(required = false) Long id,
        Pageable pageable
    ) {
        Page<Coleta> resultado = coletaService.buscarColetasFiltradas(status, id, pageable);
        return ResponseEntity.ok(resultado);
    }
}
