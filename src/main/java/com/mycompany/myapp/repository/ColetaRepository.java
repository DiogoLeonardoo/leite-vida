package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Coleta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long>, JpaSpecificationExecutor<Coleta> {
    @Query(value = "SELECT SUM(volume_ml) FROM coleta WHERE status_coleta = 'AGUARDANDO_PROCESSAMENTO'", nativeQuery = true)
    Double somarVolumeMlPorStatus();

    @Query(
        """
            SELECT c FROM Coleta c
            WHERE (:status IS NULL OR c.statusColeta = :status)
              AND (:id IS NULL OR c.id = :id)
        """
    )
    Page<Coleta> findByStatusAndIdOptional(@Param("status") StatusColeta status, @Param("id") Long id, Pageable pageable);
}
