package com.testhub.cadastropessoaspedidostesthub.controller;

import com.testhub.cadastropessoaspedidostesthub.model.Pessoa;
import com.testhub.cadastropessoaspedidostesthub.repository.PessoaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<String> criarPessoa(@Valid @RequestBody Pessoa pessoa) {
        try {
            pessoaRepository.save(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pessoa cadastrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar pessoa.");
        }
    }

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaRepository.findById(id).orElse(null);
        if (pessoa != null) {
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> buscarPorCpf(@PathVariable String cpf) {
        String somenteDigitos = cpf.replaceAll("\\D", "");
        Pessoa pessoa = pessoaRepository.findByCpf(somenteDigitos).orElse(null);
        if (pessoa != null) {
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa dados) {
        Pessoa existente = pessoaRepository.findById(id).orElse(null);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        existente.setCpf(dados.getCpf());
        existente.setIdade(dados.getIdade());
        try {
            pessoaRepository.save(existente);
            return ResponseEntity.ok("Pessoa atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar pessoa");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        pessoaRepository.deleteById(id);
        return ResponseEntity.ok("Pessoa deletada com sucesso!");
    }
}