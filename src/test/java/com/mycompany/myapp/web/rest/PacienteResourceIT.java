package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PacienteAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.mapper.PacienteMapper;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRO_HOSPITALAR = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRO_HOSPITALAR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_PESO_NASCIMENTO = 1D;
    private static final Double UPDATED_PESO_NASCIMENTO = 2D;
    private static final Double SMALLER_PESO_NASCIMENTO = 1D - 1D;

    private static final Integer DEFAULT_IDADE_GESTACIONAL = 1;
    private static final Integer UPDATED_IDADE_GESTACIONAL = 2;
    private static final Integer SMALLER_IDADE_GESTACIONAL = 1 - 1;

    private static final String DEFAULT_CONDICAO_CLINICA = "AAAAAAAAAA";
    private static final String UPDATED_CONDICAO_CLINICA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_NOME_RESPONSAVEL = "BBBBBBBBBB";

    private static final String DEFAULT_CPF_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_CPF_RESPONSAVEL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE_RESPONSAVEL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTESCO_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_PARENTESCO_RESPONSAVEL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS_ATIVO = false;
    private static final Boolean UPDATED_STATUS_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity() {
        return new Paciente()
            .nome(DEFAULT_NOME)
            .registroHospitalar(DEFAULT_REGISTRO_HOSPITALAR)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .pesoNascimento(DEFAULT_PESO_NASCIMENTO)
            .idadeGestacional(DEFAULT_IDADE_GESTACIONAL)
            .condicaoClinica(DEFAULT_CONDICAO_CLINICA)
            .nomeResponsavel(DEFAULT_NOME_RESPONSAVEL)
            .cpfResponsavel(DEFAULT_CPF_RESPONSAVEL)
            .telefoneResponsavel(DEFAULT_TELEFONE_RESPONSAVEL)
            .parentescoResponsavel(DEFAULT_PARENTESCO_RESPONSAVEL)
            .statusAtivo(DEFAULT_STATUS_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity() {
        return new Paciente()
            .nome(UPDATED_NOME)
            .registroHospitalar(UPDATED_REGISTRO_HOSPITALAR)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .pesoNascimento(UPDATED_PESO_NASCIMENTO)
            .idadeGestacional(UPDATED_IDADE_GESTACIONAL)
            .condicaoClinica(UPDATED_CONDICAO_CLINICA)
            .nomeResponsavel(UPDATED_NOME_RESPONSAVEL)
            .cpfResponsavel(UPDATED_CPF_RESPONSAVEL)
            .telefoneResponsavel(UPDATED_TELEFONE_RESPONSAVEL)
            .parentescoResponsavel(UPDATED_PARENTESCO_RESPONSAVEL)
            .statusAtivo(UPDATED_STATUS_ATIVO);
    }

    @BeforeEach
    void initTest() {
        paciente = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente);
            insertedPaciente = null;
        }
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        var returnedPacienteDTO = om.readValue(
            restPacienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PacienteDTO.class
        );

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaciente = pacienteMapper.toEntity(returnedPacienteDTO);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNome(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegistroHospitalarIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setRegistroHospitalar(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setDataNascimento(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCondicaoClinicaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setCondicaoClinica(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeResponsavelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNomeResponsavel(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfResponsavelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setCpfResponsavel(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneResponsavelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setTelefoneResponsavel(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParentescoResponsavelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setParentescoResponsavel(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setStatusAtivo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].registroHospitalar").value(hasItem(DEFAULT_REGISTRO_HOSPITALAR)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].pesoNascimento").value(hasItem(DEFAULT_PESO_NASCIMENTO)))
            .andExpect(jsonPath("$.[*].idadeGestacional").value(hasItem(DEFAULT_IDADE_GESTACIONAL)))
            .andExpect(jsonPath("$.[*].condicaoClinica").value(hasItem(DEFAULT_CONDICAO_CLINICA)))
            .andExpect(jsonPath("$.[*].nomeResponsavel").value(hasItem(DEFAULT_NOME_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].cpfResponsavel").value(hasItem(DEFAULT_CPF_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].telefoneResponsavel").value(hasItem(DEFAULT_TELEFONE_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].parentescoResponsavel").value(hasItem(DEFAULT_PARENTESCO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].statusAtivo").value(hasItem(DEFAULT_STATUS_ATIVO)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.registroHospitalar").value(DEFAULT_REGISTRO_HOSPITALAR))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.pesoNascimento").value(DEFAULT_PESO_NASCIMENTO))
            .andExpect(jsonPath("$.idadeGestacional").value(DEFAULT_IDADE_GESTACIONAL))
            .andExpect(jsonPath("$.condicaoClinica").value(DEFAULT_CONDICAO_CLINICA))
            .andExpect(jsonPath("$.nomeResponsavel").value(DEFAULT_NOME_RESPONSAVEL))
            .andExpect(jsonPath("$.cpfResponsavel").value(DEFAULT_CPF_RESPONSAVEL))
            .andExpect(jsonPath("$.telefoneResponsavel").value(DEFAULT_TELEFONE_RESPONSAVEL))
            .andExpect(jsonPath("$.parentescoResponsavel").value(DEFAULT_PARENTESCO_RESPONSAVEL))
            .andExpect(jsonPath("$.statusAtivo").value(DEFAULT_STATUS_ATIVO));
    }

    @Test
    @Transactional
    void getPacientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        Long id = paciente.getId();

        defaultPacienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPacienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPacienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome equals to
        defaultPacienteFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome in
        defaultPacienteFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome is not null
        defaultPacienteFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome contains
        defaultPacienteFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome does not contain
        defaultPacienteFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByRegistroHospitalarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where registroHospitalar equals to
        defaultPacienteFiltering(
            "registroHospitalar.equals=" + DEFAULT_REGISTRO_HOSPITALAR,
            "registroHospitalar.equals=" + UPDATED_REGISTRO_HOSPITALAR
        );
    }

    @Test
    @Transactional
    void getAllPacientesByRegistroHospitalarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where registroHospitalar in
        defaultPacienteFiltering(
            "registroHospitalar.in=" + DEFAULT_REGISTRO_HOSPITALAR + "," + UPDATED_REGISTRO_HOSPITALAR,
            "registroHospitalar.in=" + UPDATED_REGISTRO_HOSPITALAR
        );
    }

    @Test
    @Transactional
    void getAllPacientesByRegistroHospitalarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where registroHospitalar is not null
        defaultPacienteFiltering("registroHospitalar.specified=true", "registroHospitalar.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByRegistroHospitalarContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where registroHospitalar contains
        defaultPacienteFiltering(
            "registroHospitalar.contains=" + DEFAULT_REGISTRO_HOSPITALAR,
            "registroHospitalar.contains=" + UPDATED_REGISTRO_HOSPITALAR
        );
    }

    @Test
    @Transactional
    void getAllPacientesByRegistroHospitalarNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where registroHospitalar does not contain
        defaultPacienteFiltering(
            "registroHospitalar.doesNotContain=" + UPDATED_REGISTRO_HOSPITALAR,
            "registroHospitalar.doesNotContain=" + DEFAULT_REGISTRO_HOSPITALAR
        );
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento equals to
        defaultPacienteFiltering("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO, "dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento in
        defaultPacienteFiltering(
            "dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.in=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento is not null
        defaultPacienteFiltering("dataNascimento.specified=true", "dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento is greater than or equal to
        defaultPacienteFiltering(
            "dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento is less than or equal to
        defaultPacienteFiltering(
            "dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento is less than
        defaultPacienteFiltering(
            "dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where dataNascimento is greater than
        defaultPacienteFiltering(
            "dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO,
            "dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento equals to
        defaultPacienteFiltering("pesoNascimento.equals=" + DEFAULT_PESO_NASCIMENTO, "pesoNascimento.equals=" + UPDATED_PESO_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento in
        defaultPacienteFiltering(
            "pesoNascimento.in=" + DEFAULT_PESO_NASCIMENTO + "," + UPDATED_PESO_NASCIMENTO,
            "pesoNascimento.in=" + UPDATED_PESO_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento is not null
        defaultPacienteFiltering("pesoNascimento.specified=true", "pesoNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento is greater than or equal to
        defaultPacienteFiltering(
            "pesoNascimento.greaterThanOrEqual=" + DEFAULT_PESO_NASCIMENTO,
            "pesoNascimento.greaterThanOrEqual=" + UPDATED_PESO_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento is less than or equal to
        defaultPacienteFiltering(
            "pesoNascimento.lessThanOrEqual=" + DEFAULT_PESO_NASCIMENTO,
            "pesoNascimento.lessThanOrEqual=" + SMALLER_PESO_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento is less than
        defaultPacienteFiltering(
            "pesoNascimento.lessThan=" + UPDATED_PESO_NASCIMENTO,
            "pesoNascimento.lessThan=" + DEFAULT_PESO_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByPesoNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoNascimento is greater than
        defaultPacienteFiltering(
            "pesoNascimento.greaterThan=" + SMALLER_PESO_NASCIMENTO,
            "pesoNascimento.greaterThan=" + DEFAULT_PESO_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional equals to
        defaultPacienteFiltering(
            "idadeGestacional.equals=" + DEFAULT_IDADE_GESTACIONAL,
            "idadeGestacional.equals=" + UPDATED_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional in
        defaultPacienteFiltering(
            "idadeGestacional.in=" + DEFAULT_IDADE_GESTACIONAL + "," + UPDATED_IDADE_GESTACIONAL,
            "idadeGestacional.in=" + UPDATED_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional is not null
        defaultPacienteFiltering("idadeGestacional.specified=true", "idadeGestacional.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional is greater than or equal to
        defaultPacienteFiltering(
            "idadeGestacional.greaterThanOrEqual=" + DEFAULT_IDADE_GESTACIONAL,
            "idadeGestacional.greaterThanOrEqual=" + UPDATED_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional is less than or equal to
        defaultPacienteFiltering(
            "idadeGestacional.lessThanOrEqual=" + DEFAULT_IDADE_GESTACIONAL,
            "idadeGestacional.lessThanOrEqual=" + SMALLER_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional is less than
        defaultPacienteFiltering(
            "idadeGestacional.lessThan=" + UPDATED_IDADE_GESTACIONAL,
            "idadeGestacional.lessThan=" + DEFAULT_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeGestacionalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idadeGestacional is greater than
        defaultPacienteFiltering(
            "idadeGestacional.greaterThan=" + SMALLER_IDADE_GESTACIONAL,
            "idadeGestacional.greaterThan=" + DEFAULT_IDADE_GESTACIONAL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCondicaoClinicaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where condicaoClinica equals to
        defaultPacienteFiltering(
            "condicaoClinica.equals=" + DEFAULT_CONDICAO_CLINICA,
            "condicaoClinica.equals=" + UPDATED_CONDICAO_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCondicaoClinicaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where condicaoClinica in
        defaultPacienteFiltering(
            "condicaoClinica.in=" + DEFAULT_CONDICAO_CLINICA + "," + UPDATED_CONDICAO_CLINICA,
            "condicaoClinica.in=" + UPDATED_CONDICAO_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCondicaoClinicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where condicaoClinica is not null
        defaultPacienteFiltering("condicaoClinica.specified=true", "condicaoClinica.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByCondicaoClinicaContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where condicaoClinica contains
        defaultPacienteFiltering(
            "condicaoClinica.contains=" + DEFAULT_CONDICAO_CLINICA,
            "condicaoClinica.contains=" + UPDATED_CONDICAO_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCondicaoClinicaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where condicaoClinica does not contain
        defaultPacienteFiltering(
            "condicaoClinica.doesNotContain=" + UPDATED_CONDICAO_CLINICA,
            "condicaoClinica.doesNotContain=" + DEFAULT_CONDICAO_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNomeResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nomeResponsavel equals to
        defaultPacienteFiltering(
            "nomeResponsavel.equals=" + DEFAULT_NOME_RESPONSAVEL,
            "nomeResponsavel.equals=" + UPDATED_NOME_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNomeResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nomeResponsavel in
        defaultPacienteFiltering(
            "nomeResponsavel.in=" + DEFAULT_NOME_RESPONSAVEL + "," + UPDATED_NOME_RESPONSAVEL,
            "nomeResponsavel.in=" + UPDATED_NOME_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNomeResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nomeResponsavel is not null
        defaultPacienteFiltering("nomeResponsavel.specified=true", "nomeResponsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByNomeResponsavelContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nomeResponsavel contains
        defaultPacienteFiltering(
            "nomeResponsavel.contains=" + DEFAULT_NOME_RESPONSAVEL,
            "nomeResponsavel.contains=" + UPDATED_NOME_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNomeResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nomeResponsavel does not contain
        defaultPacienteFiltering(
            "nomeResponsavel.doesNotContain=" + UPDATED_NOME_RESPONSAVEL,
            "nomeResponsavel.doesNotContain=" + DEFAULT_NOME_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCpfResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpfResponsavel equals to
        defaultPacienteFiltering("cpfResponsavel.equals=" + DEFAULT_CPF_RESPONSAVEL, "cpfResponsavel.equals=" + UPDATED_CPF_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpfResponsavel in
        defaultPacienteFiltering(
            "cpfResponsavel.in=" + DEFAULT_CPF_RESPONSAVEL + "," + UPDATED_CPF_RESPONSAVEL,
            "cpfResponsavel.in=" + UPDATED_CPF_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCpfResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpfResponsavel is not null
        defaultPacienteFiltering("cpfResponsavel.specified=true", "cpfResponsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByCpfResponsavelContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpfResponsavel contains
        defaultPacienteFiltering(
            "cpfResponsavel.contains=" + DEFAULT_CPF_RESPONSAVEL,
            "cpfResponsavel.contains=" + UPDATED_CPF_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByCpfResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpfResponsavel does not contain
        defaultPacienteFiltering(
            "cpfResponsavel.doesNotContain=" + UPDATED_CPF_RESPONSAVEL,
            "cpfResponsavel.doesNotContain=" + DEFAULT_CPF_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByTelefoneResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where telefoneResponsavel equals to
        defaultPacienteFiltering(
            "telefoneResponsavel.equals=" + DEFAULT_TELEFONE_RESPONSAVEL,
            "telefoneResponsavel.equals=" + UPDATED_TELEFONE_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByTelefoneResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where telefoneResponsavel in
        defaultPacienteFiltering(
            "telefoneResponsavel.in=" + DEFAULT_TELEFONE_RESPONSAVEL + "," + UPDATED_TELEFONE_RESPONSAVEL,
            "telefoneResponsavel.in=" + UPDATED_TELEFONE_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByTelefoneResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where telefoneResponsavel is not null
        defaultPacienteFiltering("telefoneResponsavel.specified=true", "telefoneResponsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByTelefoneResponsavelContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where telefoneResponsavel contains
        defaultPacienteFiltering(
            "telefoneResponsavel.contains=" + DEFAULT_TELEFONE_RESPONSAVEL,
            "telefoneResponsavel.contains=" + UPDATED_TELEFONE_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByTelefoneResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where telefoneResponsavel does not contain
        defaultPacienteFiltering(
            "telefoneResponsavel.doesNotContain=" + UPDATED_TELEFONE_RESPONSAVEL,
            "telefoneResponsavel.doesNotContain=" + DEFAULT_TELEFONE_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByParentescoResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where parentescoResponsavel equals to
        defaultPacienteFiltering(
            "parentescoResponsavel.equals=" + DEFAULT_PARENTESCO_RESPONSAVEL,
            "parentescoResponsavel.equals=" + UPDATED_PARENTESCO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByParentescoResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where parentescoResponsavel in
        defaultPacienteFiltering(
            "parentescoResponsavel.in=" + DEFAULT_PARENTESCO_RESPONSAVEL + "," + UPDATED_PARENTESCO_RESPONSAVEL,
            "parentescoResponsavel.in=" + UPDATED_PARENTESCO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByParentescoResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where parentescoResponsavel is not null
        defaultPacienteFiltering("parentescoResponsavel.specified=true", "parentescoResponsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByParentescoResponsavelContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where parentescoResponsavel contains
        defaultPacienteFiltering(
            "parentescoResponsavel.contains=" + DEFAULT_PARENTESCO_RESPONSAVEL,
            "parentescoResponsavel.contains=" + UPDATED_PARENTESCO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByParentescoResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where parentescoResponsavel does not contain
        defaultPacienteFiltering(
            "parentescoResponsavel.doesNotContain=" + UPDATED_PARENTESCO_RESPONSAVEL,
            "parentescoResponsavel.doesNotContain=" + DEFAULT_PARENTESCO_RESPONSAVEL
        );
    }

    @Test
    @Transactional
    void getAllPacientesByStatusAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where statusAtivo equals to
        defaultPacienteFiltering("statusAtivo.equals=" + DEFAULT_STATUS_ATIVO, "statusAtivo.equals=" + UPDATED_STATUS_ATIVO);
    }

    @Test
    @Transactional
    void getAllPacientesByStatusAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where statusAtivo in
        defaultPacienteFiltering(
            "statusAtivo.in=" + DEFAULT_STATUS_ATIVO + "," + UPDATED_STATUS_ATIVO,
            "statusAtivo.in=" + UPDATED_STATUS_ATIVO
        );
    }

    @Test
    @Transactional
    void getAllPacientesByStatusAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where statusAtivo is not null
        defaultPacienteFiltering("statusAtivo.specified=true", "statusAtivo.specified=false");
    }

    private void defaultPacienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPacienteShouldBeFound(shouldBeFound);
        defaultPacienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacienteShouldBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].registroHospitalar").value(hasItem(DEFAULT_REGISTRO_HOSPITALAR)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].pesoNascimento").value(hasItem(DEFAULT_PESO_NASCIMENTO)))
            .andExpect(jsonPath("$.[*].idadeGestacional").value(hasItem(DEFAULT_IDADE_GESTACIONAL)))
            .andExpect(jsonPath("$.[*].condicaoClinica").value(hasItem(DEFAULT_CONDICAO_CLINICA)))
            .andExpect(jsonPath("$.[*].nomeResponsavel").value(hasItem(DEFAULT_NOME_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].cpfResponsavel").value(hasItem(DEFAULT_CPF_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].telefoneResponsavel").value(hasItem(DEFAULT_TELEFONE_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].parentescoResponsavel").value(hasItem(DEFAULT_PARENTESCO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].statusAtivo").value(hasItem(DEFAULT_STATUS_ATIVO)));

        // Check, that the count call also returns 1
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacienteShouldNotBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nome(UPDATED_NOME)
            .registroHospitalar(UPDATED_REGISTRO_HOSPITALAR)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .pesoNascimento(UPDATED_PESO_NASCIMENTO)
            .idadeGestacional(UPDATED_IDADE_GESTACIONAL)
            .condicaoClinica(UPDATED_CONDICAO_CLINICA)
            .nomeResponsavel(UPDATED_NOME_RESPONSAVEL)
            .cpfResponsavel(UPDATED_CPF_RESPONSAVEL)
            .telefoneResponsavel(UPDATED_TELEFONE_RESPONSAVEL)
            .parentescoResponsavel(UPDATED_PARENTESCO_RESPONSAVEL)
            .statusAtivo(UPDATED_STATUS_ATIVO);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .registroHospitalar(UPDATED_REGISTRO_HOSPITALAR)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .idadeGestacional(UPDATED_IDADE_GESTACIONAL)
            .condicaoClinica(UPDATED_CONDICAO_CLINICA)
            .cpfResponsavel(UPDATED_CPF_RESPONSAVEL)
            .parentescoResponsavel(UPDATED_PARENTESCO_RESPONSAVEL)
            .statusAtivo(UPDATED_STATUS_ATIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .nome(UPDATED_NOME)
            .registroHospitalar(UPDATED_REGISTRO_HOSPITALAR)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .pesoNascimento(UPDATED_PESO_NASCIMENTO)
            .idadeGestacional(UPDATED_IDADE_GESTACIONAL)
            .condicaoClinica(UPDATED_CONDICAO_CLINICA)
            .nomeResponsavel(UPDATED_NOME_RESPONSAVEL)
            .cpfResponsavel(UPDATED_CPF_RESPONSAVEL)
            .telefoneResponsavel(UPDATED_TELEFONE_RESPONSAVEL)
            .parentescoResponsavel(UPDATED_PARENTESCO_RESPONSAVEL)
            .statusAtivo(UPDATED_STATUS_ATIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count();
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

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).orElseThrow();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
