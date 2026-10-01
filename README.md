# Motor de Logística e Fretes — Princípios SOLID com Java

Aplicação desenvolvida em Java, estrutura Maven, que demonstra a aplicação dos 5 princípios SOLID através de um domínio real: cálculo de fretes, registro de encomendas e notificação de entrega.

Ruann Gomes Walter - RGM: 38125625
M

---

## 

**Compilar:**
```
mvn clean compile
```

**Executar:**
```
mvn exec:java -Dexec.mainClass="logistica.Main"
```

**Saída esperada:** dois cenários de frete com canais de notificação distintos.

---

## Estrutura do Projeto

```
src/main/java/logistica/
├── Main.java
├── domain/
│   ├── Encomenda.java
│   ├── Veiculo.java
│   ├── Motorista.java
│   ├── Rota.java
│   ├── CalculadoraFrete.java (interface)
│   ├── NotificadorEntrega.java (interface)
│   └── RepositorioFrete.java (interface)
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

---

## Aplicação dos Princípios SOLID

### S — Single Responsibility Principle (SRP)

Cada classe possui uma única responsabilidade bem delimitada:

- **Entidades de domínio** (`Encomenda`, `Veiculo`, `Motorista`, `Rota`): guardam dados e validam suas próprias regras de integridade no construtor. Não calculam frete, não persistem, não notificam.
- **Calculadoras** (`CalculadoraFretePorPeso`, `CalculadoraFretePorDistancia`, `CalculadoraFreteExpresso`): cada uma implementa uma estratégia de cálculo de frete específica.
- **Notificadores** (`NotificadorWhatsApp`, `NotificadorEmail`): cada um implementa um canal de notificação.
- **Serviços** (`ServicoCotacaoFrete`, `ServicoDespachoFrete`): coordenam o fluxo sem violar encapsulamento.

**Exemplo:** a mudança na fórmula de cálculo por peso afeta apenas `CalculadoraFretePorPeso`, não altera as demais classes.

---

### O — Open/Closed Principle (OCP)

O código está aberto para extensão, mas fechado para modificação:

- `ServicoCotacaoFrete` não conhece qual calculadora implementa concretamente o cálculo. Recebe a abstração `CalculadoraFrete` via construtor e chama `calcular()`.
- No `Main`, o mesmo serviço é instanciado com `CalculadoraFretePorPeso`, depois com `CalculadoraFretePorDistancia` e `CalculadoraFreteExpresso`, sem que o código do serviço seja alterado.
- **Nenhum `switch`, `case` ou `instanceof` inspecionando tipos** para desviar fluxo de negócio.

**Extensão:** criar uma nova estratégia (ex.: frete refrigerado) é implementar a interface `CalculadoraFrete` em uma nova classe, sem tocar nos serviços existentes.

---

### L — Liskov Substitution Principle (LSP)

Todas as implementações concretas cumprem integralmente o contrato das interfaces:

- `CalculadoraFretePorPeso`, `CalculadoraFretePorDistancia` e `CalculadoraFreteExpresso` implementam `CalculadoraFrete.calcular()` de forma completa: recebem `Encomenda` e `Rota`, e sempre retornam um `BigDecimal` válido.
- `NotificadorWhatsApp` e `NotificadorEmail` implementam `NotificadorEntrega.notificar()` corretamente, sem deixar a execução a cargo do chamador.
- `RepositorioFreteEmMemoria` implementa `salvar()` e `buscarPorId()` de fato.
- **Nenhum método foi sobrescrito com `throw new UnsupportedOperationException()`** ou corpo vazio.

**Verificação:** nos cenários da `Main`, as implementações são trocadas (WhatsApp por Email, Peso por Distância) e o sistema continua funcionando — prova de conformidade com o contrato.

---

### I — Interface Segregation Principle (ISP)

As interfaces são coesas e específicas:

- `CalculadoraFrete`: 1 método (`calcular`)
- `NotificadorEntrega`: 1 método (`notificar`)
- `RepositorioFrete`: 2 métodos (`salvar`, `buscarPorId`)

Nenhuma classe é forçada a depender de métodos que não utiliza. Contrasta com uma interface monolítica que combinasse cálculo, notificação e persistência.

---

### D — Dependency Inversion Principle (DIP)

Os serviços dependem exclusivamente de abstrações (interfaces), não de classes concretas:

- `ServicoCotacaoFrete` recebe `CalculadoraFrete` e `RepositorioFrete` via construtor.
- `ServicoDespachoFrete` recebe `NotificadorEntrega` e `RepositorioFrete` via construtor.
- **Nenhum `new` de repositório, calculadora ou notificador dentro dos serviços.**

A injeção de dependência é manual: a `Main` monta o grafo de objetos e passa as dependências pelo construtor dos serviços.

**Benefício:** trocar o repositório em memória por um banco de dados altera apenas a `Main`; os serviços permanecem intactos.

---

## Cenários de Teste

### Cenário 1: Frete por Peso + WhatsApp
- Encomenda calculada por peso (R$ 4,50/kg + pedágio).
- Notificação via WhatsApp.

### Cenário 2: Frete por Distância e Expresso + E-mail
- Mesma encomenda cotada por distância e por frete expresso.
- Cliente escolhe expresso (demonstra a extensibilidade via OCP).
- Notificação via E-mail.

---

## Validações e Regras de Integridade

As entidades validam no construtor:
- Peso negativo → `IllegalArgumentException`
- Capacidade do veículo negativa → `IllegalArgumentException`
- Distância negativa → `IllegalArgumentException`

Os serviços verificam dados antes de operar (ex.: se a encomenda existe no repositório antes de despachar).

---

## Conformidade com o Enunciado

| Requisito | Status |
|---|---|
| Mínimo 4 classes de domínio | ✓ Encomenda, Veiculo, Motorista, Rota |
| Mínimo 3 interfaces/abstrações | ✓ CalculadoraFrete, NotificadorEntrega, RepositorioFrete |
| Mínimo 2 serviços de orquestração | ✓ ServicoCotacaoFrete, ServicoDespachoFrete |
| Nenhum switch/case/instanceof para regra de negócio | ✓ Sem ocorrências |
| Nenhum UnsupportedOperationException ou método vazio | ✓ Sem ocorrências |
| Nenhum new de dependência nos serviços | ✓ Injeção manual via construtor |
| Main com grafo manual e 2 cenários | ✓ Dois cenários distintos executados |
| Organização src/main/java + .gitignore | ✓ Padrão Maven, target/, build/, .idea/, *.class, *.iml ignorados |

---

## Teste de Extensão

A extensão de novo comportamento é trivial e não exige alteração de código existente:

**Exemplo: adicionar frete refrigerado**
```java
public class CalculadoraFreteRefrigerado implements CalculadoraFrete {
    @Override
    public BigDecimal calcular(Encomenda encomenda, Rota rota) {
        // implementação da estratégia
        return resultado;
    }
}
```

Na `Main`: `new ServicoCotacaoFrete(new CalculadoraFreteRefrigerado(), repositorio);`

Nenhuma classe existente é alterada. Isso demonstra o OCP.