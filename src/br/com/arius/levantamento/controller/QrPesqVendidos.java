package br.com.arius.levantamento.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.arius.levantamento.model.QrPesqcVO;
import br.com.arius.levantamento.model.infoLoginVO;
import br.com.arius.levantamento.oracle.Oracle;
import br.com.arius.levantamento.view.frmLevantamentoFornecedor;

public class QrPesqVendidos {
	
	//LISTA A PESQUISA DE VENDIDOS
		public static List<QrPesqcVO> listaPesqVendidos() throws SQLException {
			Connection conn        = null;
			PreparedStatement stmt = null;
			ResultSet rs           = null;
			Boolean temSql         = false;

			String pIdFornecedor     = frmLevantamentoFornecedor.txtIdFornecedor.getText();
			String pDataInicialVenda = frmLevantamentoFornecedor.txtDataInicialVenda.getText();
			String pDataFinalVenda   = frmLevantamentoFornecedor.txtDataFinalVenda.getText();
			@SuppressWarnings("unused")
			String pDataIncialCompra = frmLevantamentoFornecedor.txtDataInicialCompra.getText();
			@SuppressWarnings("unused")
			String pDataFinalCompra  = frmLevantamentoFornecedor.txtDataFinalCompra.getText();
			
			// MONTO MEU ARRAYLIST
			List<QrPesqcVO> pesqLista = new ArrayList<>();

			try {
				conn       = Oracle.conectar();
                String sql = " select pf.referencia ref, pr.id , substr(pr.descritivo,1,50) produto1, substr(pr.descritivo,31,60)produto2,  pp.venda, ' ' as sugt, ' ' as sugtforn, "
                		+ " (select round(custo,3) from vw_produtosa_precos pp where pp.id = pr.id and pp.tipo_venda = 1 and pp.estado = 'SP' and politica = 1 ) custo_tabela2,  "
                		+ " (select round(tabela,3)  from tabela_fornecedor_uf  pp where pp.produto = pr.id	and fornecedor = '" +  pIdFornecedor + "' and pp.estado = 'SP' ) custo_tabela, "
                		+ " ( select (tfu.tabela / tf.qtde_embalageme ) from tabela_fornecedor tf, tabela_fornecedor_uf tfu where tf.produto = tfu.produto and tf.fornecedor = tfu.fornecedor and tf.fornecedor = '" + pIdFornecedor + "' and tfu.estado = 'SP' and tf.produto = pr.id ) custo_tabela_unitario, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa in(2,3,5,1,99,6,4,11,9,8,15,16,17,18,19))Sg_total, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 1 )sg_gta1, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 2 )sg_crz, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 3 )sg_lor1, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 4 )sg_taub1,  "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 5 )sg_lor2,  "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 6 )sg_pinda, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 8 )sg_sjc, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 9 )sg_cacap, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 11 )sg_taub2, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 15 )sg_jac,  "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 16 )sg_sjcHumel,  "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 17 )sg_buriti, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 18 )sg_caraguatatuba, "
                		+ " (select nvl(sum(scompra),0) from vw_sugestao_compra where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa = 19 )sg_guara, "
                		+ " (select qtde_embalageme from vw_tabela_fornecedor where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and estado = 'SP')qtde_embalagem, "
                		
                		+ " (select ped.data from "
                		+ " (select max(datahora_cadastro) data, custo, produto, qtde  "
                		+ " from(  select p.id, pi.pedido, pi.produto, p.empresa, p.datahora_cadastro, pi.custo , pi.qtde_entrada qtde  "
                		+ " from pedidos_itens pi, pedidos p where  pi.pedido = p.id and p.fornecedor  = '"+ pIdFornecedor +"' and p.empresa >= " + infoLoginVO.pIdEmpresa + "  order by p.datahora_cadastro desc)  "
                		+ " group by custo, produto, qtde) ped  "
                		+ " where  ped.produto = pr.id and  rownum = 1 )data_ult_entrada_ped, "
                		
                		
                		+ " (select ped.custo "
                		+ " from  (select max(datahora_cadastro) data, custo, produto, qtde "
                		+ " from( select p.id, pi.pedido, pi.produto, p.empresa, p.datahora_cadastro, pi.custo , pi.qtde_entrada qtde  from pedidos_itens pi, pedidos p where  pi.pedido = p.id and p.fornecedor  = '"+ pIdFornecedor +"'  "
                		+ " and p.empresa = " + infoLoginVO.pIdEmpresa + " order by p.datahora_cadastro desc)  group by custo, produto, qtde) ped  "
                		+ " where  ped.produto = pr.id and  rownum = 1 )custo_ult_enrada_ped2,  "
                		
                		+ " (select sum(qtde) "
                		+ " from( select p.id, trunc(p.datahora_cadastro) data, sum(pi.qtde_entrada) qtde, p.empresa, pi.produto  "
                		+ " from pedidos_itens pi, pedidos p "
                		+ " where pi.pedido = p.id and p.datahora_entrega is not null and pi.datahora_entrada is not null and p.fornecedor = '"+ pIdFornecedor +"'  "
                		+ " and  trunc(p.datahora_cadastro) in (select max(trunc(datahora_cadastro)) data from pedidos  "
                		+ " where fornecedor = '"+ pIdFornecedor +"'  "
                		+ " and trunc(datahora_cadastro) between  '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' ) group by p.id, trunc(p.datahora_cadastro), p.empresa, pi.produto) where produto = pr.id )  "
                		+ " qtdeCompra_ult_entrada_ped, "
                		
                		+ " (select SUM(qtde_entrada) from cre_vi_ultimas_compras where fornecedor = '"+ pIdFornecedor +"'  "
                		+ " and produto = pr.id and empresa >= " + infoLoginVO.pIdEmpresa + " "
                		+ " and datahora_entrada BETWEEN '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' )ultcompra_x,  "
                		
                		+ " (select datahora_entrada from cre_vi_ultimas_compras where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa in(99,1,16) and datahora_entrada in(select max(datahora_entrada) from cre_vi_ultimas_compras   "
                		+ "     where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and empresa in(99,1,16)) and rownum = 1)dataultcompra, "
                		
                		+ " (select round(custo_anterior,3) as custo from tabela_fornecedor_uf where fornecedor = '"+ pIdFornecedor +"' and produto = pr.id and rownum = 1) custo, "
                		
                		+ "(select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque not in (7,10,14,199))total, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "'  and produto = pr.id and empresa in(2,3,5,1,99,6,4,11,9,8,15,16,17,18,19))vdtotal,  "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 2)crz, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 2)vdcrz, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 3)lor1, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 3)vdlor1, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 5)lor2, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 5)vdlor2,  "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 1)gta1, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 1)vdgta1, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 99)deposito,  "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 99)vddeposito,  "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 6)pinda,  "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 6)vdpinda,  "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 4)taub, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 4)vdtaub, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 11)taub2, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 11)vdtaub2, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 9)caçp, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 9)vdcaçp, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 8)sjc, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 8)vdsjc, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 15)jac, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 15)vdjac, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 16)sjchumel, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 16)vdsjchumel, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 17)buriti, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 17)vdburiti, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 18)caraguatatuba, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 18)vdcaraguatatuba, "
                		+ " (select nvl(sum(estoque_atual),0) from produtos_estoques where produto = pr.id and estoque = 19)guara, "
                		+ " (select nvl(sum(qtde),0) from vendas_diarias where trunc(data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and produto = pr.id and empresa = 19)vdguara "
                		
                		+ " from produtos_fornecedor pf,"
                		+ " produtos pr,"
                		+ " produtos_precos pp,"
                		+ " produtos_loja pl "
                		+ " where pr.id in (select vd.produto from "
                		+ "                  (select * from vendas_diarias union all select * from vendas_diariasm  ) vd "
                		+ " where vd.empresa >= " + infoLoginVO.pIdEmpresa + " and  trunc(vd.data) between '" + pDataInicialVenda + "' and '" + pDataFinalVenda + "' and  vd.tipovenda = 1 ) "
                		+ " and pf.produto = pr.id  "
                		+ " and pp.produto = pr.id  "
                		+ " and pp.id = 1 "
                		+ " and pl.id = pf.produto "
                		+ " and pp.politica =   " + infoLoginVO.pIdEmpresa + " "
                		+ " and pl.politica =   " + infoLoginVO.pIdEmpresa + " "
                		+ " and pf.fornecedor = '"+ pIdFornecedor +"' " 
                		+ " and pr.status <> 3 order by produto1 ";
										
	  
				stmt = conn.prepareStatement(sql);
				rs   = stmt.executeQuery();

				while (rs.next()) {
					// INSTANCIO MEU VO = TABELA
					temSql = true;
					QrPesqcVO pesVendidos = new QrPesqcVO();
					pesVendidos.setId(rs.getLong("id"));
					pesVendidos.setRef(rs.getString("ref"));
					pesVendidos.setProduto1(rs.getString("produto1"));
					pesVendidos.setVenda(rs.getString("venda"));
					pesVendidos.setCusto_tabela_unitario(rs.getString("custo_tabela_unitario"));
					pesVendidos.setCusto_ult_enrada_ped2(rs.getString("custo_ult_enrada_ped2"));
					pesVendidos.setTotal(rs.getString("total"));
					pesVendidos.setGta1(rs.getString("gta1"));
					pesVendidos.setVdgta1(rs.getString("vdgta1"));
					pesVendidos.setLor1(rs.getString("lor1"));
					pesVendidos.setVdlor1(rs.getString("vdlor1"));
					pesVendidos.setLor2(rs.getString("lor2"));
					pesVendidos.setVdlor2(rs.getString("vdlor2"));
					pesVendidos.setCAÇP(rs.getString("CAÇP"));
					pesVendidos.setVDCAÇP(rs.getString("VDCAÇP"));
					pesVendidos.setSjc(rs.getString("sjc"));
					pesVendidos.setVdsjc(rs.getString("vdsjc"));
					pesVendidos.setCrz(rs.getString("crz"));
					pesVendidos.setVdcrz(rs.getString("vdcrz"));
					pesVendidos.setPinda(rs.getString("pinda"));
					pesVendidos.setVdpinda(rs.getString("vdpinda"));
					pesVendidos.setTaub(rs.getString("taub"));
					pesVendidos.setVdtaub(rs.getString("vdtaub"));
					pesVendidos.setTaub2(rs.getString("taub2"));
					pesVendidos.setVdtaub2(rs.getString("vdtaub2"));
					pesVendidos.setJac(rs.getString("jac"));
					pesVendidos.setVdjac(rs.getString("vdjac"));
					pesVendidos.setSjchumel(rs.getString("sjchumel"));
					pesVendidos.setVdsjchumel(rs.getString("vdsjchumel"));
					pesVendidos.setBuriti(rs.getString("buriti"));
					
					pesqLista.add(pesVendidos);
				}

				if(!temSql) {
					JOptionPane.showMessageDialog(null, "Sem dados para listar." , "Atencao", JOptionPane.ERROR_MESSAGE);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Erro ao Listar " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			} finally	{
				if(rs!=null) {
					rs.close();
				}
				Oracle.desconectar(conn, stmt);
			}

			return pesqLista; 
		} 

}
