/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.estoque;

import java.util.ArrayList;
import java.util.List;

public class Estoque {

    private List<ItemEstoque> itens;
    private Notificador notificador;

    public Estoque(Notificador notificador) {

        if (notificador == null) {
            throw new IllegalArgumentException(
                    "O notificador não pode ser nulo."
            );
        }

        this.itens = new ArrayList<>();
        this.notificador = notificador;
    }

    
    // ADICIONAR ITEM
    

    public void adicionarItem(ItemEstoque item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item não pode ser nulo."
            );
        }

        itens.add(item);
    }

      // ENTRADA
    

    public void registrarEntrada(int id, int qtd, String resp) {

        ItemEstoque item = buscarPorId(id);

        if (item == null) {

            System.out.println(
                    "Item não encontrado."
            );

            return;
        }

        item.darEntrada(qtd, resp);

        System.out.println(
                "[ESTOQUE] Entrada registrada: "
                + item.getNome()
                + " | Quantidade: "
                + qtd
        );

        verificarAlertas();
    }

    // SAÍDA

    public boolean registrarSaida(int id, int qtd, String resp) {

        ItemEstoque item = buscarPorId(id);

        if (item == null) {

            System.out.println(
                    "Item não encontrado."
            );

            return false;
        }

        boolean sucesso = item.darSaida(qtd, resp);

        if (sucesso) {

            System.out.println(
                    "[ESTOQUE] Saída registrada: "
                    + item.getNome()
                    + " | Quantidade: "
                    + qtd
            );

            verificarAlertas();

        } else {

            System.out.println(
                    "[ESTOQUE] Não foi possível realizar a saída."
            );
        }

        return sucesso;
    }

    // RESERVA
    public boolean reservarParaProced(Agendamento ag) {

        if (ag == null) {
            return false;
        }


        return true;
    }

    // BUSCAR ITEM POR NOME

    public ItemEstoque buscarItem(String nome) {

        if (nome == null) {
            return null;
        }

        for (ItemEstoque item : itens) {

            if (item.getNome().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        return null;
    }

    // BUSCAR POR ID

    private ItemEstoque buscarPorId(int id) {

        for (ItemEstoque item : itens) {

            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    // ALERTA DE ESTOQUE

    public void verificarAlertas() {

        for (ItemEstoque item : itens) {

            if (item.isAbaixoMinimo()) {

                notificador.enviarAlertaEstoque(item);
            }
        }
    }

    // RASTREAR CONTROLADOS

    public List<ItemEstoque> rastrearControlados() {

        List<ItemEstoque> controlados = new ArrayList<>();

        for (ItemEstoque item : itens) {

            if (item.isControlado()) {

                controlados.add(item);
            }
        }

        return controlados;
    }

    // ALERTAR VENCIMENTOS

    public void alertarVencimentos() {

        for (ItemEstoque item : itens) {

            if (item.isVencido()) {

                notificador.enviarAlerta(
                        "Administrador",
                        "Item vencido: " + item.getNome()
                );
            }
        }
    }

    // RELATÓRIO DE RASTREABILIDADE

    public void gerarRelatorioRastreab() {

        System.out.println();
        System.out.println(
                "=== RELATÓRIO DE RASTREABILIDADE ==="
        );

        for (ItemEstoque item : itens) {

            if (item.isControlado()) {

                System.out.println(
                        "ID: " + item.getId()
                        + " | Nome: " + item.getNome()
                        + " | Lote: " + item.getLote()
                        + " | Validade: " + item.getValidade()
                        + " | Responsável: "
                        + item.getResponsavelRetirada()
                );
            }
        }
    }

    // GET ITENS

    public List<ItemEstoque> getItens() {
        return new ArrayList<>(itens);
    }

    // EXIBIR ESTOQUE

    public void exibir() {

        System.out.println();
        System.out.println("=== ESTOQUE PETCARE ===");

        for (ItemEstoque item : itens) {
            item.exibir();
        }
    }
}