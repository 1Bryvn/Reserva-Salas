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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/registro")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserVO request) {
        try {
            // ================= VALIDACIÓN DE CAMPOS =================
            if (request.getNombre() == null || request.getNombre().isBlank() ||
                    request.getEmail() == null || request.getEmail().isBlank() ||
                    request.getContrasena() == null || request.getContrasena().isBlank() ||
                    request.getRol() == null || request.getRol().isBlank()) {

                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder()
                                .status(false)
                                .message("Todos los campos (nombre, email, contraseña, rol) son obligatorios")
                                .build()
                );
            }

            // ================= VERIFICAR EMAIL DUPLICADO =================
            if (userService.findUsuarioByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        ResponseDTO.builder()
                                .status(false)
                                .message("El email ya está registrado")
                                .build()
                );
            }

            // ================= ENCRIPTAR CONTRASEÑA =================
            request.setContrasena(passwordEncoder.encode(request.getContrasena()));

            // ================= REGISTRAR USUARIO =================
            Integer register = userService.register(request);

            if (register != null && register > 0) {
                log.info("Usuario registrado con éxito: {}", request.getEmail());
                return ResponseEntity.ok(ResponseDTO.builder()
                        .status(true)
                        .message("Usuario registrado con éxito")
                        .data(register)
                        .build());
            } else {
                log.warn("Registro fallido para usuario: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ResponseDTO.builder()
                                .status(false)
                                .message("Error al registrar el usuario")
                                .build()
                );
            }

        } catch (Exception ex) {
            log.error("Error en registro", ex); // mostrar stack trace completo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status(false)
                            .message("Error interno al registrar el usuario")
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserLoginVO request) {
        try {
            // Buscar usuario por email
            UserEntity user = userService.findUsuarioByEmail(request.getEmail());

            if (user == null) {
                log.warn("Intento de login fallido: usuario no encontrado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ResponseDTO.builder()
                                .status(false)
                                .message("Email o contraseña incorrectos")
                                .build()
                );
            }

            // Validar contraseña con PasswordEncoder
            if (!passwordEncoder.matches(request.getContrasena(), user.getContrasena())) {
                log.warn("Credenciales incorrectas para email: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ResponseDTO.builder()
                                .status(false)
                                .message("Email o contraseña incorrectos")
                                .build()
                );
            }

            // Generar JWT incluyendo rol
            String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRol());

            log.info("Usuario {} inició sesión correctamente", user.getEmail());
            return ResponseEntity.ok(
                    ResponseDTO.builder()
                            .status(true)
                            .message("Login exitoso")
                            .data(token)
                            .build()
            );

        } catch (Exception ex) {
            log.error("Error en login: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status(false)
                            .message("Error interno en el login")
                            .build()
            );
        }
    }
}







