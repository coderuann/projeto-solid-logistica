package logistica.service;

import java.math.BigDecimal;

import logistica.domain.CalculadoraFrete;
import logistica.domain.Encomenda;
import logistica.domain.Rota;

public class CalculadoraFretePorPeso implements CalculadoraFrete {

    private BigDecimal precoPorQuilo = BigDecimal.valueOf(4.50);

    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        BigDecimal peso = BigDecimal.valueOf(encomenda.getPeso());
        
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());

        BigDecimal valorPeloPeso = peso.multiply(precoPorQuilo);

        BigDecimal valorFinal = valorPeloPeso.add(pedagio);


        return valorFinal;
    }
}
