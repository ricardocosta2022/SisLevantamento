package br.com.arius.levantamento.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.arius.levantamento.model.QrTrocaVO;
import br.com.arius.levantamento.oracle.Oracle;
import br.com.arius.levantamento.view.frmLevantamentoFornecedor;

public class QrTroca {

	// LISTA A PESQUISA DE TROCA
	public static List<QrTrocaVO> listatroca() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String pIdFornecedor = frmLevantamentoFornecedor.txtIdFornecedor.getText();

		// MONTO MEU ARRAYLIST
		List<QrTrocaVO> pesqListatroca = new ArrayList<>();

		try {
			conn = Oracle.conectar();
			String sql = "select vw.produto, vw.descritivo_produto, vw.estoque_atual" + " from vw_produtos_estoques vw"
					+ " where" + " vw.estoque = 1000"
					+ " and vw.produto in (select produto from produtos_fornecedor where fornecedor = '" + pIdFornecedor
					+ "' )" + " and vw.estoque_atual > 0";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				QrTrocaVO pestroca = new QrTrocaVO();
				pestroca.setProduto(rs.getInt("produto"));
				pestroca.setDescritivo_produto(rs.getString("descritivo_produto"));
				pestroca.setEstoque_atual(rs.getString("estoque_atual"));
				pesqListatroca.add(pestroca);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao Listar " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}

		return pesqListatroca;
	}

}
