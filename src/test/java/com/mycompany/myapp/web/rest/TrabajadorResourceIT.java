package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ConcesionarioApp;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.service.TrabajadorService;
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
 * Integration tests for the {@Link TrabajadorResource} REST controller.
 */
@SpringBootTest(classes = ConcesionarioApp.class)
public class TrabajadorResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private TrabajadorService trabajadorService;

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

    private MockMvc restTrabajadorMockMvc;

    private Trabajador trabajador;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrabajadorResource trabajadorResource = new TrabajadorResource(trabajadorService);
        this.restTrabajadorMockMvc = MockMvcBuilders.standaloneSetup(trabajadorResource)
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
    public static Trabajador createEntity(EntityManager em) {
        Trabajador trabajador = new Trabajador()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .cargo(DEFAULT_CARGO)
            .telefono(DEFAULT_TELEFONO);
        return trabajador;
    }

    @BeforeEach
    public void initTest() {
        trabajador = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrabajador() throws Exception {
        int databaseSizeBeforeCreate = trabajadorRepository.findAll().size();

        // Create the Trabajador
        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isCreated());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeCreate + 1);
        Trabajador testTrabajador = trabajadorList.get(trabajadorList.size() - 1);
        assertThat(testTrabajador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testTrabajador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTrabajador.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testTrabajador.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testTrabajador.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    public void createTrabajadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trabajadorRepository.findAll().size();

        // Create the Trabajador with an existing ID
        trabajador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTrabajadors() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList
        restTrabajadorMockMvc.perform(get("/api/trabajadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.toString())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }
    
    @Test
    @Transactional
    public void getTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trabajador.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    public void getNonExistingTrabajador() throws Exception {
        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajador() throws Exception {
        // Initialize the database
        trabajadorService.save(trabajador);

        int databaseSizeBeforeUpdate = trabajadorRepository.findAll().size();

        // Update the trabajador
        Trabajador updatedTrabajador = trabajadorRepository.findById(trabajador.getId()).get();
        // Disconnect from session so that the updates on updatedTrabajador are not directly saved in db
        em.detach(updatedTrabajador);
        updatedTrabajador
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cargo(UPDATED_CARGO)
            .telefono(UPDATED_TELEFONO);

        restTrabajadorMockMvc.perform(put("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrabajador)))
            .andExpect(status().isOk());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeUpdate);
        Trabajador testTrabajador = trabajadorList.get(trabajadorList.size() - 1);
        assertThat(testTrabajador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testTrabajador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajador.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testTrabajador.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testTrabajador.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void updateNonExistingTrabajador() throws Exception {
        int databaseSizeBeforeUpdate = trabajadorRepository.findAll().size();

        // Create the Trabajador

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc.perform(put("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrabajador() throws Exception {
        // Initialize the database
        trabajadorService.save(trabajador);

        int databaseSizeBeforeDelete = trabajadorRepository.findAll().size();

        // Delete the trabajador
        restTrabajadorMockMvc.perform(delete("/api/trabajadors/{id}", trabajador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trabajador.class);
        Trabajador trabajador1 = new Trabajador();
        trabajador1.setId(1L);
        Trabajador trabajador2 = new Trabajador();
        trabajador2.setId(trabajador1.getId());
        assertThat(trabajador1).isEqualTo(trabajador2);
        trabajador2.setId(2L);
        assertThat(trabajador1).isNotEqualTo(trabajador2);
        trabajador1.setId(null);
        assertThat(trabajador1).isNotEqualTo(trabajador2);
    }
}
