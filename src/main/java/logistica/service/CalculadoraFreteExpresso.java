// Esta classe fica no pacote "service", junto com as outras regras de negócio
package logistica.service;

// Importa a classe BigDecimal, usada para trabalhar com dinheiro sem erros de arredondamento
import java.math.BigDecimal;

// Importa a interface que esta classe vai implementar (o contrato)
import logistica.domain.CalculadoraFrete;
// Importa a entidade Encomenda, porque o cálculo usa o valor declarado dela
import logistica.domain.Encomenda;
// Importa a entidade Rota, porque o cálculo usa a distância e o pedágio dela
import logistica.domain.Rota;

/*
 * CLASSE CalculadoraFreteExpresso
 *
 * Regra de negócio (entrega rápida, mais cara):
 * frete = (distância x R$ 2,00) + taxa de urgência de R$ 30,00
 *         + seguro de 2% do valor declarado + pedágio da rota
 *
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: esta classe só sabe calcular o frete expresso.
 * - OCP: é mais uma extensão, criada sem alterar o serviço de cotação.
 * - LSP: cumpre o contrato por completo, então pode substituir as outras
 *   calculadoras sem o serviço perceber diferença.
 */
public class CalculadoraFreteExpresso implements CalculadoraFrete {

    // Este atributo guarda o preço por quilômetro do expresso (mais caro que o normal)
    private BigDecimal precoPorKm = BigDecimal.valueOf(2.00);
    // Este atributo guarda a taxa fixa cobrada por ser uma entrega urgente
    private BigDecimal taxaUrgencia = BigDecimal.valueOf(30.00);
    // Este atributo guarda o percentual do seguro (2% = 0.02) sobre o valor declarado
    private BigDecimal percentualSeguro = BigDecimal.valueOf(0.02);

    /*
     * MÉTODO calcular: implementação do contrato da interface CalculadoraFrete.
     * LSP: mesma "assinatura" da interface e sempre devolve um valor válido.
     */
    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        // Transforma a distância da rota (double) em BigDecimal
        BigDecimal distancia = BigDecimal.valueOf(rota.getDistanciaKm());
        // Transforma o valor do pedágio da rota (double) em BigDecimal
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());
        // Transforma o valor declarado da encomenda (double) em BigDecimal
        BigDecimal valorDeclarado = BigDecimal.valueOf(encomenda.getValorDeclarado());

        // Passo 1: multiplica a distância pelo preço por quilômetro do expresso
        BigDecimal valorPelaDistancia = distancia.multiply(precoPorKm);
        // Passo 2: calcula o seguro (valor declarado x 2%)
        BigDecimal valorSeguro = valorDeclarado.multiply(percentualSeguro);

        // Passo 3: começa o total com o valor da distância
        BigDecimal valorFinal = valorPelaDistancia;
        // Passo 4: soma a taxa de urgência
        valorFinal = valorFinal.add(taxaUrgencia);
        // Passo 5: soma o seguro
        valorFinal = valorFinal.add(valorSeguro);
        // Passo 6: soma o pedágio
        valorFinal = valorFinal.add(pedagio);

        // Devolve o valor final do frete expresso
        return valorFinal;
    }
}
