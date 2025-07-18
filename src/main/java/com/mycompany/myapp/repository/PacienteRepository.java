package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {
    @Query(
        "SELECT p FROM Paciente p WHERE " +
        "(:searchTerm IS NULL OR :searchTerm = '' OR " +
        "LOWER(p.nome) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(p.cpfResponsavel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
        "AND p.statusAtivo = true " +
        "ORDER BY p.nome"
    )
    Page<Paciente> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
