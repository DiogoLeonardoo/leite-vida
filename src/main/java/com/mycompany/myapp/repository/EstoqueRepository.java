package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import com.mycompany.myapp.service.dto.EstoqueDoadoraDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Estoque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {
    @Query("SELECT SUM(e.volumeDisponivelMl) FROM Estoque e")
    Long somarVolumeDisponivelMl();

    @Query(
        """
            SELECT e FROM Estoque e
            WHERE (:tipoLeite IS NULL OR e.tipoLeite = :tipoLeite)
              AND (:classificacao IS NULL OR e.classificacao = :classificacao)
              AND (:statusLote IS NULL OR e.statusLote = :statusLote)
        """
    )
    Page<Estoque> buscarComFiltros(
        @Param("tipoLeite") TipoLeite tipoLeite,
        @Param("classificacao") ClassificacaoLeite classificacao,
        @Param("statusLote") StatusLote statusLote,
        Pageable pageable
    );

    @Query(
        value = """
        SELECT
            e.id,
            e.data_validade,
            e.tipo_leite,
            e.classificacao,
            e.volume_disponivel_ml,
            e.temperatura_armazenamento,
            d.nome,
            d.cpf,
            d.telefone,
            p.tecnico_responsavel,
            p.data_processamento,
            p.valor_acidez_dornic,
            p.valor_calorico_kcal
        FROM
            estoque e
        JOIN
            processamento p ON p.estoque_id = e.id
        JOIN
            coleta c ON c.id = p.coleta_id
        JOIN
            doadora d ON d.id = c.doadora_id
        WHERE
            e.id = :estoqueId
        """,
        nativeQuery = true
    )
    List<Object[]> buscarDoadoraEstoque(@Param("estoqueId") Long estoqueId);

    @Query(
        value = "SELECT e FROM Estoque e WHERE " +
        "(:tipoLeite IS NULL OR e.tipoLeite = :tipoLeite) AND " +
        "(:statusLote IS NULL OR e.statusLote = :statusLote) AND " +
        "(:classificacao IS NULL OR e.classificacao = :classificacao) AND " +
        "((:dataInicio IS NULL AND :dataFim IS NULL) OR " +
        " (:dataInicio IS NOT NULL AND :dataFim IS NOT NULL AND e.dataProducao BETWEEN :dataInicio AND :dataFim))"
    )
    List<Estoque> buscarComFiltros(
        @Param("tipoLeite") TipoLeite tipoLeite,
        @Param("statusLote") StatusLote statusLote,
        @Param("classificacao") ClassificacaoLeite classificacao,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
}
