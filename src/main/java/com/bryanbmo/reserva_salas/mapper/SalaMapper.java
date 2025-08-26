package com.bryanbmo.reserva_salas.mapper;

import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.vo.DataSala;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalaMapper {

    // Obtener todas las salas
    @Select("SELECT id as id," +
            " nombre as nombre, " +
            "ubicacion as ubicacion," +
            " capacidad as capacidad," +
            " descripcion as descripcion " +
            " FROM sala")
    List<SalaEntity> findAllSalas();

    // Obtener una sala por ID
    @Select("SELECT id as id ," +
            " nombre as nombre ," +
            " ubicacion as ubicacion," +
            " capacidad as capacidad," +
            " descripcion as descripcion " +
            " FROM sala WHERE id = #{id}")
    SalaEntity findSalaById(@Param("id") Long id);

    // Crear nueva sala
    @Insert("INSERT INTO sala (nombre, ubicacion, capacidad, descripcion) " +
            "VALUES (#{dataSala.nombre}, #{dataSala.ubicacion}, #{dataSala.capacidad}, #{dataSala.descripcion})")
    Integer createSala(@Param("dataSala") DataSala dataSala);

    // Actualizar sala
    @Update("UPDATE sala SET nombre = #{salaEntity.nombre}, ubicacion = #{salaEntity.ubicacion}, " +
            "capacidad = #{salaEntity.capacidad}, descripcion = #{salaEntity.descripcion} WHERE id = #{id}")
    Integer updateSala(@Param("id") Long id, @Param("dataSala") SalaEntity salaEntity);

    // Eliminar sala
    @Delete("DELETE FROM sala WHERE id = #{id}")
    Integer deleteSala(@Param("id") Long id);
}
