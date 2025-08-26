package com.bryanbmo.reserva_salas.service;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.vo.DataSala;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaService {
    List<SalaEntity> findAllSalas();
    SalaEntity findSalaById(@Param("id") Long id);
    Integer createSala(@Param("dataSala") DataSala dataSala);
    Integer updateSala(@Param("id") Long id, @Param("salaEntity") SalaEntity salaEntity);
    Integer deleteSala(@Param("id") Long id);
}
