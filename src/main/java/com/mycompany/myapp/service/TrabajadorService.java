package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.TrabajadorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Trabajador}.
 */
public interface TrabajadorService {

    /**
     * Save a trabajador.
     *
     * @param trabajador the entity to save.
     * @return the persisted entity.
     */
    Trabajador save(Trabajador trabajador);

    /**
     * Get all the trabajadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrabajadorDTO> findAll(Pageable pageable);
     /**
     * Get all the sales from trabajadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Long getCounterAllSales(Long trabajadorId);



    /**
     * Get the "id" trabajador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Trabajador> findOne(Long id);

    /**
     * Delete the "id" trabajador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
