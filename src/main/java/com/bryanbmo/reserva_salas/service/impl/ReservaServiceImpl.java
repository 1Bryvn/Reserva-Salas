package com.bryanbmo.reserva_salas.service.impl;

import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.mapper.ReservaMapper;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private  ReservaMapper reservaMapper;

    public Integer createReserva(ReservaVO reservaVO){
        return reservaMapper.createReserva(reservaVO);
    }

    @Override
    public ReservaEntity findReservaById(Long id) {
        return reservaMapper.findReservaById(id);
    }

    public List<ReservaVO> findAllReservas(){
        return reservaMapper.findAllReservas();
    }

    public Integer updateReserva(@Param("id") Long id, @Param("reservaVO") ReservaVO reservaVO){
        return reservaMapper.updateReserva(id, reservaVO);
    }
    public Integer deleteReserva(Long id){
        return reservaMapper.deleteReserva(id);

    }
}
