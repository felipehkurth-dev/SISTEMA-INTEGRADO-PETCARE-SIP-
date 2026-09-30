/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modulofinanceiro;

/**
 *
 * @author USER
 */
import java.io.FileWriter;
import java.io.IOException;

public class Relatorio {
    private int id;
    private int mes;
    private int ano;
    private int totalAtendimentos;
    private double totalFaturamento;
    private double totalDespesas;
    private double taxaRetorno;
    private double tempMedioAtend;
    private String procedMaisRealizado;
    
    public Relatorio(){
        
    }
    public Relatorio(int mes, int ano){
        this.mes = mes;
        this.ano = ano;
    }
    public void gerar(){
        String gerarRelatorio = "============= RELATÓRIO ===============\n" 
                + "Número identificador: " + id + "\n"
                + "Nome do Empresa: Clínica Veterinária PetCare\n"
                + "-------------------------------------\n"
                + "Mês: " + mes + "\n"
                + "Ano: " + ano + "\n"
                + "=====================================";
    }
    public String getEstatisticas(){
        return "(=) Total de Atendimentos Realizados: " + totalAtendimentos + "\n"
                + "(+) Total de Faturamento: " + totalFaturamento + "\n"
                + "(-) Total de Despesas: " + totalDespesas + "\n"
                + "(+) Taxa Média de Retorno: " + taxaRetorno + "\n"
                + "() Tempo Médio gasto por Atendimento: " + tempMedioAtend + "min\n"
                + "() Procedimento mais realizado: " + procedMaisRealizado + "\n";
    }
    public double calcularFaturamento(){
        this.totalFaturamento = 0;
        return totalFaturamento;
    }
    public double calcularTaxaRetorno(){
        this.taxaRetorno = 0;
        return taxaRetorno;
    }
    public double calcularTempMedio(){
        this.tempMedioAtend = 0;
        return tempMedioAtend; 
    }
    public String getProcMaisRealizado(){
        return procedMaisRealizado;
    }
    public void exportar(){
        try (FileWriter escritor = new FileWriter("relatorio_" + mes + "_" + ano + ".txt")){
            escritor.write("============= RELATÓRIO " + mes + "/" + ano + " =============\n");
            escritor.write(getEstatisticas());
            System.out.println("Relatório exportado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao exportar o relatório: " + e.getMessage());
        }
    }
    public void exibir(){
    gerar();
    System.out.println("============= RELATÓRIO " + mes + "/" + ano + " =============");
    System.out.println(getEstatisticas());
    }   
}
