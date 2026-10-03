package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

public class TelaSeguradoPessoa extends JFrame {
	private static final long serialVersionUID = 1L;

	private final SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();

	private final JFormattedTextField txtCpf = TelaUtil.campoNumerico(11);
	private final JTextField txtNome = new JTextField(25);
	private final JFormattedTextField txtDataNascimento = TelaUtil.campoData();
	private final JTextField txtRenda = new JTextField("0", 12);
	private final JTextField txtBonus = new JTextField("0", 12);
	private final PainelEndereco painelEndereco = new PainelEndereco();

	public TelaSeguradoPessoa() {
		super("Segurado Pessoa");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Dados da pessoa"));
		TelaUtil.linha(form, 0, "CPF (11 dígitos):", txtCpf);
		TelaUtil.linha(form, 1, "Nome:", txtNome);
		TelaUtil.linha(form, 2, "Data de nascimento:", txtDataNascimento);
		TelaUtil.linha(form, 3, "Renda:", txtRenda);
		TelaUtil.linha(form, 4, "Bônus:", txtBonus);

		JPanel centro = new JPanel();
		centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
		centro.add(form);
		centro.add(painelEndereco);

		JButton btnBuscar = new JButton("Buscar");
		JButton btnIncluir = new JButton("Incluir");
		JButton btnAlterar = new JButton("Alterar");
		JButton btnExcluir = new JButton("Excluir");
		JButton btnLimpar = new JButton("Limpar");
		btnBuscar.addActionListener(e -> buscar());
		btnIncluir.addActionListener(e -> incluir());
		btnAlterar.addActionListener(e -> alterar());
		btnExcluir.addActionListener(e -> excluir());
		btnLimpar.addActionListener(e -> limpar());

		add(centro, BorderLayout.CENTER);
		add(TelaUtil.botoes(btnBuscar, btnIncluir, btnAlterar, btnExcluir, btnLimpar), BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	private SeguradoPessoa montar() {
		return new SeguradoPessoa(txtNome.getText(), painelEndereco.getEndereco(),
				TelaUtil.lerData(txtDataNascimento, "Data de nascimento"), TelaUtil.lerDecimal(txtBonus, "Bônus"),
				txtCpf.getText().trim(), TelaUtil.lerDouble(txtRenda, "Renda"));
	}

	private void buscar() {
		SeguradoPessoa s = mediator.buscarSeguradoPessoa(txtCpf.getText().trim());
		if (s == null) {
			TelaUtil.erro(this, "Segurado pessoa não encontrado");
			return;
		}
		txtNome.setText(s.getNome());
		TelaUtil.setData(txtDataNascimento, s.getDataNascimento());
		txtRenda.setText(TelaUtil.fmt(s.getRenda()));
		txtBonus.setText(s.getBonus() == null ? "0" : TelaUtil.fmt(s.getBonus()));
		painelEndereco.setEndereco(s.getEndereco());
	}

	private void incluir() {
		try {
			resultado(mediator.incluirSeguradoPessoa(montar()), "Segurado pessoa incluído com sucesso");
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void alterar() {
		try {
			resultado(mediator.alterarSeguradoPessoa(montar()), "Segurado pessoa alterado com sucesso");
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void excluir() {
		if (TelaUtil.confirmar(this, "Excluir o segurado de CPF " + txtCpf.getText().trim() + "?")) {
			resultado(mediator.excluirSeguradoPessoa(txtCpf.getText().trim()), "Segurado pessoa excluído com sucesso");
		}
	}

	private void resultado(String msgErro, String msgOk) {
		if (msgErro == null) {
			TelaUtil.info(this, msgOk);
		} else {
			TelaUtil.erro(this, msgErro);
		}
	}

	private void limpar() {
		txtCpf.setValue(null);
		txtNome.setText("");
		txtDataNascimento.setValue(null);
		txtRenda.setText("0");
		txtBonus.setText(BigDecimal.ZERO.toPlainString());
		painelEndereco.limpar();
	}
}