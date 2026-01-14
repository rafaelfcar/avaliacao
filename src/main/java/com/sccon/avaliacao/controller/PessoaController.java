package com.sccon.avaliacao.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sccon.avaliacao.model.Pessoa;
import com.sccon.avaliacao.service.PessoaService;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pessoa> listarTodas() {
        return service.listarTodasPessoas();
    }

    @GetMapping("/{id}")
    public Pessoa buscarPorId(@PathVariable Long id) {
        return service.retornaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criar(@RequestBody Pessoa pessoa) {
        return service.incluirPessoa(pessoa);
    }

    @PutMapping("/{id}")
    public Pessoa atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return service.alterarPessoa(id, pessoa);
    }

    @PatchMapping("/{id}")
    public Pessoa atualizarParcial(@PathVariable Long id,
                                   @RequestBody Map<String, Object> fields) {
        return service.alterarAtributoPessoa(id, fields);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluirPessoa(id);
    }

    @GetMapping("/{id}/age")
    public long calcularIdade(@PathVariable Long id,
                              @RequestParam String output) {
        return service.calcularIdade(id, output);
    }

    @GetMapping("/{id}/salary")
    public BigDecimal calcularSalario(@PathVariable Long id,
                                      @RequestParam String output) {
        return service.calcularSalario(id, output);
    }
}
