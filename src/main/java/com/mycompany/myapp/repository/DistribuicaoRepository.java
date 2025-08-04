package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Distribuicao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Distribuicao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistribuicaoRepository extends JpaRepository<Distribuicao, Long>, JpaSpecificationExecutor<Distribuicao> {
    @Query(
        """
            SELECT d FROM Distribuicao d
            JOIN d.paciente p
            WHERE (
                (:searchTerm IS NULL OR :searchTerm = '')
                OR (
                    CAST(d.id AS string) = :searchTerm
                    OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(p.cpfResponsavel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                )
            )
            ORDER BY p.nome
        """
    )
    Page<Distribuicao> buscarDistribuicoesPorPaciente(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(
        value = """
        SELECT
            p.nome AS nome_paciente,
            p.telefone_responsavel,
            p.cpf_responsavel,
            p.parentesco_responsavel,
            e.id AS estoque_id,
            e.tipo_leite,
            e.classificacao,
            d.nome AS nome_doadora
        FROM
            distribuicao dist
        JOIN paciente p ON p.id = dist.paciente_id
        JOIN estoque e ON e.id = dist.estoque_id
        LEFT JOIN processamento proc ON proc.estoque_id = e.id
        LEFT JOIN coleta c ON c.id = proc.coleta_id
        LEFT JOIN doadora d ON d.id = c.doadora_id
        WHERE
            dist.id = :idDistribuicao
        """,
        nativeQuery = true
    )
    List<Object[]> buscarDetalhesDistribuicao(@Param("idDistribuicao") Long idDistribuicao);
}
