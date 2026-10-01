package logistica.domain;

import java.math.BigDecimal;

public interface CalculadoraFrete {

    BigDecimal calcular(Encomenda encomenda, Rota rota);
}
