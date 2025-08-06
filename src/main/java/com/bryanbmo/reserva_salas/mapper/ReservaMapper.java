package com.bryanbmo.reserva_salas.mapper;

import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReservaMapper {

    @Insert("""
        INSERT INTO reserva (
          fecha_reserva,
          hora_inicio,
          hora_fin,
          estado,
          usuario_id,
          sala_id
        ) VALUES (
          #{reservaVO.fechaReserva},
          #{reservaVO.horaInicio},
          #{reservaVO.horaFin},
          COALESCE(#{reservaVO.estado}, 'RESERVADA'),
          COALESCE(
            #{reservaVO.usuario.id},
            (SELECT id FROM usuario WHERE email = #{reservaVO.usuario.email} AND rol = 'ESTUDIANTE' LIMIT 1)
          ),
          COALESCE(
            #{reservaVO.sala.id},
            (SELECT id FROM sala WHERE nombre = #{reservaVO.sala.nombre} LIMIT 1)
          )
        )
        """)
    Integer createReserva(@Param("reservaVO") ReservaVO reservaVO);






    @Select("SELECT " +
            "r.id AS reserva_id," +
            " r.fecha_reserva," +
            " r.hora_inicio," +
            " r.hora_fin," +
            " r.estado, " +
            "u.id AS usuario_id," +
            " u.nombre AS usuario_nombre," +
            " u.email AS usuario_email," +
            " u.rol AS usuario_rol, " +
            "s.id AS sala_id," +
            " s.nombre AS sala_nombre," +
            " s.ubicacion AS sala_ubicacion," +
            " s.capacidad AS sala_capacidad," +
            " s.descripcion AS sala_descripcion " +
            "FROM reserva r " +
            "INNER JOIN usuario u ON r.usuario_id = u.id " +
            "INNER JOIN sala s ON r.sala_id = s.id " +
            "WHERE r.usuario_id = #{usuarioId}")
    @Results({
            @Result(property = "id", column = "reserva_id"),
            @Result(property = "fechaReserva", column = "fecha_reserva"),
            @Result(property = "horaInicio", column = "hora_inicio"),
            @Result(property = "horaFin", column = "hora_fin"),
            @Result(property = "estado", column = "estado"),

            // Mapeo para UserEntity
            @Result(property = "usuario.id", column = "usuario_id"),
            @Result(property = "usuario.nombre", column = "usuario_nombre"),
            @Result(property = "usuario.email", column = "usuario_email"),
            @Result(property = "usuario.rol", column = "usuario_rol"),

            // Mapeo para SalaEntity
            @Result(property = "sala.id", column = "sala_id"),
            @Result(property = "sala.nombre", column = "sala_nombre"),
            @Result(property = "sala.ubicacion", column = "sala_ubicacion"),
            @Result(property = "sala.capacidad", column = "sala_capacidad"),
            @Result(property = "sala.descripcion", column = "sala_descripcion"),
    })
    List<ReservaEntity> findReservasByUsuario(Long usuarioId);

}
