package logistica.domain;

public class Rota {

    private String origem;
    private String destino;
    private double distanciaKm;
    private double valorPedagio;

    public Rota(String origem, String destino, double distanciaKm, double valorPedagio) {

        if (distanciaKm <= 0) {
            throw new IllegalArgumentException("A distância da rota deve ser maior que zero.");
        }
        if (valorPedagio < 0) {
            throw new IllegalArgumentException("O valor do pedágio não pode ser negativo.");
        }

        this.origem = origem;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
        this.valorPedagio = valorPedagio;
    }
    public String getOrigem() {
        return origem;
    }
    public String getDestino() {
        return destino;
    }
    public double getDistanciaKm() {
        return distanciaKm;
    }
    public double getValorPedagio() {
        return valorPedagio;
    }
}
