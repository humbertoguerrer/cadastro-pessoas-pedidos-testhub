package com.testhub.cadastropessoaspedidostesthub.controller;

import com.testhub.cadastropessoaspedidostesthub.model.Role;
import com.testhub.cadastropessoaspedidostesthub.model.Usuario;
import com.testhub.cadastropessoaspedidostesthub.repository.UsuarioRepository;
import com.testhub.cadastropessoaspedidostesthub.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

record LoginReq(String email, String senha) {}
record TokenRes(String token, long expiresIn, String tokenType) {}

record RegisterReq(String email, String senha, String role) {}
record UserRes(Long id, String email, String role) {}

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
        var u = repo.findByEmail(req.email()).orElse(null);
        if (u == null || !encoder.matches(req.senha(), u.getSenha())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciais inválidas"));
        }
        String token = jwtService.generate(u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(new TokenRes(token, 3600, "Bearer"));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        if (req.email() == null || req.email().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email é obrigatório"));
        }
        if (req.senha() == null || req.senha().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("message", "Senha deve ter pelo menos 6 caracteres"));
        }
        if (repo.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "Email já cadastrado"));
        }

        Role role;
        try {
            role = Role.valueOf(req.role().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Role inválida. Use USER ou ADMIN"));
        }

        var novo = new Usuario(req.email(), encoder.encode(req.senha()), role);
        repo.save(novo);
        return ResponseEntity.status(201).body(new UserRes(novo.getId(), novo.getEmail(), novo.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String email = auth.getName();
        String role = auth.getAuthorities().stream()
                .findFirst().map(a -> a.getAuthority()).orElse("ROLE_USER");
        String roleName = role.startsWith("ROLE_") ? role.substring(5) : role;
        return ResponseEntity.ok(Map.of("email", email, "role", roleName));
    }
}
