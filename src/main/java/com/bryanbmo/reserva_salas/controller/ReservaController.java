package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.entity.ReservaEntity;
import com.bryanbmo.reserva_salas.service.ReservaService;
import com.bryanbmo.reserva_salas.vo.ReservaVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Log4j2
public class ReservaController {
    @Autowired
    private ReservaService reservaService;


    @PostMapping("/postReserva")
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
    @GetMapping("/getReservas")
    public ResponseEntity<ResponseDTO> getAllService(){

        ResponseDTO resp = ResponseDTO.builder().build();

        List<ReservaEntity> reservaEntityList = reservaService.findAllReservas();

        try{
            if(reservaEntityList != null){
                resp = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(reservaEntityList))
                        .message(Objects.nonNull(reservaEntityList)? "Reservas obtenidas con éxito" : "Ha ocurrido un error al obtener las reservas")
                        .data(reservaEntityList)
                        .build();
            }
            log.info("Reservas obtenidas con éxito");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
        }catch (Exception ex){
            log.error("ERROR, no se encontraron reservas");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findReservaById/{id}")
    public ResponseEntity<ResponseDTO> findReservaById(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();

        ReservaEntity reserva = reservaService.findReservaById(id);

        try{
            if (reserva != null){
    responseDTO = ResponseDTO
            .builder()
            .status(Objects.nonNull(reserva))
            .message(Objects.nonNull(reserva)?  "Id de reserva encontrada exitosamente" : "Error al encontrar la reserva")
            .data(reserva)
            .build();

            }
            log.info("Reserva encontrada exitosamente");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR, no se a podido encontrar el ID");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteReserva/{id}")
    public String deleteReserva(@PathVariable Long id){
        reservaService.deleteReserva(id);
        return "La reserva a sido eliminada";
    }

    @PutMapping("/updateReservaById/{id}")
    public ResponseEntity<ResponseDTO> updateReserva(@PathVariable Long id, @RequestBody ReservaVO request){

        ResponseDTO responseDTO = new  ResponseDTO();

        try{
            Integer updateReserva = reservaService.updateReserva(id, request);
            if(updateReserva != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(updateReserva))
                        .message(Objects.nonNull(updateReserva)?  "Reserva actualizada con éxito" : "Error al actualizar al reserva")
                        .data(updateReserva)
                        .build();
            }
            log.info("Reserva actualizada con exito");
            return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
        }catch (Exception ex){
            log.error("ERROR, no se a podido actualizar");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

}
