package com.bryanbmo.reserva_salas.service.impl;

import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.mapper.ReservaMapper;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {


    private final ReservaMapper reservaMapper;

    public Integer createReserva(ReservaVO reservaVO){
        return reservaMapper.createReserva(reservaVO);
    }
    public List<ReservaEntity> findReservasByUsuario(Long usuarioId){
        return reservaMapper.findReservasByUsuario(usuarioId);
    }


}
