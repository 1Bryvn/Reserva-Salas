package com.bryanbmo.reserva_salas.service.impl;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.mapper.SalaMapper;
import com.bryanbmo.reserva_salas.service.SalaService;
import com.bryanbmo.reserva_salas.vo.DataSala;
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
    public Integer createSala(DataSala dataSala) {
        return salaMapper.createSala(dataSala);
    }

    @Override
    public Integer updateSala(Long id, SalaEntity salaEntity) {
        return salaMapper.updateSala(id,salaEntity );
    }

    @Override
    public Integer deleteSala(Long id) {
        return salaMapper.deleteSala(id);
    }


}
