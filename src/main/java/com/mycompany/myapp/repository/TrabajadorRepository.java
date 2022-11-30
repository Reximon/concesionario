package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Trabajador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
    @Query(" SELECT count(cv) FROM CompraVenta cv WHERE cv.vendedor.id = 4 ")
    Long getAllCounterSales(Long trabajadorId );
}
