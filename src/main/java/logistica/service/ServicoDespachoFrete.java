// Esta classe fica no pacote "service", onde ficam as classes que conduzem o fluxo
package logistica.service;

// Importa a entidade Encomenda, que é o que vai ser despachado
import logistica.domain.Encomenda;
// Importa a entidade Motorista, que vai levar a encomenda
import logistica.domain.Motorista;
// Importa a INTERFACE do notificador (e não o WhatsApp ou o E-mail) - DIP
import logistica.domain.NotificadorEntrega;
// Importa a INTERFACE do repositório (e não o repositório concreto) - DIP
import logistica.domain.RepositorioFrete;
// Importa a entidade Veiculo, para verificar se a encomenda cabe nele
import logistica.domain.Veiculo;

/*
 * CLASSE ServicoDespachoFrete (serviço de orquestração)
 *
 * O que ela faz: conduz o fluxo de despacho:
 *   1) busca a encomenda no repositório;
 *   2) pergunta ao veículo do motorista se ele aguenta o peso;
 *   3) avisa o cliente pelo notificador.
 *
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: a única responsabilidade é coordenar o despacho.
 * - DIP: depende só de INTERFACES (NotificadorEntrega e RepositorioFrete),
 *   recebidas pelo construtor. Não existe nenhum "new" de notificador
 *   ou repositório aqui dentro.
 * - OCP: se surgir um novo canal (ex.: SMS), esta classe NÃO muda.
 * - LSP: qualquer notificador que implemente a interface funciona aqui.
 */
public class ServicoDespachoFrete {

    // Este atributo guarda o notificador que vai avisar o cliente (qualquer um que siga o contrato)
    private NotificadorEntrega notificador;
    // Este atributo guarda o repositório de onde a encomenda será buscada
    private RepositorioFrete repositorio;

    /*
     * CONSTRUTOR: recebe as dependências prontas, de fora (injeção de dependência manual).
     * DIP: quem decide QUAL notificador e QUAL repositório usar é a Main.
     */
    public ServicoDespachoFrete(NotificadorEntrega notificador, RepositorioFrete repositorio) {
        // Guarda o notificador recebido no atributo da classe
        this.notificador = notificador;
        // Guarda o repositório recebido no atributo da classe
        this.repositorio = repositorio;
    }

    /*
     * MÉTODO despachar: entrega a encomenda para um motorista e avisa o cliente.
     *
     * Os "if" daqui verificam dados (encontrou? cabe no veículo?).
     * Eles NÃO escolhem o tipo de notificador. Por isso não violam o OCP.
     * O notificador certo já veio pronto pelo construtor (polimorfismo).
     */
    public void despachar(int idEncomenda, Motorista motorista) {
        // Pede para o repositório procurar a encomenda pelo id
        Encomenda encomenda = repositorio.buscarPorId(idEncomenda);

        // Verifica se a encomenda NÃO foi encontrada (o repositório devolveu null)
        if (encomenda == null) {
            // Mostra uma mensagem de aviso na tela
            System.out.println("Encomenda " + idEncomenda + " não encontrada. Despacho cancelado.");
            // Sai do método, porque não tem o que despachar
            return;
        }

        // Pega o veículo que o motorista dirige
        Veiculo veiculo = motorista.getVeiculo();
        // Pergunta ao veículo se ele aguenta o peso da encomenda (regra do próprio veículo - SRP)
        boolean cabeNoVeiculo = veiculo.suportaPeso(encomenda.getPeso());

        // Verifica se a encomenda NÃO cabe no veículo
        if (cabeNoVeiculo == false) {
            // Avisa o cliente que houve um problema no despacho
            notificador.notificar(encomenda, "AGUARDANDO VEÍCULO MAIOR");
            // Sai do método, porque a encomenda não pode ser despachada neste veículo
            return;
        }

        // Mostra na tela quem vai levar a encomenda e em qual veículo
        System.out.println("Encomenda " + encomenda.getId() + " entregue ao motorista "
                + motorista.getNome() + " (" + veiculo.getTipo() + " placa " + veiculo.getPlaca() + ").");
        // Pede para o notificador (seja ele qual for) avisar o cliente
        notificador.notificar(encomenda, "SAIU PARA ENTREGA");
    }
}
