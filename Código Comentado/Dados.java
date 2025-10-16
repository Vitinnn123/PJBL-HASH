import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Dados {

    // Gera N códigos únicos de 9 dígitos e salva em um arquivo
    // Usa uma seed para embaralhar e garantir que os dados sejam reproduzíveis
    public static void gerarESalvar(String nomeArquivo, int quantidade, long seed) throws IOException {
        int[] numeros = new int[quantidade];
        int inicio = 100_000_000; // garante que os códigos tenham 9 dígitos

        // Cria sequência inicial de códigos
        for (int i = 0; i < quantidade; i++) {
            numeros[i] = inicio + i;
        }

        // Embaralha os códigos com seed fixa
        Random aleatorio = new Random(seed);
        for (int i = quantidade - 1; i > 0; i--) {
            int j = aleatorio.nextInt(i + 1);
            int temp = numeros[i];
            numeros[i] = numeros[j];
            numeros[j] = temp;
        }

        // Salva os códigos no arquivo, um por linha
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (int valor : numeros) {
                escritor.write(String.valueOf(valor));
                escritor.newLine();
            }
        }

        System.out.println("Arquivo gerado: " + nomeArquivo + " (N=" + quantidade + ", seed=" + seed + ")");
    }
}
