package com.mycompany.clinica;

public abstract class RegistroClinico {
    private int id;
    private String data;
    private String descricao;
    private Veterinario veterinario;
    private String laudoAnexo;
    private boolean finalizado;

    public RegistroClinico(int id, String data, String descricao, Veterinario veterinario) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.veterinario = veterinario;
        this.finalizado = false;
    }

    public int getId() {
         return id; 
        }
    public String getData() { 
        return data;
        }
    public String getDescricao() { 
        return descricao; 
        }
    public Veterinario getVeterinario() { 
        return veterinario; 
        }

   public void finalizar() {
        this.finalizado = true;
        }

    public boolean isFinalizado() {
        return finalizado;
    }

    
    public void anexarLaudo(String path) {
        verificarEdicao();
        this.laudoAnexo = path;
    }

    public String getLaudo() {
        return laudoAnexo;
    }

    
    protected void verificarEdicao() {
        if (finalizado) {
            System.out.println  ("Registro #" + id + " já foi finalizado e não pode ser alterado.");
        }
    }
 public abstract void exibir();
}
