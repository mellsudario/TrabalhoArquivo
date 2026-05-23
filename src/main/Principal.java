package main;

import interfacegrafica.Painel;
import classes.GeradorBoletim;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.io.*;


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
			try{
				opcao = Integer.parseInt(JOptionPane.showInputDialog("Escolha uma opção:"));
			}catch(Exception e){
				System.exit(0);
			}
			switch (opcao) {
			case 1:
				criarArquivo(sistema, painel);
				break;

			case 2:
				gerarResultado(sistema,painel);
				break;

			case 3:
				GeradorBoletim.gerarBoletins(sistema, painel);
				painel.exibirMensagem("Boletins gerados com sucesso!",1500);
				break;

			case 4:
				painel.exibirMensagem("Saindo...",1500);
				break;

			default:
				painel.exibirMensagem("Opção inválida!",1500);
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

		File pastaDisciplina = new File(sistema,nomeDisciplina);
		File notas = new File(pastaDisciplina, nomeDisciplina + ".txt");

		try {
			if (!pastaDisciplina.exists()) {
				pastaDisciplina.mkdir();
			}
			if (!notas.exists()) {
				notas.createNewFile();
			}
		} catch (IOException e) {
			painel.exibirMensagem(e.getMessage(),1500);
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

	
		
		while (true) {
			int opcao = JOptionPane.showConfirmDialog(null, "Deseja adicionar outro disciplina?", "Continuar",
							JOptionPane.YES_NO_OPTION);
	
			if (opcao == JOptionPane.YES_OPTION) {
				criarArquivo(sistema,painel);
			}
			return;
		}
	}

	private static void gerarResultado(File sistema, Painel painel) {
		String nomeDisciplina = JOptionPane.showInputDialog("Nome da disciplina:");
		if (nomeDisciplina == null) {
			return;
		}

		String caminhoGabarito = JOptionPane.showInputDialog("Digite o caminho do gabarito:");
		if (caminhoGabarito == null) {
			return;
		}

		ArrayList<String> dadosAlunos = new ArrayList<>();
		File pastaDisciplina = new File(sistema, nomeDisciplina);
		File arquivoDisciplina = new File(pastaDisciplina, nomeDisciplina + ".txt");
		try (BufferedReader disciplina = new BufferedReader(new FileReader(arquivoDisciplina))) {
			String linha;
			while ((linha = disciplina.readLine()) != null) {
				dadosAlunos.add(linha);
			}
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Disciplina não encontrada!");
			return;
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Erro!");
			return;
		}

		try (BufferedReader arqGabarito = new BufferedReader(new FileReader(caminhoGabarito))) {
			String gabarito = arqGabarito.readLine();
			if (gabarito != null) {
				gabarito = gabarito.toUpperCase();
				listaOrdemAlfabetica(gabarito, dadosAlunos, nomeDisciplina,painel);
				listaOrdemDecrescente(gabarito, dadosAlunos, nomeDisciplina,painel);
				JOptionPane.showMessageDialog(null, "Resultados gerados com sucesso!");
			}

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Gabarito não encontrado!");
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}


	}

	private static void listaOrdemAlfabetica(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina,Painel painel) {
		dadosAlunos.sort((a1, a2) -> {
			String nome1 = a1.split("\t")[1];
			String nome2 = a2.split("\t")[1];
			return nome1.compareTo(nome2);
		});

		ArrayList <String> listaPainel = new ArrayList<>();
		listaPainel.add("Lista em ordem alfabética");
		listaPainel.add("Disciplina: " + nomeDisciplina);
		File pastaDisciplina = new File("sistemaDoProfessor/" + nomeDisciplina);
		File listaAlfabetica = new File(pastaDisciplina, nomeDisciplina + "_listaAlfabetica.txt");
		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaAlfabetica))) {

			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				String nome = linha[1];
				int nota = avaliarAluno(gabarito, aluno);
				lista.write(nome + "\t" + nota);
				lista.newLine();
				listaPainel.add(nome + "    " + nota);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		painel.setConteudo(listaPainel);
		try{
			Thread.sleep(5000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	private static void listaOrdemDecrescente(String gabarito, ArrayList<String> dadosAlunos, String nomeDisciplina, Painel painel) {
		double media = 0;
		for (String aluno : dadosAlunos) {
			media += avaliarAluno(gabarito, aluno);
		}
		media = media / dadosAlunos.size();
		
		dadosAlunos.sort((a1, a2) -> Integer.compare(avaliarAluno(gabarito, a2), avaliarAluno(gabarito, a1)));
		
		ArrayList <String> listaPainel = new ArrayList<>();
		listaPainel.add("Lista em ordem decrescente de nota");
		listaPainel.add("Disciplina: " + nomeDisciplina);
		File pastaDisciplina = new File("sistemaDoProfessor/" + nomeDisciplina);
		File listaDecrescente = new File(pastaDisciplina, nomeDisciplina + "_listaNotasDecrescente.txt");
		
		try (BufferedWriter lista = new BufferedWriter(new FileWriter(listaDecrescente))) {
			for (String aluno : dadosAlunos) {
				String[] linha = aluno.split("\t");
				lista.write(linha[1] + "\t" + avaliarAluno(gabarito, aluno));
				lista.newLine();
				listaPainel.add(linha[1] +"    " + avaliarAluno(gabarito, aluno));
			}
			lista.write("Média da Turma: " + media);
			listaPainel.add("Média da Turma: " + media);
		} catch (IOException e) {
			e.printStackTrace();
		}


		painel.setConteudo(listaPainel);
		try{
			Thread.sleep(5000);
		}catch(InterruptedException e){
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