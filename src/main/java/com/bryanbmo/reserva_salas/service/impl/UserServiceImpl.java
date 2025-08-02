package com.bryanbmo.reserva_salas.service.impl;

import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.mapper.UserMapper;
import com.bryanbmo.reserva_salas.service.UserService;
import com.bryanbmo.reserva_salas.vo.UserLoginVO;
import com.bryanbmo.reserva_salas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<UserEntity> findAllUsuarios() {

        return userMapper.findAllUsuarios();
    }
    @Override
    public UserEntity findUsuarioByEmail(String email) {

        return userMapper.findUsuarioByEmail(email);
    }
    @Override
    public Integer register(UserVO userVO) {

        return userMapper.register(userVO);
    }
    @Override
    public List<UserEntity> loginUsuario(UserLoginVO userVO) {

        return userMapper.loginUsuario(userVO);
    }
}
