package interfacegrafica;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Painel extends JPanel {

    ArrayList <String> conteudo;

    public Painel(){
        this.conteudo = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Color fundo = Color.decode("FF708DC7");
        g.setColor(fundo);		
        g.fillRect(0, 0, getWidth(), getHeight());
	}


    public void desenharJanela(Graphics g){
        int largura = getWidth();
        int altura = getHeight();
        altura = altura/(conteudo.size()+2);

        for(int i = 0; i<conteudo.size();i++){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(conteudo.get(i),100, i*altura);
        }
    }


    public void menu(){
        ArrayList <String> opcoes = new ArrayList<>();

        opcoes.add("Escolha uma das opções abaixo:\n");
		opcoes.add("1 - Criar arquivo de respostas dos alunos\n");
		opcoes.add("2 - Gerar resultado de uma disciplina\n");
		opcoes.add("3 - Gerar boletins\n");
		opcoes.add("4 - Sair");
		opcoes.add("\n");
        conteudo = opcoes;
        repaint();
    }


    public void exibirMensagem(String mensagem){

    }
}
