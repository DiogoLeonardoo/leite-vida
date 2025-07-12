package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProcessamentoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.domain.enumeration.ResultadoAnalise;
import com.mycompany.myapp.domain.enumeration.StatusProcessamento;
import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import com.mycompany.myapp.service.mapper.ProcessamentoMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProcessamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessamentoResourceIT {

    private static final LocalDate DEFAULT_DATA_PROCESSAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PROCESSAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_PROCESSAMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TECNICO_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_TECNICO_RESPONSAVEL = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR_ACIDEZ_DORNIC = 1D;
    private static final Double UPDATED_VALOR_ACIDEZ_DORNIC = 2D;
    private static final Double SMALLER_VALOR_ACIDEZ_DORNIC = 1D - 1D;

    private static final Double DEFAULT_VALOR_CALORICO_KCAL = 1D;
    private static final Double UPDATED_VALOR_CALORICO_KCAL = 2D;
    private static final Double SMALLER_VALOR_CALORICO_KCAL = 1D - 1D;

    private static final ResultadoAnalise DEFAULT_RESULTADO_ANALISE = ResultadoAnalise.APROVADO;
    private static final ResultadoAnalise UPDATED_RESULTADO_ANALISE = ResultadoAnalise.REPROVADO;

    private static final StatusProcessamento DEFAULT_STATUS_PROCESSAMENTO = StatusProcessamento.EM_ANALISE;
    private static final StatusProcessamento UPDATED_STATUS_PROCESSAMENTO = StatusProcessamento.CONCLUIDO;

    private static final String ENTITY_API_URL = "/api/processamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcessamentoRepository processamentoRepository;

    @Autowired
    private ProcessamentoMapper processamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessamentoMockMvc;

    private Processamento processamento;

    private Processamento insertedProcessamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processamento createEntity(EntityManager em) {
        Processamento processamento = new Processamento()
            .dataProcessamento(DEFAULT_DATA_PROCESSAMENTO)
            .tecnicoResponsavel(DEFAULT_TECNICO_RESPONSAVEL)
            .valorAcidezDornic(DEFAULT_VALOR_ACIDEZ_DORNIC)
            .valorCaloricoKcal(DEFAULT_VALOR_CALORICO_KCAL)
            .resultadoAnalise(DEFAULT_RESULTADO_ANALISE)
            .statusProcessamento(DEFAULT_STATUS_PROCESSAMENTO);
        return processamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processamento createUpdatedEntity(EntityManager em) {
        Processamento updatedProcessamento = new Processamento()
            .dataProcessamento(UPDATED_DATA_PROCESSAMENTO)
            .tecnicoResponsavel(UPDATED_TECNICO_RESPONSAVEL)
            .valorAcidezDornic(UPDATED_VALOR_ACIDEZ_DORNIC)
            .valorCaloricoKcal(UPDATED_VALOR_CALORICO_KCAL)
            .resultadoAnalise(UPDATED_RESULTADO_ANALISE)
            .statusProcessamento(UPDATED_STATUS_PROCESSAMENTO);
        return updatedProcessamento;
    }

    @BeforeEach
    void initTest() {
        processamento = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProcessamento != null) {
            processamentoRepository.delete(insertedProcessamento);
            insertedProcessamento = null;
        }
    }

    @Test
    @Transactional
    void createProcessamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);
        var returnedProcessamentoDTO = om.readValue(
            restProcessamentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProcessamentoDTO.class
        );

        // Validate the Processamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProcessamento = processamentoMapper.toEntity(returnedProcessamentoDTO);
        assertProcessamentoUpdatableFieldsEquals(returnedProcessamento, getPersistedProcessamento(returnedProcessamento));

        insertedProcessamento = returnedProcessamento;
    }

    @Test
    @Transactional
    void createProcessamentoWithExistingId() throws Exception {
        // Create the Processamento with an existing ID
        processamento.setId(1L);
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataProcessamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        processamento.setDataProcessamento(null);

        // Create the Processamento, which fails.
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        restProcessamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTecnicoResponsavelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        processamento.setTecnicoResponsavel(null);

        // Create the Processamento, which fails.
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        restProcessamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResultadoAnaliseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        processamento.setResultadoAnalise(null);

        // Create the Processamento, which fails.
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        restProcessamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusProcessamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        processamento.setStatusProcessamento(null);

        // Create the Processamento, which fails.
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        restProcessamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessamentos() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProcessamento").value(hasItem(DEFAULT_DATA_PROCESSAMENTO.toString())))
            .andExpect(jsonPath("$.[*].tecnicoResponsavel").value(hasItem(DEFAULT_TECNICO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].valorAcidezDornic").value(hasItem(DEFAULT_VALOR_ACIDEZ_DORNIC)))
            .andExpect(jsonPath("$.[*].valorCaloricoKcal").value(hasItem(DEFAULT_VALOR_CALORICO_KCAL)))
            .andExpect(jsonPath("$.[*].resultadoAnalise").value(hasItem(DEFAULT_RESULTADO_ANALISE.toString())))
            .andExpect(jsonPath("$.[*].statusProcessamento").value(hasItem(DEFAULT_STATUS_PROCESSAMENTO.toString())));
    }

    @Test
    @Transactional
    void getProcessamento() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get the processamento
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, processamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processamento.getId().intValue()))
            .andExpect(jsonPath("$.dataProcessamento").value(DEFAULT_DATA_PROCESSAMENTO.toString()))
            .andExpect(jsonPath("$.tecnicoResponsavel").value(DEFAULT_TECNICO_RESPONSAVEL))
            .andExpect(jsonPath("$.valorAcidezDornic").value(DEFAULT_VALOR_ACIDEZ_DORNIC))
            .andExpect(jsonPath("$.valorCaloricoKcal").value(DEFAULT_VALOR_CALORICO_KCAL))
            .andExpect(jsonPath("$.resultadoAnalise").value(DEFAULT_RESULTADO_ANALISE.toString()))
            .andExpect(jsonPath("$.statusProcessamento").value(DEFAULT_STATUS_PROCESSAMENTO.toString()));
    }

    @Test
    @Transactional
    void getProcessamentosByIdFiltering() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        Long id = processamento.getId();

        defaultProcessamentoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProcessamentoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProcessamentoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento equals to
        defaultProcessamentoFiltering(
            "dataProcessamento.equals=" + DEFAULT_DATA_PROCESSAMENTO,
            "dataProcessamento.equals=" + UPDATED_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento in
        defaultProcessamentoFiltering(
            "dataProcessamento.in=" + DEFAULT_DATA_PROCESSAMENTO + "," + UPDATED_DATA_PROCESSAMENTO,
            "dataProcessamento.in=" + UPDATED_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento is not null
        defaultProcessamentoFiltering("dataProcessamento.specified=true", "dataProcessamento.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento is greater than or equal to
        defaultProcessamentoFiltering(
            "dataProcessamento.greaterThanOrEqual=" + DEFAULT_DATA_PROCESSAMENTO,
            "dataProcessamento.greaterThanOrEqual=" + UPDATED_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento is less than or equal to
        defaultProcessamentoFiltering(
            "dataProcessamento.lessThanOrEqual=" + DEFAULT_DATA_PROCESSAMENTO,
            "dataProcessamento.lessThanOrEqual=" + SMALLER_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento is less than
        defaultProcessamentoFiltering(
            "dataProcessamento.lessThan=" + UPDATED_DATA_PROCESSAMENTO,
            "dataProcessamento.lessThan=" + DEFAULT_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByDataProcessamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where dataProcessamento is greater than
        defaultProcessamentoFiltering(
            "dataProcessamento.greaterThan=" + SMALLER_DATA_PROCESSAMENTO,
            "dataProcessamento.greaterThan=" + DEFAULT_DATA_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByTecnicoResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where tecnicoResponsavel equals to
        defaultProcessamentoFiltering(
            "tecnicoResponsavel.equals=" + DEFAULT_TECNICO_RESPONSAVEL,
            "tecnicoResponsavel.equals=" + UPDATED_TECNICO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByTecnicoResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where tecnicoResponsavel in
        defaultProcessamentoFiltering(
            "tecnicoResponsavel.in=" + DEFAULT_TECNICO_RESPONSAVEL + "," + UPDATED_TECNICO_RESPONSAVEL,
            "tecnicoResponsavel.in=" + UPDATED_TECNICO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByTecnicoResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where tecnicoResponsavel is not null
        defaultProcessamentoFiltering("tecnicoResponsavel.specified=true", "tecnicoResponsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByTecnicoResponsavelContainsSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where tecnicoResponsavel contains
        defaultProcessamentoFiltering(
            "tecnicoResponsavel.contains=" + DEFAULT_TECNICO_RESPONSAVEL,
            "tecnicoResponsavel.contains=" + UPDATED_TECNICO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByTecnicoResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where tecnicoResponsavel does not contain
        defaultProcessamentoFiltering(
            "tecnicoResponsavel.doesNotContain=" + UPDATED_TECNICO_RESPONSAVEL,
            "tecnicoResponsavel.doesNotContain=" + DEFAULT_TECNICO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic equals to
        defaultProcessamentoFiltering(
            "valorAcidezDornic.equals=" + DEFAULT_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.equals=" + UPDATED_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic in
        defaultProcessamentoFiltering(
            "valorAcidezDornic.in=" + DEFAULT_VALOR_ACIDEZ_DORNIC + "," + UPDATED_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.in=" + UPDATED_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic is not null
        defaultProcessamentoFiltering("valorAcidezDornic.specified=true", "valorAcidezDornic.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic is greater than or equal to
        defaultProcessamentoFiltering(
            "valorAcidezDornic.greaterThanOrEqual=" + DEFAULT_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.greaterThanOrEqual=" + UPDATED_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic is less than or equal to
        defaultProcessamentoFiltering(
            "valorAcidezDornic.lessThanOrEqual=" + DEFAULT_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.lessThanOrEqual=" + SMALLER_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic is less than
        defaultProcessamentoFiltering(
            "valorAcidezDornic.lessThan=" + UPDATED_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.lessThan=" + DEFAULT_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorAcidezDornicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorAcidezDornic is greater than
        defaultProcessamentoFiltering(
            "valorAcidezDornic.greaterThan=" + SMALLER_VALOR_ACIDEZ_DORNIC,
            "valorAcidezDornic.greaterThan=" + DEFAULT_VALOR_ACIDEZ_DORNIC
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal equals to
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.equals=" + DEFAULT_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.equals=" + UPDATED_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal in
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.in=" + DEFAULT_VALOR_CALORICO_KCAL + "," + UPDATED_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.in=" + UPDATED_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal is not null
        defaultProcessamentoFiltering("valorCaloricoKcal.specified=true", "valorCaloricoKcal.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal is greater than or equal to
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.greaterThanOrEqual=" + DEFAULT_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.greaterThanOrEqual=" + UPDATED_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal is less than or equal to
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.lessThanOrEqual=" + DEFAULT_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.lessThanOrEqual=" + SMALLER_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal is less than
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.lessThan=" + UPDATED_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.lessThan=" + DEFAULT_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByValorCaloricoKcalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where valorCaloricoKcal is greater than
        defaultProcessamentoFiltering(
            "valorCaloricoKcal.greaterThan=" + SMALLER_VALOR_CALORICO_KCAL,
            "valorCaloricoKcal.greaterThan=" + DEFAULT_VALOR_CALORICO_KCAL
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByResultadoAnaliseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where resultadoAnalise equals to
        defaultProcessamentoFiltering(
            "resultadoAnalise.equals=" + DEFAULT_RESULTADO_ANALISE,
            "resultadoAnalise.equals=" + UPDATED_RESULTADO_ANALISE
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByResultadoAnaliseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where resultadoAnalise in
        defaultProcessamentoFiltering(
            "resultadoAnalise.in=" + DEFAULT_RESULTADO_ANALISE + "," + UPDATED_RESULTADO_ANALISE,
            "resultadoAnalise.in=" + UPDATED_RESULTADO_ANALISE
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByResultadoAnaliseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where resultadoAnalise is not null
        defaultProcessamentoFiltering("resultadoAnalise.specified=true", "resultadoAnalise.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByStatusProcessamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where statusProcessamento equals to
        defaultProcessamentoFiltering(
            "statusProcessamento.equals=" + DEFAULT_STATUS_PROCESSAMENTO,
            "statusProcessamento.equals=" + UPDATED_STATUS_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByStatusProcessamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where statusProcessamento in
        defaultProcessamentoFiltering(
            "statusProcessamento.in=" + DEFAULT_STATUS_PROCESSAMENTO + "," + UPDATED_STATUS_PROCESSAMENTO,
            "statusProcessamento.in=" + UPDATED_STATUS_PROCESSAMENTO
        );
    }

    @Test
    @Transactional
    void getAllProcessamentosByStatusProcessamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        // Get all the processamentoList where statusProcessamento is not null
        defaultProcessamentoFiltering("statusProcessamento.specified=true", "statusProcessamento.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessamentosByEstoqueIsEqualToSomething() throws Exception {
        Estoque estoque;
        if (TestUtil.findAll(em, Estoque.class).isEmpty()) {
            processamentoRepository.saveAndFlush(processamento);
            estoque = EstoqueResourceIT.createEntity(em);
        } else {
            estoque = TestUtil.findAll(em, Estoque.class).get(0);
        }
        em.persist(estoque);
        em.flush();
        processamento.setEstoque(estoque);
        processamentoRepository.saveAndFlush(processamento);
        Long estoqueId = estoque.getId();
        // Get all the processamentoList where estoque equals to estoqueId
        defaultProcessamentoShouldBeFound("estoqueId.equals=" + estoqueId);

        // Get all the processamentoList where estoque equals to (estoqueId + 1)
        defaultProcessamentoShouldNotBeFound("estoqueId.equals=" + (estoqueId + 1));
    }

    private void defaultProcessamentoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProcessamentoShouldBeFound(shouldBeFound);
        defaultProcessamentoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcessamentoShouldBeFound(String filter) throws Exception {
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProcessamento").value(hasItem(DEFAULT_DATA_PROCESSAMENTO.toString())))
            .andExpect(jsonPath("$.[*].tecnicoResponsavel").value(hasItem(DEFAULT_TECNICO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].valorAcidezDornic").value(hasItem(DEFAULT_VALOR_ACIDEZ_DORNIC)))
            .andExpect(jsonPath("$.[*].valorCaloricoKcal").value(hasItem(DEFAULT_VALOR_CALORICO_KCAL)))
            .andExpect(jsonPath("$.[*].resultadoAnalise").value(hasItem(DEFAULT_RESULTADO_ANALISE.toString())))
            .andExpect(jsonPath("$.[*].statusProcessamento").value(hasItem(DEFAULT_STATUS_PROCESSAMENTO.toString())));

        // Check, that the count call also returns 1
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcessamentoShouldNotBeFound(String filter) throws Exception {
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcessamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProcessamento() throws Exception {
        // Get the processamento
        restProcessamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessamento() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processamento
        Processamento updatedProcessamento = processamentoRepository.findById(processamento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProcessamento are not directly saved in db
        em.detach(updatedProcessamento);
        updatedProcessamento
            .dataProcessamento(UPDATED_DATA_PROCESSAMENTO)
            .tecnicoResponsavel(UPDATED_TECNICO_RESPONSAVEL)
            .valorAcidezDornic(UPDATED_VALOR_ACIDEZ_DORNIC)
            .valorCaloricoKcal(UPDATED_VALOR_CALORICO_KCAL)
            .resultadoAnalise(UPDATED_RESULTADO_ANALISE)
            .statusProcessamento(UPDATED_STATUS_PROCESSAMENTO);
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(updatedProcessamento);

        restProcessamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProcessamentoToMatchAllProperties(updatedProcessamento);
    }

    @Test
    @Transactional
    void putNonExistingProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessamentoWithPatch() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processamento using partial update
        Processamento partialUpdatedProcessamento = new Processamento();
        partialUpdatedProcessamento.setId(processamento.getId());

        partialUpdatedProcessamento
            .dataProcessamento(UPDATED_DATA_PROCESSAMENTO)
            .valorAcidezDornic(UPDATED_VALOR_ACIDEZ_DORNIC)
            .resultadoAnalise(UPDATED_RESULTADO_ANALISE)
            .statusProcessamento(UPDATED_STATUS_PROCESSAMENTO);

        restProcessamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcessamento))
            )
            .andExpect(status().isOk());

        // Validate the Processamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProcessamento, processamento),
            getPersistedProcessamento(processamento)
        );
    }

    @Test
    @Transactional
    void fullUpdateProcessamentoWithPatch() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processamento using partial update
        Processamento partialUpdatedProcessamento = new Processamento();
        partialUpdatedProcessamento.setId(processamento.getId());

        partialUpdatedProcessamento
            .dataProcessamento(UPDATED_DATA_PROCESSAMENTO)
            .tecnicoResponsavel(UPDATED_TECNICO_RESPONSAVEL)
            .valorAcidezDornic(UPDATED_VALOR_ACIDEZ_DORNIC)
            .valorCaloricoKcal(UPDATED_VALOR_CALORICO_KCAL)
            .resultadoAnalise(UPDATED_RESULTADO_ANALISE)
            .statusProcessamento(UPDATED_STATUS_PROCESSAMENTO);

        restProcessamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcessamento))
            )
            .andExpect(status().isOk());

        // Validate the Processamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessamentoUpdatableFieldsEquals(partialUpdatedProcessamento, getPersistedProcessamento(partialUpdatedProcessamento));
    }

    @Test
    @Transactional
    void patchNonExistingProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processamento.setId(longCount.incrementAndGet());

        // Create the Processamento
        ProcessamentoDTO processamentoDTO = processamentoMapper.toDto(processamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessamentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(processamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessamento() throws Exception {
        // Initialize the database
        insertedProcessamento = processamentoRepository.saveAndFlush(processamento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the processamento
        restProcessamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, processamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return processamentoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Processamento getPersistedProcessamento(Processamento processamento) {
        return processamentoRepository.findById(processamento.getId()).orElseThrow();
    }

    protected void assertPersistedProcessamentoToMatchAllProperties(Processamento expectedProcessamento) {
        assertProcessamentoAllPropertiesEquals(expectedProcessamento, getPersistedProcessamento(expectedProcessamento));
    }

    protected void assertPersistedProcessamentoToMatchUpdatableProperties(Processamento expectedProcessamento) {
        assertProcessamentoAllUpdatablePropertiesEquals(expectedProcessamento, getPersistedProcessamento(expectedProcessamento));
    }
}
