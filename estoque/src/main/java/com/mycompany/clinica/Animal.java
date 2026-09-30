package com.mycompany.clinica;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//DATA DEVE MANTER O PADRÃO ANO/MÊS/DIA

public class Animal {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private String dataNascimento;
    private double peso;
    private Tutor tutor;
    private HistoricoClinico historico;

    public Animal(int id, String nome, String especie, String raca, Tutor tutor) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.tutor = tutor;
        this.historico = new HistoricoClinico(id);
    }

    public int getId() { 
        return id;
    }

    public String getNome() {
         return nome;
         }

    public String getEspecie() {
         return especie; 
        }

    public String getRaca() {
         return raca; 
        }

    public Tutor getTutor() {
         return tutor; 
        }

    public HistoricoClinico getHistorico() {
         return historico; 
        }


    public void setDataNascimento(String dataNascimento) { 
        this.dataNascimento = dataNascimento;
        }
    
    public String getDataNascimento() {
         return dataNascimento;
         }

    
    public void setPeso(double peso) {
        if (peso <= 0) {
            System.out.println ("Peso deve ser maior que zero.");
        }
        this.peso = peso;
    }

    public double getPeso() { return peso; }


    public int calcularIdade() {
        if (dataNascimento == null) return 0;
        try {
            LocalDate nascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ISO_LOCAL_DATE);
            return Period.between(nascimento, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    public void exibir() {
        System.out.println("=== Animal: " + nome + " (#" + id + ") ===");
        System.out.println(especie + " | Raça: " + raca + " | Idade: " + calcularIdade()
            + " anos | Peso: " + peso + " kg");
        System.out.println("Tutor: " + tutor);
    }
}
