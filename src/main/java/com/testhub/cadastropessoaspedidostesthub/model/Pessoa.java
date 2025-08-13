package com.testhub.cadastropessoaspedidostesthub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.validation.constraints.Pattern;


import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos (apenas números)")
    @Column(nullable = false, length = 11)
    private String cpf;

    @Min(18) @Max(110)
    private Integer idade;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist @PreUpdate
    private void normalize() {
        if (this.cpf != null) this.cpf = this.cpf.replaceAll("\\D", "");
        if (this.email != null) this.email = this.email.trim().toLowerCase();
    }
}
