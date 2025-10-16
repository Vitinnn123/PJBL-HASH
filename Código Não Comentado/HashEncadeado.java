public class HashEncadeado {

    static class No {
        Registro reg;
        No proximo;
        No(Registro reg) { this.reg = reg; }
    }

    public No[] tabela;
    public int tamanho;
    public int colisoes = 0;
    public int colisoesBusca = 0;

    public HashEncadeado(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho];
    }

    private int hash(int chave) {
        return Math.floorMod(chave, tamanho);
    }

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

    public int buscar(Registro reg) {
        int posicao = hash(reg.codigo);
        No atual = tabela[posicao];

        while (atual != null) {
            if (atual.reg.codigo == reg.codigo) return 1;
            colisoesBusca++;
            atual = atual.proximo;
        }
        return 0;
    }

    public void resetarColisoes() {
        colisoes = 0;
        colisoesBusca = 0;
    }
}
