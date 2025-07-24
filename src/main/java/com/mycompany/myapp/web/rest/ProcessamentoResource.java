package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.ProcessamentoQueryService;
import com.mycompany.myapp.service.ProcessamentoService;
import com.mycompany.myapp.service.criteria.ProcessamentoCriteria;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Processamento}.
 */
@RestController
@RequestMapping("/api/processamentos")
public class ProcessamentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessamentoResource.class);

    private static final String ENTITY_NAME = "processamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessamentoService processamentoService;

    private final ProcessamentoRepository processamentoRepository;

    private final ProcessamentoQueryService processamentoQueryService;

    public ProcessamentoResource(
        ProcessamentoService processamentoService,
        ProcessamentoRepository processamentoRepository,
        ProcessamentoQueryService processamentoQueryService
    ) {
        this.processamentoService = processamentoService;
        this.processamentoRepository = processamentoRepository;
        this.processamentoQueryService = processamentoQueryService;
    }

    /**
     * {@code POST  /processamentos} : Create a new processamento.
     *
     * @param processamentoDTO the processamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processamentoDTO, or with status {@code 400 (Bad Request)} if the processamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessamentoDTO> createProcessamento(@Valid @RequestBody ProcessamentoDTO processamentoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Processamento : {}", processamentoDTO);
        if (processamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new processamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        processamentoDTO = processamentoService.save(processamentoDTO);
        return ResponseEntity.created(new URI("/api/processamentos/" + processamentoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, processamentoDTO.getId().toString()))
            .body(processamentoDTO);
    }

    /**
     * {@code PUT  /processamentos/:id} : Updates an existing processamento.
     *
     * @param id the id of the processamentoDTO to save.
     * @param processamentoDTO the processamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processamentoDTO,
     * or with status {@code 400 (Bad Request)} if the processamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessamentoDTO> updateProcessamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProcessamentoDTO processamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Processamento : {}, {}", id, processamentoDTO);
        if (processamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        processamentoDTO = processamentoService.update(processamentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processamentoDTO.getId().toString()))
            .body(processamentoDTO);
    }

    /**
     * {@code PATCH  /processamentos/:id} : Partial updates given fields of an existing processamento, field will ignore if it is null
     *
     * @param id the id of the processamentoDTO to save.
     * @param processamentoDTO the processamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processamentoDTO,
     * or with status {@code 400 (Bad Request)} if the processamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessamentoDTO> partialUpdateProcessamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProcessamentoDTO processamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Processamento partially : {}, {}", id, processamentoDTO);
        if (processamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessamentoDTO> result = processamentoService.partialUpdate(processamentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processamentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /processamentos} : get all the processamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessamentoDTO>> getAllProcessamentos(
        ProcessamentoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Processamentos by criteria: {}", criteria);

        Page<ProcessamentoDTO> page = processamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processamentos/count} : count all the processamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProcessamentos(ProcessamentoCriteria criteria) {
        LOG.debug("REST request to count Processamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(processamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /processamentos/:id} : get the "id" processamento.
     *
     * @param id the id of the processamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessamentoDTO> getProcessamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Processamento : {}", id);
        Optional<ProcessamentoDTO> processamentoDTO = processamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processamentoDTO);
    }

    /**
     * {@code DELETE  /processamentos/:id} : delete the "id" processamento.
     *
     * @param id the id of the processamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Processamento : {}", id);
        processamentoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /processamentos/com-estoque} : Create a new processamento with estoque.
     *
     * @param processamentoDTO the processamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processamentoDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/com-estoque")
    public ResponseEntity<ProcessamentoDTO> createProcessamentoComEstoque(@Valid @RequestBody ProcessamentoDTO processamentoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Processamento with Estoque : {}", processamentoDTO);
        if (processamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new processamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        processamentoDTO = processamentoService.criarProcessamentoComEstoque(processamentoDTO);
        return ResponseEntity.created(new URI("/api/processamentos/" + processamentoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, processamentoDTO.getId().toString()))
            .body(processamentoDTO);
    }
}
