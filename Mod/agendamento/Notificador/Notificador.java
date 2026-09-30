package Mod.agendamento.Notificador;

import Mod.agendamento.Agendamento.Agendamento;

import com.mycompany.clinica.Animal;
import com.mycompany.estoque.ItemEstoque;
import com.mycompany.modulofinanceiro.Fatura;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * DELEGAÇÃO: o Notificador sabe COMO avisar (canal, formato, se está ativo).
 * Quem o usa (Agendamento, Estoque, Fatura) só sabe QUANDO avisar.
 * Envio simulado com System.out, no formato do enunciado:
 * [NOTIFICADOR] EMAIL → joao@email.com: Consulta confirmada!
 */
public class Notificador {

    /** RN11: lembrete de vacina só é enviado com no mínimo 7 dias de antecedência. */
    public static final int ANTECEDENCIA_MINIMA_VACINA = 7;

    // ENCAPSULAMENTO: tudo private
    private String canal;          // EMAIL | SMS | APP (sempre guardado em maiúsculas)
    private String destinatario;   // contato padrão deste notificador
    private boolean ativo;

    public Notificador(String canal, String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("O destinatário não pode ser nulo ou vazio.");
        }
        this.destinatario = destinatario.trim(); // precisa estar pronto ANTES do setCanal
        setCanal(canal);                         // reaproveita a validação do setter
        this.ativo = true;                       // N1: nasce ligado
    }

    // ---------- getters / setters ----------

    public String getCanal() { return canal; }

    public void setCanal(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("O canal não pode ser nulo.");
        }
        String normalizado = canal.trim().toUpperCase(Locale.ROOT); // N3
        if (!normalizado.equals("EMAIL") && !normalizado.equals("SMS") && !normalizado.equals("APP")) {
            throw new IllegalArgumentException("Canal inválido: " + canal + " (use EMAIL, SMS ou APP).");
        }
        if (normalizado.equals("EMAIL") && !destinatario.contains("@")) { // N4
            throw new IllegalArgumentException("Canal EMAIL exige destinatário com '@'.");
        }
        this.canal = normalizado;
    }

    public String getDestinatario() { return destinatario; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    // ---------- notificações de agendamento (RF10) ----------

    public void enviarConfirmacao(Agendamento ag) {
        exigir(ag);
        enviar(destinatario, rotulo(ag) + (feminino(ag) ? " confirmada!" : " confirmado!"));
    }

    public void enviarCancelamento(Agendamento ag) {
        exigir(ag);
        enviar(destinatario, rotulo(ag) + (feminino(ag) ? " cancelada!" : " cancelado!"));
    }

    public void enviarReagendamento(Agendamento ag) {
        exigir(ag);
        enviar(destinatario, rotulo(ag) + (feminino(ag) ? " reagendada" : " reagendado")
                + " para " + ag.getDataHora() + "!");
    }

    // ---------- demais notificações ----------

    /** RF27/RN11: só envia se faltarem 7 dias ou mais para o reforço (dt no formato yyyy-MM-dd). */
    public void enviarLembreteVacina(Animal a, String dt) {
        if (a == null) {
            throw new IllegalArgumentException("Animal não pode ser nulo.");
        }
        LocalDate reforco;
        try {
            reforco = LocalDate.parse(dt == null ? "" : dt.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de reforço inválida (use yyyy-MM-dd): " + dt);
        }
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), reforco);
        if (dias < ANTECEDENCIA_MINIMA_VACINA) {
            System.out.println("[NOTIFICADOR] Lembrete não enviado (faltam " + dias + " dias; mínimo "
                    + ANTECEDENCIA_MINIMA_VACINA + ").");
            return;
        }
        enviar(destinatario, "Lembrete: reforço de vacina de " + a.getNome() + " em " + reforco + ".");
    }

    public void enviarAlertaEstoque(ItemEstoque item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo.");
        }
        enviar(destinatario, "Estoque baixo: " + item.getNome() + " | Quantidade: " + item.getQuantidade());
    }

    public void enviarFatura(Fatura f) {
        if (f == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula.");
        }
        enviar(destinatario, String.format(Locale.forLanguageTag("pt-BR"),
                "Fatura de R$%.2f | Status: %s", f.getValor(), f.getStatus()));
    }

    public void enviarAlerta(String dest, String msg) {
        if (dest == null || dest.trim().isEmpty() || msg == null || msg.trim().isEmpty()) {
            throw new IllegalArgumentException("Destinatário e mensagem são obrigatórios.");
        }
        enviar(dest.trim(), msg);
    }

    /** RF28: campanhas só por EMAIL ou SMS. */
    public void enviarCampanha(List<String> lista, String msg) {
        if (canal.equals("APP")) {
            throw new IllegalStateException("Campanhas só podem ser enviadas por EMAIL ou SMS.");
        }
        if (lista == null || msg == null || msg.trim().isEmpty()) {
            throw new IllegalArgumentException("Lista e mensagem são obrigatórias.");
        }
        for (String dest : lista) {
            if (dest != null && !dest.trim().isEmpty()) {
                enviar(dest.trim(), msg);
            }
        }
    }

    // ---------- internos ----------

    /** Único ponto que "imprime": N5 (inativo não envia) fica num lugar só. */
    private void enviar(String dest, String msg) {
        if (!ativo) {
            System.out.println("[NOTIFICADOR] Notificador desativado.");
            return;
        }
        System.out.println("[NOTIFICADOR] " + canal + " → " + dest + ": " + msg);
    }

    private void exigir(Agendamento ag) {
        if (ag == null) {
            throw new IllegalArgumentException("Agendamento não pode ser nulo.");
        }
    }

    private String rotulo(Agendamento ag) {
        String t = ag.getTipo(); // CONSULTA -> Consulta
        return t.charAt(0) + t.substring(1).toLowerCase(Locale.ROOT);
    }

    private boolean feminino(Agendamento ag) {
        return !"EXAME".equals(ag.getTipo());
    }
}
