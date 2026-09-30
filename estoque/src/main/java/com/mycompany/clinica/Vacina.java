package com.mycompany.clinica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//DATA DEVE MANTER O PADRÃO ANO/MÊS/DIA

public class Vacina extends RegistroClinico {
    private static final int DIAS_ANTECEDENCIA_REFORCO = 7; 

    private String nomeVacina;
    private String dataAplicacao;
    private String dataReforco;
    private String lote;
    private String fabricante;

    public Vacina(int id, String data, String descricao, Veterinario veterinario, String nomeVacina) {
        super(id, data, descricao, veterinario);
        this.nomeVacina = nomeVacina;
        this.dataAplicacao = data;
    }

    public String getNomeVacina() { return nomeVacina; }
    public String getDataAplicacao() { return dataAplicacao; }

    public void setDataReforco(String dataReforco) {
        verificarEdicao();
        this.dataReforco = dataReforco;
    }

    public String getDataReforco() { 
        return dataReforco; 
    }

    public void setLote(String lote) {
        verificarEdicao();
        this.lote = lote;
    }

    public String getLote() { 
        return lote; 
    }

    public void setFabricante(String fabricante) {
        verificarEdicao();
        this.fabricante = fabricante;
    }

    public String getFabricante() { 
        return fabricante; 
    }

    
    public boolean precisaReforco() {
        if (dataReforco == null) 
            return false;
        try {
            LocalDate reforco = LocalDate.parse(dataReforco, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate hoje = LocalDate.now();
            return !reforco.isAfter(hoje.plusDays(DIAS_ANTECEDENCIA_REFORCO));
        } catch (DateTimeParseException e) {
            return false; 
        }
    }

    @Override
    public void exibir() {
        System.out.println("--- Vacina: " + nomeVacina + " (#" + getId() + ") ---");
        System.out.println("Aplicada em: " + dataAplicacao + " | Lote: " + (lote != null ? lote : "-"));
        System.out.println("Fabricante: " + (fabricante != null ? fabricante : "-"));
        System.out.println("Reforço em: " + (dataReforco != null ? dataReforco : "não definido")
            + (precisaReforco() ? "  [LEMBRETE: reforço próximo/vencido]" : ""));
        System.out.println("Finalizada: " + isFinalizado());
    }
}
