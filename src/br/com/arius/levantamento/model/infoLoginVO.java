package br.com.arius.levantamento.model;

public class infoLoginVO {

	public static Integer pIdEmpresa;
	public static String pIdUsuario;

	public infoLoginVO(Integer pIdEmpresa, String pIdUsuario) {
		infoLoginVO.pIdEmpresa = pIdEmpresa;
		infoLoginVO.pIdUsuario = pIdUsuario;
	}

	public static Integer getpIdEmpresa() {
		return pIdEmpresa;
	}

	public static void setpIdEmpresa(Integer pIdEmpresa) {
		infoLoginVO.pIdEmpresa = pIdEmpresa;
	}

	public static String getpIdUsuario() {
		return pIdUsuario;
	}

	public static void setpIdUsuario(String pIdUsuario) {
		infoLoginVO.pIdUsuario = pIdUsuario;
	}

}
