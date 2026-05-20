package classes;

import java.util.ArrayList;

public class Boletim {
	private Aluno aluno;
	private ArrayList<Nota> notas;

	public Boletim(Aluno aluno) {
		this.aluno = aluno;
		this.notas = new ArrayList<>();
	}
	public void adicionarNota(Nota nota) {
		notas.add(nota);
	}
	public double calcularMedia() {
		double soma = 0;
		for (Nota nota : notas) {
			soma += nota.getNota();
		}
		return soma / notas.size();
	}
	public String situacaoFinal() {
		if (calcularMedia() >= 7) {
			return "APROVADO";
		}
		return "REPROVADO";
	}
	public Aluno getAluno() {
		return aluno;
	}
	public ArrayList<Nota> getNotas() {
		return notas;
	}
}