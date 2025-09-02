package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Distribuicao;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import com.mycompany.myapp.repository.DistribuicaoRepository;
import com.mycompany.myapp.repository.EstoqueRepository;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.service.dto.DistribuicaoDTO;
import com.mycompany.myapp.service.dto.DistribuicaoDetalhesDTO;
import com.mycompany.myapp.service.dto.DistribuicaoRequestDTO;
import com.mycompany.myapp.service.mapper.DistribuicaoMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.mycompany.myapp.domain.Distribuicao}.
 */
@Service
@Transactional
public class DistribuicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(DistribuicaoService.class);

    private final DistribuicaoRepository distribuicaoRepository;

    private final DistribuicaoMapper distribuicaoMapper;

    private final EstoqueRepository estoqueRepository;

    private final PacienteRepository pacienteRepository;

    public DistribuicaoService(
        DistribuicaoRepository distribuicaoRepository,
        DistribuicaoMapper distribuicaoMapper,
        EstoqueRepository estoqueRepository,
        PacienteRepository pacienteRepository
    ) {
        this.distribuicaoRepository = distribuicaoRepository;
        this.distribuicaoMapper = distribuicaoMapper;
        this.estoqueRepository = estoqueRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Save a distribuicao.
     *
     * @param distribuicaoDTO the entity to save.
     * @return the persisted entity.
     */
    public DistribuicaoDTO save(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to save Distribuicao : {}", distribuicaoDTO);
        Distribuicao distribuicao = distribuicaoMapper.toEntity(distribuicaoDTO);
        distribuicao = distribuicaoRepository.save(distribuicao);
        return distribuicaoMapper.toDto(distribuicao);
    }

    /**
     * Update a distribuicao.
     *
     * @param distribuicaoDTO the entity to save.
     * @return the persisted entity.
     */
    public DistribuicaoDTO update(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to update Distribuicao : {}", distribuicaoDTO);
        Distribuicao distribuicao = distribuicaoMapper.toEntity(distribuicaoDTO);
        distribuicao = distribuicaoRepository.save(distribuicao);
        return distribuicaoMapper.toDto(distribuicao);
    }

    /**
     * Partially update a distribuicao.
     *
     * @param distribuicaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DistribuicaoDTO> partialUpdate(DistribuicaoDTO distribuicaoDTO) {
        LOG.debug("Request to partially update Distribuicao : {}", distribuicaoDTO);

        return distribuicaoRepository
            .findById(distribuicaoDTO.getId())
            .map(existingDistribuicao -> {
                distribuicaoMapper.partialUpdate(existingDistribuicao, distribuicaoDTO);

                return existingDistribuicao;
            })
            .map(distribuicaoRepository::save)
            .map(distribuicaoMapper::toDto);
    }

    /**
     * Get one distribuicao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DistribuicaoDTO> findOne(Long id) {
        LOG.debug("Request to get Distribuicao : {}", id);
        return distribuicaoRepository.findById(id).map(distribuicaoMapper::toDto);
    }

    /**
     * Delete the distribuicao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Distribuicao : {}", id);
        distribuicaoRepository.deleteById(id);
    }

    @Transactional
    public boolean realizarDistribuicao(DistribuicaoRequestDTO dto) {
        Estoque estoque = estoqueRepository.findById(dto.getEstoqueId()).orElse(null);
        if (estoque == null) return false;

        if (dto.getVolumeDistribuidoMl() > estoque.getVolumeDisponivelMl()) return false;

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).orElse(null);
        if (paciente == null) return false;

        double novoVolume = estoque.getVolumeDisponivelMl() - dto.getVolumeDistribuidoMl();
        estoque.setVolumeDisponivelMl(novoVolume);

        if (novoVolume <= 0) {
            estoque.setStatusLote(StatusLote.DISTRIBUIDO);
        }

        estoqueRepository.save(estoque);

        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setDataDistribuicao(LocalDate.now());
        distribuicao.setVolumeDistribuidoMl(dto.getVolumeDistribuidoMl());
        distribuicao.setEstoque(estoque);
        distribuicao.setPaciente(paciente);
        distribuicao.setResponsavelEntrega(dto.getResponsavelEntrega());
        distribuicao.setResponsavelRecebimento(dto.getResponsavelRecebimento());
        distribuicao.setObservacoes(dto.getObservacoes());

        distribuicaoRepository.save(distribuicao);
        return true;
    }

    /**
     * Busca detalhes de uma distribuição específica.
     *
     * @param idDistribuicao o ID da distribuição a ser consultada
     * @return DTO com os detalhes da distribuição ou null se não encontrado
     */
    @Transactional(readOnly = true)
    public DistribuicaoDetalhesDTO buscarDetalhesDistribuicao(Long idDistribuicao) {
        LOG.debug("Request to get details for Distribuicao : {}", idDistribuicao);

        List<Object[]> results = distribuicaoRepository.buscarDetalhesDistribuicao(idDistribuicao);

        if (results == null || results.isEmpty()) {
            return null;
        }

        Object[] row = results.get(0);
        DistribuicaoDetalhesDTO dto = new DistribuicaoDetalhesDTO();

        // Mapeamento dos resultados para o DTO
        dto.setNomePaciente((String) row[0]);
        dto.setTelefoneResponsavel((String) row[1]);
        dto.setCpfResponsavel((String) row[2]);
        dto.setParentescoResponsavel((String) row[3]);
        dto.setEstoqueId(row[4] != null ? ((Number) row[4]).longValue() : null);

        // Conversão de Strings para enums
        if (row[5] != null) {
            try {
                dto.setTipoLeite(TipoLeite.valueOf((String) row[5]));
            } catch (IllegalArgumentException e) {
                LOG.warn("Tipo de leite inválido: {}", row[5]);
            }
        }

        if (row[6] != null) {
            try {
                dto.setClassificacao(ClassificacaoLeite.valueOf((String) row[6]));
            } catch (IllegalArgumentException e) {
                LOG.warn("Classificação de leite inválida: {}", row[6]);
            }
        }

        dto.setNomeDoadora((String) row[7]);

        return dto;
    }

    public Page<Distribuicao> buscarDistribuicoesComFiltros(String searchTerm, Pageable pageable) {
        return distribuicaoRepository.buscarDistribuicoesPorPaciente(searchTerm, pageable);
    }
}
