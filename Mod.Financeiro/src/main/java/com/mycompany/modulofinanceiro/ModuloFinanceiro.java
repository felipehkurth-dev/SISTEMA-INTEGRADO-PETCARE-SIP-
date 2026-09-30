/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.modulofinanceiro;

/**
 *
 * @author USER
 */

import java.util.List;

public class ModuloFinanceiro {
    public static void main(String[] args) {

        Fatura.Tutor tutor = new Fatura.Tutor("Ana Souza", "ana@email.com");
        LogAuditoria.Usuario usuario = new LogAuditoria.Usuario("Felipe", "felipe@petcare.com");
       
            Fatura.Notificador notificador = (String destinatario, String mensagem) -> {
                System.out.println("📧 Email para " + destinatario + ": " + mensagem);
        };

        Fatura fatura = new Fatura(1, tutor, 350.0, "Consulta + Vacina", notificador);
        fatura.emitir();
        System.out.println(fatura.gerarBoleto());
        System.out.println("Link de pagamento: " + fatura.gerarLinkPagamento());
        fatura.exibir();

        boolean pago = fatura.confirmarPagOnline();
        System.out.println("Pagamento aprovado? " + pago);
        System.out.println("Status atual: " + fatura.getStatus());

        LogAuditoria log = new LogAuditoria(usuario, "EMISSAO_FATURA", "Fatura#1");
        log.registrar();
        log.exibir();

        Relatorio relatorio = new Relatorio(9, 2026);
        relatorio.exibir();
        relatorio.exportar();

        List<LogAuditoria> logsDoUsuario = log.buscarPorUsuario(usuario);
        System.out.println("Total de logs do usuário: " + logsDoUsuario.size());

    } 
} 