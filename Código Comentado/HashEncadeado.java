public class HashEncadeado {

    // nó da lista encadeada
    static class No {
        Registro reg;
        No proximo;
        No(Registro reg) { this.reg = reg; }
    }

    public No[] tabela;
    public int tamanho;
    public int colisoes = 0;
    public int colisoesBusca = 0;

    // Construtor da tabela
    public HashEncadeado(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho]; // inicializa array de listas
    }

    // Função hash vai calcular índice para um dado código
    private int hash(int chave) {
        return Math.floorMod(chave, tamanho); // índice positivo
    }

    // Inserção com encadeamento vai adicionar o registro na lista correta
    public void inserir(Registro reg) {
        int posicao = hash(reg.codigo);
        No novo = new No(reg);

        if (tabela[posicao] == null) {
            tabela[posicao] = novo;
        } else {
            colisoes++;
            novo.proximo = tabela[posicao];
            tabela[posicao] = novo;
        }
    }

    // Busca encadeada vai retornar 1 se encontrou, 0 se não
    public int buscar(Registro reg) {
        int posicao = hash(reg.codigo);
        No atual = tabela[posicao];

        while (atual != null) {// percorre lista
            if (atual.reg.codigo == reg.codigo) return 1; // achou o registro
            colisoesBusca++;
            atual = atual.proximo;
        }
        return 0; // não encontrou
    }

    // Zera os contadores de colisão
    public void resetarColisoes() {
        colisoes = 0;
        colisoesBusca = 0;
    }
}
