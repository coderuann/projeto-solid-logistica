package logistica;
import java.math.BigDecimal;
import logistica.domain.CalculadoraFrete;
import logistica.domain.Encomenda;
import logistica.domain.Motorista;
import logistica.domain.NotificadorEntrega;
import logistica.domain.RepositorioFrete;
import logistica.domain.Rota;
import logistica.domain.Veiculo;
import logistica.infra.NotificadorEmail;
import logistica.infra.NotificadorWhatsApp;
import logistica.infra.RepositorioFreteEmMemoria;
import logistica.service.CalculadoraFreteExpresso;
import logistica.service.CalculadoraFretePorDistancia;
import logistica.service.CalculadoraFretePorPeso;
import logistica.service.ServicoCotacaoFrete;
import logistica.service.ServicoDespachoFrete;

public class Main {
    public static void main(String[] args) {

        RepositorioFrete repositorio = new RepositorioFreteEmMemoria();

        Veiculo van = new Veiculo("ABC-1D23", 1500.0, "Van");
        Motorista motorista = new Motorista("Carlos Souza", "12345678900", van);

        System.out.println("========== CENÁRIO 1: Frete por PESO + WhatsApp ==========");

        CalculadoraFrete calculadoraPorPeso = new CalculadoraFretePorPeso();
        NotificadorEntrega notificadorWhatsApp = new NotificadorWhatsApp("(11) 98888-7777");
        ServicoCotacaoFrete cotacaoPorPeso = new ServicoCotacaoFrete(calculadoraPorPeso, repositorio);
        ServicoDespachoFrete despachoWhatsApp = new ServicoDespachoFrete(notificadorWhatsApp, repositorio);

        Encomenda encomendaUm = new Encomenda(1, 12.5, 30.0, 40.0, 50.0, 800.00,
                "Rua A, 100 - São Paulo/SP", "Rua B, 200 - Campinas/SP");
        Rota rotaUm = new Rota("São Paulo", "Campinas", 95.0, 15.80);

        BigDecimal freteUm = cotacaoPorPeso.cotar(encomendaUm, rotaUm);
        System.out.println("Frete por peso: R$ " + String.format("%.2f", freteUm));

        despachoWhatsApp.despachar(1, motorista);

        System.out.println();

        System.out.println("===== CENÁRIO 2: Frete por DISTÂNCIA e EXPRESSO + E-mail =====");

        CalculadoraFrete calculadoraPorDistancia = new CalculadoraFretePorDistancia();
        CalculadoraFrete calculadoraExpresso = new CalculadoraFreteExpresso();
        NotificadorEntrega notificadorEmail = new NotificadorEmail("cliente@empresa.com.br");
        ServicoCotacaoFrete cotacaoPorDistancia = new ServicoCotacaoFrete(calculadoraPorDistancia, repositorio);
        ServicoCotacaoFrete cotacaoExpresso = new ServicoCotacaoFrete(calculadoraExpresso, repositorio);
        ServicoDespachoFrete despachoEmail = new ServicoDespachoFrete(notificadorEmail, repositorio);
        Encomenda encomendaDois = new Encomenda(2, 300.0, 100.0, 80.0, 120.0, 2500.00,
                "Av. C, 300 - São Paulo/SP", "Rua D, 400 - Rio de Janeiro/RJ");
        Rota rotaDois = new Rota("São Paulo", "Rio de Janeiro", 430.0, 45.60);

        BigDecimal freteDistancia = cotacaoPorDistancia.cotar(encomendaDois, rotaDois);
        System.out.println("Frete por distância: R$ " + String.format("%.2f", freteDistancia));

        BigDecimal freteExpresso = cotacaoExpresso.cotar(encomendaDois, rotaDois);
        System.out.println("Frete expresso:      R$ " + String.format("%.2f", freteExpresso));

        System.out.println("Cliente escolheu o frete EXPRESSO.");
        despachoEmail.despachar(2, motorista);
    }
}
