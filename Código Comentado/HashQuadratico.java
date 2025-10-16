public class HashQuadratico {
    public Registro[] tabela; // armazena Registro
    public int tamanho;
    public int colisoes = 0;
    public int colisoesBusca = 0;

    public HashQuadratico(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho]; // array de Registro
    }

    // Hash simples
    private int hash(int chave) {
        int h = chave % tamanho;
        if (h < 0) h += tamanho;
        return h;
    }

    // Inserção com hash quadrático
    public void inserir(Registro reg) {
        int chave = reg.codigo;
        int h = hash(chave);
        int i = 0;
        int posicao = h;

        while (tabela[posicao] != null && i < tamanho) {
            i++;
            posicao = h + i * i;
            while (posicao >= tamanho) posicao -= tamanho; // índice positivo
            colisoes++;
        }

        if (i < tamanho) tabela[posicao] = reg;
    }

    // Busca com hash quadrático vai retornar 1 se encontrou, 0 se não
    public int buscar(Registro reg) {
        int chave = reg.codigo;
        int h = hash(chave);
        int i = 0;
        int posicao = h;

        while (tabela[posicao] != null && i < tamanho) {
            if (tabela[posicao].codigo == chave) return 1; // encontrou
            i++;
            posicao = h + i * i;
            while (posicao >= tamanho) posicao -= tamanho; // índice positivo
            colisoesBusca++;
        }
        return 0; // não encontrou
    }

    // Reseta os contadores de colisões
    public void resetarColisoes() {
        colisoes = 0;
        colisoesBusca = 0;
    }
}
