package classes;

public class Nota {
    private Disciplina disciplina;
    private int nota;

    public Nota(Disciplina disciplina, int nota) {
        this.disciplina = disciplina;
        this.nota = nota;
    }
    public Disciplina getDisciplina() {
        return disciplina;
    }
    public int getNota() {
        return nota;
    }
}