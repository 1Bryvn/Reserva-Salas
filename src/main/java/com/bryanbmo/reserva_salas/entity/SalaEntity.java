package com.bryanbmo.reserva_salas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaEntity {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Integer capacidad;
    private String descripcion;

}
