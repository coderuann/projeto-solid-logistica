// Esta classe fica no pacote "service", junto com as outras regras de negócio
package logistica.service;

// Importa a classe BigDecimal, usada para trabalhar com dinheiro sem erros de arredondamento
import java.math.BigDecimal;

// Importa a interface que esta classe vai implementar (o contrato)
import logistica.domain.CalculadoraFrete;
// Importa a entidade Encomenda, porque o método do contrato recebe uma encomenda
import logistica.domain.Encomenda;
// Importa a entidade Rota, porque o cálculo usa a distância e o pedágio dela
import logistica.domain.Rota;

/*
 * CLASSE CalculadoraFretePorDistancia
 *
 * Regra de negócio: frete = (distância em km x R$ 1,20) + pedágio da rota
 *
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: esta classe só sabe calcular o frete pela distância.
 * - OCP: é mais uma extensão, criada sem alterar o serviço de cotação.
 * - LSP: cumpre o contrato por completo, então pode ser trocada por qualquer
 *   outra calculadora dentro do ServicoCotacaoFrete sem quebrar nada.
 */
public class CalculadoraFretePorDistancia implements CalculadoraFrete {

    // Este atributo guarda quanto a empresa cobra por cada quilômetro rodado
    private BigDecimal precoPorKm = BigDecimal.valueOf(1.20);

    /*
     * MÉTODO calcular: implementação do contrato da interface CalculadoraFrete.
     * LSP: mesma "assinatura" da interface e sempre devolve um valor válido.
     */
    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        // Transforma a distância da rota (double) em BigDecimal para fazer a conta com dinheiro
        BigDecimal distancia = BigDecimal.valueOf(rota.getDistanciaKm());
        // Transforma o valor do pedágio da rota (double) em BigDecimal
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());

        // Passo 1: multiplica a distância pelo preço por quilômetro
        BigDecimal valorPelaDistancia = distancia.multiply(precoPorKm);
        // Passo 2: soma o pedágio ao valor calculado pela distância
        BigDecimal valorFinal = valorPelaDistancia.add(pedagio);

        // Devolve o valor final do frete
        return valorFinal;
    }
}
