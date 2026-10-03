package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
	private static SeguradoPessoaMediator instancia;

	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
	private SeguradoPessoaDAO seguradoPessoaDao = new SeguradoPessoaDAO();

	private SeguradoPessoaMediator() {}

	public static SeguradoPessoaMediator getInstancia() {
		if (instancia == null) {
			instancia = new SeguradoPessoaMediator();
		}
		return instancia;
	}

	public String validarCpf(String cpf) {
		if (StringUtils.ehNuloOuBranco(cpf)) {
			return "CPF deve ser informado";
		}
		if (cpf.length() != 11) {
			return "CPF deve ter 11 caracteres";
		}
		if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
			return "CPF com dígito inválido";
		}
		return null;
	}

	public String validarRenda(double renda) {
		if (renda < 0) {
			return "Renda deve ser maior ou igual à zero";
		}
		return null;
	}

	public String incluirSeguradoPessoa(SeguradoPessoa seg) {
		String msg = validarSeguradoPessoa(seg);
		if (msg == null) {
			if (!seguradoPessoaDao.incluir(seg)) {
				msg = "CPF do segurado pessoa já existente";
			}
		}
		return msg;
	}

	public String alterarSeguradoPessoa(SeguradoPessoa seg) {
		String msg = validarSeguradoPessoa(seg);
		if (msg != null) {
			return msg;
		}
		if (!seguradoPessoaDao.alterar(seg)) {
			return "CPF do segurado pessoa não existente";
		}
		return null;
	}

	public String excluirSeguradoPessoa(String cpf) {
		if (!seguradoPessoaDao.excluir(cpf)) {
			return "CPF do segurado pessoa não existente";
		}
		return null;
	}

	public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
		if (!StringUtils.ehNuloOuBranco(cpf)) {
			return seguradoPessoaDao.buscar(cpf);
		}
		return null;
	}

	public String validarSeguradoPessoa(SeguradoPessoa seg) {
		String msg = seguradoMediator.validarNome(seg.getNome());
		if (msg != null) {
			return msg;
		}
		msg = seguradoMediator.validarEndereco(seg.getEndereco());
		if (msg != null) {
			return msg;
		}
		if (seg.getDataNascimento() == null) {
			return "Data do nascimento deve ser informada";
		}
		msg = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
		if (msg != null) {
			return msg;
		}
		msg = validarCpf(seg.getCpf());
		if (msg != null) {
			return msg;
		}
		msg = validarRenda(seg.getRenda());
		if (msg != null) {
			return msg;
		}
		return null;
	}
}
