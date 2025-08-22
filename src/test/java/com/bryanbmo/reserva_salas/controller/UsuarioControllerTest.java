package com.bryanbmo.reserva_salas.controller;

import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.service.UserService;
import com.bryanbmo.reserva_salas.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    void testRegisterUser() throws Exception {

        when(userService.register(any(UserVO.class))).thenReturn(1);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Pedro Torres",
                                    "email": "pedro.torres@estudiante.cl",
                                    "contrasena": "123456",
                                    "rol": "ESTUDIANTE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado con exito"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testLoginUser() throws Exception {

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                 "email": "Luis.torre@estudiante.cl",
                                 "contrasena": "12345"
                             }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Email y contraseña ingresado correctamente"))
                .andExpect(jsonPath("$.data.email").value("Luis.torre@estudiante.cl"))
                .andExpect(jsonPath("$.data.contrasena").value("12345"));
    }


    @Test
    void testGetAllUsuarios() throws Exception {
        // Mock de datos
        List<UserEntity> mockUsers = List.of(
                new UserEntity(1L, "Admin General", "admin@roomly.cl", "admin123", "ADMIN", true),
                new UserEntity(2L, "Juan Pérez", "juan@estudiante.cl", "123456", "ESTUDIANTE", true),
                new UserEntity(3L, "Pedro Gómez", "pedro@estudiante.cl", "pass123", "ESTUDIANTE", true),
                new UserEntity(6L, "Pedro Torres", "pedro.torres@estudiante.cl", "123456", "ESTUDIANTE", true)
        );

        // Simular comportamiento del servicio
        when(userService.findAllUsuarios()).thenReturn(mockUsers);

        // Ejecutar petición y validar respuesta
        mockMvc.perform(get("/api/getUsuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuarios obtenidos con exito"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nombre").value("Admin General"))
                .andExpect(jsonPath("$.data[0].email").value("admin@roomly.cl"))
                .andExpect(jsonPath("$.data[0].rol").value("ADMIN"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[2].email").value("pedro@estudiante.cl"))
                .andExpect(jsonPath("$.data[3].nombre").value("Pedro Torres"));
    }

    @Test
    void testFindUsuarioByEmail() throws Exception {
        // Arrange - usuario de prueba
        UserEntity mockUser = new UserEntity(
                1L,
                "Juan Pérez",
                "juan@estudiante.cl",
                "123456",
                "ESTUDIANTE",
                true
        );

        when(userService.findUsuarioByEmail("juan@estudiante.cl"))
                .thenReturn(mockUser);

        // Act + Assert
        mockMvc.perform(get("/api/findUserioByEmail/{email}", "juan@estudiante.cl")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Email de usuario encontrado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.email").value("juan@estudiante.cl"))
                .andExpect(jsonPath("$.data.rol").value("ESTUDIANTE"))
                .andExpect(jsonPath("$.data.activo").value(true));
    }



    @Test
    void testDeleteUserById() throws Exception {
        // Arrange - simulamos que userService elimina el usuario con id=3 y devuelve 1
        when(userService.deleteUserById(3L)).thenReturn(1);

        // Act + Assert
        mockMvc.perform(delete("/api/deleteUserById/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuario eliminado con éxito"))
                .andExpect(jsonPath("$.data").value(1));
    }




}
