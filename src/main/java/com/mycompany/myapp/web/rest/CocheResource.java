package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Coche;
import com.mycompany.myapp.service.CocheService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Coche}.
 */
@RestController
@RequestMapping("/api")
public class CocheResource {

    private final Logger log = LoggerFactory.getLogger(CocheResource.class);

    private static final String ENTITY_NAME = "coche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CocheService cocheService;

    public CocheResource(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    /**
     * {@code POST  /coches} : Create a new coche.
     *
     * @param coche the coche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coche, or with status {@code 400 (Bad Request)} if the coche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coches")
    public ResponseEntity<Coche> createCoche(@RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to save Coche : {}", coche);
        if (coche.getId() != null) {
            throw new BadRequestAlertException("A new coche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Coche result = cocheService.save(coche);
        return ResponseEntity.created(new URI("/api/coches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coches} : Updates an existing coche.
     *
     * @param coche the coche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coche,
     * or with status {@code 400 (Bad Request)} if the coche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coches")
    public ResponseEntity<Coche> updateCoche(@RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to update Coche : {}", coche);
        if (coche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Coche result = cocheService.save(coche);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coche.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /coches} : get all the coches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coches in body.
     */
    @GetMapping("/coches")
    public ResponseEntity<List<Coche>> getAllCoches(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Coches");
        Page<Coche> page = cocheService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coches/:id} : get the "id" coche.
     *
     * @param id the id of the coche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coches/{id}")
    public ResponseEntity<Coche> getCoche(@PathVariable Long id) {
        log.debug("REST request to get Coche : {}", id);
        Optional<Coche> coche = cocheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coche);
    }

    /**
     * {@code DELETE  /coches/:id} : delete the "id" coche.
     *
     * @param id the id of the coche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coches/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable Long id) {
        log.debug("REST request to delete Coche : {}", id);
        cocheService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
