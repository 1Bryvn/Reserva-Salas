package com.bryanbmo.reserva_salas.config;

import com.bryanbmo.reserva_salas.entity.UserEntity;
import com.bryanbmo.reserva_salas.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userMapper.findUsuarioByEmail(email);
        System.out.println("Cargando usuario: " + user);

        if(user == null){
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }
        return User.builder()
                .username(user.getEmail())
                .password(user.getContrasena())   // CONTRASEÑA ENCRIPTADA
                .roles(user.getRol())
                .build();
    }
}
