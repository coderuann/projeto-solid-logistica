package logistica.service;
import java.math.BigDecimal;
import logistica.domain.CalculadoraFrete;
import logistica.domain.Encomenda;
import logistica.domain.Rota;

public class CalculadoraFretePorDistancia implements CalculadoraFrete {

    
    private BigDecimal precoPorKm = BigDecimal.valueOf(1.20);

    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        
        BigDecimal distancia = BigDecimal.valueOf(rota.getDistanciaKm());
        
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());

      
        BigDecimal valorPelaDistancia = distancia.multiply(precoPorKm);

        BigDecimal valorFinal = valorPelaDistancia.add(pedagio);

        return valorFinal;
    }
}
