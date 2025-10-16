import java.io.*;
import java.util.*;

public class Main {

    // Configurações dos testes: tamanhos de tabela, datasets e seeds
    static int[] tamanhoTabelas = {10_000, 100_000, 1_000_000};
    static int[] dataSets = {100_000, 1_000_000, 10_000_000};
    static long[] seeds = {123L, 456L, 789L};

    // Enum pra saber qual tipo de hash vamos testar
    enum Tipo {Duplo, Quadratico, Encadeado}

    public static void main(String[] args) throws Exception {
        // Cria pasta onde vão ficar os datasets
        String pastaDados = "Datasets";
        new File(pastaDados).mkdirs();

        // Gera os datasets se ainda não existirem
        for (int i = 0; i < 3; i++) {
            int dataset = dataSets[i];
            long seed = seeds[i];

            String arquivo = pastaDados + "/dados_" + dataset + "_s" + seed + ".txt";
            File f = new File(arquivo);
            if (!f.exists()) {
                System.out.println("Gerando dataset " + dataset + " ...");
                Dados.gerarESalvar(arquivo, dataset, seed); // gera arquivos de Registro
            } else {
                System.out.println("Dataset existe: " + arquivo);
            }
        }

        // CSV onde vamos gravar os resultados
        String csv = "Resultados.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csv))) {
            // escreve cabeçalho do CSV
            bw.write("tamanho_dataset,seed,tamanho_tabela,tipo,tempo_insercao_ms,tempo_busca_ms,"
                    + "colisoes_insercao,colisoes_busca,top3_lista1,top3_lista2,top3_lista3,"
                    + "gap_min,gap_max,gap_med");
            bw.newLine();

            int totalDataSets = 3;
            for (int di = 0; di < totalDataSets; di++) {
                int N = dataSets[di];
                long seed = seeds[di];
                String arquivo = pastaDados + "/dados_" + N + "_s" + seed + ".txt";
                System.out.println("\nTESTE: Dataset=" + N + " Seed=" + seed);

                // lê os registros do arquivo
                Registro[] dados = lerDataset(arquivo, N);

                // testa cada tamanho de tabela
                for (int tamanhoTabela : tamanhoTabelas) {
                    Tipo[] tipos = {Tipo.Duplo, Tipo.Quadratico, Tipo.Encadeado};
                    for (Tipo tipo : tipos) {
                        System.gc(); // força limpeza de memória antes de cada teste
                        Object tabela = criarTabela(tipo, tamanhoTabela); // cria a tabela de hash

                        // Pré-inserção de 1% dos dados pra evitar superlotar no começo
                        int preInsercao = N / 100;
                        if (preInsercao < 1) preInsercao = 1;
                        for (int i = 0; i < preInsercao; i++) {
                            inserirNaTabela(tabela, dados[i]);
                        }
                        resetarColisoes(tabela); // reseta contadores

                        // mede tempo de inserção
                        long t0 = System.nanoTime();
                        for (int i = 0; i < N; i++) {
                            inserirNaTabela(tabela, dados[i]);
                        }
                        long t1 = System.nanoTime();
                        float tempoInsercao = (t1 - t0) / 1000000f; // converte pra ms

                        // mede tempo de busca
                        long s0 = System.nanoTime();
                        for (int i = 0; i < N; i++) {
                            buscarNaTabela(tabela, dados[i]);
                        }
                        long s1 = System.nanoTime();
                        float tempoBusca = (s1 - s0) / 1000000f;

                        // pega o número de colisões
                        int colIns = colisoesInsercao(tabela);
                        int colBus = colisoesBusca(tabela);

                        // top 3 listas maiores (apenas hash encadeado)
                        int t1max = 0, t2max = 0, t3max = 0;
                        if (tipo == Tipo.Encadeado) {
                            int[] top3 = top3Encadeado(tabela);
                            t1max = top3[0];
                            t2max = top3[1];
                            t3max = top3[2];
                        }

                        // calcula gaps (intervalos entre posições preenchidas)
                        int gapMin = 0, gapMax = 0;
                        float gapMed = 0f;
                        int[] gaps = calcularGaps(tabela);
                        if (gaps != null) {
                            gapMin = gaps[0];
                            gapMax = gaps[1];
                            gapMed = gaps[2] / 100f; // transforma pra valor "real"
                        }

                        // grava no CSV
                        bw.write(String.format(Locale.US,
                                "%d,%d,%d,%s,%.3f,%.3f,%d,%d,%d,%d,%d,%d,%d,%.2f",
                                N, seed, tamanhoTabela, tipo.name(), tempoInsercao, tempoBusca,
                                colIns, colBus, t1max, t2max, t3max, gapMin, gapMax, gapMed));
                        bw.newLine();
                        bw.flush();

                        // imprime resumo no terminal
                        System.out.printf(
                                "Tipo=%s | Tabela=%d | Inserção=%.3fms | Busca=%.3fms | ColIns=%d | ColBusca=%d | Gaps-%s: Min=%d | Max=%d | Média=%.2f%n",
                                tipo.name(), tamanhoTabela, tempoInsercao, tempoBusca, colIns, colBus,
                                tipo.name(), gapMin, gapMax, gapMed
                        );
                    }
                }
            }
        }
        System.out.println("Testes finalizados! Resultados armazenados em Resultados.csv");
    }

    // cria tabela dependendo do tipo
    private static Object criarTabela(Tipo tipo, int tamanhoTabela) {
        if (tipo == Tipo.Duplo) return new HashDuplo(tamanhoTabela);
        else if (tipo == Tipo.Quadratico) return new HashQuadratico(tamanhoTabela);
        else if (tipo == Tipo.Encadeado) return new HashEncadeado(tamanhoTabela);
        else return null;
    }

    // insere um registro na tabela (qualquer tipo)
    private static void inserirNaTabela(Object tabela, Registro reg) {
        if (tabela instanceof HashDuplo) ((HashDuplo) tabela).inserir(reg);
        else if (tabela instanceof HashQuadratico) ((HashQuadratico) tabela).inserir(reg);
        else if (tabela instanceof HashEncadeado) ((HashEncadeado) tabela).inserir(reg);
    }

    // busca um registro na tabela (qualquer tipo)
    private static void buscarNaTabela(Object tabela, Registro reg) {
        if (tabela instanceof HashDuplo) ((HashDuplo) tabela).buscar(reg);
        else if (tabela instanceof HashQuadratico) ((HashQuadratico) tabela).buscar(reg);
        else if (tabela instanceof HashEncadeado) ((HashEncadeado) tabela).buscar(reg);
    }

    // reseta contadores de colisões
    private static void resetarColisoes(Object tabela) {
        if (tabela instanceof HashDuplo) ((HashDuplo) tabela).resetarColisoes();
        else if (tabela instanceof HashQuadratico) ((HashQuadratico) tabela).resetarColisoes();
        else if (tabela instanceof HashEncadeado) ((HashEncadeado) tabela).resetarColisoes();
    }

    // pega colisões de inserção
    private static int colisoesInsercao(Object tabela) {
        if (tabela instanceof HashDuplo) return ((HashDuplo) tabela).colisoes;
        else if (tabela instanceof HashQuadratico) return ((HashQuadratico) tabela).colisoes;
        else if (tabela instanceof HashEncadeado) return ((HashEncadeado) tabela).colisoes;
        return 0;
    }

    // pega colisões de busca
    private static int colisoesBusca(Object tabela) {
        if (tabela instanceof HashDuplo) return ((HashDuplo) tabela).colisoesBusca;
        else if (tabela instanceof HashQuadratico) return ((HashQuadratico) tabela).colisoesBusca;
        else if (tabela instanceof HashEncadeado) return ((HashEncadeado) tabela).colisoesBusca;
        return 0;
    }

    // calcula top 3 listas do hash encadeado
    private static int[] top3Encadeado(Object tabela) {
        if (tabela instanceof HashEncadeado) {
            HashEncadeado hc = (HashEncadeado) tabela;
            int[] top = new int[3];
            for (int i = 0; i < hc.tamanho; i++) {
                int len = 0;
                HashEncadeado.No atual = hc.tabela[i];
                while (atual != null) {
                    len++;
                    atual = atual.proximo;
                }
                // atualiza top 3
                if (len > top[0]) { top[2] = top[1]; top[1] = top[0]; top[0] = len; }
                else if (len > top[1]) { top[2] = top[1]; top[1] = len; }
                else if (len > top[2]) { top[2] = len; }
            }
            return top;
        }
        return new int[]{0, 0, 0};
    }

    // calcula gaps entre posições preenchidas
    private static int[] calcularGaps(Object tabela) {
        int ultimo = -1, min = -1, max = 0, cont = 0;
        long soma = 0;

        // repete lógica pra cada tipo de tabela
        if (tabela instanceof HashDuplo) {
            HashDuplo h = (HashDuplo) tabela;
            int i = 0;
            while (i < h.tamanho) {
                if (h.tabela[i] != null) {
                    if (ultimo != -1) {
                        int gap = i - ultimo;
                        if (min == -1 || gap < min) min = gap;
                        if (gap > max) max = gap;
                        soma += gap;
                        cont++;
                    }
                    ultimo = i;
                }
                i++;
            }
        } else if (tabela instanceof HashQuadratico) {
            HashQuadratico h = (HashQuadratico) tabela;
            int i = 0;
            while (i < h.tamanho) {
                if (h.tabela[i] != null) {
                    if (ultimo != -1) {
                        int gap = i - ultimo;
                        if (min == -1 || gap < min) min = gap;
                        if (gap > max) max = gap;
                        soma += gap;
                        cont++;
                    }
                    ultimo = i;
                }
                i++;
            }
        } else if (tabela instanceof HashEncadeado) {
            HashEncadeado h = (HashEncadeado) tabela;
            int i = 0;
            while (i < h.tamanho) {
                if (h.tabela[i] != null) {
                    if (ultimo != -1) {
                        int gap = i - ultimo;
                        if (min == -1 || gap < min) min = gap;
                        if (gap > max) max = gap;
                        soma += gap;
                        cont++;
                    }
                    ultimo = i;
                }
                i++;
            }
        }
        if (min == -1) min = 0;
        int mediaGap = cont > 0 ? (int)((soma * 100 + cont/2)/cont) : 0; // calcula média multiplicando por 100
        return new int[]{min, max, mediaGap};
    }

    // lê dataset do arquivo e transforma em array de Registro
    private static Registro[] lerDataset(String arquivo, int N) throws IOException {
        Registro[] arr = new Registro[N];
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int idx = 0;
            while ((linha = br.readLine()) != null && idx < N) {
                int codigo = Integer.parseInt(linha.trim());
                arr[idx++] = new Registro(codigo);
            }
        }
        return arr;
    }
}
