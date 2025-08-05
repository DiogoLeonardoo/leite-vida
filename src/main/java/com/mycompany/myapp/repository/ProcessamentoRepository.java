package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.service.dto.ProcessamentoProjection;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Processamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessamentoRepository extends JpaRepository<Processamento, Long>, JpaSpecificationExecutor<Processamento> {
    @Query(
        """
            SELECT
                p.id AS idProcessamento,
                p.dataProcessamento AS dataProcessamento,
                p.tecnicoResponsavel AS tecnicoResponsavel,
                p.valorAcidezDornic AS valorAcidezDornic,
                p.valorCaloricoKcal AS valorCaloricoKcal,
                p.resultadoAnalise AS resultadoAnalise,
                p.statusProcessamento AS statusProcessamento,
                p.coleta.id AS coletaId
            FROM Processamento p
            WHERE p.dataProcessamento BETWEEN :dataInicio AND :dataFim
            ORDER BY p.dataProcessamento
        """
    )
    List<ProcessamentoProjection> buscarPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
