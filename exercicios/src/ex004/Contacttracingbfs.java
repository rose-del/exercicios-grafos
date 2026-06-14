import java.util.*;

/**
 * Rastreamento de contatos COVID-19 usando BFS
 *
 * Problema: dado um grafo não ponderado de contatos sociais e um paciente zero,
 * encontrar todos os contatos de nível 1 (direto) e nível 2 (amigo de amigo).
 *
 * Complexidade:
 *   Tempo:  O(V + E) — visitamos cada vértice e aresta uma vez
 *   Espaço: O(V)     — fila + array de distâncias + conjuntos de resultado
 */
public class ContactTracingBFS {

    // Representação do grafo: lista de adjacência
    private final int numPessoas;
    private final List<List<Integer>> adj;

    public ContactTracingBFS(int numPessoas) {
        this.numPessoas = numPessoas;
        this.adj = new ArrayList<>();
        for (int i = 0; i < numPessoas; i++) {
            adj.add(new ArrayList<>());
        }
    }

    /** Adiciona uma aresta não direcionada entre pessoa A e pessoa B */
    public void adicionarContato(int a, int b) {
        adj.get(a).add(b);
        adj.get(b).add(a);
    }

    // BFS principal — retorna um resultado com dois conjuntos
    public ResultadoRastreamento rastrearContatos(int pacienteZero) {
        // dist[i] = distância BFS do paciente zero até a pessoa i
        // -1 = não visitada
        int[] dist = new int[numPessoas];
        Arrays.fill(dist, -1);

        Set<Integer> contatosPrimarios   = new LinkedHashSet<>(); // nível 1
        Set<Integer> contatosSecundarios = new LinkedHashSet<>(); // nível 2

        // --- Inicialização da BFS ---
        dist[pacienteZero] = 0;
        Queue<Integer> fila = new LinkedList<>();
        fila.add(pacienteZero);

        // --- Loop BFS ---
        while (!fila.isEmpty()) {
            int atual = fila.poll();

            // Só expandimos vizinhos se ainda estamos nos níveis 0 ou 1
            // (nível 2 fica na fila, mas seus vizinhos seriam nível 3 — ignoramos)
            if (dist[atual] >= 2) continue;

            for (int vizinho : adj.get(atual)) {
                if (dist[vizinho] == -1) {           // não visitado ainda
                    dist[vizinho] = dist[atual] + 1;
                    fila.add(vizinho);

                    if (dist[vizinho] == 1) contatosPrimarios.add(vizinho);
                    else if (dist[vizinho] == 2) contatosSecundarios.add(vizinho);
                }
            }
        }

        return new ResultadoRastreamento(
            pacienteZero,
            contatosPrimarios,
            contatosSecundarios
        );
    }

    // Classe de resultado
    public static class ResultadoRastreamento {
        public final int pacienteZero;
        public final Set<Integer> nivel1; // contatos diretos
        public final Set<Integer> nivel2; // amigos de amigos

        public ResultadoRastreamento(int pacienteZero,
                                     Set<Integer> nivel1,
                                     Set<Integer> nivel2) {
            this.pacienteZero = pacienteZero;
            this.nivel1       = Collections.unmodifiableSet(nivel1);
            this.nivel2       = Collections.unmodifiableSet(nivel2);
        }

        @Override
        public String toString() {
            return String.format(
                "Paciente zero : %d%n" +
                "Nível 1 (%3d) : %s%n" +
                "Nível 2 (%3d) : %s",
                pacienteZero,
                nivel1.size(), nivel1,
                nivel2.size(), nivel2
            );
        }
    }

    // Gerador de grafo aleatório para testes (500 vértices, ~2000 arestas)
    public static ContactTracingBFS gerarGrafoAleatorio(int n, int m, long seed) {
        ContactTracingBFS grafo = new ContactTracingBFS(n);
        Random rng = new Random(seed);

        // Garante conectividade mínima com um ciclo hamiltoniano
        for (int i = 0; i < n; i++) {
            grafo.adicionarContato(i, (i + 1) % n);
        }

        // Adiciona arestas aleatórias até atingir m arestas totais
        Set<Long> existentes = new HashSet<>();
        for (int i = 0; i < n; i++) existentes.add((long) i * n + (i + 1) % n);

        int tentativas = 0;
        while (existentes.size() < m && tentativas < m * 10) {
            tentativas++;
            int a = rng.nextInt(n);
            int b = rng.nextInt(n);
            if (a == b) continue;
            long chave = (long) Math.min(a, b) * n + Math.max(a, b);
            if (existentes.add(chave)) {
                grafo.adicionarContato(a, b);
            }
        }
        return grafo;
    }


    public static void main(String[] args) {

        // --- Exemplo pequeno para verificação visual ---
        System.out.println("=== Exemplo didático (10 pessoas) ===");
        ContactTracingBFS pequenoGrafo = new ContactTracingBFS(10);
        // Paciente 0 conhece 1, 2, 3
        pequenoGrafo.adicionarContato(0, 1);
        pequenoGrafo.adicionarContato(0, 2);
        pequenoGrafo.adicionarContato(0, 3);
        // Nível 2 de 0: quem conhece 1, 2 ou 3
        pequenoGrafo.adicionarContato(1, 4);
        pequenoGrafo.adicionarContato(1, 5);
        pequenoGrafo.adicionarContato(2, 6);
        pequenoGrafo.adicionarContato(3, 7);
        pequenoGrafo.adicionarContato(3, 8);
        // Fora do alcance de 2 níveis
        pequenoGrafo.adicionarContato(4, 9);

        ResultadoRastreamento r = pequenoGrafo.rastrearContatos(0);
        System.out.println(r);

        // --- Exemplo com o tamanho real do enunciado ---
        System.out.println("\n=== Grafo de 500 pessoas, ~2000 arestas ===");
        ContactTracingBFS grandeGrafo = gerarGrafoAleatorio(500, 2000, 42L);

        long inicio = System.nanoTime();
        ResultadoRastreamento resultado = grandeGrafo.rastrearContatos(0);
        long fim = System.nanoTime();

        System.out.printf("Paciente zero   : %d%n", resultado.pacienteZero);
        System.out.printf("Contatos nível 1: %d pessoas%n", resultado.nivel1.size());
        System.out.printf("Contatos nível 2: %d pessoas%n", resultado.nivel2.size());
        System.out.printf("Tempo de execução: %.3f ms%n", (fim - inicio) / 1_000_000.0);
    }
}