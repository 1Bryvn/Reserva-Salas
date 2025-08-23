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

    @GetMapping("/getUsuarios")
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

    @GetMapping("/findUserioByEmail/{email}")
    public ResponseEntity<ResponseDTO> findUsuarioByEmail(@PathVariable String email){
        ResponseDTO responseDTO = new ResponseDTO();
        UserEntity userEntity = userService.findUsuarioByEmail(email);
        try{
            if(userEntity != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(userEntity))
                        .message(Objects.nonNull(userEntity)?  "Email de usuario encontrado exitosamente" : "Error al encontrar email")
                        .data(userEntity)
                        .build();
            }
            log.info("Email de usuario encontrado exitosamente");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR, email de usuario inexistente");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
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

  @DeleteMapping("deleteUserById/{id}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            Integer deleteUserById = userService.deleteUserById(id);
            if(deleteUserById != null){
                responseDTO = ResponseDTO
                        .builder()
                        .status(Objects.nonNull(deleteUserById))
                        .message(Objects.nonNull(deleteUserById)?  "Usuario eliminado con éxito" : "Error al eliminar usuario")
                        .data(deleteUserById)
                        .build();
            }
            log.info("Usuario eliminado con exito");
            return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error, no se a podido eliminar el usuario");
            return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.BAD_REQUEST);
        }
  }




}
