package com.sofkau.employees.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofkau.employees.employees.dto.EmpleadoDTO;
import com.sofkau.employees.employees.services.ServicioEmpleado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ControladorEmpleadoTest {

    @MockBean
    private ServicioEmpleado servicioEmpleado;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /empleados success")
    public void findAll() throws Exception {
        //setup mock service
        var dato1 = new EmpleadoDTO();
        dato1.setId("1111");
        dato1.setNombre("Jorge Ramirez");
        dato1.setRol("Gerente");
        var dato2 = new EmpleadoDTO();
        dato2.setId("2222");
        dato2.setNombre("Pedro Contreras");
        dato2.setRol("Vicepresidente");
        var lista = new ArrayList<EmpleadoDTO>();
        lista.add(dato1);
        lista.add(dato2);
        Mockito.when(servicioEmpleado.obtenerTodos()).thenReturn(lista);

        //execute Get request
        mockMvc.perform(get("/empleados"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1111"))
                .andExpect(jsonPath("$[0].nombre").value("Jorge Ramirez"))
                .andExpect(jsonPath("$[0].rol").value("Gerente"))
                .andExpect(jsonPath("$[1].id").value("2222"))
                .andExpect(jsonPath("$[1].nombre").value("Pedro Contreras"))
                .andExpect(jsonPath("$[1].rol").value("Vicepresidente"));
    }

    @Test
    @DisplayName("POST /empleados/crear success")
    public void create() throws Exception {
        // Setup our mocked service
        var datoPost = new EmpleadoDTO();
        datoPost.setNombre("Jorge Ramirez");
        datoPost.setRol("Gerente");

        var datoReturn = new EmpleadoDTO();
        datoReturn.setId("2222");
        datoReturn.setNombre("Jorge Ramirez");
        datoReturn.setRol("Gerente");

        Mockito.when(servicioEmpleado.crear(datoPost)).thenReturn(datoReturn);

        // Execute the POST request
        mockMvc.perform(post("/empleados/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(datoPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id").value("2222"))
                .andExpect(jsonPath("$.nombre").value("Jorge Ramirez"))
                .andExpect(jsonPath("$.rol").value("Gerente"));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
