package com.bryanbmo.reserva_salas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    private Long id;
    private String nombre;
    private String email;
    private String contrasena;
    private String rol; // ADMIN o ESTUDIANTE
    private Boolean activo;


}
