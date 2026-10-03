package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;

/** Painel reutilizado pelas telas de segurado (pessoa e empresa). */
class PainelEndereco extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtLogradouro = new JTextField(25);
	private final JFormattedTextField txtCep = TelaUtil.campoNumerico(8);
	private final JTextField txtNumero = new JTextField(10);
	private final JTextField txtComplemento = new JTextField(15);
	private final JTextField txtPais = new JTextField("Brasil", 15);
	private final JComboBox<String> cbEstado = new JComboBox<>(TelaUtil.UFS);
	private final JTextField txtCidade = new JTextField(20);

	PainelEndereco() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Endereço"));
		TelaUtil.linha(this, 0, "Logradouro:", txtLogradouro);
		TelaUtil.linha(this, 1, "CEP (8 dígitos):", txtCep);
		TelaUtil.linha(this, 2, "Número:", txtNumero);
		TelaUtil.linha(this, 3, "Complemento:", txtComplemento);
		TelaUtil.linha(this, 4, "País:", txtPais);
		TelaUtil.linha(this, 5, "Estado (UF):", cbEstado);
		TelaUtil.linha(this, 6, "Cidade:", txtCidade);
	}

	Endereco getEndereco() {
		return new Endereco(txtLogradouro.getText(), txtCep.getText().trim(), txtNumero.getText(),
				txtComplemento.getText(), txtPais.getText(), (String) cbEstado.getSelectedItem(),
				txtCidade.getText());
	}

	void setEndereco(Endereco e) {
		if (e == null) {
			limpar();
			return;
		}
		txtLogradouro.setText(e.getLogradouro());
		txtCep.setValue(e.getCep());
		txtNumero.setText(e.getNumero());
		txtComplemento.setText(e.getComplemento());
		txtPais.setText(e.getPais());
		cbEstado.setSelectedItem(e.getEstado() == null ? "" : e.getEstado());
		txtCidade.setText(e.getCidade());
	}

	void limpar() {
		txtLogradouro.setText("");
		txtCep.setValue(null);
		txtNumero.setText("");
		txtComplemento.setText("");
		txtPais.setText("Brasil");
		cbEstado.setSelectedIndex(0);
		txtCidade.setText("");
	}
}
