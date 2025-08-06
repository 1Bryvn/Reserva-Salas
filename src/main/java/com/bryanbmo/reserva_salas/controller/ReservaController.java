package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Log4j2
public class ReservaController {
    @Autowired
    private ReservaService reservaService;


    @PostMapping("/reserva")
    public @ResponseBody ResponseDTO createReserva(@RequestBody ReservaVO request) {

        ResponseDTO responseDTO = new ResponseDTO();

        Integer createReserva = reservaService.createReserva(request);

        try {
            if (createReserva != null) {
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(createReserva))
                        .message(Objects.nonNull(createReserva) ? "Reserva creada con exito" : "Ha ocurrido un error al crear la reserva")
                        .data(createReserva)
                        .build();
                log.info("Reserva creada con exito");
                return responseDTO;
            } else {
                log.info("Error, la reserva se encuentra vacia");
                return null;
            }
        } catch (Exception ex) {
            log.error("Error la reserva no se a creado");
            return responseDTO;
        }

    }


}
