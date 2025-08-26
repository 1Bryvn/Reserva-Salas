package com.bryanbmo.reserva_salas.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataUser {
    private Long id;
    private String nombre;
    private String email;
    private String rol; // ADMIN o ESTUDIANTE
    private Boolean activo;
}
