package Mod.agendamento.Agendamento;

import Mod.agendamento.Notificador.Notificador;

import com.mycompany.clinica.Animal;
import com.mycompany.clinica.Tutor;
import com.mycompany.clinica.Veterinario;
import com.mycompany.estoque.ItemEstoque;
import com.mycompany.modulofinanceiro.Fatura;
import java.time.LocalDate;
import java.util.Arrays;

public class AgendamentoTeste {
    static int falhas = 0;

    static void conferir(String nome, Object obtido, Object esperado) {
        boolean ok = String.valueOf(obtido).equals(String.valueOf(esperado));
        if (!ok) falhas++;
        System.out.println((ok ? "[OK]    " : "[FALHA] ") + nome + " -> " + obtido + (ok ? "" : " (esperado " + esperado + ")"));
    }

    static boolean lanca(Runnable r) {
        try { r.run(); } catch (IllegalArgumentException | IllegalStateException e) { return true; }
        return false;
    }

    public static void main(String[] args) {
        Agendamento.limparAgenda();
        Tutor joao = new Tutor(1, "João Silva", "joao@email.com", "123", "999");
        Veterinario ana = new Veterinario(2, "Ana", "ana@petcare.com", "CRMV-123", "Clínica Geral");
        Veterinario beto = new Veterinario(3, "Beto", "beto@petcare.com", "CRMV-456", "Cirurgia");
        Animal rex = new Animal(1, "Rex", "Cão", "Labrador", joao);
        Notificador email = new Notificador("email", "joao@email.com");

        System.out.println("=== 1. NOTIFICADOR: construtor e setCanal ===");
        conferir("canal 'email' normalizado", email.getCanal(), "EMAIL");
        conferir("TELEGRAM rejeitado", lanca(() -> new Notificador("TELEGRAM", "x")), true);
        conferir("EMAIL sem @ rejeitado", lanca(() -> new Notificador("EMAIL", "joao")), true);
        conferir("destinatário vazio rejeitado", lanca(() -> new Notificador("SMS", " ")), true);
        conferir("SMS sem @ aceito", new Notificador("sms", "27999990000").getCanal(), "SMS");
        conferir("setCanal inválido rejeitado", lanca(() -> email.setCanal("fax")), true);
        conferir("canal segue EMAIL após falha", email.getCanal(), "EMAIL");

        System.out.println("\n=== 2. validarHorario (tabela de borda) ===");
        String d = "2027-03-10 ";
        Object[][] casos = {
            {"CONSULTA", d + "07:59", false}, {"CONSULTA", d + "08:00", true},
            {"CONSULTA", d + "17:00", true},  {"CONSULTA", d + "17:01", false},
            {"CONSULTA", d + "18:00", false}, {"EXAME", d + "17:00", true},
            {"CIRURGIA", d + "08:00", true},  {"CIRURGIA", d + "16:00", true},
            {"CIRURGIA", d + "16:01", false}, {"CIRURGIA", d + "17:00", false},
            {"CONSULTA", "abc", false},       {"CONSULTA", null, false},
            {"CONSULTA", "2027-02-30 09:00", false}
        };
        for (Object[] c : casos) {
            Agendamento t = new Agendamento(0, (String) c[1], (String) c[0], rex, ana, email);
            conferir(c[0] + " " + c[1], t.validarHorario(), c[2]);
        }

        System.out.println("\n=== 3. Fluxo: agendar -> reagendar inválido -> reagendar ok -> cancelar ===");
        Agendamento a1 = new Agendamento(1, d + "09:00", "CONSULTA", rex, ana, email);
        conferir("status inicial", a1.getStatus(), "PENDENTE");
        conferir("historico inicial vazio", a1.getHistorico().size(), 0);
        conferir("agendar()", a1.agendar(), true);
        conferir("status", a1.getStatus(), "AGENDADO");
        conferir("reagendar 20:00 rejeitado", lanca(() -> a1.reagendar(d + "20:00")), true);
        conferir("dataHora intacta", a1.getDataHora(), d + "09:00");
        conferir("historico ainda 1", a1.getHistorico().size(), 1);
        a1.reagendar(d + "10:00");
        conferir("dataHora nova", a1.getDataHora(), d + "10:00");
        a1.cancelar("tutor desistiu");
        conferir("status", a1.getStatus(), "CANCELADO");
        conferir("historico 3", a1.getHistorico().size(), 3);
        conferir("cancelar de novo rejeitado", lanca(() -> a1.cancelar("x")), true);
        conferir("reagendar cancelado rejeitado", lanca(() -> a1.reagendar(d + "11:00")), true);
        conferir("agendar cancelado -> false", a1.agendar(), false);
        a1.exibir();
        System.out.println(a1.getHistorico());

        System.out.println("\n=== 4. Conflito por CRMV, cirurgia e sala ===");
        Agendamento a2 = new Agendamento(2, d + "09:00", "CONSULTA", rex, ana, email);
        conferir("a2 agenda 09:00 (horario liberado pelo cancelamento)", a2.agendar(), true);
        Agendamento a3 = new Agendamento(3, d + "09:30", "EXAME", rex, ana, email);
        conferir("a3 sobrepõe a2 -> false", a3.agendar(), false);
        conferir("a3 segue PENDENTE, sem notificar", a3.getStatus(), "PENDENTE");
        a2.cancelar("remarcar");
        conferir("a3 após liberar horário", a3.agendar(), true);

        Agendamento c1 = new Agendamento(4, "2027-03-11 15:00", "CIRURGIA", rex, beto, email);
        conferir("cirurgia sem sala -> false", c1.agendar(), false);
        c1.setSala("Sala 1");
        conferir("cirurgia com sala -> true", c1.agendar(), true);
        Agendamento c2 = new Agendamento(5, "2027-03-11 16:00", "CONSULTA", rex, beto, email);
        conferir("consulta dentro da cirurgia (2º horário) -> false", c2.agendar(), false);
        Agendamento c3 = new Agendamento(6, "2027-03-11 17:00", "CONSULTA", rex, beto, email);
        conferir("consulta logo após cirurgia -> true", c3.agendar(), true);
        beto.setDisponivel(false);
        Agendamento c4 = new Agendamento(7, "2027-03-12 09:00", "CONSULTA", rex, beto, email);
        conferir("vet indisponível -> false", c4.agendar(), false);
        beto.setDisponivel(true);

        System.out.println("\n=== 5. Construtor do Agendamento ===");
        conferir("tipo inválido", lanca(() -> new Agendamento(9, d + "09:00", "BANHO", rex, ana, email)), true);
        conferir("notificador nulo", lanca(() -> new Agendamento(9, d + "09:00", "EXAME", rex, ana, null)), true);

        System.out.println("\n=== 6. Notificador inativo (N5) ===");
        email.setAtivo(false);
        Agendamento a8 = new Agendamento(8, "2027-03-15 09:00", "CONSULTA", rex, ana, email);
        conferir("agendar com notificador inativo", a8.agendar(), true);
        email.setAtivo(true);

        System.out.println("\n=== 7. Outros métodos do Notificador ===");
        email.enviarLembreteVacina(rex, LocalDate.now().plusDays(10).toString());
        email.enviarLembreteVacina(rex, LocalDate.now().plusDays(3).toString());
        email.enviarAlertaEstoque(new ItemEstoque(1, "Dipirona", 5, 10, "L1"));
        email.enviarFatura(new Fatura());
        email.enviarAlerta("Administrador", "Item vencido: Xarope");
        email.enviarCampanha(Arrays.asList("a@x.com", "b@x.com"), "Campanha de vacinação!");
        conferir("campanha por APP rejeitada", lanca(() -> new Notificador("APP", "id-app-1").enviarCampanha(Arrays.asList("a"), "m")), true);

        System.out.println("\nFalhas: " + falhas);
    }
}
