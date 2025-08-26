package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.config.JwtAuthenticationFilter;
import com.bryanbmo.reserva_salas.config.JwtTokenProvider;
import com.bryanbmo.reserva_salas.vo.DataUser;
import com.bryanbmo.reserva_salas.vo.DataSala;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private ReservaService reservaService;

    @Test
    void testGetAllReservas() throws Exception {
        DataUser user = new DataUser(11L, "Pedro Torres", "pedro.torres@estudiante.cl", "ESTUDIANTE", true);
        DataSala sala = new DataSala("Sala 101", "Edificio A - Piso 1", 8, "Sala pequeña con TV y pizarra");
        ReservaVO reserva = new ReservaVO(9L, LocalDate.parse("2025-08-15"), LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00"), "RESERVADA", user, sala);

        List<ReservaVO> mockReservas = List.of(reserva);

        // Simular comportamiento del servicio
        when(reservaService.findAllReservas()).thenReturn(mockReservas);

        // JSON esperado usando text block
        String expectedJson = """
        {
          "status": true,
          "message": "Reservas obtenidas con éxito",
          "data": [
            {
              "id": 9,
              "fechaReserva": "2025-08-15",
              "horaInicio": "14:00:00",
              "horaFin": "15:30:00",
              "estado": "RESERVADA",
              "usuario": {
                "id": 11,
                "nombre": "Pedro Torres",
                "email": "pedro.torres@estudiante.cl",
                "rol": "ESTUDIANTE",
                "activo": true
              },
              "sala": {
                "nombre": "Sala 101",
                "ubicacion": "Edificio A - Piso 1",
                "capacidad": 8,
                "descripcion": "Sala pequeña con TV y pizarra"
              }
            }
          ]
        }
        """;
        mockMvc.perform(get("/api/getReservas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}

