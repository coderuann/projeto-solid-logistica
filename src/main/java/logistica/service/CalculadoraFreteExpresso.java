package logistica.service;
import java.math.BigDecimal;
import logistica.domain.CalculadoraFrete;
import logistica.domain.Encomenda;
import logistica.domain.Rota;

public class CalculadoraFreteExpresso implements CalculadoraFrete {

    private BigDecimal precoPorKm = BigDecimal.valueOf(2.00);
    private BigDecimal taxaUrgencia = BigDecimal.valueOf(30.00);
    private BigDecimal percentualSeguro = BigDecimal.valueOf(0.02);

    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        BigDecimal distancia = BigDecimal.valueOf(rota.getDistanciaKm());
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());
        BigDecimal valorDeclarado = BigDecimal.valueOf(encomenda.getValorDeclarado());
        BigDecimal valorPelaDistancia = distancia.multiply(precoPorKm);
        BigDecimal valorSeguro = valorDeclarado.multiply(percentualSeguro);
        BigDecimal valorFinal = valorPelaDistancia;
        valorFinal = valorFinal.add(taxaUrgencia);
        valorFinal = valorFinal.add(valorSeguro);
        valorFinal = valorFinal.add(pedagio);

        return valorFinal;
    }
}
