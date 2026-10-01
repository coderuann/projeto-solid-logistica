package logistica.domain;

public class Veiculo {

    private String placa;
    private double capacidadeCarga;
    private String tipo;

    public Veiculo(String placa, double capacidadeCarga, String tipo) {

        if (capacidadeCarga <= 0) {
            throw new IllegalArgumentException("A capacidade de carga deve ser maior que zero.");
        }
        this.placa = placa;
        this.capacidadeCarga = capacidadeCarga;
        this.tipo = tipo;
    }

    public boolean suportaPeso(double pesoDaCarga) {
        boolean suporta;
        if (pesoDaCarga <= capacidadeCarga) {
            suporta = true;
        } else {
            suporta = false;
        }
        return suporta;
    }
    public String getPlaca() {
        return placa;
    }
    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }
    public String getTipo() {
        return tipo;
    }
}
