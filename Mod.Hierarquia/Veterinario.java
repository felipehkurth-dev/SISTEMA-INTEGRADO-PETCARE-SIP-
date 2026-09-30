/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author 27995
 */
import java.util.ArrayList;

public class Veterinario extends Usuario {
    private String crmv;
    private String especialidade;
    private boolean disponivel;

    public Veterinario(int id, String nome, String email, String crmv, String esp) {
        super(id, nome, email, "123456", "VET");
        this.crmv = crmv;
        this.especialidade = esp;
        this.disponivel = true;
    }

    public String getCrmv() {
        return crmv;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public ArrayList<String> visualizarLogs() {
        return LogAuditoria.listar();
    }

    public void setDisponivel(boolean v) {
        disponivel = v;
        LogAuditoria.registrar(getNome(),
                "Disponibilidade alterada para: " + disponivel);
    }

    public void registrarLaudo(int id, String texto) {
        if (!temAcesso("CLINICO")) {
            System.out.println("Acesso negado.");
            return;
        }

        if (id <= 0 || texto == null || texto.trim().equals("")) {
            System.out.println("Dados do laudo inválidos.");
            return;
        }

        System.out.println("Laudo registrado para o atendimento " + id + ".");
        LogAuditoria.registrar(getNome(),
                "Laudo registrado - Atendimento: " + id);
    }

    public void emitirPrescricao(int id, String rx) {
        if (!temAcesso("CLINICO")) {
            System.out.println("Acesso negado.");
            return;
        }

        if (id <= 0 || rx == null || rx.trim().equals("")) {
            System.out.println("Dados da prescrição inválidos.");
            return;
        }

        System.out.println("Prescrição emitida para o atendimento " + id + ".");
        LogAuditoria.registrar(getNome(),
                "Prescrição emitida - Atendimento: " + id);
    }

    public ArrayList<String> consultarAgenda() {
        if (!temAcesso("AGENDA")) {
            System.out.println("Acesso negado.");
            return new ArrayList<String>();
        }

        LogAuditoria.registrar(getNome(), "Agenda consultada");
        return new ArrayList<String>();
    }

    @Override
    public void exibir() {
        System.out.println("Veterinário: " + getNome());
        System.out.println("E-mail: " + getEmail());
        System.out.println("CRMV: " + crmv);
        System.out.println("Especialidade: " + especialidade);
        System.out.println("Disponível: " + disponivel);
    }
}
