package classes;

import java.io.*;
import java.util.HashMap;

public class GeradorBoletim {

	public static void gerarBoletins(File pastaResultados) {
		HashMap<String, Boletim> boletins = new HashMap<>();

		File[] pastas = pastaResultados.listFiles(File::isDirectory);
		if (pastas == null) {
			return;
		}

		for (File pasta : pastas) {
			File[] arquivos = pasta.listFiles();
			if (arquivos == null) {
				continue;
			}

			for (File arquivo : arquivos) {
				if (!arquivo.getName().contains("_listaNotasDecrescente")) {
					continue;
				}
				try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
					String disciplinaNome = arquivo.getName().split("_")[0];
					Disciplina disciplina = new Disciplina(disciplinaNome);
					String linha;
					while ((linha = br.readLine()) != null) {
						String[] dados = linha.split("\t");

						if (dados.length < 2) {
							continue;
						}

						String nomeAluno = dados[0];
						int nota = Integer.parseInt(dados[1]);
						boletins.putIfAbsent(nomeAluno, new Boletim(new Aluno(nomeAluno)));
						boletins.get(nomeAluno).adicionarNota(new Nota(disciplina, nota));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		criarArquivosBoletim(boletins);
	}

	private static void criarArquivosBoletim(HashMap<String, Boletim> boletins) {
		File pastaBoletins = new File("boletins");
		if (!pastaBoletins.exists()) {
			pastaBoletins.mkdir();
		}
		for (Boletim boletim : boletins.values()) {
			File arquivoAluno = new File(pastaBoletins, boletim.getAluno().getNome() + ".txt");
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoAluno))) {
				bw.write("====================================");
				bw.newLine();
				bw.write("BOLETIM GERAL DO ALUNO");
				bw.newLine();
				bw.write("====================================");
				bw.newLine();
				bw.newLine();
				bw.write("Aluno: " + boletim.getAluno().getNome());
				bw.newLine();
				bw.newLine();
				for (Nota nota : boletim.getNotas()) {
					bw.write("Disciplina: " + nota.getDisciplina().getNome() + "    Nota: " + nota.getNota());
					bw.newLine();
				}
				bw.newLine();
				bw.write("Média geral: " + boletim.calcularMedia());
				bw.newLine();
				bw.write("Situação final: " + boletim.situacaoFinal());
				bw.newLine();
				bw.newLine();
				bw.write("====================================");

				System.out.println("Boletim gerado: " + boletim.getAluno().getNome());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}