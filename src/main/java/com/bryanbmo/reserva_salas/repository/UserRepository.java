package com.bryanbmo.reserva_salas.repository;

import com.bryanbmo.reserva_salas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
