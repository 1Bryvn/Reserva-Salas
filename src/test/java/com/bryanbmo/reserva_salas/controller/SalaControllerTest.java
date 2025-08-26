package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.config.JwtAuthenticationFilter;
import com.bryanbmo.reserva_salas.config.JwtTokenProvider;
import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.service.SalaService;
import com.bryanbmo.reserva_salas.vo.DataSala;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaController.class)
@AutoConfigureMockMvc(addFilters = false)
class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private SalaService salaService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testGetAllSalas() throws Exception {
        // Crear salas basadas en tu JSON de ejemplo
        SalaEntity salas = new SalaEntity(1L, "Sala 101", "Edificio A - Piso 1", 8, "Sala pequeña con TV y pizarra");
        List<SalaEntity> mockSalas = List.of(salas);
        when(salaService.findAllSalas()).thenReturn(mockSalas);
        String expectedJson = """
       
                {
             "status": true,
             "message": "Salas obtenidas con exito",
             "data": [
                 {
                     "id": 1,
                     "nombre": "Sala 101",
                     "ubicacion": "Edificio A - Piso 1",
                     "capacidad": 8,
                     "descripcion": "Sala pequeña con TV y pizarra"
                 }
             ]
         }
        """;
        mockMvc.perform(get("/api/getSalas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testFindSalaById() throws Exception {
        // Arrange: crear entidad simulada
        SalaEntity sala = new SalaEntity(
                1L,
                "Sala 101",
                "Edificio A - Piso 1",
                8,
                "Sala pequeña con TV y pizarra"
        );

        when(salaService.findSalaById(1L)).thenReturn(sala);

        // Expected JSON
        ResponseDTO expectedResponse = new ResponseDTO(
                true,
                "Id de Sala encontrada con exito", sala
        );
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        // Act + Assert
        mockMvc.perform(get("/api/findSalaById/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }




    @Test
    void testUpdateSala() throws Exception {
        Long id = 5L;

        // Crear SalaEntity para enviar como request
        SalaEntity updatedSala = new SalaEntity(
                5L,
                "Sala 404",
                "Edificio D - Piso 4",
                10,
                "Sala mediana con computadoras"
        );

        // Mockear el servicio
        when(salaService.updateSala(eq(id), any(SalaEntity.class))).thenReturn(1);

        // Serializar request y expected response
        String requestJson = objectMapper.writeValueAsString(updatedSala);
        String expectedJson = objectMapper.writeValueAsString(
                new ResponseDTO(true, "Sala actualizada con éxito", 1)
        );

        mockMvc.perform(put("/api/updateSalaById/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }



    @Test
    void testCreateSala() throws Exception {
        DataSala salaRequest = new DataSala(
                "Sala 101",
                "Edificio A - Piso 1",
                8,
                "Sala pequeña con TV y pizarra"
        );

        when(salaService.createSala(any(DataSala.class))).thenReturn(1);

        String requestJson = objectMapper.writeValueAsString(salaRequest);
        String expectedJson = objectMapper.writeValueAsString(
                new ResponseDTO(true, "Sala creada con exito", 1)
        );

        mockMvc.perform(post("/api/createSala")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }


    @Test
    void testDeleteSala() throws Exception {
        mockMvc.perform(delete("/api/deleteSala/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("La Sala a sido eliminada"));
    }
}
