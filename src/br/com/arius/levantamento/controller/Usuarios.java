package br.com.arius.levantamento.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import br.com.arius.levantamento.oracle.Oracle;

public class Usuarios {

	/* LOGIN */
	public String logar(String pIdUsuario, String pSenha) throws Exception {
		String retorno = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean temSql = new Boolean(false);

		try {
			conn = Oracle.conectar();
			String pSenhaCripto = cripto(pSenha);

			String sql = "select * from bas_t_usuarios where id_usuario = ?  and senha = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pIdUsuario.toUpperCase());
			stmt.setString(2, pSenhaCripto);

			rs = stmt.executeQuery();

			while (rs.next()) {
				temSql = true;
				retorno = rs.getString("id_usuario");

			}
			if (!temSql) {
				JOptionPane.showMessageDialog(null, "Usuário ou senha inválido", "Atencao", JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao logar" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}

		return retorno;

	}

	/* CRIPTOGRAFA A SENHA PARA FAZER IDENTIFICACAO COM O BANCO */
	public String cripto(String pSenha) throws Exception {
		String retorno = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Oracle.conectar();
			String sql = "select rawtohex(dbms_crypto.hash(utl_i18n.string_to_raw(nvl(?, '?'), 'AL32UTF8'), 2)) as cripto from dual";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pSenha);

			rs = stmt.executeQuery();

			while (rs.next()) {
				retorno = rs.getString("cripto");

			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao logar" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}

		return retorno;

	}

}
