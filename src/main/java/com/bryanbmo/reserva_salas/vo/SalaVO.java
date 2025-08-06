package com.bryanbmo.reserva_salas.vo;

import lombok.Data;

@Data
public class SalaVO {
    private String nombre;
    private String ubicacion;
    private Integer capacidad;
    private String descripcion;
}
