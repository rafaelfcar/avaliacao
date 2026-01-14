package com.sccon.avaliacao.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Pessoa {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;

    public Pessoa(Long id, String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public Pessoa() {}

}
