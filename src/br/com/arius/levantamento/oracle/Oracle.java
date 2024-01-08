package br.com.arius.levantamento.oracle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

//CLASSE QUE FAZ CONEXAO COM O BANCO DE DADOS ORACLE. USANDO POOL  DBCP DO APACHE
public class Oracle {

	private static BasicDataSource dataSource = null;
	private static Properties props = null;
	public static Connection conexao = null;

	static {
		FileInputStream fis = null;
		try {

			File file = new File("C:\\Arius\\AriusLevantamento\\AriusLevantamento.cfg");
			fis = new FileInputStream(file);
			props = new Properties();
			props.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro de banco" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			props = null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
					e.printStackTrace();
				}
			}
		}
	}

	// @Deprecated
	public static Connection conectar() throws SQLException {
		if (props == null) {
			JOptionPane.showMessageDialog(null,
					"Arquivo de configuracao nao foi encontrado ou esta faltando configuracoes!", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}

		if (props.getProperty("ORACLE_HOST").equals("")) {
			JOptionPane.showMessageDialog(null, "Faltando endereco do servidor nas configuracoes do arquivo.", "Erro",
					JOptionPane.ERROR_MESSAGE);
		} else {

			if (dataSource == null) {
				try {
					dataSource = new BasicDataSource();
					dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
					dataSource.setUrl("jdbc:oracle:thin:@" + props.getProperty("ORACLE_HOST") + ":"
							+ props.getProperty("ORACLE_PORT") + ":" + props.getProperty("ORACLE_SERVICE"));
					// dataSource.setUsername("arius");
					// dataSource.setPassword("automa");
					dataSource.setUsername(props.getProperty("ORACLE_USER")); // DA FORMA QUE VEJO NO CFG -- USUARIO
					dataSource.setPassword(props.getProperty("ORACLE_PASSWORD")); // DA FORMA QUE VEJO NO CFG -- SENHA
					dataSource.setMaxActive(100); // MAXIMO DE CONEXOES ATIVAS
					dataSource.setMaxIdle(5); // MAXIMO DE CONEXOES OCIOSAS
					dataSource.setDefaultAutoCommit(true); // FOR�A COMMIT.
					dataSource.setMaxWait(0); // TEMPO MAXIMO DE ESPERA P/ CONECTAR.
					dataSource.setTestOnReturn(true); // TESTA A CONEXAO ANTES DE DEVOLVER
					dataSource.setTestWhileIdle(false); // IMPEDE O TESTE DE CONEXAO OCIOSA
					dataSource.setValidationQuery("select * from dual"); // SQL PARA TESTE
					dataSource.setRemoveAbandonedTimeout(10); // TEMPO CONSIDERADO ABANDONO DE CONEXAO.
					dataSource.setRemoveAbandoned(true); // SE CONSIDERADO ABANDONO DE CONEXAO, REMOVE CONEXAO.
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException(e);
				}
			}
		}
		return dataSource.getConnection();
	}

	// DECONECTA DO BANCO
	public static void desconectar(Connection conn) throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// DECONECTA DO BANCO
	public static void desconectar(Connection conn, Statement stmt) throws SQLException {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e);
		}

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// DECONECTA DO BANCO
	public static void desconectar(Connection conn, CallableStatement pstmt) throws SQLException {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e);
		}

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// DECONECTA DO BANCO
	public static void desconectar(PreparedStatement stmt, CallableStatement pstmt, Connection conn)
			throws SQLException {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ENCERRA O POOL DE CONEXOES. SO DEVE SER USANDO QUANDO FOR ENCERRAR O SERVIDOR
	// TOMCAT
	public static void closeAllConnections() throws Exception {
		System.out.println("Todas as conexoes do pool serao fechadas!");
		try {
			if (dataSource != null) {
				dataSource.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na conexao de banco: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
