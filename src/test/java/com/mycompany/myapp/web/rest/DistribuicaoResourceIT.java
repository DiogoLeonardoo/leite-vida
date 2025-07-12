package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DistribuicaoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Distribuicao;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.DistribuicaoRepository;
import com.mycompany.myapp.service.dto.DistribuicaoDTO;
import com.mycompany.myapp.service.mapper.DistribuicaoMapper;
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
 * Integration tests for the {@link DistribuicaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DistribuicaoResourceIT {

    private static final LocalDate DEFAULT_DATA_DISTRIBUICAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DISTRIBUICAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_DISTRIBUICAO = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_VOLUME_DISTRIBUIDO_ML = 1D;
    private static final Double UPDATED_VOLUME_DISTRIBUIDO_ML = 2D;
    private static final Double SMALLER_VOLUME_DISTRIBUIDO_ML = 1D - 1D;

    private static final String DEFAULT_RESPONSAVEL_ENTREGA = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL_ENTREGA = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSAVEL_RECEBIMENTO = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL_RECEBIMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/distribuicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private DistribuicaoMapper distribuicaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistribuicaoMockMvc;

    private Distribuicao distribuicao;

    private Distribuicao insertedDistribuicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distribuicao createEntity(EntityManager em) {
        Distribuicao distribuicao = new Distribuicao()
            .dataDistribuicao(DEFAULT_DATA_DISTRIBUICAO)
            .volumeDistribuidoMl(DEFAULT_VOLUME_DISTRIBUIDO_ML)
            .responsavelEntrega(DEFAULT_RESPONSAVEL_ENTREGA)
            .responsavelRecebimento(DEFAULT_RESPONSAVEL_RECEBIMENTO)
            .observacoes(DEFAULT_OBSERVACOES);
        // Add required entity
        Estoque estoque;
        if (TestUtil.findAll(em, Estoque.class).isEmpty()) {
            estoque = EstoqueResourceIT.createEntity(em);
            em.persist(estoque);
            em.flush();
        } else {
            estoque = TestUtil.findAll(em, Estoque.class).get(0);
        }
        distribuicao.setEstoque(estoque);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createEntity();
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        distribuicao.setPaciente(paciente);
        return distribuicao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distribuicao createUpdatedEntity(EntityManager em) {
        Distribuicao updatedDistribuicao = new Distribuicao()
            .dataDistribuicao(UPDATED_DATA_DISTRIBUICAO)
            .volumeDistribuidoMl(UPDATED_VOLUME_DISTRIBUIDO_ML)
            .responsavelEntrega(UPDATED_RESPONSAVEL_ENTREGA)
            .responsavelRecebimento(UPDATED_RESPONSAVEL_RECEBIMENTO)
            .observacoes(UPDATED_OBSERVACOES);
        // Add required entity
        Estoque estoque;
        if (TestUtil.findAll(em, Estoque.class).isEmpty()) {
            estoque = EstoqueResourceIT.createUpdatedEntity(em);
            em.persist(estoque);
            em.flush();
        } else {
            estoque = TestUtil.findAll(em, Estoque.class).get(0);
        }
        updatedDistribuicao.setEstoque(estoque);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createUpdatedEntity();
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        updatedDistribuicao.setPaciente(paciente);
        return updatedDistribuicao;
    }

    @BeforeEach
    void initTest() {
        distribuicao = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDistribuicao != null) {
            distribuicaoRepository.delete(insertedDistribuicao);
            insertedDistribuicao = null;
        }
    }

    @Test
    @Transactional
    void createDistribuicao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);
        var returnedDistribuicaoDTO = om.readValue(
            restDistribuicaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DistribuicaoDTO.class
        );

        // Validate the Distribuicao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDistribuicao = distribuicaoMapper.toEntity(returnedDistribuicaoDTO);
        assertDistribuicaoUpdatableFieldsEquals(returnedDistribuicao, getPersistedDistribuicao(returnedDistribuicao));

        insertedDistribuicao = returnedDistribuicao;
    }

    @Test
    @Transactional
    void createDistribuicaoWithExistingId() throws Exception {
        // Create the Distribuicao with an existing ID
        distribuicao.setId(1L);
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistribuicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataDistribuicaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuicao.setDataDistribuicao(null);

        // Create the Distribuicao, which fails.
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        restDistribuicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeDistribuidoMlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuicao.setVolumeDistribuidoMl(null);

        // Create the Distribuicao, which fails.
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        restDistribuicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsavelEntregaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuicao.setResponsavelEntrega(null);

        // Create the Distribuicao, which fails.
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        restDistribuicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsavelRecebimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuicao.setResponsavelRecebimento(null);

        // Create the Distribuicao, which fails.
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        restDistribuicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDistribuicaos() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distribuicao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDistribuicao").value(hasItem(DEFAULT_DATA_DISTRIBUICAO.toString())))
            .andExpect(jsonPath("$.[*].volumeDistribuidoMl").value(hasItem(DEFAULT_VOLUME_DISTRIBUIDO_ML)))
            .andExpect(jsonPath("$.[*].responsavelEntrega").value(hasItem(DEFAULT_RESPONSAVEL_ENTREGA)))
            .andExpect(jsonPath("$.[*].responsavelRecebimento").value(hasItem(DEFAULT_RESPONSAVEL_RECEBIMENTO)))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)));
    }

    @Test
    @Transactional
    void getDistribuicao() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get the distribuicao
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, distribuicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(distribuicao.getId().intValue()))
            .andExpect(jsonPath("$.dataDistribuicao").value(DEFAULT_DATA_DISTRIBUICAO.toString()))
            .andExpect(jsonPath("$.volumeDistribuidoMl").value(DEFAULT_VOLUME_DISTRIBUIDO_ML))
            .andExpect(jsonPath("$.responsavelEntrega").value(DEFAULT_RESPONSAVEL_ENTREGA))
            .andExpect(jsonPath("$.responsavelRecebimento").value(DEFAULT_RESPONSAVEL_RECEBIMENTO))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES));
    }

    @Test
    @Transactional
    void getDistribuicaosByIdFiltering() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        Long id = distribuicao.getId();

        defaultDistribuicaoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDistribuicaoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDistribuicaoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao equals to
        defaultDistribuicaoFiltering(
            "dataDistribuicao.equals=" + DEFAULT_DATA_DISTRIBUICAO,
            "dataDistribuicao.equals=" + UPDATED_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao in
        defaultDistribuicaoFiltering(
            "dataDistribuicao.in=" + DEFAULT_DATA_DISTRIBUICAO + "," + UPDATED_DATA_DISTRIBUICAO,
            "dataDistribuicao.in=" + UPDATED_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao is not null
        defaultDistribuicaoFiltering("dataDistribuicao.specified=true", "dataDistribuicao.specified=false");
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao is greater than or equal to
        defaultDistribuicaoFiltering(
            "dataDistribuicao.greaterThanOrEqual=" + DEFAULT_DATA_DISTRIBUICAO,
            "dataDistribuicao.greaterThanOrEqual=" + UPDATED_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao is less than or equal to
        defaultDistribuicaoFiltering(
            "dataDistribuicao.lessThanOrEqual=" + DEFAULT_DATA_DISTRIBUICAO,
            "dataDistribuicao.lessThanOrEqual=" + SMALLER_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao is less than
        defaultDistribuicaoFiltering(
            "dataDistribuicao.lessThan=" + UPDATED_DATA_DISTRIBUICAO,
            "dataDistribuicao.lessThan=" + DEFAULT_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByDataDistribuicaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where dataDistribuicao is greater than
        defaultDistribuicaoFiltering(
            "dataDistribuicao.greaterThan=" + SMALLER_DATA_DISTRIBUICAO,
            "dataDistribuicao.greaterThan=" + DEFAULT_DATA_DISTRIBUICAO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl equals to
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.equals=" + DEFAULT_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.equals=" + UPDATED_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl in
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.in=" + DEFAULT_VOLUME_DISTRIBUIDO_ML + "," + UPDATED_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.in=" + UPDATED_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl is not null
        defaultDistribuicaoFiltering("volumeDistribuidoMl.specified=true", "volumeDistribuidoMl.specified=false");
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl is greater than or equal to
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.greaterThanOrEqual=" + DEFAULT_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.greaterThanOrEqual=" + UPDATED_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl is less than or equal to
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.lessThanOrEqual=" + DEFAULT_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.lessThanOrEqual=" + SMALLER_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl is less than
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.lessThan=" + UPDATED_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.lessThan=" + DEFAULT_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByVolumeDistribuidoMlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where volumeDistribuidoMl is greater than
        defaultDistribuicaoFiltering(
            "volumeDistribuidoMl.greaterThan=" + SMALLER_VOLUME_DISTRIBUIDO_ML,
            "volumeDistribuidoMl.greaterThan=" + DEFAULT_VOLUME_DISTRIBUIDO_ML
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelEntrega equals to
        defaultDistribuicaoFiltering(
            "responsavelEntrega.equals=" + DEFAULT_RESPONSAVEL_ENTREGA,
            "responsavelEntrega.equals=" + UPDATED_RESPONSAVEL_ENTREGA
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelEntrega in
        defaultDistribuicaoFiltering(
            "responsavelEntrega.in=" + DEFAULT_RESPONSAVEL_ENTREGA + "," + UPDATED_RESPONSAVEL_ENTREGA,
            "responsavelEntrega.in=" + UPDATED_RESPONSAVEL_ENTREGA
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelEntrega is not null
        defaultDistribuicaoFiltering("responsavelEntrega.specified=true", "responsavelEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelEntregaContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelEntrega contains
        defaultDistribuicaoFiltering(
            "responsavelEntrega.contains=" + DEFAULT_RESPONSAVEL_ENTREGA,
            "responsavelEntrega.contains=" + UPDATED_RESPONSAVEL_ENTREGA
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelEntregaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelEntrega does not contain
        defaultDistribuicaoFiltering(
            "responsavelEntrega.doesNotContain=" + UPDATED_RESPONSAVEL_ENTREGA,
            "responsavelEntrega.doesNotContain=" + DEFAULT_RESPONSAVEL_ENTREGA
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelRecebimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelRecebimento equals to
        defaultDistribuicaoFiltering(
            "responsavelRecebimento.equals=" + DEFAULT_RESPONSAVEL_RECEBIMENTO,
            "responsavelRecebimento.equals=" + UPDATED_RESPONSAVEL_RECEBIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelRecebimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelRecebimento in
        defaultDistribuicaoFiltering(
            "responsavelRecebimento.in=" + DEFAULT_RESPONSAVEL_RECEBIMENTO + "," + UPDATED_RESPONSAVEL_RECEBIMENTO,
            "responsavelRecebimento.in=" + UPDATED_RESPONSAVEL_RECEBIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelRecebimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelRecebimento is not null
        defaultDistribuicaoFiltering("responsavelRecebimento.specified=true", "responsavelRecebimento.specified=false");
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelRecebimentoContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelRecebimento contains
        defaultDistribuicaoFiltering(
            "responsavelRecebimento.contains=" + DEFAULT_RESPONSAVEL_RECEBIMENTO,
            "responsavelRecebimento.contains=" + UPDATED_RESPONSAVEL_RECEBIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByResponsavelRecebimentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where responsavelRecebimento does not contain
        defaultDistribuicaoFiltering(
            "responsavelRecebimento.doesNotContain=" + UPDATED_RESPONSAVEL_RECEBIMENTO,
            "responsavelRecebimento.doesNotContain=" + DEFAULT_RESPONSAVEL_RECEBIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByObservacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where observacoes equals to
        defaultDistribuicaoFiltering("observacoes.equals=" + DEFAULT_OBSERVACOES, "observacoes.equals=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllDistribuicaosByObservacoesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where observacoes in
        defaultDistribuicaoFiltering(
            "observacoes.in=" + DEFAULT_OBSERVACOES + "," + UPDATED_OBSERVACOES,
            "observacoes.in=" + UPDATED_OBSERVACOES
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByObservacoesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where observacoes is not null
        defaultDistribuicaoFiltering("observacoes.specified=true", "observacoes.specified=false");
    }

    @Test
    @Transactional
    void getAllDistribuicaosByObservacoesContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where observacoes contains
        defaultDistribuicaoFiltering("observacoes.contains=" + DEFAULT_OBSERVACOES, "observacoes.contains=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllDistribuicaosByObservacoesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        // Get all the distribuicaoList where observacoes does not contain
        defaultDistribuicaoFiltering(
            "observacoes.doesNotContain=" + UPDATED_OBSERVACOES,
            "observacoes.doesNotContain=" + DEFAULT_OBSERVACOES
        );
    }

    @Test
    @Transactional
    void getAllDistribuicaosByEstoqueIsEqualToSomething() throws Exception {
        Estoque estoque;
        if (TestUtil.findAll(em, Estoque.class).isEmpty()) {
            distribuicaoRepository.saveAndFlush(distribuicao);
            estoque = EstoqueResourceIT.createEntity(em);
        } else {
            estoque = TestUtil.findAll(em, Estoque.class).get(0);
        }
        em.persist(estoque);
        em.flush();
        distribuicao.setEstoque(estoque);
        distribuicaoRepository.saveAndFlush(distribuicao);
        Long estoqueId = estoque.getId();
        // Get all the distribuicaoList where estoque equals to estoqueId
        defaultDistribuicaoShouldBeFound("estoqueId.equals=" + estoqueId);

        // Get all the distribuicaoList where estoque equals to (estoqueId + 1)
        defaultDistribuicaoShouldNotBeFound("estoqueId.equals=" + (estoqueId + 1));
    }

    @Test
    @Transactional
    void getAllDistribuicaosByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            distribuicaoRepository.saveAndFlush(distribuicao);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        distribuicao.setPaciente(paciente);
        distribuicaoRepository.saveAndFlush(distribuicao);
        Long pacienteId = paciente.getId();
        // Get all the distribuicaoList where paciente equals to pacienteId
        defaultDistribuicaoShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the distribuicaoList where paciente equals to (pacienteId + 1)
        defaultDistribuicaoShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    private void defaultDistribuicaoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDistribuicaoShouldBeFound(shouldBeFound);
        defaultDistribuicaoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDistribuicaoShouldBeFound(String filter) throws Exception {
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distribuicao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDistribuicao").value(hasItem(DEFAULT_DATA_DISTRIBUICAO.toString())))
            .andExpect(jsonPath("$.[*].volumeDistribuidoMl").value(hasItem(DEFAULT_VOLUME_DISTRIBUIDO_ML)))
            .andExpect(jsonPath("$.[*].responsavelEntrega").value(hasItem(DEFAULT_RESPONSAVEL_ENTREGA)))
            .andExpect(jsonPath("$.[*].responsavelRecebimento").value(hasItem(DEFAULT_RESPONSAVEL_RECEBIMENTO)))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)));

        // Check, that the count call also returns 1
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDistribuicaoShouldNotBeFound(String filter) throws Exception {
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDistribuicaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDistribuicao() throws Exception {
        // Get the distribuicao
        restDistribuicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDistribuicao() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuicao
        Distribuicao updatedDistribuicao = distribuicaoRepository.findById(distribuicao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDistribuicao are not directly saved in db
        em.detach(updatedDistribuicao);
        updatedDistribuicao
            .dataDistribuicao(UPDATED_DATA_DISTRIBUICAO)
            .volumeDistribuidoMl(UPDATED_VOLUME_DISTRIBUIDO_ML)
            .responsavelEntrega(UPDATED_RESPONSAVEL_ENTREGA)
            .responsavelRecebimento(UPDATED_RESPONSAVEL_RECEBIMENTO)
            .observacoes(UPDATED_OBSERVACOES);
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(updatedDistribuicao);

        restDistribuicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distribuicaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distribuicaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDistribuicaoToMatchAllProperties(updatedDistribuicao);
    }

    @Test
    @Transactional
    void putNonExistingDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distribuicaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distribuicaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distribuicaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistribuicaoWithPatch() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuicao using partial update
        Distribuicao partialUpdatedDistribuicao = new Distribuicao();
        partialUpdatedDistribuicao.setId(distribuicao.getId());

        partialUpdatedDistribuicao.responsavelRecebimento(UPDATED_RESPONSAVEL_RECEBIMENTO);

        restDistribuicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistribuicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistribuicao))
            )
            .andExpect(status().isOk());

        // Validate the Distribuicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistribuicaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDistribuicao, distribuicao),
            getPersistedDistribuicao(distribuicao)
        );
    }

    @Test
    @Transactional
    void fullUpdateDistribuicaoWithPatch() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuicao using partial update
        Distribuicao partialUpdatedDistribuicao = new Distribuicao();
        partialUpdatedDistribuicao.setId(distribuicao.getId());

        partialUpdatedDistribuicao
            .dataDistribuicao(UPDATED_DATA_DISTRIBUICAO)
            .volumeDistribuidoMl(UPDATED_VOLUME_DISTRIBUIDO_ML)
            .responsavelEntrega(UPDATED_RESPONSAVEL_ENTREGA)
            .responsavelRecebimento(UPDATED_RESPONSAVEL_RECEBIMENTO)
            .observacoes(UPDATED_OBSERVACOES);

        restDistribuicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistribuicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistribuicao))
            )
            .andExpect(status().isOk());

        // Validate the Distribuicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistribuicaoUpdatableFieldsEquals(partialUpdatedDistribuicao, getPersistedDistribuicao(partialUpdatedDistribuicao));
    }

    @Test
    @Transactional
    void patchNonExistingDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, distribuicaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distribuicaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distribuicaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistribuicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuicao.setId(longCount.incrementAndGet());

        // Create the Distribuicao
        DistribuicaoDTO distribuicaoDTO = distribuicaoMapper.toDto(distribuicao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(distribuicaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distribuicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistribuicao() throws Exception {
        // Initialize the database
        insertedDistribuicao = distribuicaoRepository.saveAndFlush(distribuicao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the distribuicao
        restDistribuicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, distribuicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return distribuicaoRepository.count();
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

    protected Distribuicao getPersistedDistribuicao(Distribuicao distribuicao) {
        return distribuicaoRepository.findById(distribuicao.getId()).orElseThrow();
    }

    protected void assertPersistedDistribuicaoToMatchAllProperties(Distribuicao expectedDistribuicao) {
        assertDistribuicaoAllPropertiesEquals(expectedDistribuicao, getPersistedDistribuicao(expectedDistribuicao));
    }

    protected void assertPersistedDistribuicaoToMatchUpdatableProperties(Distribuicao expectedDistribuicao) {
        assertDistribuicaoAllUpdatablePropertiesEquals(expectedDistribuicao, getPersistedDistribuicao(expectedDistribuicao));
    }
}
