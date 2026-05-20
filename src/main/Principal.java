package main;

import interfacegrafica.Painel;

import javax.swing.*;
import java.util.ArrayList;
import java.io.*;

import classes.GeradorBoletim;

public class Principal {
	public static void main(String args[]) {

		JFrame frame = new JFrame("Sistema do Professor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		Painel painel = new Painel();
		frame.add(painel);
		frame.setVisible(true);
		File sistema = new File("sistemaDoProfessor");
		if (!sistema.exists()) {
			sistema.mkdir();
		}

		int opcao = 0;
		while (opcao != 4) {
			painel.menu();
			opcao = Integer.parseInt(JOptionPane.showInputDialog("Escolha uma opção:"));
			switch (opcao) {
			case 1:
				criarArquivo(sistema, painel);
				break;

			case 2:
				gerarResultado(sistema);
				break;

			case 3:
				GeradorBoletim.gerarBoletins(sistema);
				JOptionPane.showMessageDialog(null, "Boletins gerados com sucesso!");
				break;

			case 4:
				JOptionPane.showMessageDialog(null, "Saindo...");
				break;

			default:
				JOptionPane.showMessageDialog(null, "Opção inválida!");
			}
		}

		System.exit(0);
	}

	private static void criarArquivo(File sistema, Painel painel) {
		boolean temMaisAluno = true;
		String nomeDisciplina = JOptionPane.showInputDialog("Nome da disciplina:");
		if (nomeDisciplina == null) {
			return;
		}

		File notas = new File(sistema, nomeDisciplina + ".txt");
		File pastaDisciplina = new File(sistema, nomeDisciplina);

		try {
			if (!pastaDisciplina.exists()) {
				pastaDisciplina.mkdir();
			}
			if (!notas.exists()) {
				notas.createNewFile();
			}
		} catch (IOException e) {
			painel.exibirMensagem(e.getMessage());
			return;
		}

		try (BufferedWriter notasWrite = new BufferedWriter(new FileWriter(notas, true))) {
			while (temMaisAluno) {
				String nome = JOptionPane.showInputDialog("Nome do aluno:");
				if (nome == null) {
					return;
				}

				String respostas = JOptionPane.showInputDialog("Digite as 10 respostas:");
				if (respostas == null) {
					return;
				}
				notasWrite.write(respostas + "\t" + nome);
				notasWrite.newLine();
				int opcao = JOptionPane.showConfirmDialog(null, "Deseja adicionar outro aluno?", "Continuar",
						JOptionPane.YES_NO_OPTION);

				if (opcao != JOptionPane.YES_OPTION) {
					temMaisAluno = false;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		JOptionPane.showMessageDialog(null, "Disciplina criada com sucesso!");
	}

	private static void gerarResultado(File sistema) {
		String nomeDisciplina = JOptionPane.showInputDialog("Nome da disciplina:");
		if (nomeDisciplina == null) {
			return;
		}

		String caminhoGabarito = JOptionPane.showInputDialog("Digite o caminho do gabarito:");
		if (caminhoGabarito == null) {
			return;
		}

		ArrayList<String> dadosAlunos = new ArrayList<>();
		File arquivoDisciplina = new File(sistema, nomeDisciplina + ".txt");
		try (BufferedReader disciplina = new BufferedReader(new FileReader(arquivoDisciplina))) {
			String linha;
			while ((linha = disciplina.readLine()) != null) {
				dadosAlunos.add(linha);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Disciplina não encontrada!");
			return;
		}

		try (BufferedReader arqGabarito = new BufferedReader(new FileReader(caminhoGabarito))) {
			String gabarito = arqGabarito.readLine();
			if (gabarito != null) {
				gabarito = gabarito.toUpperCase();
				listaOrdemAlfabetica(gabarito, dadosAlunos, nomeDisciplina);
				listaOrdemDecrescente(gabarito, dadosAlunos, nomeDisciplina);
				JOptionPane.showMessageDialog(null, "Resultados gerados com sucesso!");
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Gabarito não encontrado!");
		}
	}

	private static void listaOrdemAlfabetica(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina) {
		dadosAlunos.sort((a1, a2) -> {
			String nome1 = a1.split("\t")[1];
			String nome2 = a2.split("\t")[1];
			return nome1.compareTo(nome2);
		});

		File pastaDisciplina = new File("sistemaDoProfessor/" + nomeDisciplina);
		File listaAlfabetica = new File(pastaDisciplina, nomeDisciplina + "_listaAlfabetica.txt");
		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaAlfabetica))) {

			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				String nome = linha[1];
				int nota = avaliarAluno(gabarito, aluno);
				lista.write(nome + "\t" + nota);
				lista.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void listaOrdemDecrescente(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina) {
		double media = 0;
		for (String aluno : dadosAlunos) {
			media += avaliarAluno(gabarito, aluno);
		}
		media = media / dadosAlunos.size();
		dadosAlunos.sort((a1, a2) -> Integer.compare(avaliarAluno(gabarito, a2), avaliarAluno(gabarito, a1)));
		File pastaDisciplina = new File("sistemaDoProfessor/" + nomeDisciplina);
		File listaDecrescente = new File(pastaDisciplina, nomeDisciplina + "_listaNotasDecrescente.txt");
		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaDecrescente))) {
			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				lista.write(linha[1] + "\t" + avaliarAluno(gabarito, aluno));
				lista.newLine();
			}
			lista.write("Média da Turma: " + media);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int avaliarAluno(String gabarito, String aluno) {
		String[] dados = aluno.split("\t");
		String respostas = dados[0].toUpperCase();
		int nota = 0;
		int f = 0;
		int v = 0;
		for (int i = 0; i < gabarito.length(); i++) {
			if (gabarito.charAt(i) == respostas.charAt(i)) {
				nota++;
			}
			if (respostas.charAt(i) == 'F') {
				f++;
			}
			if (respostas.charAt(i) == 'V') {
				v++;
			}
		}
		if (v == 10 || f == 10) {
			return 0;
		}
		return nota;
	}
}