# Rastreamento de Contatos COVID-19 com BFS

**Disciplina:** Estruturas de Dados e Algoritmos  
**Algoritmo:** Busca em Largura (BFS — *Breadth-First Search*)  
**Problema:** Identificação de contatos de risco em até 2 níveis de profundidade

---

## 1. Contexto e Motivação

Durante a pandemia de COVID-19, aplicativos de rastreamento de contatos como o **Coronavírus-SUS** (Brasil), **StopCovid** (França) e **NHS COVID-19 App** (Reino Unido) precisavam identificar, rapidamente, quais pessoas poderiam ter sido expostas a um infectado. A lógica epidemiológica define dois grupos de risco:

- **Contatos primários (nível 1):** pessoas que tiveram contato direto com o infectado por mais de 15 minutos.
- **Contatos secundários (nível 2):** pessoas que tiveram contato direto com alguém do nível 1 — os chamados "amigos de amigos".

Esse problema é naturalmente modelado como uma **busca em largura limitada a profundidade 2** em um grafo de contatos sociais.

---

## 2. Modelagem do Problema

### 2.1 Estrutura do Grafo

| Propriedade     | Valor                          |
|-----------------|-------------------------------|
| Tipo            | Não direcionado, não ponderado |
| Vértices (V)    | 500 pessoas                    |
| Arestas (E)     | ~2.000 contatos                |
| Representação   | Lista de adjacência            |

Cada **vértice** representa uma pessoa. Cada **aresta** representa um encontro de mais de 15 minutos entre duas pessoas. Como a relação de contato é simétrica (se A encontrou B, então B encontrou A), o grafo é não direcionado.

### 2.2 Definição Formal

Dado um grafo `G = (V, E)` e um vértice de origem `s` (paciente zero), encontrar:

```
Nível 1 = { v ∈ V | dist(s, v) = 1 }
Nível 2 = { v ∈ V | dist(s, v) = 2 }
```

onde `dist(s, v)` é o menor número de arestas entre `s` e `v`.

---

## 3. Algoritmo BFS

### 3.1 Intuição

O BFS explora o grafo em "ondas" concêntricas. Partindo do paciente zero, a primeira onda alcança todos os vizinhos diretos (nível 1). A segunda onda, partindo dos nós de nível 1, alcança os vizinhos ainda não visitados (nível 2). O algoritmo para após processar os nós de nível 1, evitando expandir o nível 3.

```
Paciente Zero (0)
       │
  ─────┼─────
  ↓    ↓    ↓          ← Nível 1 (contatos diretos)
  1    2    3
  │    │    │
 ↓↓   ↓   ↓↓          ← Nível 2 (amigos de amigos)
 4 5  6   7 8
 
 └──────────┘ 9 ← fora do alcance (nível 3)
```

### 3.3 Decisões de Implementação

**Corte por profundidade:** O `if (dist[atual] >= 2) continue` é colocado após o `poll()`. Os nós de nível 2 *entram* na fila para que sua distância seja registrada, mas não *expandem* vizinhos. Isso evita visitar o nível 3.

**Array `dist[]` dupla função:** Serve simultaneamente como marcador de visitado (`dist[v] != -1`) e como indicador de nível. Isso elimina a necessidade de um array booleano separado.

**`LinkedHashSet` para os resultados:** Preserva a ordem de descoberta (útil para relatórios e debugging) e garante ausência de duplicatas em O(1) por inserção.


## 5. Análise de Complexidade

### 5.1 Complexidade de Tempo

| Etapa                        | Custo       |
|------------------------------|-------------|
| Inicialização do array `dist`| O(V)        |
| Processamento da fila BFS    | O(V + E)    |
| Inserção nos conjuntos       | O(1) amort. |
| **Total**                    | **O(V + E)**|

Com V=500 e E=2.000: aproximadamente **2.500 operações** — execução em frações de milissegundo.

### 5.2 Complexidade de Espaço

| Estrutura             | Tamanho  |
|-----------------------|----------|
| Lista de adjacência   | O(V + E) |
| Array `dist[]`        | O(V)     |
| Fila BFS              | O(V)     |
| Conjuntos de resultado| O(V)     |
| **Total**             | **O(V + E)**|

### 5.3 Comparação com Alternativas

| Abordagem          | Tempo     | Garante nível mínimo? |
|--------------------|-----------|-----------------------|
| **BFS** ✓          | O(V + E)  | Sim                   |
| DFS                | O(V + E)  | Não                   |
| Dijkstra           | O(E log V)| Sim (mas desnecessário aqui) |
| Matriz de adjacência| O(V²)    | Sim (mas ineficiente) |

O BFS é a escolha ótima: garante encontrar os caminhos de menor comprimento (essencial para classificar os níveis corretamente) com a menor complexidade possível para grafos não ponderados.

---

## 6. Exemplo de Execução

### Grafo pequeno (10 pessoas)

```
Contatos cadastrados:
  0 — 1, 2, 3
  1 — 4, 5
  2 — 6
  3 — 7, 8
  4 — 9

Rastreamento a partir do paciente zero = 0:

  Nível 1 (contatos diretos) : {1, 2, 3}
  Nível 2 (amigos de amigos) : {4, 5, 6, 7, 8}
  Fora do alcance (≥ nível 3): {9}
```

**Observação:** A pessoa 9 é vizinha de 4, que é vizinha de 1, que é vizinha de 0 — três saltos, portanto fora do protocolo de rastreamento.

### Grafo completo (500 pessoas, ~2.000 arestas)

```
Paciente zero   : Pessoa 0
Contatos nível 1: ~8 pessoas
Contatos nível 2: ~60 pessoas
Tempo de execução: < 1 ms
```

---

## 7. Conclusão

O BFS é a solução ideal para rastreamento de contatos porque:

- **Corretude garantida:** por explorar por níveis, jamais classifica erroneamente um contato de nível 2 como nível 1, mesmo que existam múltiplos caminhos entre dois nós.
- **Eficiência máxima:** a complexidade O(V + E) é o limite inferior teórico para esse problema — não existe algoritmo assintoticamente mais rápido para grafos não ponderados.
- **Escalabilidade:** com V=500 e E=2.000, a execução é imperceptível. O mesmo algoritmo funciona para grafos com milhões de vértices (como redes sociais reais) com ajustes de infraestrutura, não de lógica.
- **Naturalidade do modelo:** o BFS captura exatamente a intuição epidemiológica de "contatos de 1º e 2º grau" sem nenhuma adaptação artificial.

---

## Referências

- CORMEN, T. H. et al. *Introduction to Algorithms*. 4. ed. MIT Press, 2022. Cap. 22.2 — Breadth-First Search.
- SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4. ed. Addison-Wesley, 2011. Cap. 4.1 — Undirected Graphs.
- WHO. *Contact tracing in the context of COVID-19*. World Health Organization, 2020.