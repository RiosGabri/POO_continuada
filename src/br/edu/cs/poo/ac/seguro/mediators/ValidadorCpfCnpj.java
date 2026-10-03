package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {
	public static boolean ehCnpjValido(String cnpj) {
		if (StringUtils.temSomenteNumeros(cnpj) && cnpj.length() == 14 && cnpj.chars().distinct().count() > 1) {
			int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
			int soma1 = 0;
			int soma2 = 0;
			for (int i = 0; i < 12; i++) {
				int dig = cnpj.charAt(i) - '0';
				soma1 += dig * pesos[i + 1];
				soma2 += dig * pesos[i];
			}
			int d1 = soma1 % 11 < 2 ? 0 : 11 - soma1 % 11;
			soma2 += d1 * pesos[12];
			int d2 = soma2 % 11 < 2 ? 0 : 11 - soma2 % 11;
			return d1 == cnpj.charAt(12) - '0' && d2 == cnpj.charAt(13) - '0';
		}
		return false; 
	}
	public static boolean ehCpfValido(String cpf) {
		if (StringUtils.temSomenteNumeros(cpf) && cpf.length() == 11 && cpf.chars().distinct().count() > 1) {
			int soma1 = 0;
			int soma2 = 0;
			for (int i = 0; i < 9; i++) {
				int dig = cpf.charAt(i) - '0';
				soma1 += dig * (10 - i);
				soma2 += dig * (11 - i);
			}
			int d1 = soma1 % 11 < 2 ? 0 : 11 - soma1 % 11;
			soma2 += d1 * 2;
			int d2 = soma2 % 11 < 2 ? 0 : 11 - soma2 % 11;
			return d1 == cpf.charAt(9) - '0' && d2 == cpf.charAt(10) - '0';
		}
		return false; 
	}
}
