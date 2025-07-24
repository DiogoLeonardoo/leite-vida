package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.ResultadoAnalise;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.repository.EstoqueRepository;
import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import com.mycompany.myapp.service.mapper.ProcessamentoMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Processamento}.
 */
@Service
@Transactional
public class ProcessamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessamentoService.class);

    private final ProcessamentoRepository processamentoRepository;

    private final ProcessamentoMapper processamentoMapper;

    private final ColetaRepository coletaRepository;

    private final EstoqueRepository estoqueRepository;

    public ProcessamentoService(
        ProcessamentoRepository processamentoRepository,
        ProcessamentoMapper processamentoMapper,
        ColetaRepository coletaRepository,
        EstoqueRepository estoqueRepository
    ) {
        this.processamentoRepository = processamentoRepository;
        this.processamentoMapper = processamentoMapper;
        this.coletaRepository = coletaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    /**
     * Save a processamento.
     *
     * @param processamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessamentoDTO save(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to save Processamento : {}", processamentoDTO);
        Processamento processamento = processamentoMapper.toEntity(processamentoDTO);
        processamento = processamentoRepository.save(processamento);
        return processamentoMapper.toDto(processamento);
    }

    /**
     * Update a processamento.
     *
     * @param processamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessamentoDTO update(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to update Processamento : {}", processamentoDTO);
        Processamento processamento = processamentoMapper.toEntity(processamentoDTO);
        processamento = processamentoRepository.save(processamento);
        return processamentoMapper.toDto(processamento);
    }

    /**
     * Partially update a processamento.
     *
     * @param processamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProcessamentoDTO> partialUpdate(ProcessamentoDTO processamentoDTO) {
        LOG.debug("Request to partially update Processamento : {}", processamentoDTO);

        return processamentoRepository
            .findById(processamentoDTO.getId())
            .map(existingProcessamento -> {
                processamentoMapper.partialUpdate(existingProcessamento, processamentoDTO);

                return existingProcessamento;
            })
            .map(processamentoRepository::save)
            .map(processamentoMapper::toDto);
    }

    /**
     *  Get all the processamentos where Coleta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessamentoDTO> findAllWhereColetaIsNull() {
        LOG.debug("Request to get all processamentos where Coleta is null");
        return StreamSupport.stream(processamentoRepository.findAll().spliterator(), false)
            .filter(processamento -> processamento.getColeta() == null)
            .map(processamentoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one processamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessamentoDTO> findOne(Long id) {
        LOG.debug("Request to get Processamento : {}", id);
        return processamentoRepository.findById(id).map(processamentoMapper::toDto);
    }

    /**
     * Delete the processamento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Processamento : {}", id);
        processamentoRepository.deleteById(id);
    }

    public ProcessamentoDTO criarProcessamentoComEstoque(ProcessamentoDTO processamentoDTO) {
        Coleta coleta = coletaRepository
            .findById(processamentoDTO.getColetaId())
            .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada"));

        // Modificar o status da coleta para PROCESSADA
        coleta.setStatusColeta(StatusColeta.PROCESSADA);
        coletaRepository.save(coleta);

        Processamento processamento = new Processamento();
        processamento.setDataProcessamento(processamentoDTO.getDataProcessamento());
        processamento.setTecnicoResponsavel(processamentoDTO.getTecnicoResponsavel());
        processamento.setValorAcidezDornic(processamentoDTO.getValorAcidezDornic());
        processamento.setValorCaloricoKcal(processamentoDTO.getValorCaloricoKcal());
        processamento.setResultadoAnalise(processamentoDTO.getResultadoAnalise());
        processamento.setStatusProcessamento(processamentoDTO.getStatusProcessamento());
        processamento.setColeta(coleta);

        Estoque estoque = null;
        if (ResultadoAnalise.APROVADO.equals(processamentoDTO.getResultadoAnalise())) {
            estoque = criarEstoqueFromProcessamento(processamento, coleta, processamentoDTO);
            estoque = estoqueRepository.save(estoque);

            processamento.setEstoque(estoque);
        }

        processamento = processamentoRepository.save(processamento);

        return processamentoMapper.toDto(processamento);
    }

    private Estoque criarEstoqueFromProcessamento(Processamento processamento, Coleta coleta, ProcessamentoDTO processamentoDTO) {
        Estoque estoque = new Estoque();

        estoque.setDataProducao(processamento.getDataProcessamento());
        estoque.setDataValidade(calcularDataValidade(processamento.getDataProcessamento()));
        estoque.setTipoLeite(processamentoDTO.getTipoLeite());
        estoque.setClassificacao(processamentoDTO.getClassificacaoLeite());
        estoque.setVolumeTotalMl(coleta.getVolumeMl());
        estoque.setVolumeDisponivelMl(coleta.getVolumeMl());
        estoque.setLocalArmazenamento(processamentoDTO.getLocalArmazenamento());
        estoque.setTemperaturaArmazenamento(processamentoDTO.getTemperaturaArmazenamento());
        estoque.setStatusLote(StatusLote.DISPONIVEL);

        return estoque;
    }

    private LocalDate calcularDataValidade(LocalDate dataProducao) {
        return dataProducao.plusMonths(6);
    }
}
