package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ColetaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.service.dto.ColetaDTO;
import com.mycompany.myapp.service.mapper.ColetaMapper;
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
 * Integration tests for the {@link ColetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ColetaResourceIT {

    private static final LocalDate DEFAULT_DATA_COLETA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_COLETA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_COLETA = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_VOLUME_ML = 1D;
    private static final Double UPDATED_VOLUME_ML = 2D;
    private static final Double SMALLER_VOLUME_ML = 1D - 1D;

    private static final Double DEFAULT_TEMPERATURA = 1D;
    private static final Double UPDATED_TEMPERATURA = 2D;
    private static final Double SMALLER_TEMPERATURA = 1D - 1D;

    private static final String DEFAULT_LOCAL_COLETA = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_COLETA = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final StatusColeta DEFAULT_STATUS_COLETA = StatusColeta.AGUARDANDO_PROCESSAMENTO;
    private static final StatusColeta UPDATED_STATUS_COLETA = StatusColeta.PROCESSADA;

    private static final String ENTITY_API_URL = "/api/coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ColetaRepository coletaRepository;

    @Autowired
    private ColetaMapper coletaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restColetaMockMvc;

    private Coleta coleta;

    private Coleta insertedColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coleta createEntity(EntityManager em) {
        Coleta coleta = new Coleta()
            .dataColeta(DEFAULT_DATA_COLETA)
            .volumeMl(DEFAULT_VOLUME_ML)
            .temperatura(DEFAULT_TEMPERATURA)
            .localColeta(DEFAULT_LOCAL_COLETA)
            .observacoes(DEFAULT_OBSERVACOES)
            .statusColeta(DEFAULT_STATUS_COLETA);
        // Add required entity
        Doadora doadora;
        if (TestUtil.findAll(em, Doadora.class).isEmpty()) {
            doadora = DoadoraResourceIT.createEntity();
            em.persist(doadora);
            em.flush();
        } else {
            doadora = TestUtil.findAll(em, Doadora.class).get(0);
        }
        coleta.setDoadora(doadora);
        return coleta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coleta createUpdatedEntity(EntityManager em) {
        Coleta updatedColeta = new Coleta()
            .dataColeta(UPDATED_DATA_COLETA)
            .volumeMl(UPDATED_VOLUME_ML)
            .temperatura(UPDATED_TEMPERATURA)
            .localColeta(UPDATED_LOCAL_COLETA)
            .observacoes(UPDATED_OBSERVACOES)
            .statusColeta(UPDATED_STATUS_COLETA);
        // Add required entity
        Doadora doadora;
        if (TestUtil.findAll(em, Doadora.class).isEmpty()) {
            doadora = DoadoraResourceIT.createUpdatedEntity();
            em.persist(doadora);
            em.flush();
        } else {
            doadora = TestUtil.findAll(em, Doadora.class).get(0);
        }
        updatedColeta.setDoadora(doadora);
        return updatedColeta;
    }

    @BeforeEach
    void initTest() {
        coleta = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedColeta != null) {
            coletaRepository.delete(insertedColeta);
            insertedColeta = null;
        }
    }

    @Test
    @Transactional
    void createColeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);
        var returnedColetaDTO = om.readValue(
            restColetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ColetaDTO.class
        );

        // Validate the Coleta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedColeta = coletaMapper.toEntity(returnedColetaDTO);
        assertColetaUpdatableFieldsEquals(returnedColeta, getPersistedColeta(returnedColeta));

        insertedColeta = returnedColeta;
    }

    @Test
    @Transactional
    void createColetaWithExistingId() throws Exception {
        // Create the Coleta with an existing ID
        coleta.setId(1L);
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restColetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataColetaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coleta.setDataColeta(null);

        // Create the Coleta, which fails.
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        restColetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeMlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coleta.setVolumeMl(null);

        // Create the Coleta, which fails.
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        restColetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemperaturaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coleta.setTemperatura(null);

        // Create the Coleta, which fails.
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        restColetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusColetaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coleta.setStatusColeta(null);

        // Create the Coleta, which fails.
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        restColetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllColetas() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList
        restColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coleta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataColeta").value(hasItem(DEFAULT_DATA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].volumeMl").value(hasItem(DEFAULT_VOLUME_ML)))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA)))
            .andExpect(jsonPath("$.[*].localColeta").value(hasItem(DEFAULT_LOCAL_COLETA)))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].statusColeta").value(hasItem(DEFAULT_STATUS_COLETA.toString())));
    }

    @Test
    @Transactional
    void getColeta() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get the coleta
        restColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, coleta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coleta.getId().intValue()))
            .andExpect(jsonPath("$.dataColeta").value(DEFAULT_DATA_COLETA.toString()))
            .andExpect(jsonPath("$.volumeMl").value(DEFAULT_VOLUME_ML))
            .andExpect(jsonPath("$.temperatura").value(DEFAULT_TEMPERATURA))
            .andExpect(jsonPath("$.localColeta").value(DEFAULT_LOCAL_COLETA))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES))
            .andExpect(jsonPath("$.statusColeta").value(DEFAULT_STATUS_COLETA.toString()));
    }

    @Test
    @Transactional
    void getColetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        Long id = coleta.getId();

        defaultColetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultColetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultColetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta equals to
        defaultColetaFiltering("dataColeta.equals=" + DEFAULT_DATA_COLETA, "dataColeta.equals=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta in
        defaultColetaFiltering("dataColeta.in=" + DEFAULT_DATA_COLETA + "," + UPDATED_DATA_COLETA, "dataColeta.in=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta is not null
        defaultColetaFiltering("dataColeta.specified=true", "dataColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta is greater than or equal to
        defaultColetaFiltering(
            "dataColeta.greaterThanOrEqual=" + DEFAULT_DATA_COLETA,
            "dataColeta.greaterThanOrEqual=" + UPDATED_DATA_COLETA
        );
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta is less than or equal to
        defaultColetaFiltering("dataColeta.lessThanOrEqual=" + DEFAULT_DATA_COLETA, "dataColeta.lessThanOrEqual=" + SMALLER_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta is less than
        defaultColetaFiltering("dataColeta.lessThan=" + UPDATED_DATA_COLETA, "dataColeta.lessThan=" + DEFAULT_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByDataColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where dataColeta is greater than
        defaultColetaFiltering("dataColeta.greaterThan=" + SMALLER_DATA_COLETA, "dataColeta.greaterThan=" + DEFAULT_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl equals to
        defaultColetaFiltering("volumeMl.equals=" + DEFAULT_VOLUME_ML, "volumeMl.equals=" + UPDATED_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl in
        defaultColetaFiltering("volumeMl.in=" + DEFAULT_VOLUME_ML + "," + UPDATED_VOLUME_ML, "volumeMl.in=" + UPDATED_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl is not null
        defaultColetaFiltering("volumeMl.specified=true", "volumeMl.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl is greater than or equal to
        defaultColetaFiltering("volumeMl.greaterThanOrEqual=" + DEFAULT_VOLUME_ML, "volumeMl.greaterThanOrEqual=" + UPDATED_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl is less than or equal to
        defaultColetaFiltering("volumeMl.lessThanOrEqual=" + DEFAULT_VOLUME_ML, "volumeMl.lessThanOrEqual=" + SMALLER_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl is less than
        defaultColetaFiltering("volumeMl.lessThan=" + UPDATED_VOLUME_ML, "volumeMl.lessThan=" + DEFAULT_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByVolumeMlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where volumeMl is greater than
        defaultColetaFiltering("volumeMl.greaterThan=" + SMALLER_VOLUME_ML, "volumeMl.greaterThan=" + DEFAULT_VOLUME_ML);
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura equals to
        defaultColetaFiltering("temperatura.equals=" + DEFAULT_TEMPERATURA, "temperatura.equals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura in
        defaultColetaFiltering(
            "temperatura.in=" + DEFAULT_TEMPERATURA + "," + UPDATED_TEMPERATURA,
            "temperatura.in=" + UPDATED_TEMPERATURA
        );
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura is not null
        defaultColetaFiltering("temperatura.specified=true", "temperatura.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura is greater than or equal to
        defaultColetaFiltering(
            "temperatura.greaterThanOrEqual=" + DEFAULT_TEMPERATURA,
            "temperatura.greaterThanOrEqual=" + UPDATED_TEMPERATURA
        );
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura is less than or equal to
        defaultColetaFiltering("temperatura.lessThanOrEqual=" + DEFAULT_TEMPERATURA, "temperatura.lessThanOrEqual=" + SMALLER_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura is less than
        defaultColetaFiltering("temperatura.lessThan=" + UPDATED_TEMPERATURA, "temperatura.lessThan=" + DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllColetasByTemperaturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where temperatura is greater than
        defaultColetaFiltering("temperatura.greaterThan=" + SMALLER_TEMPERATURA, "temperatura.greaterThan=" + DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllColetasByLocalColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where localColeta equals to
        defaultColetaFiltering("localColeta.equals=" + DEFAULT_LOCAL_COLETA, "localColeta.equals=" + UPDATED_LOCAL_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByLocalColetaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where localColeta in
        defaultColetaFiltering(
            "localColeta.in=" + DEFAULT_LOCAL_COLETA + "," + UPDATED_LOCAL_COLETA,
            "localColeta.in=" + UPDATED_LOCAL_COLETA
        );
    }

    @Test
    @Transactional
    void getAllColetasByLocalColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where localColeta is not null
        defaultColetaFiltering("localColeta.specified=true", "localColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByLocalColetaContainsSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where localColeta contains
        defaultColetaFiltering("localColeta.contains=" + DEFAULT_LOCAL_COLETA, "localColeta.contains=" + UPDATED_LOCAL_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByLocalColetaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where localColeta does not contain
        defaultColetaFiltering("localColeta.doesNotContain=" + UPDATED_LOCAL_COLETA, "localColeta.doesNotContain=" + DEFAULT_LOCAL_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByObservacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where observacoes equals to
        defaultColetaFiltering("observacoes.equals=" + DEFAULT_OBSERVACOES, "observacoes.equals=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllColetasByObservacoesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where observacoes in
        defaultColetaFiltering(
            "observacoes.in=" + DEFAULT_OBSERVACOES + "," + UPDATED_OBSERVACOES,
            "observacoes.in=" + UPDATED_OBSERVACOES
        );
    }

    @Test
    @Transactional
    void getAllColetasByObservacoesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where observacoes is not null
        defaultColetaFiltering("observacoes.specified=true", "observacoes.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByObservacoesContainsSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where observacoes contains
        defaultColetaFiltering("observacoes.contains=" + DEFAULT_OBSERVACOES, "observacoes.contains=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllColetasByObservacoesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where observacoes does not contain
        defaultColetaFiltering("observacoes.doesNotContain=" + UPDATED_OBSERVACOES, "observacoes.doesNotContain=" + DEFAULT_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllColetasByStatusColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where statusColeta equals to
        defaultColetaFiltering("statusColeta.equals=" + DEFAULT_STATUS_COLETA, "statusColeta.equals=" + UPDATED_STATUS_COLETA);
    }

    @Test
    @Transactional
    void getAllColetasByStatusColetaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where statusColeta in
        defaultColetaFiltering(
            "statusColeta.in=" + DEFAULT_STATUS_COLETA + "," + UPDATED_STATUS_COLETA,
            "statusColeta.in=" + UPDATED_STATUS_COLETA
        );
    }

    @Test
    @Transactional
    void getAllColetasByStatusColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        // Get all the coletaList where statusColeta is not null
        defaultColetaFiltering("statusColeta.specified=true", "statusColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllColetasByProcessamentoIsEqualToSomething() throws Exception {
        Processamento processamento;
        if (TestUtil.findAll(em, Processamento.class).isEmpty()) {
            coletaRepository.saveAndFlush(coleta);
            processamento = ProcessamentoResourceIT.createEntity(em);
        } else {
            processamento = TestUtil.findAll(em, Processamento.class).get(0);
        }
        em.persist(processamento);
        em.flush();
        coleta.setProcessamento(processamento);
        coletaRepository.saveAndFlush(coleta);
        Long processamentoId = processamento.getId();
        // Get all the coletaList where processamento equals to processamentoId
        defaultColetaShouldBeFound("processamentoId.equals=" + processamentoId);

        // Get all the coletaList where processamento equals to (processamentoId + 1)
        defaultColetaShouldNotBeFound("processamentoId.equals=" + (processamentoId + 1));
    }

    @Test
    @Transactional
    void getAllColetasByDoadoraIsEqualToSomething() throws Exception {
        Doadora doadora;
        if (TestUtil.findAll(em, Doadora.class).isEmpty()) {
            coletaRepository.saveAndFlush(coleta);
            doadora = DoadoraResourceIT.createEntity();
        } else {
            doadora = TestUtil.findAll(em, Doadora.class).get(0);
        }
        em.persist(doadora);
        em.flush();
        coleta.setDoadora(doadora);
        coletaRepository.saveAndFlush(coleta);
        Long doadoraId = doadora.getId();
        // Get all the coletaList where doadora equals to doadoraId
        defaultColetaShouldBeFound("doadoraId.equals=" + doadoraId);

        // Get all the coletaList where doadora equals to (doadoraId + 1)
        defaultColetaShouldNotBeFound("doadoraId.equals=" + (doadoraId + 1));
    }

    private void defaultColetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultColetaShouldBeFound(shouldBeFound);
        defaultColetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultColetaShouldBeFound(String filter) throws Exception {
        restColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coleta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataColeta").value(hasItem(DEFAULT_DATA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].volumeMl").value(hasItem(DEFAULT_VOLUME_ML)))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA)))
            .andExpect(jsonPath("$.[*].localColeta").value(hasItem(DEFAULT_LOCAL_COLETA)))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].statusColeta").value(hasItem(DEFAULT_STATUS_COLETA.toString())));

        // Check, that the count call also returns 1
        restColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultColetaShouldNotBeFound(String filter) throws Exception {
        restColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingColeta() throws Exception {
        // Get the coleta
        restColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingColeta() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coleta
        Coleta updatedColeta = coletaRepository.findById(coleta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedColeta are not directly saved in db
        em.detach(updatedColeta);
        updatedColeta
            .dataColeta(UPDATED_DATA_COLETA)
            .volumeMl(UPDATED_VOLUME_ML)
            .temperatura(UPDATED_TEMPERATURA)
            .localColeta(UPDATED_LOCAL_COLETA)
            .observacoes(UPDATED_OBSERVACOES)
            .statusColeta(UPDATED_STATUS_COLETA);
        ColetaDTO coletaDTO = coletaMapper.toDto(updatedColeta);

        restColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coletaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedColetaToMatchAllProperties(updatedColeta);
    }

    @Test
    @Transactional
    void putNonExistingColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coletaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coletaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateColetaWithPatch() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coleta using partial update
        Coleta partialUpdatedColeta = new Coleta();
        partialUpdatedColeta.setId(coleta.getId());

        partialUpdatedColeta
            .dataColeta(UPDATED_DATA_COLETA)
            .volumeMl(UPDATED_VOLUME_ML)
            .temperatura(UPDATED_TEMPERATURA)
            .statusColeta(UPDATED_STATUS_COLETA);

        restColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColeta))
            )
            .andExpect(status().isOk());

        // Validate the Coleta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColetaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedColeta, coleta), getPersistedColeta(coleta));
    }

    @Test
    @Transactional
    void fullUpdateColetaWithPatch() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coleta using partial update
        Coleta partialUpdatedColeta = new Coleta();
        partialUpdatedColeta.setId(coleta.getId());

        partialUpdatedColeta
            .dataColeta(UPDATED_DATA_COLETA)
            .volumeMl(UPDATED_VOLUME_ML)
            .temperatura(UPDATED_TEMPERATURA)
            .localColeta(UPDATED_LOCAL_COLETA)
            .observacoes(UPDATED_OBSERVACOES)
            .statusColeta(UPDATED_STATUS_COLETA);

        restColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColeta))
            )
            .andExpect(status().isOk());

        // Validate the Coleta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColetaUpdatableFieldsEquals(partialUpdatedColeta, getPersistedColeta(partialUpdatedColeta));
    }

    @Test
    @Transactional
    void patchNonExistingColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coletaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coletaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coletaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamColeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coleta.setId(longCount.incrementAndGet());

        // Create the Coleta
        ColetaDTO coletaDTO = coletaMapper.toDto(coleta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coletaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coleta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteColeta() throws Exception {
        // Initialize the database
        insertedColeta = coletaRepository.saveAndFlush(coleta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coleta
        restColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, coleta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coletaRepository.count();
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

    protected Coleta getPersistedColeta(Coleta coleta) {
        return coletaRepository.findById(coleta.getId()).orElseThrow();
    }

    protected void assertPersistedColetaToMatchAllProperties(Coleta expectedColeta) {
        assertColetaAllPropertiesEquals(expectedColeta, getPersistedColeta(expectedColeta));
    }

    protected void assertPersistedColetaToMatchUpdatableProperties(Coleta expectedColeta) {
        assertColetaAllUpdatablePropertiesEquals(expectedColeta, getPersistedColeta(expectedColeta));
    }
}
