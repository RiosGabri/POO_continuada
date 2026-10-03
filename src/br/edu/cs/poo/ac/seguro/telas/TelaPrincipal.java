package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridLayout;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;

	public TelaPrincipal() {
		super("Sistema de Seguros");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel p = new JPanel(new GridLayout(0, 1, 8, 8));
		p.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
		adicionar(p, "Segurado Pessoa", TelaSeguradoPessoa::new);
		adicionar(p, "Segurado Empresa", TelaSeguradoEmpresa::new);
		adicionar(p, "Veículo", TelaVeiculo::new);
		adicionar(p, "Apólice", TelaApolice::new);
		adicionar(p, "Sinistro", TelaSinistro::new);
		add(p);
		pack();
		setLocationRelativeTo(null);
	}

	private void adicionar(JPanel p, String titulo, Supplier<JFrame> tela) {
		JButton b = new JButton(titulo);
		b.addActionListener(e -> tela.get().setVisible(true));
		p.add(b);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
	}
}