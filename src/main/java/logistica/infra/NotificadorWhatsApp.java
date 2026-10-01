package logistica.infra;
import logistica.domain.Encomenda;
import logistica.domain.NotificadorEntrega;

public class NotificadorWhatsApp implements NotificadorEntrega {
    private String numeroTelefone;

    public NotificadorWhatsApp(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
    @Override
    public void notificar(Encomenda encomenda, String status) {
        System.out.println("[WhatsApp para " + numeroTelefone + "]");
        System.out.println("  Olá! Sua encomenda nº " + encomenda.getId()
                + " para " + encomenda.getEnderecoDestino() + " está: " + status);
    }
}
