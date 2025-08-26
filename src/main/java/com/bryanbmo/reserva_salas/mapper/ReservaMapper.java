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
    Integer createReserva(@Param("reservaVO")ReservaVO reservaVO);

    @Select("""
        SELECT
            r.id,
            r.fecha_reserva AS fechaReserva,
            r.hora_inicio AS horaInicio,
            r.hora_fin AS horaFin,
            r.estado,
            u.id AS "usuario.id",
            u.nombre AS "usuario.nombre",
            u.email AS "usuario.email",
            u.rol AS "usuario.rol",
            u.activo AS "usuario.activo",
            s.id AS "sala.id",
            s.nombre AS "sala.nombre",
            s.ubicacion AS "sala.ubicacion",
            s.capacidad AS "sala.capacidad",
            s.descripcion AS "sala.descripcion"
        FROM reserva r
        JOIN usuario u ON r.usuario_id = u.id
        JOIN sala s ON r.sala_id = s.id
        WHERE u.rol = 'ESTUDIANTE'
    """)
    List<ReservaVO> findAllReservas();

    @Select("""
        SELECT
            r.id,
            r.fecha_reserva AS fechaReserva,
            r.hora_inicio AS horaInicio,
            r.hora_fin AS horaFin,
            r.estado,
            u.id AS "usuario.id",
            u.nombre AS "usuario.nombre",
            u.email AS "usuario.email",
            u.rol AS "usuario.rol",
            s.id AS "sala.id",
            s.nombre AS "sala.nombre",
            s.ubicacion AS "sala.ubicacion",
            s.capacidad AS "sala.capacidad",
            s.descripcion AS "sala.descripcion"
        FROM reserva r
        JOIN usuario u ON r.usuario_id = u.id
        JOIN sala s ON r.sala_id = s.id
        WHERE r.id = #{id}
          AND u.rol = 'ESTUDIANTE'
    """)
    ReservaEntity findReservaById(Long id);

    @Update("""
        UPDATE reserva
        SET fecha_reserva = #{reservaVO.fechaReserva},
            hora_inicio = #{reservaVO.horaInicio},
            hora_fin = #{reservaVO.horaFin},
            estado = #{reservaVO.estado},
            usuario_id = COALESCE(
                #{reservaVO.usuario.id},
                (SELECT id FROM usuario WHERE email = #{reservaVO.usuario.email} AND rol = 'ESTUDIANTE' LIMIT 1)
            ),
            sala_id = COALESCE(
                #{reservaVO.sala.id},
                (SELECT id FROM sala WHERE nombre = #{reservaVO.sala.nombre} LIMIT 1)
            )
        WHERE id = #{id}
    """)
    Integer updateReserva(@Param("id") Long id, @Param("reservaVO")ReservaVO reservaVO);

    @Delete("DELETE FROM reserva WHERE id = #{id}")
    Integer deleteReserva(Long id);

}
