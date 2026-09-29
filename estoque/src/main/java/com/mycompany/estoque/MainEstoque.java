/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.estoque;

public class MainEstoque {

    public static void main(String[] args) {

        System.out.println("=== TESTE DO MÓDULO DE ESTOQUE ===");

        // Criando o Notificador
        Notificador notificador =
                new Notificador(
                        "EMAIL",
                        "admin@petcare.com"
                );

        // Criando o Estoque
        Estoque estoque =
                new Estoque(notificador);

        // Criando um item
        ItemEstoque dipirona =
                new ItemEstoque(
                        1,
                        "Dipirona",
                        50,
                        10,
                        "L001"
                );

        dipirona.setValidade("2027-10-10");
        dipirona.setCategoria("Medicamento");

        // Adicionando ao estoque
        estoque.adicionarItem(dipirona);

        // Criando medicamento controlado
        ItemEstoque controlado =
                new ItemEstoque(
                        2,
                        "Medicamento Controlado",
                        5,
                        10,
                        "L002"
                );

        controlado.setValidade("2027-12-20");
        controlado.setResponsavelRetirada("Maria");
        controlado.setCategoria("Controlado");
        controlado.setControlado(true);

        estoque.adicionarItem(controlado);

        // Mostrar estoque
        estoque.exibir();

        // Registrar entrada
        estoque.registrarEntrada(
                1,
                20,
                "João"
        );

        // Registrar saída
        estoque.registrarSaida(
                1,
                15,
                "João"
        );

        // Mostrar quantidade
        System.out.println();
        System.out.println(
                "Quantidade de Dipirona: "
                + dipirona.getQuantidade()
        );

        // Testar estoque abaixo do mínimo
        System.out.println();
        System.out.println(
                "Controlado abaixo do mínimo? "
                + controlado.isAbaixoMinimo()
        );

        // Verificar alertas
        estoque.verificarAlertas();

        // Relatório de controlados
        estoque.gerarRelatorioRastreab();

        // Alertar vencimentos
        estoque.alertarVencimentos();

        System.out.println();
        System.out.println("=== FIM DO TESTE ===");
    }
}
