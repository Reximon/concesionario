package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vehiculo;
import org.springframework.data.domain.Pageable;


import javax.persistence.EntityManager;
import javax.validation.constraints.Null;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Vehiculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    @Query("select v from Vehiculo v LEFT JOIN v.venta cv WHERE cv IS null ")
    Page<Vehiculo> findDisponible(Pageable pageable);

    @Query("select v from Vehiculo v LEFT JOIN v.venta cv WHERE cv IS NOT null ")
    Page<Vehiculo> findNoDisponible(Pageable pageable);
}
