package com.bryanbmo.reserva_salas.controller;


import com.bryanbmo.reserva_salas.config.JwtTokenProvider;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    // ==== TEST REGISTRO EXITOSO ====
    @Test
    void testRegistroExitoso() throws Exception {
        when(userService.findUsuarioByEmail("test@test.com")).thenReturn(null);

        mockMvc.perform(post("/api/auth/registro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"Bryan",
                                  "email":"test@test.com",
                                  "contrasena":"123456",
                                  "rol":"USER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado con éxito"));
    }

    // ==== TEST REGISTRO EMAIL YA EXISTE ====
    @Test
    void testRegistroEmailDuplicado() throws Exception {
        when(userService.findUsuarioByEmail("test@test.com"))
                .thenReturn(new UserEntity()); // simulamos usuario existente

        mockMvc.perform(post("/api/auth/registro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"Bryan",
                                  "email":"test@test.com",
                                  "contrasena":"123456",
                                  "rol":"USER"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Email ya registrado"));
    }

    // ==== TEST LOGIN EXITOSO ====
    @Test
    void testLoginExitoso() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("test@test.com");
        user.setRol("USER");

        when(userService.findUsuarioByEmail("test@test.com")).thenReturn(user);
        when(jwtTokenProvider.generateToken("test@test.com", "USER")).thenReturn("fake-jwt-token");

        // AuthenticationManager.authenticate no lanza excepción = login OK
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"test@test.com",
                                  "contrasena":"123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Login exitoso"))
                .andExpect(jsonPath("$.data").value("fake-jwt-token"));
    }

    // ==== TEST LOGIN FALLIDO ====
    @Test
    void testLoginFallido() throws Exception {
        // Simulamos que falla la autenticación
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"wrong@test.com",
                                  "contrasena":"badpass"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Email o contraseña incorrectos"));
    }
}
