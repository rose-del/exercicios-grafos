# Algoritmos em Grafos — Exercícios Práticos

Implementações em Java de algoritmos clássicos em grafos aplicados a problemas reais brasileiros. Cada solução usa o algoritmo mais adequado ao tipo de grafo e problema, com exemplos práticos e análise de complexidade.

## Estrutura do Repositório

```
/exercicios/src
├── ex001/   Investigação de rede de corrupção         (DFS)
├── ex002/   Dependências circulares npm               (DFS)
├── ex003/   Metrô de São Paulo — menor caminho        (BFS)
├── ex004/   Rastreamento de contatos COVID-19         (BFS)
├── ex005/   Fibra ótica no Nordeste                   (Prim)
├── ex006/   Distribuição elétrica na Amazônia         (Prim)
├── ex007/   Interconexão de data centers              (Kruskal)
├── ex008/   Recuperação de trilhas ecológicas         (Kruskal)
├── ex009/   Rota de entregas iFood em SP              (Dijkstra)
└── ex010/   Planejamento de voos com escalas          (Dijkstra)
```

## Questões

### DFS — Busca em Profundidade

| # | Problema | Grafo | V | E |
|---|----------|-------|---|---|
| [ex001](./ex001/) | Investigação de rede de corrupção (Lava Jato) | Não ponderado, não direcionado | 50 | ~200 |
| [ex002](./ex002/) | Detecção de ciclos em dependências npm | Direcionado | 100 | ~300 |

**ex001 — Investigação de corrupção:** a partir de um suspeito inicial, lista todos os envolvidos conectados direta ou indiretamente. Modela empreiteiras, políticos e doleiros como vértices e transações suspeitas como arestas.

**ex002 — Dependências npm:** verifica se há ciclo de dependências em um `package.json` usando DFS com 3 estados (não visitado / visitando / visitado). Retorna `true` se existe ciclo, `false` caso contrário.


### BFS — Busca em Largura

| # | Problema | Grafo | V | E |
|---|----------|-------|---|---|
| [ex003](./ex003/) | Metrô de SP — menor número de estações | Não ponderado | 60 | ~80 |
| [ex004](./ex004/) | Rastreamento de contatos COVID-19 | Não ponderado | 500 | ~2000 |

**ex003 — Metrô de SP:** encontra o caminho com menos paradas entre duas estações das 5 linhas do metrô paulistano. Retorna o número mínimo de estações intermediárias.

**ex004 — Rastreamento COVID-19:** dado um paciente zero, identifica todos os contatos de nível 1 (direto) e nível 2 (amigo de amigo). BFS limitada a profundidade 2.


### Prim — Árvore Geradora Mínima (grafos densos)

| # | Problema | Grafo | V | E |
|---|----------|-------|---|---|
| [ex005](./ex005/) | Fibra ótica entre capitais nordestinas | Ponderado, denso | 9 | 36 |
| [ex006](./ex006/) | Distribuição elétrica na Amazônia | Ponderado | 20 | 50 |

**ex005 — Fibra ótica NE:** conecta as 9 capitais nordestinas (São Luís, Teresina, Fortaleza, Natal, João Pessoa, Recife, Maceió, Aracaju, Salvador) com o menor total de km de cabo usando a matriz de distâncias rodoviárias.

**ex006 — Luz para Todos:** conecta 20 comunidades ribeirinhas amazônicas minimizando o custo total de cabos e postes (em mil R$). Pesos representam custo de cada ligação possível.


### Kruskal — Árvore Geradora Mínima (arestas ordenadas)

| # | Problema | Grafo | V | E |
|---|----------|-------|---|---|
| [ex007](./ex007/) | Interconexão de data centers globais | Ponderado | 15 | 30 |
| [ex008](./ex008/) | Recuperação de trilhas ecológicas | Ponderado | 12 | 20 |

**ex007 — Data centers:** conecta 15 data centers (modelo AWS/Google/Azure) via cabos submarinos e terrestres com menor custo total em milhões USD. Usa Union-Find para evitar ciclos eficientemente.

**ex008 — Trilhas da Chapada Diamantina:** após incêndios, reconecta 12 mirantes reabrindo o subconjunto de trilhas com menor total de horas de trabalho de limpeza.


### Dijkstra — Caminho Mínimo em Grafos Ponderados

| # | Problema | Grafo | V | E |
|---|----------|-------|---|---|
| [ex009](./ex009/) | Rota de entregas iFood em SP | Ponderado, direcionado | 200 | ~600 |
| [ex010](./ex010/) | Planejamento de voos com escalas | Ponderado, direcionado | 30 | ~120 |

**ex009 — iFood SP:** encontra a rota de menor tempo (em minutos) entre restaurante e cliente em um grafo direcionado de 200 cruzamentos, com pesos que modelam trânsito, semáforos e subidas.

**ex010 — Voos Brasil:** encontra o roteiro mais barato entre dois aeroportos brasileiros considerando escalas. Exemplo: GRU → CNF → SSA → FOR. Entrada inclui origem, destino e preço de cada voo.


## Algoritmos e Complexidade

| Algoritmo | Tempo | Espaço | Ideal para |
|-----------|-------|--------|------------|
| DFS | O(V + E) | O(V) | Alcançabilidade, detecção de ciclos |
| BFS | O(V + E) | O(V) | Menor caminho em grafos não ponderados |
| Prim | O(E log V) | O(V) | AGM em grafos densos |
| Kruskal | O(E log E) | O(V) | AGM com arestas já ordenadas |
| Dijkstra | O(E log V) | O(V) | Menor caminho em grafos ponderados |


## Requisitos

- Java 11 ou superior
- Nenhuma dependência externa — apenas `java.util`


## Observações

As soluções foram desenvolvidas com os algoritmos definidos no enunciado. Cada implementação inclui comentários explicativos, exemplos práticos fundamentados e análise sobre algoritmos alternativos viáveis para o mesmo problema.

## Desenvolvedores
- Rosenilda Santos da Silva | @rose-del
- Paulo Sérgio Albino | @PauloPSAS
- Arthur dos Santos | @Fastusx