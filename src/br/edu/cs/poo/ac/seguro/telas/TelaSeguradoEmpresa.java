package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

/** CRUD de Segurado Empresa. */
public class TelaSeguradoEmpresa extends JFrame {
	private static final long serialVersionUID = 1L;

	private final SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();

	private final JFormattedTextField txtCnpj = TelaUtil.campoNumerico(14);
	private final JTextField txtNome = new JTextField(25);
	private final JFormattedTextField txtDataAbertura = TelaUtil.campoData();
	private final JTextField txtFaturamento = new JTextField("0", 12);
	private final JTextField txtBonus = new JTextField("0", 12);
	private final JCheckBox chkLocadora = new JCheckBox("É locadora de veículos");
	private final PainelEndereco painelEndereco = new PainelEndereco();

	public TelaSeguradoEmpresa() {
		super("Segurado Empresa");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Dados da empresa"));
		TelaUtil.linha(form, 0, "CNPJ (14 dígitos):", txtCnpj);
		TelaUtil.linha(form, 1, "Nome:", txtNome);
		TelaUtil.linha(form, 2, "Data de abertura:", txtDataAbertura);
		TelaUtil.linha(form, 3, "Faturamento:", txtFaturamento);
		TelaUtil.linha(form, 4, "Bônus:", txtBonus);
		TelaUtil.linha(form, 5, "", chkLocadora);

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

	private SeguradoEmpresa montar() {
		return new SeguradoEmpresa(txtNome.getText(), painelEndereco.getEndereco(),
				TelaUtil.lerData(txtDataAbertura, "Data de abertura"), TelaUtil.lerDecimal(txtBonus, "Bônus"),
				txtCnpj.getText().trim(), TelaUtil.lerDouble(txtFaturamento, "Faturamento"),
				chkLocadora.isSelected());
	}

	private void buscar() {
		SeguradoEmpresa s = mediator.buscarSeguradoEmpresa(txtCnpj.getText().trim());
		if (s == null) {
			TelaUtil.erro(this, "Segurado empresa não encontrado");
			return;
		}
		txtNome.setText(s.getNome());
		TelaUtil.setData(txtDataAbertura, s.getDataAbertura());
		txtFaturamento.setText(TelaUtil.fmt(s.getFaturamento()));
		txtBonus.setText(s.getBonus() == null ? "0" : TelaUtil.fmt(s.getBonus()));
		chkLocadora.setSelected(s.isEhLocadoraDeVeiculos());
		painelEndereco.setEndereco(s.getEndereco());
	}

	private void incluir() {
		try {
			resultado(mediator.incluirSeguradoEmpresa(montar()), "Segurado empresa incluído com sucesso");
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void alterar() {
		try {
			resultado(mediator.alterarSeguradoEmpresa(montar()), "Segurado empresa alterado com sucesso");
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void excluir() {
		if (TelaUtil.confirmar(this, "Excluir o segurado de CNPJ " + txtCnpj.getText().trim() + "?")) {
			resultado(mediator.excluirSeguradoEmpresa(txtCnpj.getText().trim()),
					"Segurado empresa excluído com sucesso");
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
		txtCnpj.setValue(null);
		txtNome.setText("");
		txtDataAbertura.setValue(null);
		txtFaturamento.setText("0");
		txtBonus.setText("0");
		chkLocadora.setSelected(false);
		painelEndereco.limpar();
	}
}