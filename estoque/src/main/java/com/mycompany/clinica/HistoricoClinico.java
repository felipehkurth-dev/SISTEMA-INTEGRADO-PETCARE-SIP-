package com.mycompany.clinica;

import java.util.ArrayList;
import java.util.List;


public class HistoricoClinico {
    private int idAnimal;
    private List<Consulta> consultas = new ArrayList<>();
    private List<Vacina> vacinas = new ArrayList<>();
    private List<Cirurgia> cirurgias = new ArrayList<>();
    private List<Exame> exames = new ArrayList<>();
    private List<Tratamento> tratamentos = new ArrayList<>();
    private boolean finalizado;

    public HistoricoClinico(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    private void verificarEdicao() {
        if (finalizado) {
            System.out.println( "Histórico do animal #" + idAnimal + " está finalizado.");
        }
    }

    public void adicionarConsulta(Consulta c) { 
        verificarEdicao(); consultas.add(c); 
    }

    public void adicionarVacina(Vacina v) { 
        verificarEdicao(); vacinas.add(v); 
    }

    public void adicionarCirurgia(Cirurgia c) { 
        verificarEdicao(); cirurgias.add(c); 
    }

    public void adicionarExame(Exame e) {
         verificarEdicao(); exames.add(e);
    }

    public void adicionarTratamento(Tratamento t) { 
        verificarEdicao(); tratamentos.add(t); 
    }

    
    public void finalizar() {
        this.finalizado = true;
    }

    public boolean isFinalizado() {
         return finalizado;
        }

    public List<Consulta> getConsultas() { 
        return consultas; 
        }

    public List<Vacina> getVacinas() { 
        return vacinas; 
        }

    public List<Cirurgia> getCirurgias() { 
        return cirurgias; 
        }

    public List<Exame> getExames() {
        return exames; 
        }

    public List<Tratamento> getTratamentos() { 
        return tratamentos; 
        }
        

    public void exibir() {
        System.out.println("### Histórico Clínico — Animal #" + idAnimal + " ###");
        consultas.forEach(Consulta::exibir);
        vacinas.forEach(Vacina::exibir);
        cirurgias.forEach(Cirurgia::exibir);
        exames.forEach(Exame::exibir);
        tratamentos.forEach(Tratamento::exibir);
        System.out.println("(Histórico finalizado: " + finalizado + ")");
    }
}
