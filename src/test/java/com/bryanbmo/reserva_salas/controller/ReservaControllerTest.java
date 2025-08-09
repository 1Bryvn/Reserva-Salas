package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;



    @Test
    void testFindReservaById_success() throws Exception {
        ReservaEntity reservaMock = ReservaEntity.builder()
                .id(6L)
                .fechaReserva(LocalDate.of(2025, 8, 15))
                .horaInicio(LocalTime.of(14, 0))
                .horaFin(LocalTime.of(15, 30))
                .estado("RESERVADA")
                .usuario(UserEntity.builder()
                        .id(6L)
                        .nombre("Pedro Torres")
                        .email("pedro.torres@estudiante.cl")
                        .rol("ESTUDIANTE")
                        .build())
                .sala(SalaEntity.builder()
                        .id(1L)
                        .nombre("Sala 101")
                        .ubicacion("Edificio A - Piso 1")
                        .capacidad(8)
                        .descripcion("Sala pequeña con TV y pizarra")
                        .build())
                .build();

        when(reservaService.findReservaById(6L)).thenReturn(reservaMock);

        mockMvc.perform(get("/api/findReservaById/{id}", 6L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Id de reserva encontrada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(6))
                .andExpect(jsonPath("$.data.estado").value("RESERVADA"))
                .andExpect(jsonPath("$.data.usuario.email").value("pedro.torres@estudiante.cl"))
                .andExpect(jsonPath("$.data.sala.nombre").value("Sala 101"));
    }

    @Test
    void testGetAllReservas_success() throws Exception {
        List<ReservaEntity> reservaEntityList = List.of(
                ReservaEntity.builder()
                        .id(5L)
                        .fechaReserva(LocalDate.of(2025, 8, 15))
                        .horaInicio(LocalTime.of(14, 0))
                        .horaFin(LocalTime.of(15, 30))
                        .estado("RESERVADA")
                        .usuario(UserEntity.builder()
                                .id(6L)
                                .email("pedro.torres@estudiante.cl")
                                .rol("ESTUDIANTE")
                                .build())
                        .sala(SalaEntity.builder()
                                .id(1L)
                                .nombre("Sala 101")
                                .ubicacion("Edificio A - Piso 1")
                                .capacidad(8)
                                .descripcion("Sala pequeña con TV y pizarra")
                                .build())
                        .build(),
                ReservaEntity.builder()
                        .id(6L)
                        .fechaReserva(LocalDate.of(2025, 8, 15))
                        .horaInicio(LocalTime.of(14, 0))
                        .horaFin(LocalTime.of(15, 30))
                        .estado("RESERVADA")
                        .usuario(UserEntity.builder()
                                .id(5L)
                                .nombre("Pedro Torres")
                                .email("pedro.torres@estudiante.cl")
                                .rol("ESTUDIANTE")
                                .build())
                        .sala(SalaEntity.builder()
                                .id(1L)
                                .nombre("Sala 101")
                                .ubicacion("Edificio A - Piso 1")
                                .capacidad(8)
                                .descripcion("Sala pequeña con TV y pizarra")
                                .build())
                        .build()
        );

        when(reservaService.findAllReservas()).thenReturn(reservaEntityList);

        mockMvc.perform(get("/api/getReservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Reservas obtenidas con éxito"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(reservaEntityList.size()));
    }




    @Test
    void testCreateReserva_success() throws Exception {

        when(reservaService.createReserva(any(ReservaVO.class))).thenReturn(1);

        mockMvc.perform(post("/api/postReserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "fechaReserva": "2025-08-15",
                                "horaInicio": "14:00:00",
                                "horaFin": "15:30:00",
                                "estado": "RESERVADA",
                                "usuario": { 
                                         "email": "pedro.torres@estudiante.cl"
                                 },
                                "sala": { 
                                         "nombre": "Sala 202"
                                 }
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Reserva creada con exito"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testUpdateReserva_success() throws Exception{
        Long id = 1L;

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
                                    "usuario": { "id": 5 },
                                    "sala": { "id": 4 }
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("ERROR, no se ha podido actualizar"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

}
