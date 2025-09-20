package com.testhub.cadastropessoaspedidostesthub.controller;

import com.testhub.cadastropessoaspedidostesthub.model.Usuario;
import com.testhub.cadastropessoaspedidostesthub.repository.UsuarioRepository;
import com.testhub.cadastropessoaspedidostesthub.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

record LoginReq(String email, String senha) {
}

record TokenRes(String token, long expiresIn, String tokenType) {
}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        Usuario u = repo.findByEmail(req.email()).orElse(null);
        if (u == null || !encoder.matches(req.senha(), u.getSenha())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciais inválidas"));
        }
        String token = jwtService.generate(u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(new TokenRes(token, 3600, "Bearer"));
    }
}
