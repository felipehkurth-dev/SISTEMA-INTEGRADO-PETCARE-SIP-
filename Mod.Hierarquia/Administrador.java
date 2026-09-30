/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author 27995
 */
public class Administrador extends Usuario {
     private int nivelAcesso;
    private String departamento;

    public Administrador(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, "ADMIN");
        nivelAcesso = 1;
        departamento = "Administrativo";
    }

    public boolean excluirRegistro(int id, String tipo) {
        if (id <= 0 || tipo == null || tipo.trim().equals("")) {
            System.out.println("Dados inválidos.");
            return false;
        }

        LogAuditoria.registrar(getNome(),
                "Exclusão de registro - Tipo: " + tipo + " - ID: " + id);

        System.out.println("Registro excluído.");
        return true;
    }

    public void ajustarEstoque(ItemEstoque item, int qtd) {
        if (item == null || qtd <= 0) {
            System.out.println("Dados de estoque inválidos.");
            return;
        }

        item.darEntrada(qtd);
        LogAuditoria.registrar(getNome(),
                "Ajuste manual de estoque - Item: " + item.getNome() +
                " - Quantidade: " + qtd);
    }

    public Relatorio gerarRelatorio(int mes, int ano) {
        if (mes < 1 || mes > 12 || ano < 1) {
            System.out.println("Data inválida.");
            return null;
        }

        LogAuditoria.registrar(getNome(),
                "Relatório mensal gerado - " + mes + "/" + ano);

        return new Relatorio(mes, ano);
    }

    public void gerenciarUsuario(Usuario u) {
        if (u == null) {
            System.out.println("Usuário inválido.");
            return;
        }

        System.out.println("Usuário gerenciado: " + u.getNome());
        LogAuditoria.registrar(getNome(),
                "Gerenciamento do usuário: " + u.getNome());
    }

    @Override
    public void exibir() {
        System.out.println("Administrador: " + getNome());
        System.out.println("E-mail: " + getEmail());
        System.out.println("Nível de acesso: " + nivelAcesso);
        System.out.println("Departamento: " + departamento);
        System.out.println("Ativo: " + isAtivo());
    }
}

}
