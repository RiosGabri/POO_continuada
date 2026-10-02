package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TesteSinistroDAO extends TesteDAO {
	private SinistroDAO dao = new SinistroDAO();

	protected Class getClasse() {
		return Sinistro.class;
	}

	private Sinistro criar(String numero, TipoSinistro tipo) {
		return new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
				"usuario", new BigDecimal("1000.00"), tipo);
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}

	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar("11000000");
		Assertions.assertNull(si);
	}

	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, TipoSinistro.COLISAO), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
		Assertions.assertNull(dao.buscar(numero));
	}

	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, TipoSinistro.COLISAO), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, TipoSinistro.COLISAO));
		Assertions.assertTrue(ret);
		Assertions.assertNotNull(dao.buscar(numero));
	}

	@Test
	public void teste06() {
		String numero = "50000000";
		Sinistro si = criar(numero, TipoSinistro.COLISAO);
		cadastro.incluir(si, numero);
		boolean ret = dao.incluir(si);
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste07() {
		String numero = "60000000";
		boolean ret = dao.alterar(criar(numero, TipoSinistro.COLISAO));
		Assertions.assertFalse(ret);
		Assertions.assertNull(dao.buscar(numero));
	}

	@Test
	public void teste08() {
		String numero = "70000000";
		cadastro.incluir(criar(numero, TipoSinistro.COLISAO), numero);
		boolean ret = dao.alterar(criar(numero, TipoSinistro.FURTO));
		Assertions.assertTrue(ret);
		Assertions.assertEquals(TipoSinistro.FURTO, dao.buscar(numero).getTipo());
	}
}
