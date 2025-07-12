package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DoadoraRepository;
import com.mycompany.myapp.service.DoadoraQueryService;
import com.mycompany.myapp.service.DoadoraService;
import com.mycompany.myapp.service.criteria.DoadoraCriteria;
import com.mycompany.myapp.service.dto.DoadoraDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Doadora}.
 */
@RestController
@RequestMapping("/api/doadoras")
public class DoadoraResource {

    private static final Logger LOG = LoggerFactory.getLogger(DoadoraResource.class);

    private static final String ENTITY_NAME = "doadora";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoadoraService doadoraService;

    private final DoadoraRepository doadoraRepository;

    private final DoadoraQueryService doadoraQueryService;

    public DoadoraResource(DoadoraService doadoraService, DoadoraRepository doadoraRepository, DoadoraQueryService doadoraQueryService) {
        this.doadoraService = doadoraService;
        this.doadoraRepository = doadoraRepository;
        this.doadoraQueryService = doadoraQueryService;
    }

    /**
     * {@code POST  /doadoras} : Create a new doadora.
     *
     * @param doadoraDTO the doadoraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doadoraDTO, or with status {@code 400 (Bad Request)} if the doadora has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DoadoraDTO> createDoadora(@Valid @RequestBody DoadoraDTO doadoraDTO) throws URISyntaxException {
        LOG.debug("REST request to save Doadora : {}", doadoraDTO);
        if (doadoraDTO.getId() != null) {
            throw new BadRequestAlertException("A new doadora cannot already have an ID", ENTITY_NAME, "idexists");
        }
        doadoraDTO = doadoraService.save(doadoraDTO);
        return ResponseEntity.created(new URI("/api/doadoras/" + doadoraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, doadoraDTO.getId().toString()))
            .body(doadoraDTO);
    }

    /**
     * {@code PUT  /doadoras/:id} : Updates an existing doadora.
     *
     * @param id the id of the doadoraDTO to save.
     * @param doadoraDTO the doadoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doadoraDTO,
     * or with status {@code 400 (Bad Request)} if the doadoraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doadoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoadoraDTO> updateDoadora(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoadoraDTO doadoraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Doadora : {}, {}", id, doadoraDTO);
        if (doadoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doadoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doadoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        doadoraDTO = doadoraService.update(doadoraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doadoraDTO.getId().toString()))
            .body(doadoraDTO);
    }

    /**
     * {@code PATCH  /doadoras/:id} : Partial updates given fields of an existing doadora, field will ignore if it is null
     *
     * @param id the id of the doadoraDTO to save.
     * @param doadoraDTO the doadoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doadoraDTO,
     * or with status {@code 400 (Bad Request)} if the doadoraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the doadoraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the doadoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoadoraDTO> partialUpdateDoadora(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoadoraDTO doadoraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Doadora partially : {}, {}", id, doadoraDTO);
        if (doadoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doadoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doadoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoadoraDTO> result = doadoraService.partialUpdate(doadoraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doadoraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doadoras} : get all the doadoras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doadoras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DoadoraDTO>> getAllDoadoras(
        DoadoraCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Doadoras by criteria: {}", criteria);

        Page<DoadoraDTO> page = doadoraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doadoras/count} : count all the doadoras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDoadoras(DoadoraCriteria criteria) {
        LOG.debug("REST request to count Doadoras by criteria: {}", criteria);
        return ResponseEntity.ok().body(doadoraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doadoras/:id} : get the "id" doadora.
     *
     * @param id the id of the doadoraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doadoraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoadoraDTO> getDoadora(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Doadora : {}", id);
        Optional<DoadoraDTO> doadoraDTO = doadoraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doadoraDTO);
    }

    /**
     * {@code DELETE  /doadoras/:id} : delete the "id" doadora.
     *
     * @param id the id of the doadoraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoadora(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Doadora : {}", id);
        doadoraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
