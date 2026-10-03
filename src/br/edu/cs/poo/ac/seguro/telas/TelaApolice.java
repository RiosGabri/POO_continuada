package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

/**
 * CRUD de Apólice. Não há ApoliceMediator na especificação; a tela usa
 * ApoliceDAO (e VeiculoDAO para resolver a placa).
 */
public class TelaApolice extends JFrame {
	private static final long serialVersionUID = 1L;

	private final ApoliceDAO apoliceDao = new ApoliceDAO();
	private final VeiculoDAO veiculoDao = new VeiculoDAO();

	private final JTextField txtNumero = new JTextField(15);
	private final JTextField txtPlaca = new JTextField(12);
	private final JTextField txtFranquia = new JTextField("0", 12);
	private final JTextField txtPremio = new JTextField("0", 12);
	private final JTextField txtValorMaximo = new JTextField("0", 12);

	public TelaApolice() {
		super("Apólice");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Dados da apólice"));
		TelaUtil.linha(form, 0, "Número:", txtNumero);
		TelaUtil.linha(form, 1, "Placa do veículo:", txtPlaca);
		TelaUtil.linha(form, 2, "Valor da franquia:", txtFranquia);
		TelaUtil.linha(form, 3, "Valor do prêmio:", txtPremio);
		TelaUtil.linha(form, 4, "Valor máximo segurado:", txtValorMaximo);

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

	private Apolice montar() {
		String numero = txtNumero.getText().trim();
		if (numero.isEmpty()) {
			throw new IllegalArgumentException("Número da apólice deve ser informado");
		}
		String placa = txtPlaca.getText().trim();
		if (placa.isEmpty()) {
			throw new IllegalArgumentException("Placa do veículo deve ser informada");
		}
		Veiculo veiculo = veiculoDao.buscar(placa);
		if (veiculo == null) {
			throw new IllegalArgumentException("Veículo não cadastrado para a placa informada");
		}
		Apolice ap = new Apolice(veiculo, TelaUtil.lerDecimal(txtFranquia, "Valor da franquia"),
				TelaUtil.lerDecimal(txtPremio, "Valor do prêmio"),
				TelaUtil.lerDecimal(txtValorMaximo, "Valor máximo segurado"));
		ap.setNumero(numero);
		return ap;
	}

	private void buscar() {
		Apolice a = apoliceDao.buscar(txtNumero.getText().trim());
		if (a == null) {
			TelaUtil.erro(this, "Apólice não encontrada");
			return;
		}
		txtPlaca.setText(a.getVeiculo() == null ? "" : a.getVeiculo().getPlaca());
		txtFranquia.setText(TelaUtil.fmt(a.getValorFranquia()));
		txtPremio.setText(TelaUtil.fmt(a.getValorPremio()));
		txtValorMaximo.setText(TelaUtil.fmt(a.getValorMaximoSegurado()));
	}

	private void incluir() {
		try {
			if (apoliceDao.incluir(montar())) {
				TelaUtil.info(this, "Apólice incluída com sucesso");
			} else {
				TelaUtil.erro(this, "Número da apólice já existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void alterar() {
		try {
			if (apoliceDao.alterar(montar())) {
				TelaUtil.info(this, "Apólice alterada com sucesso");
			} else {
				TelaUtil.erro(this, "Número da apólice não existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void excluir() {
		String numero = txtNumero.getText().trim();
		if (TelaUtil.confirmar(this, "Excluir a apólice " + numero + "?")) {
			if (apoliceDao.excluir(numero)) {
				TelaUtil.info(this, "Apólice excluída com sucesso");
			} else {
				TelaUtil.erro(this, "Número da apólice não existente");
			}
		}
	}

	private void limpar() {
		txtNumero.setText("");
		txtPlaca.setText("");
		txtFranquia.setText("0");
		txtPremio.setText("0");
		txtValorMaximo.setText("0");
	}
}
