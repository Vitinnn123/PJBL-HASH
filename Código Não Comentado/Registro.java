public class Registro {
    public final int codigo;

    public Registro(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return String.format("%09d", codigo);
    }
}
