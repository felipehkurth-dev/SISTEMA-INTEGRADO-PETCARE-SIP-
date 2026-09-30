/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modulofinanceiro;

/**
 *
 * @author USER
 */
import java.time.LocalDate;

public class Fatura {

    private int id;
    private Tutor tutor;
    private double valor;
    private String dataEmissao;
    private String dataVencimento;
    private String status;
    private String descricaoServico;
    private Notificador notificador;

    public Fatura(){

    }
    public Fatura(int id, Tutor tutor, double valor,
    String descricaoServico, Notificador notificador){
        this.id = id;
        this.tutor = tutor;
        this.valor = valor;
        this.descricaoServico = descricaoServico;
        this.notificador = notificador;
        
        this.dataEmissao = LocalDate.now().toString();
        this.dataVencimento = LocalDate.now().plusDays(30).toString();
        this.status = "PENDENTE";
    }
    
    public interface Notificador{
        void enviar(String destinatario, String mensagem);
    }
    public static class Tutor{
        private final String nome;
        private final String email;
        
        public Tutor (String nome, String email){
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
    public void emitir(){
        if (valor <= 0){
            throw new IllegalArgumentException ("A Fatura não pode conter valor negativo, nulo ou zero"); 
        }
        this.status = "PENDENTE";
    }
    
    public String gerarBoleto(){
        String Boleto = "============= BOLETO ===============\n" 
                + "Número identificador: " + id + "\n"
                + "Nome do Beneficiário: Clínica Veterinária PetCare\n"
                + "Data de Emissão: " + dataEmissao + "\n"
                + "Data de Vencimento: " + dataVencimento + "\n"
                + "(=) Valor do Documento: " + valor + "\n"
                + "-------------------------------------\n"
                + "Nome do Pagador: " + tutor + "\n"
                + "Descrição do Serviço: " + descricaoServico + "\n"
                + "=====================================";
        return Boleto;
    }
    
    public String gerarLinkPagamento(){
        String link = "https://pagamento.ClínicaVeterináriaPetCare.com/checkout/" + id;
        return link;
    }
    
    public void registrarPagamento(){
        if (status.equals("PAGO")){
            throw new IllegalArgumentException ("O pagamento desta fatura já foi registrado!"); 
        }
        status = "PAGO";
        notificador.enviar(tutor.getEmail(), "Pagamento da fatura #" + id + " registrado manualmente.");
    }
    
    public boolean confirmarPagOnline(){
        
        boolean pagamentoAprovado = true;
        
        if (pagamentoAprovado){
            this.status = "PAGO";
            notificador.enviar(tutor.getEmail(),"Pagamento da fatura#" + id + " realizado com sucesso!");
        } else {
            notificador.enviar(tutor.getEmail(),"ERRO! Não foi possível realizar o pagamento da fatura#" + id + "\n Tente novamente!");
        }
        return pagamentoAprovado;
    }
    
    public boolean isPendente() {
        boolean pendente = status.equals("PENDENTE");
        if (pendente){notificador.enviar(tutor.getEmail(),"O pagamento da Fatura#" + id + " está PENDENTE!\n"
                + "O NÃO PAGAMENTO da fatura resultará em juros de mora e juros diários");
        }
        return status.equals("PENDENTE");
    }
    
    public double getValor(){
        return valor;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void exibir (){
        System.out.println(gerarBoleto());
        notificador.enviar(tutor.getEmail(), "Você visualizou a fatura #" + id + ".");
    }
}