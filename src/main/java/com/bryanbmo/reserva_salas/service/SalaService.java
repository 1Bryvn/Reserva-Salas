package com.bryanbmo.reserva_salas.service;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.vo.SalaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaService {
    List<SalaEntity> findAllSalas();
    SalaEntity findSalaById(@Param("id") Long id);
    Integer createSala(@Param("salaVO") SalaVO salaVO);
    Integer updateSala(@Param("id") Long id, @Param("salaVO") SalaVO salaVO);
    Integer deleteSala(@Param("id") Long id);
}
