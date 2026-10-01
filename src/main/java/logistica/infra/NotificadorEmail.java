package logistica.infra;
import logistica.domain.Encomenda;
import logistica.domain.NotificadorEntrega;

public class NotificadorEmail implements NotificadorEntrega {
    private String enderecoEmail;
    public NotificadorEmail(String enderecoEmail) {
        this.enderecoEmail = enderecoEmail;
    }
    @Override
    public void notificar(Encomenda encomenda, String status) {
        System.out.println("[E-mail para " + enderecoEmail + "]");
        System.out.println("  Assunto: Atualização da encomenda nº " + encomenda.getId());
        System.out.println("  Sua encomenda para " + encomenda.getEnderecoDestino() + " está: " + status);
    }
}
