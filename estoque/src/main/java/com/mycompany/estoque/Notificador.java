/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.estoque;


public class Notificador {

    private String canal;
    private String destinatario;
    private boolean ativo;

    public Notificador(String canal, String destinatario) {

        this.canal = canal;
        this.destinatario = destinatario;
        this.ativo = true;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public void enviarAlerta(String dest, String msg) {

        if (!ativo) {
            System.out.println("[NOTIFICADOR] Notificador desativado.");
            return;
        }

        System.out.println(
                "[NOTIFICADOR] "
                + canal
                + " → "
                + dest
                + ": "
                + msg
        );
    }

    public void enviarAlertaEstoque(ItemEstoque item) {

        enviarAlerta(
                destinatario,
                "Estoque baixo: "
                + item.getNome()
                + " | Quantidade: "
                + item.getQuantidade()
        );
    }
}