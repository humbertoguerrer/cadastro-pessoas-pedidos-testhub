package com.testhub.cadastropessoaspedidostesthub.controller;

import com.testhub.cadastropessoaspedidostesthub.dto.PedidoDto;
import com.testhub.cadastropessoaspedidostesthub.model.Pedido;
import com.testhub.cadastropessoaspedidostesthub.model.Pessoa;
import com.testhub.cadastropessoaspedidostesthub.repository.PedidoRepository;
import com.testhub.cadastropessoaspedidostesthub.repository.PessoaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final PessoaRepository pessoaRepository;

    public PedidoController(PedidoRepository pedidoRepository, PessoaRepository pessoaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    // POST /pedidos  (body: descricao, valor, status, pessoaId)
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody PedidoDto req) {
        // validações básicas e diretas
        if (req.getPessoaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("pessoaId é obrigatório");
        }
        if (req.getDescricao() == null || req.getDescricao().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("descricao é obrigatória");
        }
        if (req.getValor() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("valor é obrigatório");
        }
        if (req.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("status é obrigatório");
        }

        Pessoa pessoa = pessoaRepository.findById(req.getPessoaId()).orElse(null);
        if (pessoa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }

        try {
            Pedido novo = Pedido.builder()
                    .descricao(req.getDescricao())
                    .valor(req.getValor())
                    .status(req.getStatus())
                    .pessoa(pessoa)
                    .build();
            pedidoRepository.save(novo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pedido criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar pedido");
        }
    }

    // GET /pedidos  (lista todos)
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // GET /pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // GET /pessoas/{pessoaId}/pedidos  (lista pedidos de uma pessoa)
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<Pedido>> listarPorPessoa(@PathVariable Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pedidoRepository.findByPessoaId(pessoaId));
    }

    // PUT /pedidos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody PedidoDto req) {
        Pedido existente = pedidoRepository.findById(id).orElse(null);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }

        // se quiser permitir trocar a pessoa do pedido
        if (req.getPessoaId() != null) {
            Pessoa pessoa = pessoaRepository.findById(req.getPessoaId()).orElse(null);
            if (pessoa == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
            }
            existente.setPessoa(pessoa);
        }

        if (req.getDescricao() != null) existente.setDescricao(req.getDescricao());
        if (req.getValor() != null) existente.setValor(req.getValor());
        if (req.getStatus() != null) existente.setStatus(req.getStatus());

        try {
            pedidoRepository.save(existente);
            return ResponseEntity.ok("Pedido atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar pedido");
        }
    }

    // DELETE /pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.ok("Pedido deletado com sucesso!");
    }
}
