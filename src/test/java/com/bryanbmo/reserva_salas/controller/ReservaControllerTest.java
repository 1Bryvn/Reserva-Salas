package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;


    @Test
    void testUpdateReserva_success() throws Exception{
        Long id = 1L;

        ReservaVO reservaVO = new ReservaVO();

        when(reservaService.updateReserva(eq(id), any(ReservaVO.class))).thenReturn(1);

        mockMvc.perform(put("/api/updateReservaById/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                    "fechaReserva": "2025-08-15",
                                    "horaInicio": "14:00:00",
                                    "horaFin": "15:30:00",
                                    "estado": "RESERVADA",
                                    "usuario": { "id": 1 },
                                    "sala": { "id": 1 }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Reserva actualizada con éxito"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testUpdateReserva_error() throws Exception {
        Long id = 1L;

        when(reservaService.updateReserva(eq(id), any(ReservaVO.class)))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(put("/api/updateReservaById/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "fechaReserva": "2025-08-15",
                                    "horaInicio": "14:00:00",
                                    "horaFin": "15:30:00",
                                    "estado": "RESERVADA",
                                    "usuario": { "id": 1 },
                                    "sala": { "id": 1 }
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("ERROR, no se ha podido actualizar"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

}
