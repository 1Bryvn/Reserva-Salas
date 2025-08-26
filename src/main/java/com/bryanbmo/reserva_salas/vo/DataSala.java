package com.bryanbmo.reserva_salas.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSala {
    private String nombre;
    private String ubicacion;
    private Integer capacidad;
    private String descripcion;
}
