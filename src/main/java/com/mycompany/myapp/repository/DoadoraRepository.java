package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Doadora;
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
}
