package logistica.domain;
public class Motorista {

    private String nome;
    private String cnh;
    private Veiculo veiculo;

    public Motorista(String nome, String cnh, Veiculo veiculo) {

        if (veiculo == null) {
            throw new IllegalArgumentException("O motorista precisa ter um veículo associado.");
        }
        this.nome = nome;
        this.cnh = cnh;
        this.veiculo = veiculo;
    }
    public String getNome() {
        return nome;
    }
    public String getCnh() {
        return cnh;
    }
    public Veiculo getVeiculo() {
        return veiculo;
    }
}
