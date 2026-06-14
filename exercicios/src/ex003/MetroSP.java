/*
 *  REDE DE METRO DE SÃO PAULO — BFS (Busca em Largura)
 *  Menor número de estações entre duas paradas
 *
 *  POR QUE BFS E NÃO DFS AQUI?
 *  O DFS mergulha fundo por um caminho antes de tentar outros.
 *  Ele pode chegar ao destino por um caminho longo antes de
 *  descobrir que havia um caminho curto por outro lado.
 *
 *  O BFS expande em "ondas" — primeiro todas as estações a
 *  1 parada, depois a 2, depois a 3... A primeira vez que
 *  ele chega ao destino, GARANTIDAMENTE é o caminho com
 *  menos estações. Essa é a propriedade fundamental do BFS
 *  em grafos não ponderados.
 *
 *  Estrutura usada: ArrayDeque como FILA (FIFO)
 *  - DFS usa PILHA (LIFO) — vai fundo antes de voltar
 *  - BFS usa FILA (FIFO) — processa por ordem de chegada
 *  essa diferença de estrutura é o que determina o comportamento.
 *
 */

import java.util.*;

public class MetroSP {

    // Grafo representado por lista de adjacência
    // Chave: nome da estação | Valor: lista de estações vizinhas
    private final Map<String, List<String>> adjacencia;

    public MetroSP() {
        this.adjacencia = new LinkedHashMap<>(); // mantém ordem de inserção
    }

    /** Registra uma estação no grafo. */
    public void adicionarEstacao(String estacao) {
        adjacencia.putIfAbsent(estacao, new ArrayList<>());
    }

    /**
     * Conecta duas estações adjacentes (aresta bidirecional).
     * O metrô é não-dirigido: se você pode ir de A para B,
     * também pode ir de B para A pela mesma via.
     */
    public void conectar(String a, String b) {
        adicionarEstacao(a);
        adicionarEstacao(b);
        adjacencia.get(a).add(b);
        adjacencia.get(b).add(a);
    }

    /**
     * BFS para encontrar o menor número de paradas entre origem e destino.
     *
     * A ideia central: usamos uma FILA e um mapa de distâncias.
     * Cada vez que descobrimos uma estação nova, sua distância é
     * exatamente dist[vizinho] = dist[atual] + 1.
     * Como processamos em ordem de distância crescente (FIFO),
     * a primeira vez que chegamos ao destino é pelo caminho mínimo.
     *
     * @param origem   estação de partida
     * @param destino  estação de chegada
     * @return resultado com distância mínima e caminho percorrido
     */
    public ResultadoBFS menorCaminho(String origem, String destino) {

        // Caso trivial: origem é o próprio destino
        if (origem.equals(destino)) {
            return new ResultadoBFS(0, List.of(origem));
        }

        // Verifica se as estações existem no grafo
        if (!adjacencia.containsKey(origem) || !adjacencia.containsKey(destino)) {
            return new ResultadoBFS(-1, List.of()); // estação não encontrada
        }

        // Mapa de distâncias: dist[estacao] = número de paradas a partir da origem
        Map<String, Integer> dist = new HashMap<>();

        // Mapa de predecessores: usado para reconstruir o caminho ao final
        // predecessor["Sé"] = "Luz" significa que chegamos a Sé vindo de Luz
        Map<String, String> predecessor = new HashMap<>();

        // A fila é o coração do BFS — garante o processamento por distância crescente
        // Usamos ArrayDeque pois é mais eficiente que LinkedList para filas
        Deque<String> fila = new ArrayDeque<>();

        // Inicializa: coloca a origem na fila com distância 0
        dist.put(origem, 0);
        fila.offer(origem); // offer() = enqueue (adiciona no fim da fila)

        while (!fila.isEmpty()) {
            // poll() = dequeue (retira do início da fila — FIFO)
            String atual = fila.poll();
            int distAtual = dist.get(atual);

            // Explora todos os vizinhos da estação atual
            for (String vizinho : adjacencia.getOrDefault(atual, List.of())) {

                // Se o vizinho ainda não foi visitado (não está no mapa de distâncias)
                if (!dist.containsKey(vizinho)) {
                    dist.put(vizinho, distAtual + 1);
                    predecessor.put(vizinho, atual);
                    fila.offer(vizinho);

                    // Chegamos ao destino! Como é BFS, esta é a menor distância possível.
                    // Não precisamos continuar — podemos reconstruir o caminho agora.
                    if (vizinho.equals(destino)) {
                        List<String> caminho = reconstruirCaminho(predecessor, origem, destino);
                        return new ResultadoBFS(dist.get(destino), caminho);
                    }
                }
            }
        }

        // Se saímos do loop sem encontrar o destino, ele não é alcançável
        return new ResultadoBFS(-1, List.of());
    }

    /**
     * Reconstrói o caminho percorrido usando o mapa de predecessores.
     * Começa do destino e volta até a origem seguindo os predecessores,
     * depois inverte a lista para ter a ordem origem→destino.
     */
    private List<String> reconstruirCaminho(Map<String, String> predecessor,
                                             String origem, String destino) {
        List<String> caminho = new ArrayList<>();
        String atual = destino;

        // Percorre de trás pra frente: destino → ... → origem
        while (atual != null) {
            caminho.add(atual);
            atual = predecessor.get(atual); // null quando chegar na origem
        }

        Collections.reverse(caminho); // inverte: origem → ... → destino
        return caminho;
    }

    /**
     * Exibe o resultado formatado no console.
     */
    public void exibirResultado(String origem, String destino, ResultadoBFS resultado) {
        System.out.println("\n" + "-".repeat(50));
        System.out.printf("  %s → %s%n", origem, destino);
        System.out.println("-".repeat(50));

        if (resultado.distancia == -1) {
            System.out.println("  Sem conexão entre essas estações.");
        } else if (resultado.distancia == 0) {
            System.out.println("  Origem e destino são a mesma estação (0 paradas).");
        } else {
            System.out.printf("  Paradas: %d%n", resultado.distancia);
            System.out.printf("  Caminho: %s%n", String.join(" → ", resultado.caminho));
            System.out.printf("  Estações no trajeto (incluindo origem): %d%n",
                resultado.caminho.size());
        }
    }

    //  CLASSE INTERNA: encapsula o resultado do BFS
    static class ResultadoBFS {
        final int distancia;           // número mínimo de paradas (arestas)
        final List<String> caminho;    // sequência de estações

        ResultadoBFS(int distancia, List<String> caminho) {
            this.distancia = distancia;
            this.caminho   = caminho;
        }
    }

    //  MÉTODO PRINCIPAL — monta o grafo 
    public static void main(String[] args) {

        MetroSP metro = new MetroSP();

        /*
         * LINHA 1 - AZUL (Norte-Sul)
         * Tucuruvi ↔ Jabaquara (passando por Luz, Sé)
         */
        String[] linha1 = {
            "Tucuruvi","Parana","Jardim São Paulo","Carandiru","Santana",
            "Caetê","Tiradentes","Luz","São Bento","Sé",
            "Liberdade","São Judas","Saúde","Conceição","Jabaquara"
        };
        for (int i = 0; i < linha1.length - 1; i++) {
            metro.conectar(linha1[i], linha1[i + 1]);
        }

        /*
         * LINHA 2 - VERDE (Leste-Oeste, via Paulista)
         * Vila Madalena ↔ Vila Prudente
         */
        String[] linha2 = {
            "Vila Madalena","Pinheiros","Faria Lima","Oscar Freire","Consolação",
            "Trianon-MASP","Brigadeiro","Paraíso","Ana Rosa","Chácara Klabin",
            "Santo André","São Caetano","Vila Prudente"
        };
        for (int i = 0; i < linha2.length - 1; i++) {
            metro.conectar(linha2[i], linha2[i + 1]);
        }

        /*
         * LINHA 3 - VERMELHA (Leste-Oeste, via Centro)
         * Palmeiras-Barra Funda ↔ Corinthians-Itaquera
         */
        String[] linha3 = {
            "Palmeiras-Barra Funda","Marechal Deodoro","Santa Cecília","República",
            "Anhangabaú","Sé","Pedro II","Brás","Bresser","Belém",
            "Tatuapé","Carrão","Penha","Vila Matilde","Guilhermina-Esperança",
            "Vila Formosa","Patriarca","Artur Alvim","Corinthians-Itaquera"
        };
        for (int i = 0; i < linha3.length - 1; i++) {
            metro.conectar(linha3[i], linha3[i + 1]);
        }

        /*
         * LINHA 4 - AMARELA (Sudoeste)
         * Butantã ↔ Luz
         */
        String[] linha4 = {
            "Butantã","Pinheiros","Fradique Coutinho","Oscar Freire","Higienópolis-Mackenzie",
            "República","Paulista","Consolação","Luz"
        };
        for (int i = 0; i < linha4.length - 1; i++) {
            metro.conectar(linha4[i], linha4[i + 1]);
        }

        /*
         * LINHA 5 - LILÁS (Sul)
         * Capão Redondo ↔ Chácara Klabin
         */
        String[] linha5 = {
            "Capão Redondo","Campo Limpo","Vila das Belezas","Giovanni Gronchi",
            "Santo Amaro","Largo Treze","Adolfo Pinheiro","Alto da Boa Vista",
            "Borba Gato","Brooklin","Campo Belo","Eucaliptos","Moema",
            "AACD-Servidor","Hospital São Paulo","Santa Cruz","Chácara Klabin"
        };
        for (int i = 0; i < linha5.length - 1; i++) {
            metro.conectar(linha5[i], linha5[i + 1]);
        }

        /*
         * CONEXÕES DE INTEGRAÇÃO (baldeações entre linhas)
         * Estações que aparecem em mais de uma linha já estão
         * conectadas por nome — ex: "Sé" conecta linha 1 e linha 3.
         * Adicionamos algumas conexões explícitas de integração:
         */
        metro.conectar("Sé",           "República");       // L1 ↔ L3
        metro.conectar("Luz",          "República");       // L1 ↔ L3
        metro.conectar("Consolação",   "Trianon-MASP");    // L2 ↔ L4
        metro.conectar("Paraíso",      "Ana Rosa");        // L2 ↔ L5
        metro.conectar("Chácara Klabin","Ana Rosa");       // L2 ↔ L5
        metro.conectar("Pinheiros",    "Faria Lima");      // L2 ↔ L4
        metro.conectar("Oscar Freire", "Consolação");      // L2 ↔ L4

        //  EXEMPLOS DE CONSULTAS
        System.out.println("=".repeat(50));
        System.out.println("  METRÔ SP — MENOR CAMINHO (BFS)");
        System.out.println("=".repeat(50));

        ResultadoBFS r1 = metro.menorCaminho("Tucuruvi", "Sé");
        metro.exibirResultado("Tucuruvi", "Sé", r1);

        ResultadoBFS r2 = metro.menorCaminho("Vila Madalena", "Corinthians-Itaquera");
        metro.exibirResultado("Vila Madalena", "Corinthians-Itaquera", r2);

        ResultadoBFS r3 = metro.menorCaminho("Capão Redondo", "Luz");
        metro.exibirResultado("Capão Redondo", "Luz", r3);

        ResultadoBFS r4 = metro.menorCaminho("Jabaquara", "Corinthians-Itaquera");
        metro.exibirResultado("Jabaquara", "Corinthians-Itaquera", r4);

        ResultadoBFS r5 = metro.menorCaminho("Sé", "Sé");
        metro.exibirResultado("Sé", "Sé", r5);

        System.out.println("\n" + "=".repeat(50));

        // Exibe estatísticas do grafo
        int totalEstacoes = metro.adjacencia.size();
        int totalArestas  = metro.adjacencia.values().stream()
                                .mapToInt(List::size).sum() / 2;
        System.out.printf("  Estações no grafo : %d%n", totalEstacoes);
        System.out.printf("  Conexões no grafo : %d%n", totalArestas);
        System.out.println("=".repeat(50));
    }
}