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
    @Query("select u from Vehiculo u where u.tipo = 'coche'")
    Page<Vehiculo> findDisponible(Pageable pageable);
}
