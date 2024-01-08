package br.com.arius.levantamento.oracle;

public class Excecoes extends Exception {

	private static final long serialVersionUID = 1L;

	public Excecoes(String mensagem, Exception e) {
		super(mensagem, e);
	}

	public Excecoes(String mensagem) {
		super(mensagem);
	}

	public static void print(Exception e, String mensagem) {
		System.out.println(mensagem);
	}

	public void print() {
		System.out.println(getMessage());
	}
}
