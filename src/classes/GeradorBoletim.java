package classes;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import interfacegrafica.Painel;

public class GeradorBoletim {

	public static void gerarBoletins(File pastaResultados,Painel painel) {

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
						if (linha.startsWith("Média")) {
							continue;
						}
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

		criarArquivosBoletim(boletins, pastaResultados,painel);
	}

	private static void criarArquivosBoletim(HashMap<String, Boletim> boletins, File pastaResultados,Painel painel) {
		File pastaBoletins = new File(pastaResultados, "boletins");
		if (!pastaBoletins.exists()) {
			pastaBoletins.mkdir();
		}
		ArrayList <String> listaPainel = new ArrayList<>();
		for (Boletim boletim : boletins.values()) {
			listaPainel.clear();
			File arquivoAluno = new File(pastaBoletins, boletim.getAluno().getNome() + ".txt");
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoAluno))) {

				bw.write("====================================");
				bw.newLine();
				listaPainel.add("");
				listaPainel.add("====================================");


				bw.write("BOLETIM GERAL DO ALUNO");
				bw.newLine();
				listaPainel.add("BOLETIM GERAL DO ALUNO");

				bw.write("====================================");
				bw.newLine();
				bw.newLine();
				listaPainel.add("====================================");

				bw.write("Aluno: " + boletim.getAluno().getNome());
				listaPainel.add("Aluno: " + boletim.getAluno().getNome());
				bw.newLine();
				bw.newLine();

				for (Nota nota : boletim.getNotas()) {
					bw.write("Disciplina: " + nota.getDisciplina().getNome() + "    Nota: " + nota.getNota());
					bw.newLine();
					listaPainel.add("Disciplina: " + nota.getDisciplina().getNome() + "    Nota: " + nota.getNota());
				}

				bw.newLine();
				bw.write("Média geral: " + String.format("%.2f", boletim.calcularMedia()));
				listaPainel.add("Média geral: " + String.format("%.2f", boletim.calcularMedia()));
				bw.newLine();
				bw.write("Situação final: " + boletim.situacaoFinal());
				listaPainel.add("Situação final: " + boletim.situacaoFinal());
				bw.newLine();
				bw.write("====================================");
				listaPainel.add("====================================");

				painel.setConteudo(listaPainel);
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}