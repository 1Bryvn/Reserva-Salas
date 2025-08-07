package com.bryanbmo.reserva_salas.vo;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaVO {
    private Long id;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private UserEntity usuario;
    private SalaEntity sala;
}
