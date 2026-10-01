package logistica.domain;

public class Encomenda {

    private int id;
    private double peso;
    private double altura;
    private double largura;
    private double comprimento;
    private double valorDeclarado;
    private String enderecoOrigem;
    private String enderecoDestino;

    public Encomenda(int id, double peso, double altura, double largura, double comprimento,
                     double valorDeclarado, String enderecoOrigem, String enderecoDestino) {

        if (peso <= 0) {
            throw new IllegalArgumentException("O peso da encomenda deve ser maior que zero.");
        }

        if (altura <= 0) {
            throw new IllegalArgumentException("A altura da encomenda deve ser maior que zero.");
        }

        if (largura <= 0) {
            throw new IllegalArgumentException("A largura da encomenda deve ser maior que zero.");
        }

        if (comprimento <= 0) {
            throw new IllegalArgumentException("O comprimento da encomenda deve ser maior que zero.");
        }

        if (valorDeclarado < 0) {
            throw new IllegalArgumentException("O valor declarado não pode ser negativo.");
        }

        this.id = id;
        this.peso = peso;
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
        this.valorDeclarado = valorDeclarado;
        this.enderecoOrigem = enderecoOrigem;
        this.enderecoDestino = enderecoDestino;
    }

    public int getId() {
        return id;
    }
    public double getPeso() {
        return peso;
    }
    public double getAltura() {
        return altura;
    }
    public double getLargura() {
        return largura;
    }
    public double getComprimento() {
        return comprimento;
    }
    public double getValorDeclarado() {
        return valorDeclarado;
    }
    public String getEnderecoOrigem() {
        return enderecoOrigem;
    }
    public String getEnderecoDestino() {
        return enderecoDestino;
    }
}
