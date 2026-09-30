/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modulofinanceiro;

/**
 *
 * @author USER
 */
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class LogAuditoria {
    private int id;
    private String dataHora;
    private Usuario usuario;
    private String acao;
    private String entidadeAfetada;
    private int idEntidade;
    private String ipOrigem;

    private static List<LogAuditoria> registros = new ArrayList<>();
    
    public static class Usuario {
        private String nome;
        private String email;
        
        public Usuario (String nome, String email){
            if (email == null || !email.contains("@")){
                throw new IllegalArgumentException("Email inválido: precisa conter '@'.");
            }
            this.nome = nome;
            this.email = email;
        }
        
        public String getNome(){
            return nome;
        }
        public String getEmail(){
            return email;
        }
    }
    public LogAuditoria(){

    }
    public LogAuditoria(Usuario usuario, String acao, String entidadeAfetada){
        this.usuario = usuario;
        this.acao = acao;
        this.entidadeAfetada = entidadeAfetada;
    }
    public void registrar(){
        if (usuario == null){
            throw new IllegalStateException("Não é possível registrar um log sem usuário.");
        }
        if (acao == null || acao.isEmpty()){
            throw new IllegalStateException("Não é possível registrar um log sem ação.");
        }
        this.dataHora = LocalDateTime.now().toString();
        registros.add(this);
    }
    public String getAcao(){
        return acao;
    }
    public Usuario getUsuario(){
        return usuario;
    }
    public String getDataHora(){
        return dataHora;
    }
    public void exibir(){
        System.out.println("[" + dataHora + "] " + usuario.getNome() + " → " + acao + " (" + entidadeAfetada + ")");
    }
    public List<LogAuditoria> buscarPorUsuario(Usuario u){
        List<LogAuditoria> resultado = new ArrayList<>();
        for (LogAuditoria log : registros){
            if (log.usuario.equals(u)){
                resultado.add(log);
            }
        }
        return resultado;
    }
    public List<LogAuditoria> buscarPorEntidade(String e){
        List<LogAuditoria> resultado = new ArrayList<>();
        for (LogAuditoria log : registros){
            if (log.entidadeAfetada.equals(e)){
                resultado.add(log);
            }
        }
        return resultado;
    }
}