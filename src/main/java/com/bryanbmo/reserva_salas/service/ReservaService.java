package com.bryanbmo.reserva_salas.service;


import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReservaService {
    Integer createReserva(@Param("reservaVO") ReservaVO reservaVO);
    List<ReservaEntity> findAllReservas();
    ReservaEntity findReservaById(Long id);
    Integer updateReserva(@Param("id") Long id, @Param("reservaVO") ReservaVO reservaVO);
    Integer deleteReserva(Long id);
}
