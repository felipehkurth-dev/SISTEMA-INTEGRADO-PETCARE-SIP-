package com.mycompany.clinica;

public class Exame extends RegistroClinico {
    private String tipo;
    private String resultado;
    private String imagemAnexo;
    private String laboratorio;

    public Exame(int id, String data, String descricao, Veterinario veterinario, String tipo) {
        super(id, data, descricao, veterinario);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
         }

    public void setResultado(String resultado) {
        verificarEdicao();
        this.resultado = resultado;
    }

    public String getResultado() { 
        return resultado; 
        }

    public void anexarImagem(String path) {
        verificarEdicao();
        this.imagemAnexo = path;
    }

    public String getImagem() {
         return imagemAnexo; 
        }

    public void setLaboratorio(String laboratorio) {
        verificarEdicao();
        this.laboratorio = laboratorio;
    }

    public String getLaboratorio() { 
        return laboratorio; 
        }

    @Override
    public void exibir() {
        System.out.println("--- Exame #" + getId() + " (" + tipo + ") ---");
        System.out.println("Data: " + getData() + " | Laboratório: " + (laboratorio != null ? laboratorio : "-"));
        System.out.println("Resultado: " + (resultado != null ? resultado : "pendente"));
        System.out.println("Imagem anexada: " + (imagemAnexo != null ? imagemAnexo : "nenhuma"));
        System.out.println("Finalizado: " + isFinalizado());
    }
}
