# Motor de Logística e Fretes — SOLID com Java

Trabalho da matéria de Padrões de Projetos. A ideia foi montar, em Java puro, um pequeno sistema de logística que calcula o frete de encomendas, registra os pedidos e avisa o cliente quando a encomenda sai para entrega, seguindo os cinco princípios do SOLID.


---

## Como rodar

O projeto usa Maven e Java 17 ou superior.

```
mvn clean compile
mvn exec:java -Dexec.mainClass="logistica.Main"
```

Ao executar, o `Main` roda dois cenários seguidos, cada um com um tipo de frete e um canal de notificação diferentes.

---

## Como o projeto está organizado

```
src/main/java/logistica/
├── Main.java
├── domain/
│   ├── Encomenda.java
│   ├── Veiculo.java
│   ├── Motorista.java
│   ├── Rota.java
│   ├── CalculadoraFrete.java      (interface)
│   ├── NotificadorEntrega.java    (interface)
│   └── RepositorioFrete.java      (interface)
├── service/
│   ├── CalculadoraFretePorPeso.java
│   ├── CalculadoraFretePorDistancia.java
│   ├── CalculadoraFreteExpresso.java
│   ├── ServicoCotacaoFrete.java
│   └── ServicoDespachoFrete.java
└── infra/
    ├── NotificadorWhatsApp.java
    ├── NotificadorEmail.java
    └── RepositorioFreteEmMemoria.java
```

- **domain:** as entidades e os contratos (interfaces) do sistema.
- **service:** as regras de cálculo de frete e os dois serviços que conduzem o fluxo.
- **infra:** as implementações "de fora", como os canais de notificação e o repositório em memória.

---

## Como apliquei cada princípio

### S — Responsabilidade única

Cada classe faz uma coisa só:

- As entidades (`Encomenda`, `Veiculo`, `Motorista`, `Rota`) guardam seus dados e validam suas próprias regras no construtor. Elas não calculam frete, não salvam nada e não notificam ninguém.
- Cada calculadora cuida de um jeito de cobrar o frete.
- Cada notificador cuida de um canal de aviso.
- Os serviços só coordenam o fluxo.

Se a fórmula do frete por peso mudar, só a `CalculadoraFretePorPeso` precisa ser mexida.

### O — Aberto para extensão, fechado para modificação

O `ServicoCotacaoFrete` não sabe qual calculadora está usando. Ele recebe a interface `CalculadoraFrete` no construtor e chama `calcular()`. No `Main`, o mesmo serviço é montado com calculadoras diferentes (peso, distância e expresso) e o código dele não muda.

Não há `switch`, `case` nem `instanceof` para escolher regra de negócio. Para criar um novo tipo de frete, basta uma nova classe que implemente `CalculadoraFrete`.

### L — Substituição de Liskov

Todas as implementações cumprem o contrato da interface de verdade:

- As três calculadoras recebem uma `Encomenda` e uma `Rota` e sempre devolvem um `BigDecimal`.
- `NotificadorWhatsApp` e `NotificadorEmail` realmente enviam (no caso, imprimem) a notificação.
- `RepositorioFreteEmMemoria` implementa `salvar()` e `buscarPorId()` por completo.

Nenhum método lança `UnsupportedOperationException` e nenhum fica vazio. Dá para trocar uma implementação pela outra e o sistema continua funcionando, que é o que o `Main` mostra.

### I — Segregação de interfaces

As interfaces são pequenas e cada uma tem um propósito:

- `CalculadoraFrete`: 1 método (`calcular`)
- `NotificadorEntrega`: 1 método (`notificar`)
- `RepositorioFrete`: 2 métodos (`salvar` e `buscarPorId`)

Assim, ninguém é obrigado a implementar algo que não usa. Uma interface única que misturasse cálculo, notificação e persistência obrigaria cada classe a carregar métodos que não são dela.

### D — Inversão de dependência

Os dois serviços dependem só de interfaces, que chegam pelo construtor:

- `ServicoCotacaoFrete` recebe `CalculadoraFrete` e `RepositorioFrete`.
- `ServicoDespachoFrete` recebe `NotificadorEntrega` e `RepositorioFrete`.

Não existe `new` de classe concreta dentro dos serviços. Quem monta tudo é o `Main`, que faz a injeção de dependências na mão. Se um dia o repositório em memória for trocado por um banco de dados, só o `Main` precisa mudar.

---

## Os dois cenários do Main

**Cenário 1 — frete por peso + WhatsApp**
Cotação de uma encomenda de 12,5 kg (R$ 4,50 por kg mais o pedágio) e aviso de saída para entrega por WhatsApp.

**Cenário 2 — frete por distância e expresso + e-mail**
Uma encomenda de 300 kg de São Paulo ao Rio de Janeiro, cotada pelos dois tipos de frete. O cliente escolhe o expresso e é avisado por e-mail.

---

## Validações nas entidades

Os construtores lançam `IllegalArgumentException` quando recebem dados que não fazem sentido:

- `Encomenda`: peso, altura, largura e comprimento precisam ser maiores que zero, e o valor declarado não pode ser negativo.
- `Veiculo`: a capacidade de carga precisa ser maior que zero.
- `Rota`: a distância precisa ser maior que zero e o pedágio não pode ser negativo.
- `Motorista`: precisa ter um veículo associado.

Além disso, o `ServicoDespachoFrete` confere se a encomenda existe no repositório e se ela cabe no veículo do motorista antes de despachar.

---

## Conferindo com o enunciado

| Requisito | Situação |
|---|---|
| Pelo menos 4 classes de domínio | `Encomenda`, `Veiculo`, `Motorista`, `Rota` |
| Pelo menos 3 interfaces | `CalculadoraFrete`, `NotificadorEntrega`, `RepositorioFrete` |
| Pelo menos 2 serviços de orquestração | `ServicoCotacaoFrete`, `ServicoDespachoFrete` |
| Sem `switch`/`instanceof` nas regras de negócio | Nenhuma ocorrência |
| Sem `UnsupportedOperationException` nem método vazio | Nenhuma ocorrência |
| Sem `new` de dependência nos serviços | Tudo injetado pelo construtor |
| `Main` com injeção manual e 2 cenários | Dois cenários executados |
| `src/main/java` e `.gitignore` | `target/`, `build/`, `.idea/`, `*.class` e `*.iml` ignorados |

---

## Exemplo de extensão

Para adicionar, por exemplo, um frete refrigerado, basta criar uma classe nova:

```java
public class CalculadoraFreteRefrigerado implements CalculadoraFrete {
    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        BigDecimal peso = BigDecimal.valueOf(encomenda.getPeso());
        BigDecimal pedagio = BigDecimal.valueOf(rota.getValorPedagio());
        return peso.multiply(BigDecimal.valueOf(6.00)).add(pedagio);
    }
}
```

E usá-la no `Main`:

```java
new ServicoCotacaoFrete(new CalculadoraFreteRefrigerado(), repositorio);
```

Nenhuma classe existente precisa ser alterada.
