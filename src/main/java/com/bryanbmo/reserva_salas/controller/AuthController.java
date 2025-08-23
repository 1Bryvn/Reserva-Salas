package com.bryanbmo.reserva_salas.controller;


import com.bryanbmo.reserva_salas.config.JwtTokenProvider;
import com.bryanbmo.reserva_salas.dto.ResponseDTO;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.UserService;
import com.bryanbmo.reserva_salas.vo.UserLoginVO;
import com.bryanbmo.reserva_salas.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registro")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserVO request) {
        try {
            if (request.getNombre() == null || request.getEmail() == null
                    || request.getContrasena() == null || request.getRol() == null) {
                return ResponseEntity.badRequest()
                        .body(ResponseDTO.builder().status(false).message("Todos los campos son obligatorios").build());
            }

            if (userService.findUsuarioByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ResponseDTO.builder().status(false).message("Email ya registrado").build());
            }

            // Registrar usuario (el password se encripta dentro del service)
            userService.register(request);

            return ResponseEntity.ok(ResponseDTO.builder().status(true).message("Usuario registrado con éxito").build());

        } catch (Exception ex) {
            log.error("Error en registro", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.builder().status(false).message("Error interno").build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserLoginVO request) {
        try {
            // Autenticación usando Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContrasena())
            );

            // Buscar usuario en BD
            UserEntity user = userService.findUsuarioByEmail(request.getEmail());

            // Generar JWT
            String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRol());

            return ResponseEntity.ok(ResponseDTO.builder().status(true).message("Login exitoso").data(token).build());

        } catch (Exception ex) {
            log.error("Error en login", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.builder().status(false).message("Email o contraseña incorrectos").build());
        }
    }
}







