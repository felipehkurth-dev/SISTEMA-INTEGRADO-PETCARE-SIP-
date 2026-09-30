package com.mycompany.clinica;


public class Consulta extends RegistroClinico {
    private String motivo;
    private String prescricao;
    private String dataRetorno;

    public Consulta(int id, String data, String descricao, Veterinario veterinario, String motivo) {
        super(id, data, descricao, veterinario);
        this.motivo = motivo;
    }

    public String getMotivo() { 
        return motivo; 
    }

    public void setPrescricao(String prescricao) {
        verificarEdicao();
        this.prescricao = prescricao;
    }

    public String getPrescricao() { 
        return prescricao; 
    }

    public void setDataRetorno(String dataRetorno) {
        verificarEdicao();
        this.dataRetorno = dataRetorno;
    }

    public String getDataRetorno() {
        return dataRetorno;
        }

    @Override
    public void exibir() {
        System.out.println("--- Consulta #" + getId() + " ---");
        System.out.println("Data: " + getData() + " | Veterinário: " + getVeterinario());
        System.out.println("Motivo: " + motivo);
        System.out.println("Prescrição: " + (prescricao != null ? prescricao : "-"));
        System.out.println("Retorno: " + (dataRetorno != null ? dataRetorno : "não agendado"));
        System.out.println("Finalizada: " + isFinalizado());
    }
}
