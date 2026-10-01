package logistica.domain;

public interface NotificadorEntrega {

    void notificar(Encomenda encomenda, String status);
}
