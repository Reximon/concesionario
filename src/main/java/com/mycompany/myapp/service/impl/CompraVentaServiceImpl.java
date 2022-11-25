package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CompraVentaService;
import com.mycompany.myapp.domain.CompraVenta;
import com.mycompany.myapp.domain.Vehiculo;
import com.mycompany.myapp.repository.CompraVentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CompraVenta}.
 */
@Service
@Transactional
public class CompraVentaServiceImpl implements CompraVentaService {

    private final Logger log = LoggerFactory.getLogger(CompraVentaServiceImpl.class);

    private final CompraVentaRepository compraVentaRepository;

    public CompraVentaServiceImpl(CompraVentaRepository compraVentaRepository) {
        this.compraVentaRepository = compraVentaRepository;
    }

    /**
     * Save a compraVenta.
     *
     * @param compraVenta the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CompraVenta save(CompraVenta compraVenta) {
        log.debug("Request to save CompraVenta : {}", compraVenta);
        Vehiculo vehiculo = compraVenta.getVehiculo();
        compraVenta.setPrecioTotal((double)vehiculo.getPrecio() * 1.21);
        LocalDate date = LocalDate.now().plusDays(1);
        compraVenta.setFechaVenta(date);
        return compraVentaRepository.save(compraVenta);
    }

    /**SS
     * Get all the compraVentas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompraVenta> findAll(Pageable pageable) {
        log.debug("Request to get all CompraVentas");
        return compraVentaRepository.findAll(pageable);
    }


    /**
     * Get one compraVenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompraVenta> findOne(Long id) {
        log.debug("Request to get CompraVenta : {}", id);
        return compraVentaRepository.findById(id);
    }

    /**
     * Delete the compraVenta by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompraVenta : {}", id);
        compraVentaRepository.deleteById(id);
    }
}
