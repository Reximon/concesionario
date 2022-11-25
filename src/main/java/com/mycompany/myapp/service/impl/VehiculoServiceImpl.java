package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.VehiculoService;
import com.mycompany.myapp.domain.Vehiculo;
import com.mycompany.myapp.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Vehiculo}.
 */
@Service
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final Logger log = LoggerFactory.getLogger(VehiculoServiceImpl.class);

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Save a vehiculo.
     *
     * @param vehiculo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Vehiculo save(Vehiculo vehiculo) {
        log.debug("Request to save Vehiculo : {}", vehiculo);
        return vehiculoRepository.save(vehiculo);
    }

    /**
     * Get all the vehiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Vehiculo> findAll(Pageable pageable) {
        log.debug("Request to get all Vehiculos");
        return vehiculoRepository.findAll(pageable);
    }

    /**
     * Get disponibl the vehiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Vehiculo> findDisponible(Pageable pageable) {
        log.debug("Request to get all Vehiculos");
        return vehiculoRepository.findDisponible(pageable);
    }

      /**
     * Get disponibl the vehiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Vehiculo> findNoDisponible(Pageable pageable) {
        log.debug("Request to get all Vehiculos");
        return vehiculoRepository.findNoDisponible(pageable);
    }



    /**
     * Get one vehiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Vehiculo> findOne(Long id) {
        log.debug("Request to get Vehiculo : {}", id);
        return vehiculoRepository.findById(id);
    }

    /**
     * Delete the vehiculo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vehiculo : {}", id);
        vehiculoRepository.deleteById(id);
    }
}
