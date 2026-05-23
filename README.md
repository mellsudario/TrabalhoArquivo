# 📚 Sistema do Professor

Para gerenciamento de provas objetivas, geração de resultados e boletins de alunos.  
---

## Funcionalidades

- **Criar arquivo de respostas** — cadastra alunos e suas respostas (10 questões V/F) por disciplina.
- **Gerar resultado** — compara respostas com o gabarito e gera dois arquivos: lista alfabética e lista por nota decrescente com média da turma. Alunos que marcaram tudo V ou tudo F recebem nota 0.
- **Gerar boletins** — lê os resultados de todas as disciplinas e gera um arquivo `.txt` por aluno com notas, média geral e situação final (APROVADO se média ≥ 7).

---

## Estrutura do Projeto

```
src/
├── main/Principal.java
├── classes/
│   ├── Aluno.java
│   ├── Disciplina.java
│   ├── Nota.java
│   ├── Boletim.java
│   └── GeradorBoletim.java
└── interfacegrafica/Painel.java
```

---

## Como os arquivos são salvos

Todos os dados ficam dentro da pasta `sistemaDoProfessor/`, criada automaticamente no diretório raiz do projeto.

```
sistemaDoProfessor/
├── <Disciplina>/
│   ├── <Disciplina>.txt                       # Respostas dos alunos
│   ├── <Disciplina>_listaAlfabetica.txt       # Resultado em ordem alfabética
│   └── <Disciplina>_listaNotasDecrescente.txt # Resultado por nota + média da turma
└── boletins/
    └── <NomeDoAluno>.txt                      # Boletim individual de cada aluno
```
