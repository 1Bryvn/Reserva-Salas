package com.bryanbmo.reserva_salas.service;

import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.vo.UserLoginVO;
import com.bryanbmo.reserva_salas.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    List<UserEntity> findAllUsuarios();
    Integer register(@Param("userVO") UserVO userVO);
    List<UserEntity> loginUsuario(@Param("userVO") UserLoginVO userVO);
    UserEntity findUsuarioByEmail(@Param("email") String email);
    Integer deleteUserById(Long id);
}
