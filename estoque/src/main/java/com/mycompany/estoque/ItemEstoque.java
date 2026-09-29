/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.estoque;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author izabe
 */
public class ItemEstoque {
    
    private int id;
    private String nome;
    private int quantidade;
    private int quantidadeMinima;
    private String lote;
    private String validade;
    private boolean controlado;
    private String responsavelRetirada;
    private String categoria;
    
    public ItemEstoque(int id, String nome, int quantidade,
            int quantidadeMinima, String lote) {
    this.id = id;
    setNome(nome);
    
    if(quantidade < 0) {
       throw new IllegalArgumentException(
       "A quantidade não pode ser negativada."
       );
    }
    if(quantidadeMinima < 0) {
       throw new IllegalArgumentException(
       "A quantidade mínima não pode ser negativada."
       );
    }
    
    this.quantidade = quantidade;
    this.quantidadeMinima = quantidadeMinima;
    this.lote = lote;
    
    this.validade = null;
    this.controlado = false;
    this.responsavelRetirada = null;
    this.categoria = null;
    }
    
    //Métodos de Estoque 
    
    public void darEntrada(int qtd, String resp){
    
        if(qtd <=0){
            throw new IllegalArgumentException(
                    "A quantidade de entrada deve ser maior que zero."
            );
        }
        
        quantidade = quantidade + qtd;
        
        if(resp != null && !resp.trim().isEmpty()){
            responsavelRetirada = resp;
        }
    }
    
    public boolean darSaida(int qtd, String resp){
        if(qtd <= 0){
            return false;
        }
        if (qtd > quantidade){
            return false;
        }
        if (controlado){
        
            if(lote == null || lote.trim().isEmpty()){
                return false;
            }
           if (validade == null || validade.trim().isEmpty()) {
                return false;
            }

            if (resp == null || resp.trim().isEmpty()) {
                return false;
            }

            responsavelRetirada = resp;
        }

        quantidade = quantidade - qtd;

        return true;
    }

    public boolean reservar(int qtd) {

        if (qtd <= 0) {
            return false;
        }

        if (qtd > quantidade) {
            return false;
        }

        quantidade = quantidade - qtd;

        return true;
    }

    // VERIFICAÇÕES

    public boolean isAbaixoMinimo() {
        return quantidade < quantidadeMinima;
    }

    public boolean isControlado() {
        return controlado;
    }

    public boolean isVencido() {

        if (validade == null || validade.trim().isEmpty()) {
            return false;
        }

        try {

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dataValidade =
                    LocalDate.parse(validade, formato);

            return dataValidade.isBefore(LocalDate.now());

        } catch (DateTimeParseException e) {

            return false;
        }
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public String getLote() {
        return lote;
    }

    public String getValidade() {
        return validade;
    }

    public String getResponsavelRetirada() {
        return responsavelRetirada;
    }

    public String getCategoria() {
        return categoria;
    }
    // SETTERS

    public void setNome(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome não pode ser vazio."
            );
        }

        this.nome = nome;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {

        if (quantidadeMinima < 0) {
            throw new IllegalArgumentException(
                    "A quantidade mínima não pode ser negativa."
            );
        }

        this.quantidadeMinima = quantidadeMinima;
    }

    public void setLote(String lote) {
        this.lote = lote;

        if (controlado) {
            validarDadosControlado();
        }
    }

    public void setValidade(String validade) {

        if (validade != null && !validade.trim().isEmpty()) {

            try {

                DateTimeFormatter formato =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate.parse(validade, formato);

            } catch (DateTimeParseException e) {

                throw new IllegalArgumentException(
                        "A validade deve estar no formato yyyy-MM-dd."
                );
            }
        }

        this.validade = validade;

        if (controlado) {
            validarDadosControlado();
        }
    }

    public void setControlado(boolean controlado) {

        if (controlado) {
            this.controlado = true;
            validarDadosControlado();
        } else {
            this.controlado = false;
        }
    }

    public void setResponsavelRetirada(String responsavelRetirada) {

        this.responsavelRetirada = responsavelRetirada;

        if (controlado) {
            validarDadosControlado();
        }
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // VALIDAÇÃO DE CONTROLADO

    private void validarDadosControlado() {

        if (lote == null || lote.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Medicamento controlado precisa de lote."
            );
        }

        if (validade == null || validade.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Medicamento controlado precisa de validade."
            );
        }

        if (responsavelRetirada == null
                || responsavelRetirada.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Medicamento controlado precisa de responsável pela retirada."
            );
        }
    }


    public void exibir() {

        System.out.println(
                "[ITEM] " + nome
                + " | Qtd: " + quantidade
                + " | Mínimo: " + quantidadeMinima
                + " | Lote: " + lote
                + " | Validade: " + validade
                + " | Controlado: " + controlado
        );
    }
}
            
            
        
