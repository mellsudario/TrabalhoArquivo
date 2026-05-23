package interfacegrafica;


import javax.swing.Timer;
import javax.swing.JDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Painel extends JPanel {

	ArrayList<String> conteudo;

	public Painel() {

		this.conteudo = new ArrayList<>();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Color fundo = new Color(70, 130, 180);
		g.setColor(fundo);
		g.fillRect(0, 0, getWidth(), getHeight());
		desenharJanela(g);
	}

	public void desenharJanela(Graphics g) {

		int largura = getWidth();
		int altura = getHeight();
		altura = altura / (conteudo.size() + 4);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 42));
		String titulo = "Sistema do Professor";
		FontMetrics fm = g.getFontMetrics();
		int larguraTitulo = fm.stringWidth(titulo);
		g.drawString(titulo, (largura - larguraTitulo) / 2, 90);
		for (int i = 0; i < conteudo.size(); i++) {
			g.setColor(new Color(240, 240, 240));
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(conteudo.get(i), 100, (i + 2) * altura);
		}
	}

	public void menu() {

		ArrayList<String> opcoes = new ArrayList<>();

		opcoes.add("1 - Criar arquivo de respostas");
		opcoes.add("2 - Gerar resultado da disciplina");
		opcoes.add("3 - Gerar boletins");
		opcoes.add("4 - Sair");

		conteudo = opcoes;
		repaint();
	}

    public void setConteudo(ArrayList <String> conteudo){
        this.conteudo = conteudo;
        repaint();
    }

    public void exibirMensagem(String mensagem, int milissegundos) {
		// exibir mensagem temporariomente
		final JOptionPane optionPane = new JOptionPane(mensagem, JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);

		// Criamos um diálogo a partir desse painel(paniel é modelo visual e dialog é q
		// consegue fechar a janela quando o timer terminar a contagem)
		final JDialog dialog = optionPane.createDialog("Aviso");

		// Timer que fecha o diálogo
		Timer timer = new Timer(milissegundos, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose(); // Fecha a janela
			}
		});

		timer.setRepeats(false); // Garante que o timer rode apenas uma vez
		timer.start();

		dialog.setVisible(true); // Exibe a janela
	}
}