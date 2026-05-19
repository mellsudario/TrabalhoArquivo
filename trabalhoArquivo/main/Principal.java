package main;

import interfacegrafica.Painel;

import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.io.*;
import classes.GeradorBoletim;

public class Principal {

	public static void main(String args[]) {
		JFrame frame = new JFrame("Sistema do Professor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        Painel painel = new Painel();
        frame.add(painel);
        frame.setVisible(true);

		Scanner teclado = new Scanner(System.in);

		File sistema = new File("sistemaDoProfessor");
		if (!sistema.exists()) {
			sistema.mkdir();
		}

		int opcao = 0;
		while (opcao != 4) {
			painel.menu();
			opcao = Integer.parseInt(JOptionPane.showInputDialog("Opção escolhida :  "));
			switch (opcao) {
			case 1:
				criarArquivo(teclado, sistema,painel);
				break;

			case 2:
				gerarResultado(teclado, sistema);
				break;

			case 3:
				GeradorBoletim.gerarBoletins(sistema);
				JOptionPane.showMessageDialog(null, "Boletins gerados com sucesso!");
				break;

			case 4:
				JOptionPane.showMessageDialog(null, "Saindo...");
				break;

			default:
				System.out.println("Opção inválida!");
				break;
			}
		}
		teclado.close();
		System.exit(0);
	}
	private static int menu(Scanner teclado, int opcao) {

		System.out.println();
		System.out.println("Escolha uma das opções abaixo:");
		System.out.println("1 - Criar arquivo de respostas dos alunos");
		System.out.println("2 - Gerar resultado de uma disciplina");
		System.out.println("3 - Gerar boletins");
		System.out.println("4 - Sair");
		System.out.println();

		opcao = teclado.nextInt();
		return opcao;
	}

	private static void criarArquivo(Scanner teclado, File sistema, Painel painel) {
		teclado.nextLine();
		boolean temMaisAluno = true;
		System.out.println("Insira o nome da disciplina:");
		String nomeDisciplina = teclado.nextLine();
		File notas = new File(sistema, nomeDisciplina + ".txt");
		File pastaDisciplina = new File(nomeDisciplina);

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
				System.out.println();
				System.out.println("Insira as informações do aluno:");
				System.out.println("Nome:");
				String nome = teclado.nextLine();
				System.out.println("Respostas:");
				String respostas = teclado.nextLine();
				notasWrite.write(respostas + "\t" + nome);
				notasWrite.newLine();
				int opcao = 0;
				while (opcao != 1 && opcao != 2) {
					System.out.println();
					System.out.println("Gostaria de inserir mais alunos?");
					System.out.println("(1-Sim, 2-Não)");
					opcao = teclado.nextInt();
					teclado.nextLine();
					switch (opcao) {
					case 1:
						break;
					case 2:
						temMaisAluno = false;
						break;

					default:
						System.out.println("Opção inválida!");
						break;
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

			return;
		}

		System.out.println();
		System.out.println("Gostaria de adicionar outra disciplina?");

		while (true) {
			System.out.println("(1-Sim, 2-Não)");
			int opcao = teclado.nextInt();
			teclado.nextLine();
			switch (opcao) {
			case 1:
				criarArquivo(teclado, sistema,painel);
				return;

			case 2:
				return;

			default:
				System.out.println("Opção inválida!");
			}
		}
	}

	private static void gerarResultado(Scanner teclado, File sistema) {
		teclado.nextLine();
		System.out.println("Informe o nome da disciplina:");
		String nomeDisciplina = teclado.nextLine();
		System.out.println("Insira o caminho do gabarito oficial:");
		String caminhoGabarito = teclado.nextLine();
		ArrayList<String> dadosAlunos = new ArrayList<>();
		File arquivoDisciplina = new File(sistema, nomeDisciplina + ".txt");
		try (BufferedReader disciplina = new BufferedReader(new FileReader(arquivoDisciplina))) {
			String linha;
			while ((linha = disciplina.readLine()) != null) {
				dadosAlunos.add(linha);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Disciplina não encontrada!");
			return;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		try (BufferedReader arqGabarito = new BufferedReader(new FileReader(caminhoGabarito))) {
			String gabarito = arqGabarito.readLine();
			if (gabarito != null) {
				gabarito = gabarito.toUpperCase();
				listaOrdemAlfabetica(gabarito, dadosAlunos, nomeDisciplina);
				listaOrdemDecrescente(gabarito, dadosAlunos, nomeDisciplina);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Gabarito não encontrado!");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private static void listaOrdemAlfabetica(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina) {
		dadosAlunos.sort((a1, a2) -> {
			String nome1 = a1.split("\t")[1];
			String nome2 = a2.split("\t")[1];
			return nome1.compareTo(nome2);
		});

		File listaAlfabetica = new File(nomeDisciplina, nomeDisciplina + "_listaAlfabetica.txt");
		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaAlfabetica))) {
			System.out.println();
			System.out.println("===== ORDEM ALFABÉTICA =====");

			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				String nome = linha[1];
				int nota = avaliarAluno(gabarito, aluno);
				lista.write(nome + "\t" + nota);
				lista.newLine();
				System.out.println(nome + " - " + nota);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private static void listaOrdemDecrescente(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina) {
		double media = 0;
		for (String aluno : dadosAlunos) {
			media += avaliarAluno(gabarito, aluno);
		}
		media = media / dadosAlunos.size();
		dadosAlunos.sort((a1, a2) -> Integer.compare(avaliarAluno(gabarito, a2),
				avaliarAluno(gabarito, a1)));
		File listaDecrescente = new File(nomeDisciplina, nomeDisciplina + "_listaNotasDecrescente.txt");

		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaDecrescente))) {
			System.out.println();
			System.out.println("===== ORDEM DECRESCENTE =====");

			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				lista.write(linha[1] + "\t" + avaliarAluno(gabarito, aluno));
				lista.newLine();
				System.out.println(linha[1] + " - " + avaliarAluno(gabarito, aluno));
			}
			lista.write("Média da Turma: " + media);

			System.out.println();
			System.out.println("Média da Turma: " + media);

		} catch (IOException e) {
			e.printStackTrace();
			return;
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