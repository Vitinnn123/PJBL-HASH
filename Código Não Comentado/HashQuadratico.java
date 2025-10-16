public class HashQuadratico {
    public Registro[] tabela;
    public int tamanho;
    public int colisoes = 0;
    public int colisoesBusca = 0;

    public HashQuadratico(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho];
    }

    private int hash(int chave) {
        int h = chave % tamanho;
        if (h < 0) h += tamanho;
        return h;
    }

    public void inserir(Registro reg) {
        int chave = reg.codigo;
        int h = hash(chave);
        int i = 0;
        int posicao = h;

        while (tabela[posicao] != null && i < tamanho) {
            i++;
            posicao = h + i * i;
            while (posicao >= tamanho) posicao -= tamanho;
            colisoes++;
        }

        if (i < tamanho) tabela[posicao] = reg;
    }

    public int buscar(Registro reg) {
        int chave = reg.codigo;
        int h = hash(chave);
        int i = 0;
        int posicao = h;

        while (tabela[posicao] != null && i < tamanho) {
            if (tabela[posicao].codigo == chave) return 1;
            i++;
            posicao = h + i * i;
            while (posicao >= tamanho) posicao -= tamanho;
            colisoesBusca++;
        }
        return 0;
    }

    public void resetarColisoes() {
        colisoes = 0;
        colisoesBusca = 0;
    }
}
