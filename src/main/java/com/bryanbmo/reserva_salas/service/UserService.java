package com.bryanbmo.reserva_salas.service;

import com.bryanbmo.reserva_salas.model.Usuario;
import com.bryanbmo.reserva_salas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public Usuario autoIncrement(String email, String nombre){
        return userRepository.findByEmail(email).orElseGet(() ->{
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setEmail(email);
            nuevo.setPassword("");
            nuevo.setRol(Usuario.Rol.ESTUDIANTE);
            return userRepository.save(nuevo);

        });
    }





}
