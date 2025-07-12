package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Doadora;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doadora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoadoraRepository extends JpaRepository<Doadora, Long>, JpaSpecificationExecutor<Doadora> {}
