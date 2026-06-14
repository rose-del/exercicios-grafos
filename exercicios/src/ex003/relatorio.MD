# Relatório de análise — Metrô SP 
## 1. Resumo da solução

O problema exige encontrar o caminho com menor número de arestas (paradas) entre duas
estações em um grafo não ponderado com até 60 estações e ~80 conexões. A solução usa BFS
com `ArrayDeque` como fila FIFO, mapa de distâncias e mapa de predecessores para
reconstrução do caminho.

---

## 2. Por que BFS e não DFS?

Esta é a decisão central. O grafo é **não ponderado** e queremos **minimizar o número de
arestas** — exatamente o caso de uso canônico do BFS.

O BFS expande em "ondas" por distância crescente:
- Onda 1: todas as estações a 1 parada da origem
- Onda 2: todas as estações a 2 paradas
- Onda k: todas as estações a k paradas

A primeira vez que o BFS chega ao destino, ele vem pela onda de menor k possível. Isso
garante a otimalidade sem precisar explorar o grafo inteiro.

O DFS não fornece essa garantia — ele pode chegar ao destino por um caminho longo antes de
descobrir que existe um caminho mais curto por outra ramificação.

---

## 3. Estrutura de dados: fila vs pilha

A diferença entre BFS e DFS está na estrutura usada:

| Algoritmo | Estrutura | Comportamento |
|---|---|---|
| BFS | Fila (FIFO) — `ArrayDeque.offer/poll` | Processa por distância crescente |
| DFS | Pilha (LIFO) — `ArrayDeque.push/pop` | Mergulha fundo antes de voltar |

Trocar `offer/poll` por `push/pop` na mesma estrutura `ArrayDeque` transforma o BFS em DFS.

---

## 4. Complexidade

| Métrica | Valor |
|---|---|
| Tempo | O(V + E) |
| Espaço | O(V) — fila + mapa de distâncias + predecessores |
| Vértices (estações) | 66 |
| Arestas (conexões) | 75 |

---

## 5. Resultados dos exemplos

| Consulta | Paradas | Observação |
|---|---|---|
| Tucuruvi → Sé | 9 | Linha 1 direta, sem baldeação |
| Vila Madalena → Corinthians-Itaquera | 19 | L2 → L4 → L3 via República/Sé |
| Capão Redondo → Luz | 22 | L5 → L2 → L4, longo trajeto sul-norte |
| Jabaquara → Corinthians-Itaquera | 18 | L1 → L3 via Sé |
| Sé → Sé | 0 | Caso trivial |

---

## 6. Seria possível usar outro algoritmo?

### Dijkstra

**Possível, mas desnecessário aqui.** Dijkstra resolve o problema de menor caminho em
grafos **ponderados** (com pesos nas arestas). Para grafos não ponderados, Dijkstra se
reduz ao BFS — todas as arestas têm peso 1, então a fila de prioridade se comporta igual
a uma fila simples. Usar Dijkstra seria O((V + E) log V) contra O(V + E) do BFS — mais
complexo sem nenhum benefício.

**Quando Dijkstra seria adequado:** se o problema fosse o caminho mais *rápido* em tempo
(com os pesos sendo o tempo entre estações), aí Dijkstra seria a escolha correta.

### DFS

**Não garante o caminho mínimo.** DFS pode encontrar um caminho, mas não
necessariamente o mais curto. Para este problema, DFS seria incorreto.

### Bellman-Ford

**Não aplicável.** Bellman-Ford é para grafos com arestas de peso negativo. Completamente
desnecessário aqui.

### A* (A-estrela)

**Possível e mais eficiente em casos práticos.** O A* usa uma heurística (como distância
geográfica entre estações) para guiar a busca em direção ao destino, visitando menos nós
que o BFS puro. Para mapas de metrô reais com coordenadas geográficas, o A* seria
superior em performance. Para este problema acadêmico (grafo abstrato sem coordenadas
reais), BFS é suficiente.
