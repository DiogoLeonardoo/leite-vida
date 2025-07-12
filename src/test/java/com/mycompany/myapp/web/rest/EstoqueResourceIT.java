package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EstoqueAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import com.mycompany.myapp.repository.EstoqueRepository;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.mapper.EstoqueMapper;
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
 * Integration tests for the {@link EstoqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstoqueResourceIT {

    private static final LocalDate DEFAULT_DATA_PRODUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PRODUCAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_PRODUCAO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATA_VALIDADE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VALIDADE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_VALIDADE = LocalDate.ofEpochDay(-1L);

    private static final TipoLeite DEFAULT_TIPO_LEITE = TipoLeite.COLOSTRO;
    private static final TipoLeite UPDATED_TIPO_LEITE = TipoLeite.TRANSICAO;

    private static final ClassificacaoLeite DEFAULT_CLASSIFICACAO = ClassificacaoLeite.A;
    private static final ClassificacaoLeite UPDATED_CLASSIFICACAO = ClassificacaoLeite.B;

    private static final Double DEFAULT_VOLUME_TOTAL_ML = 1D;
    private static final Double UPDATED_VOLUME_TOTAL_ML = 2D;
    private static final Double SMALLER_VOLUME_TOTAL_ML = 1D - 1D;

    private static final Double DEFAULT_VOLUME_DISPONIVEL_ML = 1D;
    private static final Double UPDATED_VOLUME_DISPONIVEL_ML = 2D;
    private static final Double SMALLER_VOLUME_DISPONIVEL_ML = 1D - 1D;

    private static final String DEFAULT_LOCAL_ARMAZENAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_ARMAZENAMENTO = "BBBBBBBBBB";

    private static final Double DEFAULT_TEMPERATURA_ARMAZENAMENTO = 1D;
    private static final Double UPDATED_TEMPERATURA_ARMAZENAMENTO = 2D;
    private static final Double SMALLER_TEMPERATURA_ARMAZENAMENTO = 1D - 1D;

    private static final StatusLote DEFAULT_STATUS_LOTE = StatusLote.DISPONIVEL;
    private static final StatusLote UPDATED_STATUS_LOTE = StatusLote.RESERVADO;

    private static final String ENTITY_API_URL = "/api/estoques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private EstoqueMapper estoqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstoqueMockMvc;

    private Estoque estoque;

    private Estoque insertedEstoque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estoque createEntity(EntityManager em) {
        Estoque estoque = new Estoque()
            .dataProducao(DEFAULT_DATA_PRODUCAO)
            .dataValidade(DEFAULT_DATA_VALIDADE)
            .tipoLeite(DEFAULT_TIPO_LEITE)
            .classificacao(DEFAULT_CLASSIFICACAO)
            .volumeTotalMl(DEFAULT_VOLUME_TOTAL_ML)
            .volumeDisponivelMl(DEFAULT_VOLUME_DISPONIVEL_ML)
            .localArmazenamento(DEFAULT_LOCAL_ARMAZENAMENTO)
            .temperaturaArmazenamento(DEFAULT_TEMPERATURA_ARMAZENAMENTO)
            .statusLote(DEFAULT_STATUS_LOTE);
        return estoque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estoque createUpdatedEntity(EntityManager em) {
        Estoque updatedEstoque = new Estoque()
            .dataProducao(UPDATED_DATA_PRODUCAO)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .tipoLeite(UPDATED_TIPO_LEITE)
            .classificacao(UPDATED_CLASSIFICACAO)
            .volumeTotalMl(UPDATED_VOLUME_TOTAL_ML)
            .volumeDisponivelMl(UPDATED_VOLUME_DISPONIVEL_ML)
            .localArmazenamento(UPDATED_LOCAL_ARMAZENAMENTO)
            .temperaturaArmazenamento(UPDATED_TEMPERATURA_ARMAZENAMENTO)
            .statusLote(UPDATED_STATUS_LOTE);
        return updatedEstoque;
    }

    @BeforeEach
    void initTest() {
        estoque = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEstoque != null) {
            estoqueRepository.delete(insertedEstoque);
            insertedEstoque = null;
        }
    }

    @Test
    @Transactional
    void createEstoque() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);
        var returnedEstoqueDTO = om.readValue(
            restEstoqueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstoqueDTO.class
        );

        // Validate the Estoque in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstoque = estoqueMapper.toEntity(returnedEstoqueDTO);
        assertEstoqueUpdatableFieldsEquals(returnedEstoque, getPersistedEstoque(returnedEstoque));

        insertedEstoque = returnedEstoque;
    }

    @Test
    @Transactional
    void createEstoqueWithExistingId() throws Exception {
        // Create the Estoque with an existing ID
        estoque.setId(1L);
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataProducaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setDataProducao(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataValidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setDataValidade(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoLeiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setTipoLeite(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassificacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setClassificacao(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeTotalMlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setVolumeTotalMl(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeDisponivelMlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setVolumeDisponivelMl(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocalArmazenamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setLocalArmazenamento(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemperaturaArmazenamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setTemperaturaArmazenamento(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusLoteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estoque.setStatusLote(null);

        // Create the Estoque, which fails.
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        restEstoqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstoques() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estoque.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProducao").value(hasItem(DEFAULT_DATA_PRODUCAO.toString())))
            .andExpect(jsonPath("$.[*].dataValidade").value(hasItem(DEFAULT_DATA_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].tipoLeite").value(hasItem(DEFAULT_TIPO_LEITE.toString())))
            .andExpect(jsonPath("$.[*].classificacao").value(hasItem(DEFAULT_CLASSIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].volumeTotalMl").value(hasItem(DEFAULT_VOLUME_TOTAL_ML)))
            .andExpect(jsonPath("$.[*].volumeDisponivelMl").value(hasItem(DEFAULT_VOLUME_DISPONIVEL_ML)))
            .andExpect(jsonPath("$.[*].localArmazenamento").value(hasItem(DEFAULT_LOCAL_ARMAZENAMENTO)))
            .andExpect(jsonPath("$.[*].temperaturaArmazenamento").value(hasItem(DEFAULT_TEMPERATURA_ARMAZENAMENTO)))
            .andExpect(jsonPath("$.[*].statusLote").value(hasItem(DEFAULT_STATUS_LOTE.toString())));
    }

    @Test
    @Transactional
    void getEstoque() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get the estoque
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL_ID, estoque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estoque.getId().intValue()))
            .andExpect(jsonPath("$.dataProducao").value(DEFAULT_DATA_PRODUCAO.toString()))
            .andExpect(jsonPath("$.dataValidade").value(DEFAULT_DATA_VALIDADE.toString()))
            .andExpect(jsonPath("$.tipoLeite").value(DEFAULT_TIPO_LEITE.toString()))
            .andExpect(jsonPath("$.classificacao").value(DEFAULT_CLASSIFICACAO.toString()))
            .andExpect(jsonPath("$.volumeTotalMl").value(DEFAULT_VOLUME_TOTAL_ML))
            .andExpect(jsonPath("$.volumeDisponivelMl").value(DEFAULT_VOLUME_DISPONIVEL_ML))
            .andExpect(jsonPath("$.localArmazenamento").value(DEFAULT_LOCAL_ARMAZENAMENTO))
            .andExpect(jsonPath("$.temperaturaArmazenamento").value(DEFAULT_TEMPERATURA_ARMAZENAMENTO))
            .andExpect(jsonPath("$.statusLote").value(DEFAULT_STATUS_LOTE.toString()));
    }

    @Test
    @Transactional
    void getEstoquesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        Long id = estoque.getId();

        defaultEstoqueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEstoqueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEstoqueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao equals to
        defaultEstoqueFiltering("dataProducao.equals=" + DEFAULT_DATA_PRODUCAO, "dataProducao.equals=" + UPDATED_DATA_PRODUCAO);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao in
        defaultEstoqueFiltering(
            "dataProducao.in=" + DEFAULT_DATA_PRODUCAO + "," + UPDATED_DATA_PRODUCAO,
            "dataProducao.in=" + UPDATED_DATA_PRODUCAO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao is not null
        defaultEstoqueFiltering("dataProducao.specified=true", "dataProducao.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao is greater than or equal to
        defaultEstoqueFiltering(
            "dataProducao.greaterThanOrEqual=" + DEFAULT_DATA_PRODUCAO,
            "dataProducao.greaterThanOrEqual=" + UPDATED_DATA_PRODUCAO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao is less than or equal to
        defaultEstoqueFiltering(
            "dataProducao.lessThanOrEqual=" + DEFAULT_DATA_PRODUCAO,
            "dataProducao.lessThanOrEqual=" + SMALLER_DATA_PRODUCAO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao is less than
        defaultEstoqueFiltering("dataProducao.lessThan=" + UPDATED_DATA_PRODUCAO, "dataProducao.lessThan=" + DEFAULT_DATA_PRODUCAO);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataProducaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataProducao is greater than
        defaultEstoqueFiltering("dataProducao.greaterThan=" + SMALLER_DATA_PRODUCAO, "dataProducao.greaterThan=" + DEFAULT_DATA_PRODUCAO);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade equals to
        defaultEstoqueFiltering("dataValidade.equals=" + DEFAULT_DATA_VALIDADE, "dataValidade.equals=" + UPDATED_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade in
        defaultEstoqueFiltering(
            "dataValidade.in=" + DEFAULT_DATA_VALIDADE + "," + UPDATED_DATA_VALIDADE,
            "dataValidade.in=" + UPDATED_DATA_VALIDADE
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade is not null
        defaultEstoqueFiltering("dataValidade.specified=true", "dataValidade.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade is greater than or equal to
        defaultEstoqueFiltering(
            "dataValidade.greaterThanOrEqual=" + DEFAULT_DATA_VALIDADE,
            "dataValidade.greaterThanOrEqual=" + UPDATED_DATA_VALIDADE
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade is less than or equal to
        defaultEstoqueFiltering(
            "dataValidade.lessThanOrEqual=" + DEFAULT_DATA_VALIDADE,
            "dataValidade.lessThanOrEqual=" + SMALLER_DATA_VALIDADE
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade is less than
        defaultEstoqueFiltering("dataValidade.lessThan=" + UPDATED_DATA_VALIDADE, "dataValidade.lessThan=" + DEFAULT_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllEstoquesByDataValidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where dataValidade is greater than
        defaultEstoqueFiltering("dataValidade.greaterThan=" + SMALLER_DATA_VALIDADE, "dataValidade.greaterThan=" + DEFAULT_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllEstoquesByTipoLeiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where tipoLeite equals to
        defaultEstoqueFiltering("tipoLeite.equals=" + DEFAULT_TIPO_LEITE, "tipoLeite.equals=" + UPDATED_TIPO_LEITE);
    }

    @Test
    @Transactional
    void getAllEstoquesByTipoLeiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where tipoLeite in
        defaultEstoqueFiltering("tipoLeite.in=" + DEFAULT_TIPO_LEITE + "," + UPDATED_TIPO_LEITE, "tipoLeite.in=" + UPDATED_TIPO_LEITE);
    }

    @Test
    @Transactional
    void getAllEstoquesByTipoLeiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where tipoLeite is not null
        defaultEstoqueFiltering("tipoLeite.specified=true", "tipoLeite.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByClassificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where classificacao equals to
        defaultEstoqueFiltering("classificacao.equals=" + DEFAULT_CLASSIFICACAO, "classificacao.equals=" + UPDATED_CLASSIFICACAO);
    }

    @Test
    @Transactional
    void getAllEstoquesByClassificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where classificacao in
        defaultEstoqueFiltering(
            "classificacao.in=" + DEFAULT_CLASSIFICACAO + "," + UPDATED_CLASSIFICACAO,
            "classificacao.in=" + UPDATED_CLASSIFICACAO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByClassificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where classificacao is not null
        defaultEstoqueFiltering("classificacao.specified=true", "classificacao.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl equals to
        defaultEstoqueFiltering("volumeTotalMl.equals=" + DEFAULT_VOLUME_TOTAL_ML, "volumeTotalMl.equals=" + UPDATED_VOLUME_TOTAL_ML);
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl in
        defaultEstoqueFiltering(
            "volumeTotalMl.in=" + DEFAULT_VOLUME_TOTAL_ML + "," + UPDATED_VOLUME_TOTAL_ML,
            "volumeTotalMl.in=" + UPDATED_VOLUME_TOTAL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl is not null
        defaultEstoqueFiltering("volumeTotalMl.specified=true", "volumeTotalMl.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl is greater than or equal to
        defaultEstoqueFiltering(
            "volumeTotalMl.greaterThanOrEqual=" + DEFAULT_VOLUME_TOTAL_ML,
            "volumeTotalMl.greaterThanOrEqual=" + UPDATED_VOLUME_TOTAL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl is less than or equal to
        defaultEstoqueFiltering(
            "volumeTotalMl.lessThanOrEqual=" + DEFAULT_VOLUME_TOTAL_ML,
            "volumeTotalMl.lessThanOrEqual=" + SMALLER_VOLUME_TOTAL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl is less than
        defaultEstoqueFiltering("volumeTotalMl.lessThan=" + UPDATED_VOLUME_TOTAL_ML, "volumeTotalMl.lessThan=" + DEFAULT_VOLUME_TOTAL_ML);
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeTotalMlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeTotalMl is greater than
        defaultEstoqueFiltering(
            "volumeTotalMl.greaterThan=" + SMALLER_VOLUME_TOTAL_ML,
            "volumeTotalMl.greaterThan=" + DEFAULT_VOLUME_TOTAL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl equals to
        defaultEstoqueFiltering(
            "volumeDisponivelMl.equals=" + DEFAULT_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.equals=" + UPDATED_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl in
        defaultEstoqueFiltering(
            "volumeDisponivelMl.in=" + DEFAULT_VOLUME_DISPONIVEL_ML + "," + UPDATED_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.in=" + UPDATED_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl is not null
        defaultEstoqueFiltering("volumeDisponivelMl.specified=true", "volumeDisponivelMl.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl is greater than or equal to
        defaultEstoqueFiltering(
            "volumeDisponivelMl.greaterThanOrEqual=" + DEFAULT_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.greaterThanOrEqual=" + UPDATED_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl is less than or equal to
        defaultEstoqueFiltering(
            "volumeDisponivelMl.lessThanOrEqual=" + DEFAULT_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.lessThanOrEqual=" + SMALLER_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl is less than
        defaultEstoqueFiltering(
            "volumeDisponivelMl.lessThan=" + UPDATED_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.lessThan=" + DEFAULT_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByVolumeDisponivelMlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where volumeDisponivelMl is greater than
        defaultEstoqueFiltering(
            "volumeDisponivelMl.greaterThan=" + SMALLER_VOLUME_DISPONIVEL_ML,
            "volumeDisponivelMl.greaterThan=" + DEFAULT_VOLUME_DISPONIVEL_ML
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByLocalArmazenamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where localArmazenamento equals to
        defaultEstoqueFiltering(
            "localArmazenamento.equals=" + DEFAULT_LOCAL_ARMAZENAMENTO,
            "localArmazenamento.equals=" + UPDATED_LOCAL_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByLocalArmazenamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where localArmazenamento in
        defaultEstoqueFiltering(
            "localArmazenamento.in=" + DEFAULT_LOCAL_ARMAZENAMENTO + "," + UPDATED_LOCAL_ARMAZENAMENTO,
            "localArmazenamento.in=" + UPDATED_LOCAL_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByLocalArmazenamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where localArmazenamento is not null
        defaultEstoqueFiltering("localArmazenamento.specified=true", "localArmazenamento.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByLocalArmazenamentoContainsSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where localArmazenamento contains
        defaultEstoqueFiltering(
            "localArmazenamento.contains=" + DEFAULT_LOCAL_ARMAZENAMENTO,
            "localArmazenamento.contains=" + UPDATED_LOCAL_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByLocalArmazenamentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where localArmazenamento does not contain
        defaultEstoqueFiltering(
            "localArmazenamento.doesNotContain=" + UPDATED_LOCAL_ARMAZENAMENTO,
            "localArmazenamento.doesNotContain=" + DEFAULT_LOCAL_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento equals to
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.equals=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.equals=" + UPDATED_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento in
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.in=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO + "," + UPDATED_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.in=" + UPDATED_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento is not null
        defaultEstoqueFiltering("temperaturaArmazenamento.specified=true", "temperaturaArmazenamento.specified=false");
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento is greater than or equal to
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.greaterThanOrEqual=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.greaterThanOrEqual=" + UPDATED_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento is less than or equal to
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.lessThanOrEqual=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.lessThanOrEqual=" + SMALLER_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento is less than
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.lessThan=" + UPDATED_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.lessThan=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByTemperaturaArmazenamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where temperaturaArmazenamento is greater than
        defaultEstoqueFiltering(
            "temperaturaArmazenamento.greaterThan=" + SMALLER_TEMPERATURA_ARMAZENAMENTO,
            "temperaturaArmazenamento.greaterThan=" + DEFAULT_TEMPERATURA_ARMAZENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllEstoquesByStatusLoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where statusLote equals to
        defaultEstoqueFiltering("statusLote.equals=" + DEFAULT_STATUS_LOTE, "statusLote.equals=" + UPDATED_STATUS_LOTE);
    }

    @Test
    @Transactional
    void getAllEstoquesByStatusLoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where statusLote in
        defaultEstoqueFiltering("statusLote.in=" + DEFAULT_STATUS_LOTE + "," + UPDATED_STATUS_LOTE, "statusLote.in=" + UPDATED_STATUS_LOTE);
    }

    @Test
    @Transactional
    void getAllEstoquesByStatusLoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        // Get all the estoqueList where statusLote is not null
        defaultEstoqueFiltering("statusLote.specified=true", "statusLote.specified=false");
    }

    private void defaultEstoqueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEstoqueShouldBeFound(shouldBeFound);
        defaultEstoqueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstoqueShouldBeFound(String filter) throws Exception {
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estoque.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProducao").value(hasItem(DEFAULT_DATA_PRODUCAO.toString())))
            .andExpect(jsonPath("$.[*].dataValidade").value(hasItem(DEFAULT_DATA_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].tipoLeite").value(hasItem(DEFAULT_TIPO_LEITE.toString())))
            .andExpect(jsonPath("$.[*].classificacao").value(hasItem(DEFAULT_CLASSIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].volumeTotalMl").value(hasItem(DEFAULT_VOLUME_TOTAL_ML)))
            .andExpect(jsonPath("$.[*].volumeDisponivelMl").value(hasItem(DEFAULT_VOLUME_DISPONIVEL_ML)))
            .andExpect(jsonPath("$.[*].localArmazenamento").value(hasItem(DEFAULT_LOCAL_ARMAZENAMENTO)))
            .andExpect(jsonPath("$.[*].temperaturaArmazenamento").value(hasItem(DEFAULT_TEMPERATURA_ARMAZENAMENTO)))
            .andExpect(jsonPath("$.[*].statusLote").value(hasItem(DEFAULT_STATUS_LOTE.toString())));

        // Check, that the count call also returns 1
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstoqueShouldNotBeFound(String filter) throws Exception {
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstoqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstoque() throws Exception {
        // Get the estoque
        restEstoqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstoque() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estoque
        Estoque updatedEstoque = estoqueRepository.findById(estoque.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstoque are not directly saved in db
        em.detach(updatedEstoque);
        updatedEstoque
            .dataProducao(UPDATED_DATA_PRODUCAO)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .tipoLeite(UPDATED_TIPO_LEITE)
            .classificacao(UPDATED_CLASSIFICACAO)
            .volumeTotalMl(UPDATED_VOLUME_TOTAL_ML)
            .volumeDisponivelMl(UPDATED_VOLUME_DISPONIVEL_ML)
            .localArmazenamento(UPDATED_LOCAL_ARMAZENAMENTO)
            .temperaturaArmazenamento(UPDATED_TEMPERATURA_ARMAZENAMENTO)
            .statusLote(UPDATED_STATUS_LOTE);
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(updatedEstoque);

        restEstoqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estoqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstoqueToMatchAllProperties(updatedEstoque);
    }

    @Test
    @Transactional
    void putNonExistingEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estoqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estoqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstoqueWithPatch() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estoque using partial update
        Estoque partialUpdatedEstoque = new Estoque();
        partialUpdatedEstoque.setId(estoque.getId());

        partialUpdatedEstoque
            .volumeTotalMl(UPDATED_VOLUME_TOTAL_ML)
            .temperaturaArmazenamento(UPDATED_TEMPERATURA_ARMAZENAMENTO)
            .statusLote(UPDATED_STATUS_LOTE);

        restEstoqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstoque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstoque))
            )
            .andExpect(status().isOk());

        // Validate the Estoque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstoqueUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEstoque, estoque), getPersistedEstoque(estoque));
    }

    @Test
    @Transactional
    void fullUpdateEstoqueWithPatch() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estoque using partial update
        Estoque partialUpdatedEstoque = new Estoque();
        partialUpdatedEstoque.setId(estoque.getId());

        partialUpdatedEstoque
            .dataProducao(UPDATED_DATA_PRODUCAO)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .tipoLeite(UPDATED_TIPO_LEITE)
            .classificacao(UPDATED_CLASSIFICACAO)
            .volumeTotalMl(UPDATED_VOLUME_TOTAL_ML)
            .volumeDisponivelMl(UPDATED_VOLUME_DISPONIVEL_ML)
            .localArmazenamento(UPDATED_LOCAL_ARMAZENAMENTO)
            .temperaturaArmazenamento(UPDATED_TEMPERATURA_ARMAZENAMENTO)
            .statusLote(UPDATED_STATUS_LOTE);

        restEstoqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstoque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstoque))
            )
            .andExpect(status().isOk());

        // Validate the Estoque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstoqueUpdatableFieldsEquals(partialUpdatedEstoque, getPersistedEstoque(partialUpdatedEstoque));
    }

    @Test
    @Transactional
    void patchNonExistingEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estoqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estoqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estoqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstoque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estoque.setId(longCount.incrementAndGet());

        // Create the Estoque
        EstoqueDTO estoqueDTO = estoqueMapper.toDto(estoque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstoqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estoqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estoque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstoque() throws Exception {
        // Initialize the database
        insertedEstoque = estoqueRepository.saveAndFlush(estoque);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estoque
        restEstoqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, estoque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estoqueRepository.count();
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

    protected Estoque getPersistedEstoque(Estoque estoque) {
        return estoqueRepository.findById(estoque.getId()).orElseThrow();
    }

    protected void assertPersistedEstoqueToMatchAllProperties(Estoque expectedEstoque) {
        assertEstoqueAllPropertiesEquals(expectedEstoque, getPersistedEstoque(expectedEstoque));
    }

    protected void assertPersistedEstoqueToMatchUpdatableProperties(Estoque expectedEstoque) {
        assertEstoqueAllUpdatablePropertiesEquals(expectedEstoque, getPersistedEstoque(expectedEstoque));
    }
}
