# Java Studies

Repositório de estudos em **Java 21**, reunindo implementações de estruturas de dados, algoritmos, exercícios de LeetCode, estudos de caso aplicados e experimentos com recursos modernos da linguagem (records, switch expressions, sealed interfaces, pattern matching, receiver parameters, streams).

## Tecnologias

- Java 21
- Maven
- JUnit 5 (Jupiter) para testes

## Como executar

```bash
# Compilar o projeto
mvn compile

# Rodar os testes
mvn test

# Executar uma classe específica (com main)
mvn compile exec:java -Dexec.mainClass="arrays.TwoSumArray"
```

## Estrutura do projeto

```
src/main/java
├── arrays/               # Exercícios clássicos de arrays (LeetCode)
├── datastructures/       # Estruturas de dados e algoritmos de junção
├── objects/              # Modelagem de objetos e recursos da linguagem
├── switchexpressions/    # Switch expressions (Java 14+)
├── patternsofinstanceof/ # Pattern matching com instanceof, sealed interfaces e generics
├── patternmatching/      # Pattern matching for switch com sealed interfaces e records
└── ica/                  # Estudos de caso (ICA) aplicados
    ├── bank/             # Processamento de transações bancárias
    ├── librarysystem/    # Sistema de biblioteca (empréstimos, filas, relatórios)
    └── task/             # Ordenação e manipulação de tarefas com Streams
```

## Conteúdo por pacote

### `arrays`
Exercícios de manipulação de arrays, típicos de entrevistas técnicas:
- **TwoSumArray** - soma de dois números que atingem um valor alvo, usando `HashMap`.
- **MedianSortedArrays** - mediana de dois arrays ordenados.
- **NestedLoops** - combinações de pares entre duas listas.

### `datastructures`
Estruturas de dados e algoritmos clássicos implementados do zero:
- **RopeStructure** - árvore binária (Rope) para manipulação eficiente de strings grandes.
- **SkipListStructure** - lista com múltiplas camadas para busca em tempo logarítmico.
- **KDTreeStructure** - árvore k-dimensional para busca espacial (nearest neighbor em 2D).
- **SortMergeJoin** - algoritmo de junção por ordenação e intercalação (sort-merge join).
- **HashJoinAlgorithms** - algoritmo de junção baseado em tabela hash (hash join).

### `objects`
Modelagem de objetos com recursos avançados de Java:
- **Parcel** - exemplo prático de *receiver parameters* com anotações `TYPE_USE`.
- **ImmutableStack** - pilha imutável e persistente, implementada como lista encadeada.

### `switchexpressions`
- **PlayerFactory** - factory usando switch expressions com `yield` e setas (`->`).

### `patternsofinstanceof`
- **WildcardPatternExample** - wildcards genéricos (`? extends`) e pattern matching com `instanceof`.
- **EngineStreamManager** - sealed interfaces, records e filtragem com Streams.

### `patternmatching`
- **GestorLogistica** - pattern matching for switch (Java 21) com sealed interfaces (`Veiculo`), records (`Caminhao`, `Van`, `Drone`), padrões guardados (`when`) e ordenação de cases do mais específico para o mais genérico.

### `ica` (estudos de caso)
- **bank/TransactionProcessor** - processamento de transações entre contas, limites diários e relatórios (`BigDecimal`, `records`, `Streams`).
- **librarysystem/LibrarySystem** - sistema de biblioteca com catálogo, empréstimos, devoluções e fila de reservas.
- **task/TaskMain** - ordenação de tarefas por nome, prioridade e data de criação usando `Comparator` e Streams.

Cada estudo de caso em `ica/` possui documentação de fases (`fase-one.md`, `fase-two.md`, ...) descrevendo a evolução incremental da solução.

## Testes

Os testes ficam em `src/test/java`, espelhando a estrutura de pacotes do `src/main/java` (ex: `arrays/TwoSumArrayTest.java`).
