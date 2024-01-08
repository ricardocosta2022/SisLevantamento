package br.com.arius.levantamento.oracle;

//CLASSE UTIL PARA RETORNAR O SISTEMA OPERACIONAL 
public class SistemaOperacional {

	// PARA RETORNAR O SISTEMA OPERACIONAL.
	public String so() {
		String retorno = "";
		retorno = System.getProperty("os.name");
		retorno = retorno.toUpperCase();

		if (retorno.indexOf("WINDOWS") > -1) {
			retorno = "WINDOWS";
		}
		return retorno;
	}
}
