package com.bryanbmo.reserva_salas.service.impl;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.mapper.SalaMapper;
import com.bryanbmo.reserva_salas.service.SalaService;
import com.bryanbmo.reserva_salas.vo.SalaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaMapper salaMapper;

    @Override
    public List<SalaEntity> findAllSalas() {
        return salaMapper.findAllSalas();
    }

    @Override
    public SalaEntity findSalaById(Long id) {
        return salaMapper.findSalaById(id);
    }

    @Override
    public Integer createSala(SalaVO salaVO) {
        return salaMapper.createSala(salaVO);
    }

    @Override
    public Integer updateSala(Long id, SalaVO salaVO) {
        return salaMapper.updateSala(id, salaVO);
    }

    @Override
    public Integer deleteSala(Long id) {
        return salaMapper.deleteSala(id);
    }


}
