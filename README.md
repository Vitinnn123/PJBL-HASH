# Sistema de Implementação e Analise de Tabelas Hash
- **Disciplina:** Resolução de Problemas Estruturados em Computação
- **Instituição:** Pontifícia Universidade Católica do Paraná    
- **Professor:** Andrey Cabral Meira
- **Alunos:**
  - Gustavo Ferronatto Ribeiro — [@gustavoferronattoribeiro](https://github.com/gustavoferronattoribeiro)   
  - Victor Augusto Esmaniotto — [@Vitinnn123](https://github.com/Vitinnn123)  
---

## Objetivo
O objetivo deste trabalho é implementar e analisar o desempenho de diferentes tabelas hash com diferentes tamanhos de dados medindo tempo de inserção, busca e número de colisões.

---

## Estrutura do Projeto

```
/Projeto Sistema de Implementação e Analise de Tabelas Hash
├── Resultados.csv (Resultados, inserções, buscas, tempo, etc)
└── README.md (Documentação do projeto)
  /Código completo comentado(src)
  ├── Main.java
  ├── HashDuplo.java
  ├── HashQuadratico.java
  ├── HashEncadeado.java
  ├── Registro.java
  └── Dados.java
  /Código completo não comentado(src)
  ├── Main.java
  ├── HashDuplo.java
  ├── HashQuadratico.java
  ├── HashEncadeado.java
  ├── Registro.java
  └── Dados.java
```

---

## Tecnologias Utilizadas
- **Linguagem:** Java
- **Ambiente de execução:** IntelliJ IDEA, Google Colab
- **Editor utilizado:** IntelliJ IDEA

---

## Implementações
- **Hash Duplo:** Usa uma segunda função hash para achar outro índice quando tem colisão.
- **Hash Quadrático:** Faz novas tentativas de posição usando uma fórmula quadrática.
- **Hash Encadeado:** Usa listas ligadas para guardar os valores quando acontece colisão.

## Tamanhos Utilizados

- **Tamanhos da Tabela Hash:** 1.000, 10.000 e 100.000 posições.  
- **Tamanhos dos Conjuntos de Dados:** 100.000, 1.000.000 e 10.000.000 registros.  
- Todos os dados foram gerados aleatoriamente com *seed fixa* para garantir igualdade de testes.

## Coleta de Dados

Durante os testes, foram coletadas os sequintes dados:
- **Seed** do teste;
- Tempo total de **inserção**;
- Tempo total de **busca**;
- Número total de **colisões** durante inserção;
- As **3 maiores listas encadeadas** (no caso de encadeamento);
- O **menor, maior e valor médio de gap** entre elementos no vetor.

## Análise dos Resultados

## Gráficos

---

## Licença

Este projeto foi desenvolvido **exclusivamente para fins educacionais** na disciplina de *Resolução de Problemas Estruturados em Computação* da Pontifícia Universidade Católica do Paraná.
Não possui finalidade comercial e não concede direitos de uso além do contexto acadêmico.

