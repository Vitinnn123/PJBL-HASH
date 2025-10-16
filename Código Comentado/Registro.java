public class Registro {
    public final int codigo; // código do registro (9 dígitos)

    // cria um registro com um código
    public Registro(int codigo) {
        this.codigo = codigo;
    }

    // Retorna o código formatado com 9 dígitos
    @Override
    public String toString() {
        return String.format("%09d", codigo);
    }
}
