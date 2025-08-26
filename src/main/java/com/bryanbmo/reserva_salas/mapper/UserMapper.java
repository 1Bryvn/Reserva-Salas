package com.bryanbmo.reserva_salas.mapper;

import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.vo.DataUser;
import com.bryanbmo.reserva_salas.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT " +
            "id as id," +
            " nombre as nombre," +
            " email as email," +
            " contrasena as contrasena," +
            " rol as rol," +
            " activo as activo " +
            " FROM usuario")
    List<UserEntity> findAllUsuarios();


    @Select("SELECT " +
            "id as id," +
            " nombre as nombre," +
            " email as email," +
            " rol as rol," +
            " activo as activo " +
            " FROM usuario")
    List<DataUser> findAllUsers();

    @Select("SELECT id as id, nombre as nombre, email as email, contrasena as contrasena, rol as rol, activo as activo " +
            "FROM usuario WHERE email = #{email} AND activo = 1")
    UserEntity findUsuarioByEmail(@Param("email") String email);


    @Insert("INSERT INTO usuario (nombre, email, contrasena, rol, activo) " +
            "VALUES (#{userVO.nombre}, #{userVO.email}, #{userVO.contrasena}, #{userVO.rol}, 1)")
    Integer register(@Param("userVO") UserVO userVO);


    @Delete("DELETE FROM usuario WHERE id = #{id}")
    Integer deleteUserById(Long id);
}
