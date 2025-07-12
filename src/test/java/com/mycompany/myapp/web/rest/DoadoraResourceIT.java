package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DoadoraAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.domain.enumeration.LocalPreNatal;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.TipoDoadora;
import com.mycompany.myapp.repository.DoadoraRepository;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import com.mycompany.myapp.service.mapper.DoadoraMapper;
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
 * Integration tests for the {@link DoadoraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoadoraResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CARTAO_SUS = "AAAAAAAAAA";
    private static final String UPDATED_CARTAO_SUS = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFISSAO = "AAAAAAAAAA";
    private static final String UPDATED_PROFISSAO = "BBBBBBBBBB";

    private static final TipoDoadora DEFAULT_TIPO_DOADORA = TipoDoadora.DOMICILIAR;
    private static final TipoDoadora UPDATED_TIPO_DOADORA = TipoDoadora.EXCLUSIVA;

    private static final LocalPreNatal DEFAULT_LOCAL_PRE_NATAL = LocalPreNatal.REDE_PUBLICA;
    private static final LocalPreNatal UPDATED_LOCAL_PRE_NATAL = LocalPreNatal.REDE_PRIVADA;

    private static final ResultadoExame DEFAULT_RESULTADO_VDRL = ResultadoExame.POSITIVO;
    private static final ResultadoExame UPDATED_RESULTADO_VDRL = ResultadoExame.NEGATIVO;

    private static final ResultadoExame DEFAULT_RESULTADO_H_BS_AG = ResultadoExame.POSITIVO;
    private static final ResultadoExame UPDATED_RESULTADO_H_BS_AG = ResultadoExame.NEGATIVO;

    private static final ResultadoExame DEFAULT_RESULTADO_FT_AABS = ResultadoExame.POSITIVO;
    private static final ResultadoExame UPDATED_RESULTADO_FT_AABS = ResultadoExame.NEGATIVO;

    private static final ResultadoExame DEFAULT_RESULTADO_HIV = ResultadoExame.POSITIVO;
    private static final ResultadoExame UPDATED_RESULTADO_HIV = ResultadoExame.NEGATIVO;

    private static final Boolean DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS = false;
    private static final Boolean UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS = true;

    private static final LocalDate DEFAULT_DATA_REGISTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_REGISTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_REGISTRO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/doadoras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Autowired
    private DoadoraMapper doadoraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoadoraMockMvc;

    private Doadora doadora;

    private Doadora insertedDoadora;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doadora createEntity() {
        return new Doadora()
            .nome(DEFAULT_NOME)
            .cartaoSUS(DEFAULT_CARTAO_SUS)
            .cpf(DEFAULT_CPF)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .cep(DEFAULT_CEP)
            .estado(DEFAULT_ESTADO)
            .cidade(DEFAULT_CIDADE)
            .endereco(DEFAULT_ENDERECO)
            .telefone(DEFAULT_TELEFONE)
            .profissao(DEFAULT_PROFISSAO)
            .tipoDoadora(DEFAULT_TIPO_DOADORA)
            .localPreNatal(DEFAULT_LOCAL_PRE_NATAL)
            .resultadoVDRL(DEFAULT_RESULTADO_VDRL)
            .resultadoHBsAg(DEFAULT_RESULTADO_H_BS_AG)
            .resultadoFTAabs(DEFAULT_RESULTADO_FT_AABS)
            .resultadoHIV(DEFAULT_RESULTADO_HIV)
            .transfusaoUltimos5Anos(DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS)
            .dataRegistro(DEFAULT_DATA_REGISTRO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doadora createUpdatedEntity() {
        return new Doadora()
            .nome(UPDATED_NOME)
            .cartaoSUS(UPDATED_CARTAO_SUS)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cep(UPDATED_CEP)
            .estado(UPDATED_ESTADO)
            .cidade(UPDATED_CIDADE)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .profissao(UPDATED_PROFISSAO)
            .tipoDoadora(UPDATED_TIPO_DOADORA)
            .localPreNatal(UPDATED_LOCAL_PRE_NATAL)
            .resultadoVDRL(UPDATED_RESULTADO_VDRL)
            .resultadoHBsAg(UPDATED_RESULTADO_H_BS_AG)
            .resultadoFTAabs(UPDATED_RESULTADO_FT_AABS)
            .resultadoHIV(UPDATED_RESULTADO_HIV)
            .transfusaoUltimos5Anos(UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS)
            .dataRegistro(UPDATED_DATA_REGISTRO);
    }

    @BeforeEach
    void initTest() {
        doadora = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDoadora != null) {
            doadoraRepository.delete(insertedDoadora);
            insertedDoadora = null;
        }
    }

    @Test
    @Transactional
    void createDoadora() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);
        var returnedDoadoraDTO = om.readValue(
            restDoadoraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DoadoraDTO.class
        );

        // Validate the Doadora in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDoadora = doadoraMapper.toEntity(returnedDoadoraDTO);
        assertDoadoraUpdatableFieldsEquals(returnedDoadora, getPersistedDoadora(returnedDoadora));

        insertedDoadora = returnedDoadora;
    }

    @Test
    @Transactional
    void createDoadoraWithExistingId() throws Exception {
        // Create the Doadora with an existing ID
        doadora.setId(1L);
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setNome(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setCpf(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setDataNascimento(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setCep(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setEstado(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setCidade(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnderecoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setEndereco(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setTelefone(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDoadoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setTipoDoadora(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doadora.setDataRegistro(null);

        // Create the Doadora, which fails.
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        restDoadoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoadoras() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doadora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cartaoSUS").value(hasItem(DEFAULT_CARTAO_SUS)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].profissao").value(hasItem(DEFAULT_PROFISSAO)))
            .andExpect(jsonPath("$.[*].tipoDoadora").value(hasItem(DEFAULT_TIPO_DOADORA.toString())))
            .andExpect(jsonPath("$.[*].localPreNatal").value(hasItem(DEFAULT_LOCAL_PRE_NATAL.toString())))
            .andExpect(jsonPath("$.[*].resultadoVDRL").value(hasItem(DEFAULT_RESULTADO_VDRL.toString())))
            .andExpect(jsonPath("$.[*].resultadoHBsAg").value(hasItem(DEFAULT_RESULTADO_H_BS_AG.toString())))
            .andExpect(jsonPath("$.[*].resultadoFTAabs").value(hasItem(DEFAULT_RESULTADO_FT_AABS.toString())))
            .andExpect(jsonPath("$.[*].resultadoHIV").value(hasItem(DEFAULT_RESULTADO_HIV.toString())))
            .andExpect(jsonPath("$.[*].transfusaoUltimos5Anos").value(hasItem(DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS)))
            .andExpect(jsonPath("$.[*].dataRegistro").value(hasItem(DEFAULT_DATA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getDoadora() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get the doadora
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL_ID, doadora.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doadora.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cartaoSUS").value(DEFAULT_CARTAO_SUS))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.profissao").value(DEFAULT_PROFISSAO))
            .andExpect(jsonPath("$.tipoDoadora").value(DEFAULT_TIPO_DOADORA.toString()))
            .andExpect(jsonPath("$.localPreNatal").value(DEFAULT_LOCAL_PRE_NATAL.toString()))
            .andExpect(jsonPath("$.resultadoVDRL").value(DEFAULT_RESULTADO_VDRL.toString()))
            .andExpect(jsonPath("$.resultadoHBsAg").value(DEFAULT_RESULTADO_H_BS_AG.toString()))
            .andExpect(jsonPath("$.resultadoFTAabs").value(DEFAULT_RESULTADO_FT_AABS.toString()))
            .andExpect(jsonPath("$.resultadoHIV").value(DEFAULT_RESULTADO_HIV.toString()))
            .andExpect(jsonPath("$.transfusaoUltimos5Anos").value(DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS))
            .andExpect(jsonPath("$.dataRegistro").value(DEFAULT_DATA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getDoadorasByIdFiltering() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        Long id = doadora.getId();

        defaultDoadoraFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDoadoraFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDoadoraFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoadorasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where nome equals to
        defaultDoadoraFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDoadorasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where nome in
        defaultDoadoraFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDoadorasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where nome is not null
        defaultDoadoraFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where nome contains
        defaultDoadoraFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDoadorasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where nome does not contain
        defaultDoadoraFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllDoadorasByCartaoSUSIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cartaoSUS equals to
        defaultDoadoraFiltering("cartaoSUS.equals=" + DEFAULT_CARTAO_SUS, "cartaoSUS.equals=" + UPDATED_CARTAO_SUS);
    }

    @Test
    @Transactional
    void getAllDoadorasByCartaoSUSIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cartaoSUS in
        defaultDoadoraFiltering("cartaoSUS.in=" + DEFAULT_CARTAO_SUS + "," + UPDATED_CARTAO_SUS, "cartaoSUS.in=" + UPDATED_CARTAO_SUS);
    }

    @Test
    @Transactional
    void getAllDoadorasByCartaoSUSIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cartaoSUS is not null
        defaultDoadoraFiltering("cartaoSUS.specified=true", "cartaoSUS.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByCartaoSUSContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cartaoSUS contains
        defaultDoadoraFiltering("cartaoSUS.contains=" + DEFAULT_CARTAO_SUS, "cartaoSUS.contains=" + UPDATED_CARTAO_SUS);
    }

    @Test
    @Transactional
    void getAllDoadorasByCartaoSUSNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cartaoSUS does not contain
        defaultDoadoraFiltering("cartaoSUS.doesNotContain=" + UPDATED_CARTAO_SUS, "cartaoSUS.doesNotContain=" + DEFAULT_CARTAO_SUS);
    }

    @Test
    @Transactional
    void getAllDoadorasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cpf equals to
        defaultDoadoraFiltering("cpf.equals=" + DEFAULT_CPF, "cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllDoadorasByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cpf in
        defaultDoadoraFiltering("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF, "cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllDoadorasByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cpf is not null
        defaultDoadoraFiltering("cpf.specified=true", "cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByCpfContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cpf contains
        defaultDoadoraFiltering("cpf.contains=" + DEFAULT_CPF, "cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllDoadorasByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cpf does not contain
        defaultDoadoraFiltering("cpf.doesNotContain=" + UPDATED_CPF, "cpf.doesNotContain=" + DEFAULT_CPF);
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento equals to
        defaultDoadoraFiltering("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO, "dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento in
        defaultDoadoraFiltering(
            "dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.in=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento is not null
        defaultDoadoraFiltering("dataNascimento.specified=true", "dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento is greater than or equal to
        defaultDoadoraFiltering(
            "dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento is less than or equal to
        defaultDoadoraFiltering(
            "dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento is less than
        defaultDoadoraFiltering("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO, "dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDoadorasByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataNascimento is greater than
        defaultDoadoraFiltering(
            "dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO,
            "dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cep equals to
        defaultDoadoraFiltering("cep.equals=" + DEFAULT_CEP, "cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllDoadorasByCepIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cep in
        defaultDoadoraFiltering("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP, "cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllDoadorasByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cep is not null
        defaultDoadoraFiltering("cep.specified=true", "cep.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByCepContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cep contains
        defaultDoadoraFiltering("cep.contains=" + DEFAULT_CEP, "cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllDoadorasByCepNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cep does not contain
        defaultDoadoraFiltering("cep.doesNotContain=" + UPDATED_CEP, "cep.doesNotContain=" + DEFAULT_CEP);
    }

    @Test
    @Transactional
    void getAllDoadorasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where estado equals to
        defaultDoadoraFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where estado in
        defaultDoadoraFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where estado is not null
        defaultDoadoraFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where estado contains
        defaultDoadoraFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where estado does not contain
        defaultDoadoraFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllDoadorasByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cidade equals to
        defaultDoadoraFiltering("cidade.equals=" + DEFAULT_CIDADE, "cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllDoadorasByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cidade in
        defaultDoadoraFiltering("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE, "cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllDoadorasByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cidade is not null
        defaultDoadoraFiltering("cidade.specified=true", "cidade.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByCidadeContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cidade contains
        defaultDoadoraFiltering("cidade.contains=" + DEFAULT_CIDADE, "cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllDoadorasByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where cidade does not contain
        defaultDoadoraFiltering("cidade.doesNotContain=" + UPDATED_CIDADE, "cidade.doesNotContain=" + DEFAULT_CIDADE);
    }

    @Test
    @Transactional
    void getAllDoadorasByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where endereco equals to
        defaultDoadoraFiltering("endereco.equals=" + DEFAULT_ENDERECO, "endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where endereco in
        defaultDoadoraFiltering("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO, "endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where endereco is not null
        defaultDoadoraFiltering("endereco.specified=true", "endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where endereco contains
        defaultDoadoraFiltering("endereco.contains=" + DEFAULT_ENDERECO, "endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllDoadorasByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where endereco does not contain
        defaultDoadoraFiltering("endereco.doesNotContain=" + UPDATED_ENDERECO, "endereco.doesNotContain=" + DEFAULT_ENDERECO);
    }

    @Test
    @Transactional
    void getAllDoadorasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where telefone equals to
        defaultDoadoraFiltering("telefone.equals=" + DEFAULT_TELEFONE, "telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllDoadorasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where telefone in
        defaultDoadoraFiltering("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE, "telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllDoadorasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where telefone is not null
        defaultDoadoraFiltering("telefone.specified=true", "telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where telefone contains
        defaultDoadoraFiltering("telefone.contains=" + DEFAULT_TELEFONE, "telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllDoadorasByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where telefone does not contain
        defaultDoadoraFiltering("telefone.doesNotContain=" + UPDATED_TELEFONE, "telefone.doesNotContain=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllDoadorasByProfissaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where profissao equals to
        defaultDoadoraFiltering("profissao.equals=" + DEFAULT_PROFISSAO, "profissao.equals=" + UPDATED_PROFISSAO);
    }

    @Test
    @Transactional
    void getAllDoadorasByProfissaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where profissao in
        defaultDoadoraFiltering("profissao.in=" + DEFAULT_PROFISSAO + "," + UPDATED_PROFISSAO, "profissao.in=" + UPDATED_PROFISSAO);
    }

    @Test
    @Transactional
    void getAllDoadorasByProfissaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where profissao is not null
        defaultDoadoraFiltering("profissao.specified=true", "profissao.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByProfissaoContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where profissao contains
        defaultDoadoraFiltering("profissao.contains=" + DEFAULT_PROFISSAO, "profissao.contains=" + UPDATED_PROFISSAO);
    }

    @Test
    @Transactional
    void getAllDoadorasByProfissaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where profissao does not contain
        defaultDoadoraFiltering("profissao.doesNotContain=" + UPDATED_PROFISSAO, "profissao.doesNotContain=" + DEFAULT_PROFISSAO);
    }

    @Test
    @Transactional
    void getAllDoadorasByTipoDoadoraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where tipoDoadora equals to
        defaultDoadoraFiltering("tipoDoadora.equals=" + DEFAULT_TIPO_DOADORA, "tipoDoadora.equals=" + UPDATED_TIPO_DOADORA);
    }

    @Test
    @Transactional
    void getAllDoadorasByTipoDoadoraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where tipoDoadora in
        defaultDoadoraFiltering(
            "tipoDoadora.in=" + DEFAULT_TIPO_DOADORA + "," + UPDATED_TIPO_DOADORA,
            "tipoDoadora.in=" + UPDATED_TIPO_DOADORA
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByTipoDoadoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where tipoDoadora is not null
        defaultDoadoraFiltering("tipoDoadora.specified=true", "tipoDoadora.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByLocalPreNatalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where localPreNatal equals to
        defaultDoadoraFiltering("localPreNatal.equals=" + DEFAULT_LOCAL_PRE_NATAL, "localPreNatal.equals=" + UPDATED_LOCAL_PRE_NATAL);
    }

    @Test
    @Transactional
    void getAllDoadorasByLocalPreNatalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where localPreNatal in
        defaultDoadoraFiltering(
            "localPreNatal.in=" + DEFAULT_LOCAL_PRE_NATAL + "," + UPDATED_LOCAL_PRE_NATAL,
            "localPreNatal.in=" + UPDATED_LOCAL_PRE_NATAL
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByLocalPreNatalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where localPreNatal is not null
        defaultDoadoraFiltering("localPreNatal.specified=true", "localPreNatal.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoVDRLIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoVDRL equals to
        defaultDoadoraFiltering("resultadoVDRL.equals=" + DEFAULT_RESULTADO_VDRL, "resultadoVDRL.equals=" + UPDATED_RESULTADO_VDRL);
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoVDRLIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoVDRL in
        defaultDoadoraFiltering(
            "resultadoVDRL.in=" + DEFAULT_RESULTADO_VDRL + "," + UPDATED_RESULTADO_VDRL,
            "resultadoVDRL.in=" + UPDATED_RESULTADO_VDRL
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoVDRLIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoVDRL is not null
        defaultDoadoraFiltering("resultadoVDRL.specified=true", "resultadoVDRL.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHBsAgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHBsAg equals to
        defaultDoadoraFiltering("resultadoHBsAg.equals=" + DEFAULT_RESULTADO_H_BS_AG, "resultadoHBsAg.equals=" + UPDATED_RESULTADO_H_BS_AG);
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHBsAgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHBsAg in
        defaultDoadoraFiltering(
            "resultadoHBsAg.in=" + DEFAULT_RESULTADO_H_BS_AG + "," + UPDATED_RESULTADO_H_BS_AG,
            "resultadoHBsAg.in=" + UPDATED_RESULTADO_H_BS_AG
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHBsAgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHBsAg is not null
        defaultDoadoraFiltering("resultadoHBsAg.specified=true", "resultadoHBsAg.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoFTAabsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoFTAabs equals to
        defaultDoadoraFiltering(
            "resultadoFTAabs.equals=" + DEFAULT_RESULTADO_FT_AABS,
            "resultadoFTAabs.equals=" + UPDATED_RESULTADO_FT_AABS
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoFTAabsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoFTAabs in
        defaultDoadoraFiltering(
            "resultadoFTAabs.in=" + DEFAULT_RESULTADO_FT_AABS + "," + UPDATED_RESULTADO_FT_AABS,
            "resultadoFTAabs.in=" + UPDATED_RESULTADO_FT_AABS
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoFTAabsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoFTAabs is not null
        defaultDoadoraFiltering("resultadoFTAabs.specified=true", "resultadoFTAabs.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHIVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHIV equals to
        defaultDoadoraFiltering("resultadoHIV.equals=" + DEFAULT_RESULTADO_HIV, "resultadoHIV.equals=" + UPDATED_RESULTADO_HIV);
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHIVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHIV in
        defaultDoadoraFiltering(
            "resultadoHIV.in=" + DEFAULT_RESULTADO_HIV + "," + UPDATED_RESULTADO_HIV,
            "resultadoHIV.in=" + UPDATED_RESULTADO_HIV
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByResultadoHIVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where resultadoHIV is not null
        defaultDoadoraFiltering("resultadoHIV.specified=true", "resultadoHIV.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByTransfusaoUltimos5AnosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where transfusaoUltimos5Anos equals to
        defaultDoadoraFiltering(
            "transfusaoUltimos5Anos.equals=" + DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS,
            "transfusaoUltimos5Anos.equals=" + UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByTransfusaoUltimos5AnosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where transfusaoUltimos5Anos in
        defaultDoadoraFiltering(
            "transfusaoUltimos5Anos.in=" + DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS + "," + UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS,
            "transfusaoUltimos5Anos.in=" + UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByTransfusaoUltimos5AnosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where transfusaoUltimos5Anos is not null
        defaultDoadoraFiltering("transfusaoUltimos5Anos.specified=true", "transfusaoUltimos5Anos.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro equals to
        defaultDoadoraFiltering("dataRegistro.equals=" + DEFAULT_DATA_REGISTRO, "dataRegistro.equals=" + UPDATED_DATA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro in
        defaultDoadoraFiltering(
            "dataRegistro.in=" + DEFAULT_DATA_REGISTRO + "," + UPDATED_DATA_REGISTRO,
            "dataRegistro.in=" + UPDATED_DATA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro is not null
        defaultDoadoraFiltering("dataRegistro.specified=true", "dataRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro is greater than or equal to
        defaultDoadoraFiltering(
            "dataRegistro.greaterThanOrEqual=" + DEFAULT_DATA_REGISTRO,
            "dataRegistro.greaterThanOrEqual=" + UPDATED_DATA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro is less than or equal to
        defaultDoadoraFiltering(
            "dataRegistro.lessThanOrEqual=" + DEFAULT_DATA_REGISTRO,
            "dataRegistro.lessThanOrEqual=" + SMALLER_DATA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro is less than
        defaultDoadoraFiltering("dataRegistro.lessThan=" + UPDATED_DATA_REGISTRO, "dataRegistro.lessThan=" + DEFAULT_DATA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllDoadorasByDataRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        // Get all the doadoraList where dataRegistro is greater than
        defaultDoadoraFiltering("dataRegistro.greaterThan=" + SMALLER_DATA_REGISTRO, "dataRegistro.greaterThan=" + DEFAULT_DATA_REGISTRO);
    }

    private void defaultDoadoraFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDoadoraShouldBeFound(shouldBeFound);
        defaultDoadoraShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoadoraShouldBeFound(String filter) throws Exception {
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doadora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cartaoSUS").value(hasItem(DEFAULT_CARTAO_SUS)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].profissao").value(hasItem(DEFAULT_PROFISSAO)))
            .andExpect(jsonPath("$.[*].tipoDoadora").value(hasItem(DEFAULT_TIPO_DOADORA.toString())))
            .andExpect(jsonPath("$.[*].localPreNatal").value(hasItem(DEFAULT_LOCAL_PRE_NATAL.toString())))
            .andExpect(jsonPath("$.[*].resultadoVDRL").value(hasItem(DEFAULT_RESULTADO_VDRL.toString())))
            .andExpect(jsonPath("$.[*].resultadoHBsAg").value(hasItem(DEFAULT_RESULTADO_H_BS_AG.toString())))
            .andExpect(jsonPath("$.[*].resultadoFTAabs").value(hasItem(DEFAULT_RESULTADO_FT_AABS.toString())))
            .andExpect(jsonPath("$.[*].resultadoHIV").value(hasItem(DEFAULT_RESULTADO_HIV.toString())))
            .andExpect(jsonPath("$.[*].transfusaoUltimos5Anos").value(hasItem(DEFAULT_TRANSFUSAO_ULTIMOS_5_ANOS)))
            .andExpect(jsonPath("$.[*].dataRegistro").value(hasItem(DEFAULT_DATA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoadoraShouldNotBeFound(String filter) throws Exception {
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoadoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoadora() throws Exception {
        // Get the doadora
        restDoadoraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoadora() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doadora
        Doadora updatedDoadora = doadoraRepository.findById(doadora.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDoadora are not directly saved in db
        em.detach(updatedDoadora);
        updatedDoadora
            .nome(UPDATED_NOME)
            .cartaoSUS(UPDATED_CARTAO_SUS)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cep(UPDATED_CEP)
            .estado(UPDATED_ESTADO)
            .cidade(UPDATED_CIDADE)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .profissao(UPDATED_PROFISSAO)
            .tipoDoadora(UPDATED_TIPO_DOADORA)
            .localPreNatal(UPDATED_LOCAL_PRE_NATAL)
            .resultadoVDRL(UPDATED_RESULTADO_VDRL)
            .resultadoHBsAg(UPDATED_RESULTADO_H_BS_AG)
            .resultadoFTAabs(UPDATED_RESULTADO_FT_AABS)
            .resultadoHIV(UPDATED_RESULTADO_HIV)
            .transfusaoUltimos5Anos(UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS)
            .dataRegistro(UPDATED_DATA_REGISTRO);
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(updatedDoadora);

        restDoadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doadoraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDoadoraToMatchAllProperties(updatedDoadora);
    }

    @Test
    @Transactional
    void putNonExistingDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doadoraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoadoraWithPatch() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doadora using partial update
        Doadora partialUpdatedDoadora = new Doadora();
        partialUpdatedDoadora.setId(doadora.getId());

        partialUpdatedDoadora
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .cidade(UPDATED_CIDADE)
            .localPreNatal(UPDATED_LOCAL_PRE_NATAL)
            .resultadoHIV(UPDATED_RESULTADO_HIV)
            .dataRegistro(UPDATED_DATA_REGISTRO);

        restDoadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoadora.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoadora))
            )
            .andExpect(status().isOk());

        // Validate the Doadora in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoadoraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDoadora, doadora), getPersistedDoadora(doadora));
    }

    @Test
    @Transactional
    void fullUpdateDoadoraWithPatch() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doadora using partial update
        Doadora partialUpdatedDoadora = new Doadora();
        partialUpdatedDoadora.setId(doadora.getId());

        partialUpdatedDoadora
            .nome(UPDATED_NOME)
            .cartaoSUS(UPDATED_CARTAO_SUS)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cep(UPDATED_CEP)
            .estado(UPDATED_ESTADO)
            .cidade(UPDATED_CIDADE)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .profissao(UPDATED_PROFISSAO)
            .tipoDoadora(UPDATED_TIPO_DOADORA)
            .localPreNatal(UPDATED_LOCAL_PRE_NATAL)
            .resultadoVDRL(UPDATED_RESULTADO_VDRL)
            .resultadoHBsAg(UPDATED_RESULTADO_H_BS_AG)
            .resultadoFTAabs(UPDATED_RESULTADO_FT_AABS)
            .resultadoHIV(UPDATED_RESULTADO_HIV)
            .transfusaoUltimos5Anos(UPDATED_TRANSFUSAO_ULTIMOS_5_ANOS)
            .dataRegistro(UPDATED_DATA_REGISTRO);

        restDoadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoadora.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoadora))
            )
            .andExpect(status().isOk());

        // Validate the Doadora in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoadoraUpdatableFieldsEquals(partialUpdatedDoadora, getPersistedDoadora(partialUpdatedDoadora));
    }

    @Test
    @Transactional
    void patchNonExistingDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doadoraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoadora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doadora.setId(longCount.incrementAndGet());

        // Create the Doadora
        DoadoraDTO doadoraDTO = doadoraMapper.toDto(doadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoadoraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(doadoraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doadora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoadora() throws Exception {
        // Initialize the database
        insertedDoadora = doadoraRepository.saveAndFlush(doadora);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the doadora
        restDoadoraMockMvc
            .perform(delete(ENTITY_API_URL_ID, doadora.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return doadoraRepository.count();
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

    protected Doadora getPersistedDoadora(Doadora doadora) {
        return doadoraRepository.findById(doadora.getId()).orElseThrow();
    }

    protected void assertPersistedDoadoraToMatchAllProperties(Doadora expectedDoadora) {
        assertDoadoraAllPropertiesEquals(expectedDoadora, getPersistedDoadora(expectedDoadora));
    }

    protected void assertPersistedDoadoraToMatchUpdatableProperties(Doadora expectedDoadora) {
        assertDoadoraAllUpdatablePropertiesEquals(expectedDoadora, getPersistedDoadora(expectedDoadora));
    }
}
