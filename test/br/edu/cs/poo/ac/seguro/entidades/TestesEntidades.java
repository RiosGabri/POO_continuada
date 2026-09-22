package br.edu.cs.poo.ac.seguro.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TestesEntidades {

    @Test
    void testarTipoSinistro() {
        assertEquals(1, TipoSinistro.COLISAO.getCodigo());
        assertEquals("Colisão", TipoSinistro.COLISAO.getNome());

        assertEquals(2, TipoSinistro.INCENDIO.getCodigo());
        assertEquals("Incêndio", TipoSinistro.INCENDIO.getNome());

        assertEquals(3, TipoSinistro.FURTO.getCodigo());
        assertEquals("Furto", TipoSinistro.FURTO.getNome());

        assertEquals(4, TipoSinistro.ENCHENTE.getCodigo());
        assertEquals("Enchente", TipoSinistro.ENCHENTE.getNome());

        assertEquals(5, TipoSinistro.DEPREDACAO.getCodigo());
        assertEquals("Depredação", TipoSinistro.DEPREDACAO.getNome());

        assertSame(TipoSinistro.COLISAO, TipoSinistro.getTipoSinistro(1));
        assertSame(TipoSinistro.INCENDIO, TipoSinistro.getTipoSinistro(2));
        assertSame(TipoSinistro.FURTO, TipoSinistro.getTipoSinistro(3));
        assertSame(TipoSinistro.ENCHENTE, TipoSinistro.getTipoSinistro(4));
        assertSame(TipoSinistro.DEPREDACAO, TipoSinistro.getTipoSinistro(5));
    }

    @Test
    void testarEndereco() {
        Endereco endereco = new Endereco(
                "Rua A",
                "50000-000",
                "100",
                "Apto 101",
                "Brasil",
                "PE",
                "Recife"
        );

        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("50000-000", endereco.getCep());
        assertEquals("100", endereco.getNumero());
        assertEquals("Apto 101", endereco.getComplemento());
        assertEquals("Brasil", endereco.getPais());
        assertEquals("PE", endereco.getEstado());
        assertEquals("Recife", endereco.getCidade());

        endereco.setLogradouro("Rua B");
        endereco.setCep("51000-000");
        endereco.setNumero("200");
        endereco.setComplemento("Casa");
        endereco.setPais("Brasil");
        endereco.setEstado("SP");
        endereco.setCidade("São Paulo");

        assertEquals("Rua B", endereco.getLogradouro());
        assertEquals("51000-000", endereco.getCep());
        assertEquals("200", endereco.getNumero());
        assertEquals("Casa", endereco.getComplemento());
        assertEquals("Brasil", endereco.getPais());
        assertEquals("SP", endereco.getEstado());
        assertEquals("São Paulo", endereco.getCidade());
    }

    @Test
    void testarSegurado() {
        Endereco endereco = criarEndereco();
        LocalDate dataCriacao = LocalDate.now().minusYears(25);
        BigDecimal bonus = new BigDecimal("100.00");

        Segurado segurado = new Segurado(
                "Gabriel",
                endereco,
                dataCriacao,
                bonus
        );

        assertEquals("Gabriel", segurado.getNome());
        assertSame(endereco, segurado.getEndereco());
        assertEquals(dataCriacao, segurado.getDataCriacao());
        assertEquals(bonus, segurado.getBonus());

        segurado.setNome("João");
        Endereco novoEndereco = criarEndereco();
        segurado.setEndereco(novoEndereco);

        assertEquals("João", segurado.getNome());
        assertSame(novoEndereco, segurado.getEndereco());

        assertEquals(25, segurado.getIdade());

        segurado.creditarBonus(new BigDecimal("50.00"));
        assertEquals(
                new BigDecimal("150.00"),
                segurado.getBonus()
        );

        segurado.debitarBonus(new BigDecimal("30.00"));
        assertEquals(
                new BigDecimal("120.00"),
                segurado.getBonus()
        );
    }

    @Test
    void testarSeguradoPessoa() {
        Endereco endereco = criarEndereco();
        LocalDate dataNascimento = LocalDate.of(2000, 5, 10);

        SeguradoPessoa pessoa = new SeguradoPessoa(
                "Gabriel",
                endereco,
                dataNascimento,
                new BigDecimal("100.00"),
                "12345678900",
                5000.00
        );

        assertEquals("Gabriel", pessoa.getNome());
        assertSame(endereco, pessoa.getEndereco());
        assertEquals(dataNascimento, pessoa.getDataNascimento());
        assertEquals("12345678900", pessoa.getCpf());
        assertEquals(5000.00, pessoa.getRenda());

        LocalDate novaDataNascimento = LocalDate.of(2001, 6, 20);

        pessoa.setDataNascimento(novaDataNascimento);
        pessoa.setCpf("98765432100");
        pessoa.setRenda(7000.00);

        assertEquals(novaDataNascimento, pessoa.getDataNascimento());
        assertEquals("98765432100", pessoa.getCpf());
        assertEquals(7000.00, pessoa.getRenda());
    }

    @Test
    void testarSeguradoEmpresa() {
        Endereco endereco = criarEndereco();
        LocalDate dataAbertura = LocalDate.of(2010, 3, 15);

        SeguradoEmpresa empresa = new SeguradoEmpresa(
                "Empresa Teste",
                endereco,
                dataAbertura,
                new BigDecimal("200.00"),
                "12345678000199",
                1000000.00,
                true
        );

        assertEquals("Empresa Teste", empresa.getNome());
        assertSame(endereco, empresa.getEndereco());
        assertEquals(dataAbertura, empresa.getDataAbertura());
        assertEquals("12345678000199", empresa.getCnpj());
        assertEquals(1000000.00, empresa.getFaturamento());
        assertTrue(empresa.isEhLocadoraDeVeiculos());

        LocalDate novaDataAbertura = LocalDate.of(2015, 7, 20);

        empresa.setDataAbertura(novaDataAbertura);
        empresa.setCnpj("98765432000188");
        empresa.setFaturamento(2000000.00);
        empresa.setEhLocadoraDeVeiculos(false);

        assertEquals(novaDataAbertura, empresa.getDataAbertura());
        assertEquals("98765432000188", empresa.getCnpj());
        assertEquals(2000000.00, empresa.getFaturamento());
        assertFalse(empresa.isEhLocadoraDeVeiculos());
    }

    @Test
    void testarVeiculo() {
        SeguradoEmpresa empresa = criarEmpresa();
        SeguradoPessoa pessoa = criarPessoa();
        CategoriaVeiculo categoria = null;

        Veiculo veiculo = new Veiculo(
                "ABC-1234",
                2020,
                empresa,
                pessoa,
                categoria
        );

        assertEquals("ABC-1234", veiculo.getPlaca());
        assertEquals(2020, veiculo.getAno());
        assertSame(empresa, veiculo.getProprietarioEmpresa());
        assertSame(pessoa, veiculo.getProprietarioPessoa());
        assertEquals(categoria, veiculo.getCategoria());

        veiculo.setPlaca("XYZ-9876");
        veiculo.setAno(2022);
        veiculo.setProprietarioEmpresa(null);
        veiculo.setProprietarioPessoa(null);

        assertEquals("XYZ-9876", veiculo.getPlaca());
        assertEquals(2022, veiculo.getAno());
        assertEquals(null, veiculo.getProprietarioEmpresa());
        assertEquals(null, veiculo.getProprietarioPessoa());
    }

    @Test
    void testarApolice() {
        Veiculo veiculo = criarVeiculo();

        BigDecimal franquia = new BigDecimal("1500.00");
        BigDecimal premio = new BigDecimal("3000.00");
        BigDecimal valorMaximo = new BigDecimal("50000.00");

        Apolice apolice = new Apolice(
                veiculo,
                franquia,
                premio,
                valorMaximo
        );

        assertSame(veiculo, apolice.getVeiculo());
        assertEquals(franquia, apolice.getValorFranquia());
        assertEquals(premio, apolice.getValorPremio());
        assertEquals(valorMaximo, apolice.getValorMaximoSegurado());

        apolice.setVeiculo(null);
        apolice.setValorFranquia(new BigDecimal("2000.00"));
        apolice.setValorPremio(new BigDecimal("4000.00"));
        apolice.setValorMaximoSegurado(new BigDecimal("60000.00"));

        assertEquals(null, apolice.getVeiculo());
        assertEquals(
                new BigDecimal("2000.00"),
                apolice.getValorFranquia()
        );
        assertEquals(
                new BigDecimal("4000.00"),
                apolice.getValorPremio()
        );
        assertEquals(
                new BigDecimal("60000.00"),
                apolice.getValorMaximoSegurado()
        );
    }

    @Test
    void testarSinistro() {
        Veiculo veiculo = criarVeiculo();

        LocalDateTime dataHoraSinistro =
                LocalDateTime.of(2026, 9, 22, 10, 30);

        LocalDateTime dataHoraRegistro =
                LocalDateTime.of(2026, 9, 22, 11, 0);

        BigDecimal valorSinistro =
                new BigDecimal("7500.00");

        Sinistro sinistro = new Sinistro(
                "SIN-001",
                veiculo,
                dataHoraSinistro,
                dataHoraRegistro,
                "usuario",
                valorSinistro,
                TipoSinistro.COLISAO
        );

        assertEquals("SIN-001", sinistro.getNumero());
        assertSame(veiculo, sinistro.getVeiculo());
        assertEquals(dataHoraSinistro, sinistro.getDataHoraSinistro());
        assertEquals(dataHoraRegistro, sinistro.getDataHoraRegistro());
        assertEquals("usuario", sinistro.getUsuarioRegistro());
        assertEquals(valorSinistro, sinistro.getValorSinistro());
        assertEquals(TipoSinistro.COLISAO, sinistro.getTipo());

        sinistro.setNumero("SIN-002");
        sinistro.setVeiculo(null);
        sinistro.setDataHoraSinistro(
                LocalDateTime.of(2026, 9, 23, 12, 0)
        );
        sinistro.setDataHoraRegistro(
                LocalDateTime.of(2026, 9, 23, 13, 0)
        );
        sinistro.setUsuarioRegistro("novoUsuario");
        sinistro.setValorSinistro(new BigDecimal("8000.00"));
        sinistro.setTipo(TipoSinistro.FURTO);

        assertEquals("SIN-002", sinistro.getNumero());
        assertEquals(null, sinistro.getVeiculo());
        assertEquals(
                LocalDateTime.of(2026, 9, 23, 12, 0),
                sinistro.getDataHoraSinistro()
        );
        assertEquals(
                LocalDateTime.of(2026, 9, 23, 13, 0),
                sinistro.getDataHoraRegistro()
        );
        assertEquals("novoUsuario", sinistro.getUsuarioRegistro());
        assertEquals(
                new BigDecimal("8000.00"),
                sinistro.getValorSinistro()
        );
        assertEquals(TipoSinistro.FURTO, sinistro.getTipo());
    }

    private Endereco criarEndereco() {
        return new Endereco(
                "Rua A",
                "50000-000",
                "100",
                "Apto 101",
                "Brasil",
                "PE",
                "Recife"
        );
    }

    private SeguradoPessoa criarPessoa() {
        return new SeguradoPessoa(
                "Pessoa Teste",
                criarEndereco(),
                LocalDate.of(2000, 1, 1),
                new BigDecimal("100.00"),
                "12345678900",
                5000.00
        );
    }

    private SeguradoEmpresa criarEmpresa() {
        return new SeguradoEmpresa(
                "Empresa Teste",
                criarEndereco(),
                LocalDate.of(2010, 1, 1),
                new BigDecimal("100.00"),
                "12345678000199",
                1000000.00,
                true
        );
    }

    private Veiculo criarVeiculo() {
        return new Veiculo(
                "ABC-1234",
                2020,
                criarEmpresa(),
                criarPessoa(),
                null
        );
    }
}