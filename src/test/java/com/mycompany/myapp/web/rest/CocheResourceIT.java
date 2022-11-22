package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ConcesionarioApp;
import com.mycompany.myapp.domain.Coche;
import com.mycompany.myapp.repository.CocheRepository;
import com.mycompany.myapp.service.CocheService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link CocheResource} REST controller.
 */
@SpringBootTest(classes = ConcesionarioApp.class)
public class CocheResourceIT {

    private static final String DEFAULT_AUDI = "AAAAAAAAAA";
    private static final String UPDATED_AUDI = "BBBBBBBBBB";

    @Autowired
    private CocheRepository cocheRepository;

    @Autowired
    private CocheService cocheService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCocheMockMvc;

    private Coche coche;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CocheResource cocheResource = new CocheResource(cocheService);
        this.restCocheMockMvc = MockMvcBuilders.standaloneSetup(cocheResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coche createEntity(EntityManager em) {
        Coche coche = new Coche()
            .audi(DEFAULT_AUDI);
        return coche;
    }

    @BeforeEach
    public void initTest() {
        coche = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoche() throws Exception {
        int databaseSizeBeforeCreate = cocheRepository.findAll().size();

        // Create the Coche
        restCocheMockMvc.perform(post("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isCreated());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeCreate + 1);
        Coche testCoche = cocheList.get(cocheList.size() - 1);
        assertThat(testCoche.getAudi()).isEqualTo(DEFAULT_AUDI);
    }

    @Test
    @Transactional
    public void createCocheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cocheRepository.findAll().size();

        // Create the Coche with an existing ID
        coche.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCocheMockMvc.perform(post("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCoches() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get all the cocheList
        restCocheMockMvc.perform(get("/api/coches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coche.getId().intValue())))
            .andExpect(jsonPath("$.[*].audi").value(hasItem(DEFAULT_AUDI.toString())));
    }
    
    @Test
    @Transactional
    public void getCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get the coche
        restCocheMockMvc.perform(get("/api/coches/{id}", coche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coche.getId().intValue()))
            .andExpect(jsonPath("$.audi").value(DEFAULT_AUDI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoche() throws Exception {
        // Get the coche
        restCocheMockMvc.perform(get("/api/coches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoche() throws Exception {
        // Initialize the database
        cocheService.save(coche);

        int databaseSizeBeforeUpdate = cocheRepository.findAll().size();

        // Update the coche
        Coche updatedCoche = cocheRepository.findById(coche.getId()).get();
        // Disconnect from session so that the updates on updatedCoche are not directly saved in db
        em.detach(updatedCoche);
        updatedCoche
            .audi(UPDATED_AUDI);

        restCocheMockMvc.perform(put("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoche)))
            .andExpect(status().isOk());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeUpdate);
        Coche testCoche = cocheList.get(cocheList.size() - 1);
        assertThat(testCoche.getAudi()).isEqualTo(UPDATED_AUDI);
    }

    @Test
    @Transactional
    public void updateNonExistingCoche() throws Exception {
        int databaseSizeBeforeUpdate = cocheRepository.findAll().size();

        // Create the Coche

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCocheMockMvc.perform(put("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoche() throws Exception {
        // Initialize the database
        cocheService.save(coche);

        int databaseSizeBeforeDelete = cocheRepository.findAll().size();

        // Delete the coche
        restCocheMockMvc.perform(delete("/api/coches/{id}", coche.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coche.class);
        Coche coche1 = new Coche();
        coche1.setId(1L);
        Coche coche2 = new Coche();
        coche2.setId(coche1.getId());
        assertThat(coche1).isEqualTo(coche2);
        coche2.setId(2L);
        assertThat(coche1).isNotEqualTo(coche2);
        coche1.setId(null);
        assertThat(coche1).isNotEqualTo(coche2);
    }
}
