package com.testhub.cadastropessoaspedidostesthub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Valor não pode ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoStatus status;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
