package com.testhub.cadastropessoaspedidostesthub.config;

import com.testhub.cadastropessoaspedidostesthub.model.Role;
import com.testhub.cadastropessoaspedidostesthub.model.Usuario;
import com.testhub.cadastropessoaspedidostesthub.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            var email = "admin@testhub.com";
            if (repo.findByEmail(email).isEmpty()) {
                var admin = new Usuario(email, encoder.encode("123456"), Role.ADMIN);
                repo.save(admin);
                System.out.println("Admin criado: " + email + " / 123456");
            }
        };
    }
}
