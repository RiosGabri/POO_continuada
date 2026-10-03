package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

//CRUD de Veículo.

public class TelaVeiculo extends JFrame {
	private static final long serialVersionUID = 1L;

	private final VeiculoDAO dao = new VeiculoDAO();
	private final SeguradoPessoaMediator pessoaMediator = SeguradoPessoaMediator.getInstancia();
	private final SeguradoEmpresaMediator empresaMediator = SeguradoEmpresaMediator.getInstancia();

	private final JTextField txtPlaca = new JTextField(12);
	private final JSpinner spAno = new JSpinner(
			new SpinnerNumberModel(LocalDate.now().getYear(), 1900, LocalDate.now().getYear() + 1, 1));
	private final JComboBox<CategoriaVeiculo> cbCategoria = new JComboBox<>();
	private final JRadioButton rbPessoa = new JRadioButton("Pessoa (CPF)", true);
	private final JRadioButton rbEmpresa = new JRadioButton("Empresa (CNPJ)");
	private final JTextField txtDocumento = new JTextField(16);

	public TelaVeiculo() {
		super("Veículo");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		spAno.setEditor(new JSpinner.NumberEditor(spAno, "#"));
		cbCategoria.addItem(null);
		for (CategoriaVeiculo c : CategoriaVeiculo.values()) {
			cbCategoria.addItem(c);
		}
		TelaUtil.renderer(cbCategoria, CategoriaVeiculo::getNome);

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rbPessoa);
		grupo.add(rbEmpresa);
		JPanel tipoProp = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		tipoProp.add(rbPessoa);
		tipoProp.add(rbEmpresa);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Dados do veículo"));
		TelaUtil.linha(form, 0, "Placa:", txtPlaca);
		TelaUtil.linha(form, 1, "Ano:", spAno);
		TelaUtil.linha(form, 2, "Categoria:", cbCategoria);
		TelaUtil.linha(form, 3, "Tipo de proprietário:", tipoProp);
		TelaUtil.linha(form, 4, "CPF/CNPJ do proprietário:", txtDocumento);

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

		add(form, BorderLayout.CENTER);
		add(TelaUtil.botoes(btnBuscar, btnIncluir, btnAlterar, btnExcluir, btnLimpar), BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	private Veiculo montar() {
		String placa = txtPlaca.getText().trim();
		if (placa.isEmpty()) {
			throw new IllegalArgumentException("Placa deve ser informada");
		}
		CategoriaVeiculo categoria = (CategoriaVeiculo) cbCategoria.getSelectedItem();
		if (categoria == null) {
			throw new IllegalArgumentException("Categoria deve ser informada");
		}
		String doc = txtDocumento.getText().trim();
		if (doc.isEmpty()) {
			throw new IllegalArgumentException("CPF/CNPJ do proprietário deve ser informado");
		}
		SeguradoPessoa pessoa = null;
		SeguradoEmpresa empresa = null;
		if (rbPessoa.isSelected()) {
			pessoa = pessoaMediator.buscarSeguradoPessoa(doc);
			if (pessoa == null) {
				throw new IllegalArgumentException("Segurado pessoa não cadastrado para o CPF informado");
			}
		} else {
			empresa = empresaMediator.buscarSeguradoEmpresa(doc);
			if (empresa == null) {
				throw new IllegalArgumentException("Segurado empresa não cadastrado para o CNPJ informado");
			}
		}
		return new Veiculo(placa, (Integer) spAno.getValue(), empresa, pessoa, categoria);
	}

	private void buscar() {
		Veiculo v = dao.buscar(txtPlaca.getText().trim());
		if (v == null) {
			TelaUtil.erro(this, "Veículo não encontrado");
			return;
		}
		spAno.setValue(v.getAno());
		cbCategoria.setSelectedItem(v.getCategoria());
		if (v.getProprietarioPessoa() != null) {
			rbPessoa.setSelected(true);
			txtDocumento.setText(v.getProprietarioPessoa().getCpf());
		} else if (v.getProprietarioEmpresa() != null) {
			rbEmpresa.setSelected(true);
			txtDocumento.setText(v.getProprietarioEmpresa().getCnpj());
		} else {
			txtDocumento.setText("");
		}
	}

	private void incluir() {
		try {
			if (dao.incluir(montar())) {
				TelaUtil.info(this, "Veículo incluído com sucesso");
			} else {
				TelaUtil.erro(this, "Placa do veículo já existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void alterar() {
		try {
			if (dao.alterar(montar())) {
				TelaUtil.info(this, "Veículo alterado com sucesso");
			} else {
				TelaUtil.erro(this, "Placa do veículo não existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void excluir() {
		String placa = txtPlaca.getText().trim();
		if (TelaUtil.confirmar(this, "Excluir o veículo de placa " + placa + "?")) {
			if (dao.excluir(placa)) {
				TelaUtil.info(this, "Veículo excluído com sucesso");
			} else {
				TelaUtil.erro(this, "Placa do veículo não existente");
			}
		}
	}

	private void limpar() {
		txtPlaca.setText("");
		spAno.setValue(LocalDate.now().getYear());
		cbCategoria.setSelectedIndex(0);
		rbPessoa.setSelected(true);
		txtDocumento.setText("");
	}
}