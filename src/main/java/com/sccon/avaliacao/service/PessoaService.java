package com.sccon.avaliacao.service;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.sccon.avaliacao.exception.*;
import com.sccon.avaliacao.model.Pessoa;

import jakarta.annotation.PostConstruct;

@Service
public class PessoaService {

    private final Map<Long, Pessoa> pessoas = new ConcurrentHashMap<>();
    
    public static final String DATA_NASCIMENTO_NULA = "Data de nascimento não informada.";
    public static final String DATA_ADMISSAO_NULA = "Data de admissão não informada.";
    public static final String OUTPUT_NAO_IDENTIFICADO = "Output não identificado.";

    @PostConstruct
    public void inicializarListaPessoas() {
        pessoas.put(1L, new Pessoa(1L, "Ana Silva",
                LocalDate.of(1995, 5, 10),
                LocalDate.of(2020, 3, 1)));

        pessoas.put(2L, new Pessoa(2L, "Bruno Costa",
                LocalDate.of(1990, 8, 20),
                LocalDate.of(2018, 7, 15)));

        pessoas.put(3L, new Pessoa(3L, "Carlos Souza",
                LocalDate.of(1988, 1, 5),
                LocalDate.of(2015, 1, 10)));
    }

    public List<Pessoa> listarTodasPessoas() {
        return pessoas.values().stream()
                .sorted(Comparator.comparing(Pessoa::getNome, Comparator.nullsLast(String::compareTo)))
                .toList();
    }

    public Pessoa retornaPorId(Long id) {
        Pessoa p = pessoas.get(id);
        if (p == null) 
        	throw new ResourceNotFoundException();
        return p;
    }

    public Pessoa incluirPessoa(Pessoa pessoa) {
    	if (pessoa.getDataNascimento() == null) 
    		throw new BadRequestException(DATA_NASCIMENTO_NULA);
    	if (pessoa.getDataAdmissao() == null) 
    	    throw new BadRequestException(DATA_ADMISSAO_NULA);
        if (pessoa.getId() == null) {
            long nextId = pessoas.keySet().stream().max(Long::compare).orElse(0L) + 1;
            pessoa.setId(nextId);
        } else if (pessoas.containsKey(pessoa.getId())) 
            throw new ConflictException();
        pessoas.put(pessoa.getId(), pessoa);
        return pessoa;
    }

    public Pessoa alterarPessoa(Long id, Pessoa pessoa) {
    	if (pessoa.getDataNascimento() == null) 
    		throw new BadRequestException(DATA_NASCIMENTO_NULA);
    	if (pessoa.getDataAdmissao() == null) 
    	    throw new BadRequestException(DATA_ADMISSAO_NULA);
        if (!pessoas.containsKey(id)) 
        	throw new ResourceNotFoundException();
        pessoa.setId(id);
        pessoas.put(id, pessoa);
        return pessoa;
    }

    public Pessoa alterarAtributoPessoa(Long id, Map<String, Object> fields) {
        Pessoa pessoa = retornaPorId(id);

        fields.forEach((key, value) -> {
            switch (key) {
                case "nome" -> pessoa.setNome(value.toString());

                case "dataNascimento" ->{
	                if (value == null || value.toString().trim().equals(""))
	                    throw new BadRequestException(DATA_NASCIMENTO_NULA);
                    pessoa.setDataNascimento(LocalDate.parse(value.toString()));
                }
                case "dataAdmissao" ->{
                	if (value == null || value.toString().trim().equals(""))
	                    throw new BadRequestException(DATA_ADMISSAO_NULA);
                    pessoa.setDataAdmissao(LocalDate.parse(value.toString()));
                }
                default -> throw new BadRequestException("Atributo não identificado.");
            }
        });

        return pessoa;
    }

    public void excluirPessoa(Long id) {
        if (pessoas.remove(id) == null) 
        	throw new ResourceNotFoundException();
    }

    public long calcularIdade(Long id, String output) {
        Pessoa p = retornaPorId(id);
        
        if (p.getDataNascimento() == null) 
            throw new BadRequestException(DATA_NASCIMENTO_NULA);
        
        LocalDate now = LocalDate.of(2023, 2, 7);

        return switch (output) {
            case "days" -> ChronoUnit.DAYS.between(p.getDataNascimento(), now);
            case "months" -> ChronoUnit.MONTHS.between(p.getDataNascimento(), now);
            case "years" -> Period.between(p.getDataNascimento(), now).getYears();
            default -> throw new BadRequestException(OUTPUT_NAO_IDENTIFICADO);
        };
    }

    public BigDecimal calcularSalario(Long id, String output) {
        Pessoa p = retornaPorId(id);

        if (p.getDataAdmissao() == null) 
            throw new BadRequestException(DATA_ADMISSAO_NULA);
        
        long years = ChronoUnit.YEARS.between(
                p.getDataAdmissao(), LocalDate.of(2023, 2, 7));

        BigDecimal salary = BigDecimal.valueOf(1558);

        for (int i = 0; i < years; i++) {
            salary = salary.multiply(BigDecimal.valueOf(1.18))
                    .add(BigDecimal.valueOf(500));
        }

        salary = salary.setScale(2, RoundingMode.UP);

        if ("full".equals(output)) 
            return salary;

        if ("min".equals(output)) {
            BigDecimal minimum = BigDecimal.valueOf(1302);
            return salary.divide(minimum, 2, RoundingMode.UP);
        }

        throw new BadRequestException(OUTPUT_NAO_IDENTIFICADO);
    }
}
