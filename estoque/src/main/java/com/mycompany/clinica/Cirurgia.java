package com.mycompany.clinica;

import java.util.ArrayList;
import java.util.List;


public class Cirurgia extends RegistroClinico {
    public static final int DURACAO_MINIMA = 120; //duração mínima das cirurgia/preparo da cirurgia

    private int duracao;
    private String sala;
    private List<String> equipe = new ArrayList<>();
    private String anestesia;
    private List<String> medicamentos = new ArrayList<>();

    public Cirurgia(int id, String data, String descricao, Veterinario veterinario, String sala) {
        super(id, data, descricao, veterinario);
        this.sala = sala;
        this.duracao = DURACAO_MINIMA; // mínimo de 2h já reservado por padrão
    }

    public String getSala() { 
        return sala;
    }
    public int getDuracao() { 
        return duracao;
    }

    
    public void setDuracao(int minutos) {
        verificarEdicao();
        if (minutos < 120) System.out.println( "Cirurgia deve reservar no mínimo 120 minutos.");
        this.duracao = minutos;
    }

    public void setEquipe(List<String> equipe) {
        verificarEdicao();
        this.equipe = equipe;
    }

    public List<String> getEquipe() { 
        return equipe;
    }

    public void setAnestesia(String anestesia) {
        verificarEdicao();
        this.anestesia = anestesia;
    }

    public void adicionarMedicamento(String medicamento) {
        verificarEdicao();
        medicamentos.add(medicamento);
    }

    public List<String> getMedicamentos() {
        return medicamentos;
    }

    public boolean validarRecursos() {
        return sala != null && !sala.isEmpty()
            && !equipe.isEmpty()
            && anestesia != null && !anestesia.isEmpty()
            && !medicamentos.isEmpty();
    }

    @Override
    public void exibir() {
        System.out.println("--- Cirurgia #" + getId() + " ---");
        System.out.println("Data: " + getData() + " | Sala: " + sala + " | Duração: " + duracao + " min");
        System.out.println("Equipe: " + equipe);
        System.out.println("Anestesia: " + (anestesia != null ? anestesia : "-"));
        System.out.println("Medicamentos: " + medicamentos);
        System.out.println("Recursos disponíveis para agendar: " + validarRecursos());
        System.out.println("Finalizada: " + isFinalizado());
    }
}
