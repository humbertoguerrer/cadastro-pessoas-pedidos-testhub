package com.testhub.cadastropessoaspedidostesthub.dto;

import com.testhub.cadastropessoaspedidostesthub.model.PedidoStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoDto {
    private String descricao;
    private BigDecimal valor;
    private PedidoStatus status;
    private Long pessoaId;
}
