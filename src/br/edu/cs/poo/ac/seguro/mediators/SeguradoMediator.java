package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;

public class SeguradoMediator {
	private static SeguradoMediator instancia;
	private SeguradoMediator() {}

	public static SeguradoMediator getInstancia() {
		if (instancia == null) {
			instancia = new SeguradoMediator();
		}
		return instancia;
	}

	public String validarNome(String nome) {
		if (StringUtils.ehNuloOuBranco(nome)) {
			return "Nome deve ser informado";
		}
		if (nome.length() > 100) {
			return "Tamanho do nome deve ser no máximo 100 caracteres";
		}
		return null;
	}

	public String validarEndereco(Endereco endereco) {
		if (endereco == null) {
			return "Endereço deve ser informado";
		}
		if (StringUtils.ehNuloOuBranco(endereco.getLogradouro())) {
			return "Logradouro deve ser informado";
		}
		String cep = endereco.getCep();
		if (StringUtils.ehNuloOuBranco(cep)) {
			return "CEP deve ser informado";
		}
		if (cep.length() != 8) {
			return "Tamanho do CEP deve ser 8 caracteres";
		}
		if (!StringUtils.temSomenteNumeros(cep)) {
			return "CEP deve ter formato NNNNNNNN";
		}
		String cidade = endereco.getCidade();
		if (StringUtils.ehNuloOuBranco(cidade)) {
			return "Cidade deve ser informada";
		}
		if (cidade.length() > 100) {
			return "Tamanho da cidade deve ser no máximo 100 caracteres";
		}
		String estado = endereco.getEstado();
		if (StringUtils.ehNuloOuBranco(estado)) {
			return "Sigla do estado deve ser informada";
		}
		if (estado.length() != 2) {
			return "Tamanho da sigla do estado deve ser 2 caracteres";
		}
		String pais = endereco.getPais();
		if (StringUtils.ehNuloOuBranco(pais)) {
			return "País deve ser informado";
		}
		if (pais.length() > 40) {
			return "Tamanho do país deve ser no máximo 40 caracteres";
		}
		String numero = endereco.getNumero();
		if (numero != null && numero.length() > 20) {
			return "Tamanho do número deve ser no máximo 20 caracteres";
		}
		String complemento = endereco.getComplemento();
		if (complemento != null && complemento.length() > 30) {
			return "Tamanho do complemento deve ser no máximo 30 caracteres";
		}
		return null;
	}

	public String validarDataCriacao(LocalDate dataCriacao) {
		if (dataCriacao == null) {
			return "Data da criação deve ser informada";
		}
		if (dataCriacao.isAfter(LocalDate.now())) {
			return "Data da criação deve ser menor ou igual à data atual";
		}
		return null;
	}

	public BigDecimal ajustarDebitoBonus(BigDecimal bonus, BigDecimal valorDebito) {
		if (bonus != null && valorDebito != null) {
			if (bonus.compareTo(valorDebito) >= 0) {
				return valorDebito;
			}
			return bonus;
		}
		return null;
	}
}