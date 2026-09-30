/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author 27995
 */
public class Usuario {
       private int id;
    private String nome;
    private String email;
    private String senha;
    private String perfil;
    private boolean ativo;
    private String dataCadastro;

    public Usuario(int id, String nome, String email, String senha, String perfil) {
        this.id = id;
        setNome(nome);
        setEmail(email);
        setSenha(senha);

        if (perfil == null ||
            (!perfil.equals("ADMIN") &&
             !perfil.equals("VET") &&
             !perfil.equals("RECEP") &&
             !perfil.equals("TUTOR"))) {
            System.out.println("Perfil inválido. Usuário não foi criado.");
            this.perfil = "TUTOR";
        } else {
            this.perfil = perfil;
        }

        this.ativo = true;
        this.dataCadastro = "30/09/2026";
    }

    public boolean login(String email, String senha) {
        if (!ativo) {
            return false;
        }

        if (this.email.equals(email) && this.senha.equals(senha)) {
            LogAuditoria.registrar(nome, "Login realizado");
            return true;
        }

        return false;
    }

    public void logout() {
        LogAuditoria.registrar(nome, "Logout realizado");
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().equals("")) {
            System.out.println("Nome inválido.");
        } else {
            this.nome = nome;
        }
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            System.out.println("E-mail inválido.");
        } else {
            this.email = email;
        }
    }

    public void setSenha(String senha) {
        if (senha == null || senha.trim().equals("")) {
            System.out.println("Senha inválida.");
        } else {
            this.senha = senha;
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
        LogAuditoria.registrar(nome, "Usuário desativado");
    }

    public void exibir() {
        System.out.println("Nome: " + nome);
        System.out.println("E-mail: " + email);
        System.out.println("Perfil: " + perfil);
        System.out.println("Ativo: " + ativo);
        System.out.println("Data de cadastro: " + dataCadastro);
    }

    public int getId() {
        return id;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public boolean temAcesso(String area) {
        if (!ativo) {
            return false;
        }

        if (perfil.equals("ADMIN")) {
            return true;
        }

        if (perfil.equals("VET")) {
            return area.equals("CLINICO") || area.equals("AGENDA");
        }

        if (perfil.equals("RECEP")) {
            return area.equals("CADASTRO") || area.equals("AGENDA");
        }

        if (perfil.equals("TUTOR")) {
            return area.equals("MEUS_ANIMAIS") || area.equals("PAGAMENTOS");
        }

        return false; 
}
}