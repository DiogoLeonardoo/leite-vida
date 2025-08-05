package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.service.dto.DoadoraColetasProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doadora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoadoraRepository extends JpaRepository<Doadora, Long>, JpaSpecificationExecutor<Doadora> {
    boolean existsByCpf(String cpf);

    @Query(
        """
            SELECT d FROM Doadora d
            WHERE
                (:search IS NULL OR :search = ''
                 OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR d.cpf LIKE CONCAT('%', :search, '%'))
        """
    )
    Page<Doadora> searchByNomeOrCpf(@Param("search") String search, Pageable pageable);

    Optional<Doadora> findByCpf(String cpf);

    @Query(
        """
            SELECT
                c.id AS coletaId,
                c.dataColeta AS dataColeta,
                c.volumeMl AS volumeMl,
                c.localColeta AS localColeta,
                c.statusColeta AS statusColeta,
                d.nome AS nomeDoadora,
                d.cpf AS cpfDoadora,
                d.telefone AS telefoneDoadora
            FROM Coleta c
            JOIN c.doadora d
            WHERE d.id = :doadoraId
              AND (CAST(:dataInicio AS LocalDate) IS NULL OR c.dataColeta >= :dataInicio)
              AND (CAST(:dataFim AS LocalDate) IS NULL OR c.dataColeta <= :dataFim)
            ORDER BY c.dataColeta DESC
        """
    )
    List<DoadoraColetasProjection> buscarPorDoadoraEPeriodo(
        @Param("doadoraId") Long doadoraId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
}
