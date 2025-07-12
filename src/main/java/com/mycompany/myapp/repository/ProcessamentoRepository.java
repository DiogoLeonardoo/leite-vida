package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Processamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Processamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessamentoRepository extends JpaRepository<Processamento, Long>, JpaSpecificationExecutor<Processamento> {}
