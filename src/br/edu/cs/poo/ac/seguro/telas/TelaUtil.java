package br.edu.cs.poo.ac.seguro.telas;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.function.Function;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

final class TelaUtil {

	static final String[] UFS = { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
			"PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };

	private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);
	private static final DateTimeFormatter FMT_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
			.withResolverStyle(ResolverStyle.STRICT);

	private TelaUtil() {
	}

	static JFormattedTextField campoMascara(String mascara, char placeholder) {
		try {
			MaskFormatter mf = new MaskFormatter(mascara);
			mf.setPlaceholderCharacter(placeholder);
			return new JFormattedTextField(mf);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
	}

	static JFormattedTextField campoNumerico(int tamanho) {
		return campoMascara("#".repeat(tamanho), ' ');
	}

	static JFormattedTextField campoData() {
		return campoMascara("##/##/####", '_');
	}

	static JFormattedTextField campoDataHora() {
		return campoMascara("##/##/#### ##:##", '_');
	}

	static LocalDate lerData(JFormattedTextField c, String nome) {
		String t = c.getText();
		if (t.replaceAll("[_/ :]", "").isEmpty()) {
			return null;
		}
		try {
			return LocalDate.parse(t, FMT_DATA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(nome + " inválida (use dd/mm/aaaa)");
		}
	}

	static LocalDateTime lerDataHora(JFormattedTextField c, String nome) {
		String t = c.getText();
		if (t.replaceAll("[_/ :]", "").isEmpty()) {
			return null;
		}
		try {
			return LocalDateTime.parse(t, FMT_DATA_HORA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(nome + " inválida (use dd/mm/aaaa hh:mm)");
		}
	}

	static void setData(JFormattedTextField c, LocalDate d) {
		c.setValue(d == null ? null : d.format(FMT_DATA));
	}

	static void setDataHora(JFormattedTextField c, LocalDateTime d) {
		c.setValue(d == null ? null : d.format(FMT_DATA_HORA));
	}

	static BigDecimal lerDecimal(JTextField c, String nome) {
		String t = c.getText().trim().replace(',', '.');
		try {
			return new BigDecimal(t);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(nome + " deve ser numérico (ex.: 1500.50)");
		}
	}

	static double lerDouble(JTextField c, String nome) {
		return lerDecimal(c, nome).doubleValue();
	}

	static String fmt(double v) {
		return BigDecimal.valueOf(v).toPlainString();
	}

	static String fmt(BigDecimal v) {
		return v == null ? "" : v.toPlainString();
	}

	static void linha(JPanel p, int row, String rotulo, JComponent c) {
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row;
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(3, 5, 3, 5);
		p.add(new JLabel(rotulo), g);
		g.gridx = 1;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		p.add(c, g);
	}

	static JPanel botoes(JButton... bs) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
		for (JButton b : bs) {
			p.add(b);
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	static <T> void renderer(JComboBox<T> cb, Function<T, String> texto) {
		cb.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean sel,
					boolean focus) {
				return super.getListCellRendererComponent(list, value == null ? "" : texto.apply((T) value), index, sel,
						focus);
			}
		});
	}

	static void erro(Component pai, String msg) {
		JOptionPane.showMessageDialog(pai, msg, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	static void info(Component pai, String msg) {
		JOptionPane.showMessageDialog(pai, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	static boolean confirmar(Component pai, String msg) {
		return JOptionPane.showConfirmDialog(pai, msg, "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
}