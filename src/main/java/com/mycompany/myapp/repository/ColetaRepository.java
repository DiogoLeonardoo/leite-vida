package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coleta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Coleta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long>, JpaSpecificationExecutor<Coleta> {}
