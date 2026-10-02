package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
	private ApoliceDAO dao = new ApoliceDAO();

	protected Class getClasse() {
		return Apolice.class;
	}

	private Apolice criar(String numero, String premio) {
		Apolice ap = new Apolice(null, new BigDecimal("1500.00"),
				new BigDecimal(premio), new BigDecimal("50000.00"));
		ap.setNumero(numero);
		return ap;
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, "3000.00"), numero);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNotNull(ap);
	}

	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, "3000.00"), numero);
		Apolice ap = dao.buscar("11000000");
		Assertions.assertNull(ap);
	}

	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, "3000.00"), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
		Assertions.assertNull(dao.buscar(numero));
	}

	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, "3000.00"), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, "3000.00"));
		Assertions.assertTrue(ret);
		Assertions.assertNotNull(dao.buscar(numero));
	}

	@Test
	public void teste06() {
		String numero = "50000000";
		Apolice ap = criar(numero, "3000.00");
		cadastro.incluir(ap, numero);
		boolean ret = dao.incluir(ap);
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste07() {
		String numero = "60000000";
		boolean ret = dao.alterar(criar(numero, "3000.00"));
		Assertions.assertFalse(ret);
		Assertions.assertNull(dao.buscar(numero));
	}

	@Test
	public void teste08() {
		String numero = "70000000";
		cadastro.incluir(criar(numero, "3000.00"), numero);
		boolean ret = dao.alterar(criar(numero, "4000.00"));
		Assertions.assertTrue(ret);
		Assertions.assertEquals(new BigDecimal("4000.00"), dao.buscar(numero).getValorPremio());
	}
}
