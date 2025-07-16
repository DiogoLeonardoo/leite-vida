package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coleta;
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
}
