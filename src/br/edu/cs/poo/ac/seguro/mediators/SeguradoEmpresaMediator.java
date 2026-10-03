package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
	private static SeguradoEmpresaMediator instancia;

	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
	private SeguradoEmpresaDAO seguradoEmpresaDao = new SeguradoEmpresaDAO();

	private SeguradoEmpresaMediator() {}

	public static SeguradoEmpresaMediator getInstancia() {
		if (instancia == null) {
			instancia = new SeguradoEmpresaMediator();
		}
		return instancia;
	}

	public String validarCnpj(String cnpj) {
		if (StringUtils.ehNuloOuBranco(cnpj)) {
			return "CNPJ deve ser informado";
		}
		if (cnpj.length() != 14) {
			return "CNPJ deve ter 14 caracteres";
		}
		if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
			return "CNPJ com dígito inválido";
		}
		return null;
	}

	public String validarFaturamento(double faturamento) {
		if (faturamento <= 0) {
			return "Faturamento deve ser maior que zero";
		}
		return null;
	}

	public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = validarSeguradoEmpresa(seg);
		if (msg == null) {
			if (!seguradoEmpresaDao.incluir(seg)) {
				msg = "CNPJ do segurado empresa já existente";
			}
		}
		return msg;
	}

	public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = validarSeguradoEmpresa(seg);
		if (msg != null) {
			return msg;
		}
		if (!seguradoEmpresaDao.alterar(seg)) {
			return "CNPJ do segurado empresa não existente";
		}
		return null;
	}

	public String excluirSeguradoEmpresa(String cnpj) {
		if (!seguradoEmpresaDao.excluir(cnpj)) {
			return "CNPJ do segurado empresa não existente";
		}
		return null;
	}

	public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
		if (!StringUtils.ehNuloOuBranco(cnpj)) {
			return seguradoEmpresaDao.buscar(cnpj);
		}
		return null;
	}

	public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = seguradoMediator.validarNome(seg.getNome());
		if (msg != null) {
			return msg;
		}
		msg = seguradoMediator.validarEndereco(seg.getEndereco());
		if (msg != null) {
			return msg;
		}
		if (seg.getDataAbertura() == null) {
			return "Data da abertura deve ser informada";
		}
		msg = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
		if (msg != null) {
			return msg;
		}
		msg = validarCnpj(seg.getCnpj());
		if (msg != null) {
			return msg;
		}
		msg = validarFaturamento(seg.getFaturamento());
		if (msg != null) {
			return msg;
		}
		return null;
	}
}
