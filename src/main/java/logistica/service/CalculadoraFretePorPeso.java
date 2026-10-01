// Esta classe fica no pacote "service", junto com as outras regras de negócio
package logistica.service;

// Importa a classe BigDecimal, usada para trabalhar com dinheiro sem erros de arredondamento
import java.math.BigDecimal;

// Importa a interface que esta classe vai implementar (o contrato)
import logistica.domain.CalculadoraFrete;
// Importa a entidade Encomenda, porque o cálculo usa o peso dela
import logistica.domain.Encomenda;
// Importa a entidade Rota, porque o cálculo usa o pedágio dela
import logistica.domain.Rota;

/*
 * CLASSE CalculadoraFretePorPeso
 *
 * Regra de negócio: frete = (peso em kg x R$ 4,50) + pedágio da rota
 *
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: esta classe só sabe fazer UMA coisa: calcular o frete pelo peso.
 * - OCP: ela foi criada SEM mexer em nenhuma outra classe. É uma "extensão".
 * - LSP: ela cumpre o contrato da interface por completo (sempre devolve um valor
 *   válido). Por isso pode substituir qualquer outra CalculadoraFrete sem quebrar o serviço.
 */
public class CalculadoraFretePorPeso implements CalculadoraFrete {

    // Este atributo guarda quanto a empresa cobra por cada quilo transportado
    private BigDecimal precoPorQuilo = BigDecimal.valueOf(4.50);

    /*
     * MÉTODO calcular: implementação do contrato da interface CalculadoraFrete.
     * LSP: recebe os mesmos parâmetros e devolve o mesmo tipo que a interface promete.
     */
    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        // Transforma o peso da encomenda (double) em BigDecimal para fazer a conta com dinheiro
        BigDecimal peso = BigDecimal.valueOf(encomenda.getPeso());
        // Transforma o valor do pedágio da rota (double) em BigDecimal
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());

        // Passo 1: multiplica o peso pelo preço por quilo
        BigDecimal valorPeloPeso = peso.multiply(precoPorQuilo);
        // Passo 2: soma o pedágio ao valor calculado pelo peso
        BigDecimal valorFinal = valorPeloPeso.add(pedagio);

        // Devolve o valor final do frete
        return valorFinal;
    }
}
