package Mod.agendamento.Agendamento;

import Mod.agendamento.Notificador.Notificador;

import com.mycompany.clinica.Animal;
import com.mycompany.clinica.Veterinario;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * COMPOSIÇÃO: tem-um Animal, Veterinario e Notificador.
 * DELEGAÇÃO: nunca imprime/envia mensagem; só chama o Notificador.
 *
 * Regras (RF06-RF11, RN02-RN05):
 *  - dataHora no formato "yyyy-MM-dd HH:mm".
 *  - O procedimento INTEIRO cabe entre 08:00 e 18:00.
 *  - Tempo mínimo de atendimento: CONSULTA e EXAME = 1 hora; CIRURGIA = 2 horas (RN02).
 *    Por isso consulta/exame podem começar até 17:00 (terminam 18:00)
 *    e a cirurgia até 16:00 (termina 18:00).
 *  - O veterinário é identificado pelo CRMV; uma cirurgia reserva 2 horários do CRMV.
 */
public class Agendamento {

    public static final String PENDENTE = "PENDENTE";   // valor extra (A4): ainda não agendado
    public static final String AGENDADO = "AGENDADO";
    public static final String CANCELADO = "CANCELADO";
    public static final String CONCLUIDO = "CONCLUIDO";

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final LocalTime ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(18, 0);
    private static final int MIN_CONSULTA_EXAME = 60;   // tempo mínimo de atendimento: 1 hora
    private static final int MIN_CIRURGIA = 120;        // RN02

    // ---- agenda compartilhada por CRMV (RN03): todos os Agendamentos consultam a mesma ----
    private static class Reserva {
        final LocalDateTime inicio;
        final LocalDateTime fim;
        final Agendamento dono;

        Reserva(LocalDateTime inicio, LocalDateTime fim, Agendamento dono) {
            this.inicio = inicio;
            this.fim = fim;
            this.dono = dono;
        }

        boolean sobrepoe(LocalDateTime i, LocalDateTime f) {
            return inicio.isBefore(f) && i.isBefore(fim);
        }
    }

    private static final Map<String, List<Reserva>> AGENDA_POR_CRMV = new HashMap<>();

    /** Uso em testes: zera a agenda compartilhada. */
    static void limparAgenda() {
        AGENDA_POR_CRMV.clear();
    }

    // ---- atributos (ENCAPSULAMENTO) ----
    private int id;
    private String dataHora;
    private String tipo;      // CONSULTA | EXAME | CIRURGIA
    private String status;    // PENDENTE | AGENDADO | CANCELADO | CONCLUIDO
    private Animal animal;
    private Veterinario veterinario;
    private String sala;
    private Notificador notificador;
    private List<String> historico;

    public Agendamento(int id, String dt, String tipo, Animal animal, Veterinario vet, Notificador notif) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo não pode ser nulo.");
        }
        String t = tipo.trim().toUpperCase();
        if (!t.equals("CONSULTA") && !t.equals("EXAME") && !t.equals("CIRURGIA")) {
            throw new IllegalArgumentException("Tipo inválido: " + tipo);
        }
        if (animal == null || vet == null || notif == null) { // A8: falha cedo
            throw new IllegalArgumentException("Animal, veterinário e notificador são obrigatórios.");
        }
        this.id = id;
        this.dataHora = dt;            // formato inválido não quebra: validarHorario() devolve false
        this.tipo = t;
        this.animal = animal;
        this.veterinario = vet;
        this.notificador = notif;
        this.status = PENDENTE;
        this.historico = new ArrayList<>();   // sem isso, o 1º add daria NullPointerException
    }

    // ---- getters / setter ----
    public int getId() { return id; }
    public String getDataHora() { return dataHora; }
    public String getTipo() { return tipo; }
    public String getStatus() { return status; }
    public Animal getAnimal() { return animal; }
    public Veterinario getVeterinario() { return veterinario; }
    public String getSala() { return sala; }
    public List<String> getHistorico() { return Collections.unmodifiableList(historico); }

    public void setSala(String sala) {
        if (sala == null || sala.trim().isEmpty()) {
            throw new IllegalArgumentException("A sala não pode ser vazia.");
        }
        this.sala = sala.trim();
    }

    // ---- validações ----

    /** RF08/RN04/RN05: o procedimento inteiro cabe entre 08:00 e 18:00. */
    public boolean validarHorario() {
        return horarioValido(dataHora);
    }

    /** RN03: vet disponível E sem outro procedimento sobreposto no mesmo CRMV. */
    public boolean validarVeterinario() {
        return veterinarioLivreEm(dataHora);
    }

    /** RF11: aqui só o que é do Agendamento (sala da cirurgia). Itens de estoque: Estoque.reservarParaProced. */
    public boolean reservarRecursos() {
        if (tipo.equals("CIRURGIA")) {
            return sala != null && !sala.trim().isEmpty();
        }
        return true;
    }

    // ---- ações ----

    /** Ordem: horário -> veterinário -> recursos. Falhou? devolve false sem mexer em nada e sem notificar. */
    public boolean agendar() {
        if (!status.equals(PENDENTE)) return false;
        if (!validarHorario()) return false;
        if (!validarVeterinario()) return false;
        if (!reservarRecursos()) return false;

        reservarAgenda(dataHora);
        status = AGENDADO;
        historico.add("AGENDADO | " + dataHora);
        notificarTutor();
        return true;
    }

    public void cancelar(String motivo) {
        if (!status.equals(AGENDADO)) {
            throw new IllegalStateException("Só é possível cancelar agendamento AGENDADO (atual: " + status + ").");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("O motivo do cancelamento é obrigatório.");
        }
        liberarAgenda();                                  // libera o horário do CRMV
        status = CANCELADO;
        historico.add("CANCELADO | motivo: " + motivo.trim());
        notificarTutor();                                 // por último: mensagem sai com o estado final
    }

    public void reagendar(String novaData) {
        if (!status.equals(AGENDADO)) {
            throw new IllegalStateException("Só é possível reagendar agendamento AGENDADO (atual: " + status + ").");
        }
        // 1) VALIDAR tudo antes de alterar qualquer atributo
        if (!horarioValido(novaData)) {
            throw new IllegalArgumentException("Horário inválido para reagendamento: " + novaData);
        }
        if (!veterinarioLivreEm(novaData)) {
            throw new IllegalArgumentException("Veterinário indisponível em " + novaData);
        }
        // 2) só agora muda o estado
        String antiga = dataHora;
        liberarAgenda();
        reservarAgenda(novaData);
        dataHora = novaData.trim();
        historico.add("REAGENDADO | de " + antiga + " para " + dataHora);
        notificar(Evento.REAGENDAMENTO);
    }

    /** DELEGAÇÃO: decide QUANDO avisar (pelo status); o COMO é do Notificador. */
    public void notificarTutor() {
        if (status.equals(AGENDADO)) {
            notificar(Evento.CONFIRMACAO);
        } else if (status.equals(CANCELADO)) {
            notificar(Evento.CANCELAMENTO);
        }
    }

    public void exibir() {
        System.out.println("[AGENDAMENTO] Tipo: " + tipo + " | Status: " + status);
        System.out.println("   Animal: " + animal.getNome() + " | Vet: " + veterinario.getNome()
                + " (" + veterinario.getCrmv() + ") | Data: " + dataHora
                + " | Sala: " + (sala != null ? sala : "-"));
    }

    // ---- internos ----

    private enum Evento { CONFIRMACAO, CANCELAMENTO, REAGENDAMENTO }

    private void notificar(Evento e) {
        switch (e) {
            case CONFIRMACAO:   notificador.enviarConfirmacao(this); break;
            case CANCELAMENTO:  notificador.enviarCancelamento(this); break;
            case REAGENDAMENTO: notificador.enviarReagendamento(this); break;
        }
    }

    private LocalDateTime converter(String texto) {
        if (texto == null) return null;
        try {
            return LocalDateTime.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int duracaoMinutos() {
        return tipo.equals("CIRURGIA") ? MIN_CIRURGIA : MIN_CONSULTA_EXAME;
    }

    private boolean horarioValido(String texto) {
        LocalDateTime inicio = converter(texto);
        if (inicio == null) return false;
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutos());
        boolean mesmoDia = fim.toLocalDate().equals(inicio.toLocalDate());
        return mesmoDia
                && !inicio.toLocalTime().isBefore(ABERTURA)
                && !fim.toLocalTime().isAfter(FECHAMENTO);
    }

    private boolean veterinarioLivreEm(String texto) {
        if (veterinario == null || !veterinario.isDisponivel()) return false;
        String crmv = veterinario.getCrmv();
        if (crmv == null || crmv.trim().isEmpty()) return false;
        LocalDateTime inicio = converter(texto);
        if (inicio == null) return false;
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutos());
        List<Reserva> reservas = AGENDA_POR_CRMV.get(crmv.trim());
        if (reservas == null) return true;
        for (Reserva r : reservas) {
            if (r.dono != this && r.sobrepoe(inicio, fim)) return false; // ignora a própria reserva
        }
        return true;
    }

    private void reservarAgenda(String texto) {
        LocalDateTime inicio = converter(texto);
        AGENDA_POR_CRMV
                .computeIfAbsent(veterinario.getCrmv().trim(), k -> new ArrayList<>())
                .add(new Reserva(inicio, inicio.plusMinutes(duracaoMinutos()), this));
    }

    private void liberarAgenda() {
        List<Reserva> reservas = AGENDA_POR_CRMV.get(veterinario.getCrmv().trim());
        if (reservas != null) reservas.removeIf(r -> r.dono == this);
    }
}
