package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.entity.SalaEntity;
import com.bryanbmo.reserva_salas.service.SalaService;
import com.bryanbmo.reserva_salas.vo.DataSala;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Log4j2
@CrossOrigin(origins="*")
public class SalaController {

    @Autowired
    public SalaService salaService;


    @GetMapping("/getSalas")
    public ResponseEntity<ResponseDTO> getAllSalas(){
        ResponseDTO resp = new ResponseDTO().builder().build();
        List<SalaEntity> salaEntityList = salaService.findAllSalas();

        try{
            if(salaEntityList != null){
                resp = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(salaEntityList))
                        .message(Objects.nonNull(salaEntityList)? "Salas obtenidas con exito": "Ha ocurrido un error al obtener las salas")
                        .data(salaEntityList)
                        .build();
            }
            log.info("Salas obtenidas con exito");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
        }catch (Exception ex){
            log.error("ERROR, no se encontraron salas");
            return new ResponseEntity<ResponseDTO>(resp,HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping("/findSalaById/{id}")
    public ResponseEntity<ResponseDTO> findSalaById(@PathVariable Long id){
        ResponseDTO resp = new ResponseDTO().builder().build();

        SalaEntity salaEntity = salaService.findSalaById(id);
        try{
            if(salaEntity != null){
                resp = ResponseDTO.builder()
                        .status(Objects.nonNull(salaEntity))
                        .message(Objects.nonNull(salaEntity)? "Id de Sala encontrada con exito": "Error al encontrar el id")
                        .data(salaEntity)
                        .build();

            }
            log.info("Sala obtenida con exito");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);

        }catch (Exception ex){
            log.error("ERROR, no se encontraron salas");
            return  new ResponseEntity<ResponseDTO>(resp,HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/createSala")
    public @ResponseBody ResponseDTO createSala(@RequestBody DataSala request){
        ResponseDTO responseDTO = new ResponseDTO();

        Integer createSala = salaService.createSala(request);

        try{
            if(createSala != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(createSala))
                        .message(Objects.nonNull(createSala)? "Sala creada con exito" : "Ha ocurrido un error al crear la sala")
                        .data(createSala)
                        .build();
                log.info("Reserva creada con exito");
                return responseDTO;
            }else {
                log.info("Error, la sala se encuentra vacia");
                return  null;
            }
        } catch (Exception e) {
            log.error("Error la sala no se a creado");
            return responseDTO;
        }
    }


    @PutMapping("/updateSalaById/{id}")
    public ResponseEntity<ResponseDTO> updateSala(@PathVariable Long id, @RequestBody SalaEntity request){

        ResponseDTO responseDTO = new  ResponseDTO();

        Integer updateSala = salaService.updateSala(id, request);
        try{
            if(updateSala != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(updateSala))
                        .message(Objects.nonNull(updateSala)?  "Sala actualizada con éxito" : "Error al actualizar la sala")
                        .data(updateSala)
                        .build();
            }
            log.info("Sala actualizada con exito");
            return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
        }catch (Exception ex){
            log.error("ERROR, no se a podido actualizar la sala");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteSala/{id}")
    public String deleteSala(@PathVariable Long id){
        salaService.deleteSala(id);
        return "La Sala a sido eliminada";
    }
}
