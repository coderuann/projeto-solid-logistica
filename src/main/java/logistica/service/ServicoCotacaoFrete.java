// Esta classe fica no pacote "service", onde ficam as classes que conduzem o fluxo
package logistica.service;

// Importa a classe BigDecimal, usada para trabalhar com dinheiro
import java.math.BigDecimal;

// Importa a INTERFACE da calculadora (e não uma calculadora concreta) - DIP
import logistica.domain.CalculadoraFrete;
// Importa a entidade Encomenda, que é o que vai ser cotado
import logistica.domain.Encomenda;
// Importa a INTERFACE do repositório (e não o repositório concreto) - DIP
import logistica.domain.RepositorioFrete;
// Importa a entidade Rota, usada no cálculo do frete
import logistica.domain.Rota;

/*
 * CLASSE ServicoCotacaoFrete (serviço de orquestração)
 *
 * O que ela faz: conduz o fluxo de cotação:
 *   1) pede para a calculadora calcular o frete;
 *   2) pede para o repositório guardar a encomenda;
 *   3) devolve o valor do frete.
 * Ela NÃO faz a conta e NÃO sabe como os dados são guardados. Ela só coordena.
 *
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: a única responsabilidade é coordenar a cotação.
 * - DIP: depende só de INTERFACES (CalculadoraFrete e RepositorioFrete),
 *   que chegam prontas pelo construtor. Não existe nenhum "new" de
 *   calculadora ou repositório aqui dentro.
 * - OCP: se surgir uma nova regra de frete, esta classe NÃO muda.
 *   Basta entregar outra calculadora no construtor.
 * - LSP: qualquer calculadora que implemente a interface funciona aqui.
 */
public class ServicoCotacaoFrete {

    // Este atributo guarda a calculadora que vai ser usada (qualquer uma que siga o contrato)
    private CalculadoraFrete calculadora;
    // Este atributo guarda o repositório onde a encomenda cotada será registrada
    private RepositorioFrete repositorio;

    /*
     * CONSTRUTOR: recebe as dependências prontas, de fora (injeção de dependência manual).
     * DIP: quem decide QUAL calculadora e QUAL repositório usar é a Main, não este serviço.
     */
    public ServicoCotacaoFrete(CalculadoraFrete calculadora, RepositorioFrete repositorio) {
        // Guarda a calculadora recebida no atributo da classe
        this.calculadora = calculadora;
        // Guarda o repositório recebido no atributo da classe
        this.repositorio = repositorio;
    }

    /*
     * MÉTODO cotar: calcula o frete de uma encomenda em uma rota e registra a encomenda.
     *
     * OCP / polimorfismo: a linha "calculadora.calcular(...)" funciona com QUALQUER
     * calculadora. Não existe if/else nem switch para escolher o tipo de frete.
     */
    public BigDecimal cotar(Encomenda encomenda, Rota rota) {
        // Pede para a calculadora (seja ela qual for) calcular o valor do frete
        BigDecimal valorFrete = calculadora.calcular(encomenda, rota);
        // Pede para o repositório guardar a encomenda, para o despacho encontrá-la depois
        repositorio.salvar(encomenda);
        // Devolve o valor do frete para quem chamou (a Main)
        return valorFrete;
    }
}
