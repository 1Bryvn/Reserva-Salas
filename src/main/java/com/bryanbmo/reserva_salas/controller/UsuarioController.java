package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.UserService;
import com.bryanbmo.reserva_salas.vo.UserLoginVO;
import com.bryanbmo.reserva_salas.vo.UserVO;
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
public class UsuarioController {

    @Autowired
    private UserService userService;

    @GetMapping("/usuarios")
    public ResponseEntity<ResponseDTO> getAllUsuarios() {
        ResponseDTO resp = ResponseDTO.builder().build();
        List<UserEntity> users = userService.findAllUsuarios();
        try{
            if(users != null){
                resp =ResponseDTO
                        .builder()
                        .status(Objects.nonNull(users))
                        .message(Objects.nonNull(users) ? "Usuarios obtenidos con exito" : "Ha ocurrido un error al obtener la lista de usuarios")
                        .data(users)
                        .build();
            }
            log.info("Usuarios obtenidos con exito");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
        }catch (Exception ex){
            log.error("Error, no se encontraron los usuarios");
            return new ResponseEntity<ResponseDTO>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/usuarios/registro")
    public @ResponseBody ResponseDTO register(@RequestBody UserVO request){

        ResponseDTO responseDTO = new ResponseDTO();

        Integer register = userService.register(request);
        try{
            if(register != null) {
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(register))
                        .message(Objects.nonNull(register)? "Usuario registrado con exito" : "Ha ocurrido un error al registrar el usuario")
                        .data(register)
                        .build();
                log.info("Usuario registrado con exito");
                return responseDTO;
            }else{
                log.info("Error, el usuario se encuentra vacio");
                return null;
            }
        } catch (Exception ex) {
           log.error("Error, no se registro el usuario");
           return responseDTO;
        }

    }

    @PostMapping("/usuarios/login")
    public @ResponseBody ResponseDTO loginUsuario(@RequestBody UserLoginVO request) {

        ResponseDTO responseDTO = new ResponseDTO();

        List<UserEntity> userlistLogin = userService.loginUsuario(request);

        try{
            if(userlistLogin != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(request))
                        .message(Objects.nonNull(request) ? "Email y contraseña ingresado correctamente" : "Email y contraseña incorrectos")
                        .data(request)
                        .build();

            log.info("Email y contraseña ingresados correctamente");
            return responseDTO;
        }else {
            log.info("Error, email o contraseña se encuentran vacios");
            return null;
        }
    }catch(Exception ex){
        log.error("Error, email y contraseña incorrectos");
        return responseDTO;
    }

  }

}
