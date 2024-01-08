package br.com.arius.levantamento.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import br.com.arius.levantamento.model.QrProdutosVO;
import br.com.arius.levantamento.model.infoLoginVO;
import br.com.arius.levantamento.oracle.Oracle;
import br.com.arius.levantamento.view.frmLevantamentoFornecedor;

public class QrProdutos {

	// LISTA A PESQUISA DE PRODUTOS
	public static List<QrProdutosVO> listaPesqProdutos() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean temSql = false;

		String pIdProduto = frmLevantamentoFornecedor.txtIdProduto.getText();
		String pDataInicialVenda = frmLevantamentoFornecedor.txtDataInicialVenda.getText();
		String pDataFinalVenda = frmLevantamentoFornecedor.txtDataFinalVenda.getText();
		String pDataIncialCompra = frmLevantamentoFornecedor.txtDataInicialCompra.getText();
		String pDataFinalCompra = frmLevantamentoFornecedor.txtDataFinalCompra.getText();
		String pIdFornecedor = frmLevantamentoFornecedor.txtIdFornecedor.getText();

		// MONTO MEU ARRAYLIST
		List<QrProdutosVO> pesqListaProdutos = new ArrayList<>();

		try {
			conn = Oracle.conectar();
			String sql = "select distinct"
					+ " pf.referencia ref, pr.id, substr(pr.descritivo,1,50) produto1, substr(pr.descritivo,31,60)produto2, pp.venda,"
					+ " ' ' as sugt,' ' as sugtforn,"
					+ " (select qtde_embalageme from vw_tabela_fornecedor where produto = pr.id and estado = 'SP' and rownum =1)qtde_embalagem,"

					+ " (select round(custo,3) from vw_produtosa_precos pp  where pp.id = pr.id and pp.tipo_venda = 1 and pp.estado = 'SP' and politica = 1 ) custo_tabela2,"
					+ " (select round(tabela,3)  from tabela_fornecedor_uf  pp where pp.produto = pr.id	and fornecedor = '"
					+ pIdFornecedor + "' and pp.estado = 'SP' ) custo_tabela, "
					
+ " ( select (tfu.tabela / tf.qtde_embalageme ) from tabela_fornecedor tf, tabela_fornecedor_uf tfu where tf.produto = tfu.produto and tf.fornecedor = tfu.fornecedor and tf.fornecedor = '" + pIdFornecedor + "' and tfu.estado = 'SP' and tf.produto = pr.id ) custo_tabela_unitario, "

					+ " (select ped.custo from  " + " (select max(datahora_cadastro) data, custo, produto, qtde  from("
					+ " select p.id, pi.pedido, pi.produto, p.empresa, p.datahora_cadastro, pi.custo , pi.qtde_entrada qtde"
					+ " from pedidos_itens pi, pedidos p where  pi.pedido = p.id and " + " pi.produto = '" + pIdProduto
					+ "'" + " and p.empresa >= " + infoLoginVO.pIdEmpresa + "" + " order by p.datahora_cadastro desc)"
					+ " group by custo, produto, qtde) ped" + " where" + " ped.produto = pr.id and" + " rownum = 1 "
					+ " )custo_ult_enrada_ped2,"

					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id )sg_total,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 1 )sg_gta1,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 2 )sg_crz,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 3 )sg_lor1,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 4 )sg_taub1,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 5 )sg_lor2,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 6 )sg_pinda,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 8 )sg_sjc,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 9 )sg_cacap,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 11 )sg_taub2,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 15 )sg_jac,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 16 )sg_sjchumel,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 17 )sg_buriti,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 18 )sg_caraguatatuba,"
					+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where produto = pr.id and empresa = 19 )sg_guara,"

					+ " (select ped.data from" + " (select max(datahora_cadastro) data, custo, produto, qtde  from("
					+ " select p.id, pi.pedido, pi.produto, p.empresa, p.datahora_cadastro, pi.custo , pi.qtde_entrada qtde"
					+ " from pedidos_itens pi, pedidos p where  pi.pedido = p.id and" + " produto = '" + pIdProduto
					+ "' " + " and p.empresa >= " + infoLoginVO.pIdEmpresa + "" + " order by p.datahora_cadastro desc)"
					+ " group by custo, produto, qtde) ped" + " where" + " ped.produto = pr.id and "
					+ " rownum = 1 )data_ult_entrada_ped,"

					+ " (select ped.custo from  " + " (select max(datahora_cadastro) data, custo, produto, qtde  from("
					+ " select p.id, pi.pedido, pi.produto, p.empresa, p.datahora_cadastro, pi.custo , pi.qtde_entrada qtde"
					+ " from pedidos_itens pi, pedidos p where  pi.pedido = p.id and " + " pi.produto = '" + pIdProduto
					+ "'" + " and p.empresa >= " + infoLoginVO.pIdEmpresa + "" + " order by p.datahora_cadastro desc)"
					+ " group by custo, produto, qtde) ped" + " where" + " ped.produto = pr.id and" + " rownum = 1 "
					+ " )custo_ult_enrada_ped2,"

					+ " ( select SUM(pi.qtde_compra * pi.qtde_embalagem )qtde from  "
					+ "  pedidos_itens pi, pedidos p where  pi.pedido = p.id and " + " pi.produto = '" + pIdProduto
					+ "'" + " and p.empresa >= " + infoLoginVO.pIdEmpresa + " and"
					+ " trunc(p.datahora_cadastro) between '" + pDataIncialCompra + "' and '" + pDataFinalCompra
					+ "' and" + " pi.produto = pr.id" + " )qtdeCompra_ult_entrada_pedxxx,"
					+ " (select SUM(qtde) from vw_rpultimascompras where forn = '" + pIdFornecedor + "' and"
					+ " produto = pr.id and" + " empresa = " + infoLoginVO.pIdEmpresa + "" + " and entrada BETWEEN '"
					+ pDataIncialCompra + "' and '" + pDataFinalCompra + "' )ultcompra,"
					+ " ( select max(entrada) as entrada from vw_rpultimascompras where forn = '" + pIdFornecedor
					+ "' and" + "  produto = pr.id   and empresa >= " + infoLoginVO.pIdEmpresa + ""
					+ " ) dataultcompra,"
					+ " (select SUM(qtde_entrada) from cre_vi_ultimas_compras where  fornecedor = '" + pIdFornecedor
					+ "' and produto = pr.id and empresa >= " + infoLoginVO.pIdEmpresa + ""
					+ " and datahora_entrada BETWEEN '" + pDataIncialCompra + "' and '" + pDataFinalCompra
					+ "' )ultcompra_x,"
					+ " (select round(custo_anterior,3) as custo from tabela_fornecedor_uf where fornecedor = '"
					+ pIdFornecedor + "' and produto = pr.id and rownum = 1) custo,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque not in (7,10,14,199))total,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda
					+ "' and produto = pr.id and empresa in(2,3,5,1,99,6,4,11,9,8,15,16,17,18,19))vdtotal,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 2)crz,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 2)vdcrz,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 3)lor1,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 3)vdlor1,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 5)lor2,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 5)vdlor2,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 1)gta1,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 1)vdgta1,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 99)deposito,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 99)vddeposito,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 6)pinda,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 6)vdpinda,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 4)taub,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 4)vdtaub,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 11)taub2,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 11)vdtaub2,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 9)caçp,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 9)vdcaçp,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 8)sjc,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 8)vdsjc,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 15)jac,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 15)vdjac,"
					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 16)sjchumel,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 16)vdsjchumel,"

					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 17)buriti,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 17)vdburiti,"

					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 18)caraguatatuba,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 18)vdcaraguatatuba,"

					+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 18)guara,"
					+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda
					+ "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 18)vdguara"

					+ " from produtos_fornecedor pf, produtos pr, produtos_precos pp, produtos_loja pl" + " where"
					+ " pr.id in (select vd.produto from (select * from vendas_diarias" + " union all"
					+ " select * from vendas_diariasm ) vd where" + " vd.empresa >= " + infoLoginVO.pIdEmpresa + " and"
					+ " trunc(vd.data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and "
					+ " vd.tipovenda = 1 )" + " and" + " pf.produto = pr.id" + " and pp.produto = pr.id"
					+ " and pp.id = 1" + " and pl.id = pf.produto" + " and pp.politica   = " + infoLoginVO.pIdEmpresa
					+ "" + " and pl.politica   = " + infoLoginVO.pIdEmpresa + "" + " and pf.produto    = '" + pIdProduto
					+ "'" + " and pf.fornecedor = '" + pIdFornecedor + "'" + " and pr.status <> 3" // --EXCLUIDO NAO
					+ " order by produto1 ";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				// INSTANCIO MEU VO = TABELA
				temSql = true;
				QrProdutosVO pesProduto = new QrProdutosVO();

				pesProduto.setId(rs.getLong("id"));
				pesProduto.setRef(rs.getString("ref"));
				pesProduto.setProduto1(rs.getString("produto1"));
				pesProduto.setVenda(rs.getString("venda"));
				pesProduto.setCusto_tabela(rs.getString("custo_tabela"));
				pesProduto.setCusto_ult_enrada_ped2(rs.getString("custo_ult_enrada_ped2"));
				pesProduto.setTotal(rs.getString("total"));
				pesProduto.setGta1(rs.getString("gta1"));
				pesProduto.setVdgta1(rs.getString("vdgta1"));
				pesProduto.setLor1(rs.getString("lor1"));
				pesProduto.setVdlor1(rs.getString("vdlor1"));
				pesProduto.setLor2(rs.getString("lor2"));
				pesProduto.setVdlor2(rs.getString("vdlor2"));
				pesProduto.setCAÇP(rs.getString("CAÇP"));
				pesProduto.setVDCAÇP(rs.getString("VDCAÇP"));
				pesProduto.setSjc(rs.getString("sjc"));
				pesProduto.setVdsjc(rs.getString("vdsjc"));
				pesProduto.setCrz(rs.getString("crz"));
				pesProduto.setVdcrz(rs.getString("vdcrz"));
				pesProduto.setPinda(rs.getString("pinda"));
				pesProduto.setVdpinda(rs.getString("vdpinda"));
				pesProduto.setTaub(rs.getString("taub"));
				pesProduto.setVdtaub(rs.getString("vdtaub"));
				pesProduto.setTaub2(rs.getString("taub2"));
				pesProduto.setVdtaub2(rs.getString("vdtaub2"));
				pesProduto.setJac(rs.getString("jac"));
				pesProduto.setVdjac(rs.getString("vdjac"));
				pesProduto.setSjchumel(rs.getString("sjchumel"));
				pesProduto.setVdsjchumel(rs.getString("vdsjchumel"));
				pesProduto.setBuriti(rs.getString("buriti"));
				// pesVendidos.setVdburiti(rs.getString("vdburiti"));

				pesqListaProdutos.add(pesProduto);
			}

			if (!temSql) {
				JOptionPane.showMessageDialog(null, "Sem dados para listar.", "Atencao", JOptionPane.ERROR_MESSAGE);
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

		return pesqListaProdutos;
	}

}
