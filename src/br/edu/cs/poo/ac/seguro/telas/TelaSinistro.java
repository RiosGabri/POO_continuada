package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

/**
 * CRUD de Sinistro. Não há SinistroMediator na especificação; a tela usa
 * SinistroDAO (e VeiculoDAO para resolver a placa).
 */
public class TelaSinistro extends JFrame {
	private static final long serialVersionUID = 1L;

	private final SinistroDAO sinistroDao = new SinistroDAO();
	private final VeiculoDAO veiculoDao = new VeiculoDAO();

	private final JTextField txtNumero = new JTextField(15);
	private final JTextField txtPlaca = new JTextField(12);
	private final JFormattedTextField txtDataHoraSinistro = TelaUtil.campoDataHora();
	private final JFormattedTextField txtDataHoraRegistro = TelaUtil.campoDataHora();
	private final JTextField txtUsuario = new JTextField(20);
	private final JTextField txtValor = new JTextField("0", 12);
	private final JComboBox<TipoSinistro> cbTipo = new JComboBox<>();

	public TelaSinistro() {
		super("Sinistro");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		cbTipo.addItem(null);
		for (TipoSinistro t : TipoSinistro.values()) {
			cbTipo.addItem(t);
		}
		TelaUtil.renderer(cbTipo, TipoSinistro::getNome);
		TelaUtil.setDataHora(txtDataHoraRegistro, LocalDateTime.now().withSecond(0).withNano(0));

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Dados do sinistro"));
		TelaUtil.linha(form, 0, "Número:", txtNumero);
		TelaUtil.linha(form, 1, "Placa do veículo:", txtPlaca);
		TelaUtil.linha(form, 2, "Data/hora do sinistro:", txtDataHoraSinistro);
		TelaUtil.linha(form, 3, "Data/hora do registro:", txtDataHoraRegistro);
		TelaUtil.linha(form, 4, "Usuário do registro:", txtUsuario);
		TelaUtil.linha(form, 5, "Valor do sinistro:", txtValor);
		TelaUtil.linha(form, 6, "Tipo:", cbTipo);

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

	private Sinistro montar() {
		String numero = txtNumero.getText().trim();
		if (numero.isEmpty()) {
			throw new IllegalArgumentException("Número do sinistro deve ser informado");
		}
		String placa = txtPlaca.getText().trim();
		if (placa.isEmpty()) {
			throw new IllegalArgumentException("Placa do veículo deve ser informada");
		}
		Veiculo veiculo = veiculoDao.buscar(placa);
		if (veiculo == null) {
			throw new IllegalArgumentException("Veículo não cadastrado para a placa informada");
		}
		LocalDateTime dhSinistro = TelaUtil.lerDataHora(txtDataHoraSinistro, "Data/hora do sinistro");
		if (dhSinistro == null) {
			throw new IllegalArgumentException("Data/hora do sinistro deve ser informada");
		}
		LocalDateTime dhRegistro = TelaUtil.lerDataHora(txtDataHoraRegistro, "Data/hora do registro");
		if (dhRegistro == null) {
			throw new IllegalArgumentException("Data/hora do registro deve ser informada");
		}
		if (txtUsuario.getText().trim().isEmpty()) {
			throw new IllegalArgumentException("Usuário do registro deve ser informado");
		}
		TipoSinistro tipo = (TipoSinistro) cbTipo.getSelectedItem();
		if (tipo == null) {
			throw new IllegalArgumentException("Tipo do sinistro deve ser informado");
		}
		return new Sinistro(numero, veiculo, dhSinistro, dhRegistro, txtUsuario.getText().trim(),
				TelaUtil.lerDecimal(txtValor, "Valor do sinistro"), tipo);
	}

	private void buscar() {
		Sinistro s = sinistroDao.buscar(txtNumero.getText().trim());
		if (s == null) {
			TelaUtil.erro(this, "Sinistro não encontrado");
			return;
		}
		txtPlaca.setText(s.getVeiculo() == null ? "" : s.getVeiculo().getPlaca());
		TelaUtil.setDataHora(txtDataHoraSinistro, s.getDataHoraSinistro());
		TelaUtil.setDataHora(txtDataHoraRegistro, s.getDataHoraRegistro());
		txtUsuario.setText(s.getUsuarioRegistro());
		txtValor.setText(TelaUtil.fmt(s.getValorSinistro()));
		cbTipo.setSelectedItem(s.getTipo());
	}

	private void incluir() {
		try {
			if (sinistroDao.incluir(montar())) {
				TelaUtil.info(this, "Sinistro incluído com sucesso");
			} else {
				TelaUtil.erro(this, "Número do sinistro já existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void alterar() {
		try {
			if (sinistroDao.alterar(montar())) {
				TelaUtil.info(this, "Sinistro alterado com sucesso");
			} else {
				TelaUtil.erro(this, "Número do sinistro não existente");
			}
		} catch (IllegalArgumentException ex) {
			TelaUtil.erro(this, ex.getMessage());
		}
	}

	private void excluir() {
		String numero = txtNumero.getText().trim();
		if (TelaUtil.confirmar(this, "Excluir o sinistro " + numero + "?")) {
			if (sinistroDao.excluir(numero)) {
				TelaUtil.info(this, "Sinistro excluído com sucesso");
			} else {
				TelaUtil.erro(this, "Número do sinistro não existente");
			}
		}
	}

	private void limpar() {
		txtNumero.setText("");
		txtPlaca.setText("");
		txtDataHoraSinistro.setValue(null);
		TelaUtil.setDataHora(txtDataHoraRegistro, LocalDateTime.now().withSecond(0).withNano(0));
		txtUsuario.setText("");
		txtValor.setText("0");
		cbTipo.setSelectedIndex(0);
	}
}
