public class HashDuplo {
    public Registro[] tabela;
    public int tamanho;
    public int colisoes = 0;
    public int colisoesBusca = 0;

    public HashDuplo(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho];
    }

    private int hash1(int chave) {
        int h = chave % tamanho;
        if (h < 0) h += tamanho;
        return h;
    }

    private int hash2(int chave) {
        int h = chave % (tamanho - 1);
        if (h < 0) h += (tamanho - 1);
        if (h == 0) h = 1;
        return h;
    }

    public void inserir(Registro reg) {
        int chave = reg.codigo;
        int h1 = hash1(chave);
        int h2 = hash2(chave);
        int i = 0;
        int posicao = h1;

        while (tabela[posicao] != null && i < tamanho) {
            i++;
            posicao = h1 + i * h2;
            while (posicao >= tamanho) posicao -= tamanho;
            colisoes++;
        }

        if (i < tamanho) tabela[posicao] = reg;
    }

    public int buscar(Registro reg) {
        int chave = reg.codigo;
        int h1 = hash1(chave);
        int h2 = hash2(chave);
        int i = 0;
        int posicao = h1;

        while (tabela[posicao] != null && i < tamanho) {
            if (tabela[posicao].codigo == chave) return 1;
            i++;
            posicao = h1 + i * h2;
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
