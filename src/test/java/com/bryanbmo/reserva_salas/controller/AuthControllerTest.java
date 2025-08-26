package com.bryanbmo.reserva_salas.controller;


import com.bryanbmo.reserva_salas.config.JwtTokenProvider;
import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // deshabilita filtros de seguridad
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
                        .with(csrf())  // importante si CSRF está habilitado
                        .with(user("testuser").roles("ESTUDIANTE")) // simula usuario
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Bryan\",\"email\":\"test@test.com\",\"contrasena\":\"123456\",\"rol\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado con éxito"));
    }

    // ==== TEST REGISTRO EMAIL YA EXISTE ====
    @Test
    void testRegistroEmailDuplicado() throws Exception {
        // Simulamos que el email ya está registrado
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test@test.com");
        existingUser.setRol("ESTUDIANTE");

        when(userService.findUsuarioByEmail("test@test.com")).thenReturn(existingUser);

        String requestBody = """
                {
                  "nombre": "Bryan",
                  "email": "test@test.com",
                  "contrasena": "123456",
                  "rol": "ESTUDIANTE"
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .with(csrf())
                        .with(user("testuser").roles("ESTUDIANTE")) // simula usuario autenticado
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict()) // 👈 ahora coincide con tu controlador
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Email ya registrado"));
    }


    // ==== TEST LOGIN EXITOSO ====
    @Test
    void testLoginExitoso() throws Exception {
        // Mock del usuario que devuelve el service
        UserEntity user = new UserEntity();
        user.setEmail("test@test.com");
        user.setRol("ESTUDIANTE");

        when(userService.findUsuarioByEmail("test@test.com")).thenReturn(user);
        when(jwtTokenProvider.generateToken("test@test.com", "ESTUDIANTE")).thenReturn("fake-jwt-token");

        // Mock de AuthenticationManager: no lanza excepción = login OK
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .with(user("testuser").roles("ESTUDIANTE")) // simula usuario logueado
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"contrasena\":\"123456\"}"))
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

        // Mockear userService para evitar NPE (aunque no se llegue a usar)
        when(userService.findUsuarioByEmail(anyString())).thenReturn(null);

        String requestBody = """
            {
              "email":"wrong@test.com",
              "contrasena":"badpass"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Email o contraseña incorrectos"));
    }

}
