package br.com.arius.levantamento.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import br.com.arius.levantamento.oracle.Oracle;
import br.com.arius.levantamento.oracle.SistemaOperacional;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

//CLASSE RESPONSAVEL PELA GERACAO DO RELATORIO .JASPER.
@SuppressWarnings("deprecation")
public class SisGeraRelatorioQrPesqVendidos {

	// OBTEM O CAMINHO DOS RELATORIOS
	private String getPathRelatorios() throws Exception {
		String retorno = "";
		SistemaOperacional so = new SistemaOperacional();

		if (so.so().equals("WINDOWS")) {
			retorno = "C:/Arius/AriusLevantamento/relatorios/"; // WINDOWS.
		} else {
			retorno = "/Arius/AriusLevantamento/relatorios/"; // LINUX
		}

		return retorno;
	}

	// GERA O RELATORIO \s
	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	// String p_IdUsuario,
	public void geraRelatorioQrPesqVendidos(Integer p_IdEmpresa, String pIdFornecedor, String p_dataIniVenda,
			String p_dataFinVenda, String pDataIncialCompra, String pDataFinalCompra) throws Exception {

		try {
			String jrxml = getPathRelatorios() + "layouts/creLevantamentovendidos" + ".jrxml";
			String pdf = getPathRelatorios() + "gerados/creLevantamentovendidos" + ".pdf";
			String jasper = JasperCompileManager.compileReportToFile(jrxml);

			System.out.println("### " + pdf);
			System.out.println("Relatório compilado com êxito."); // crelevantamento.jasper

			HashMap paramsRel = new HashMap();

			paramsRel.put("pIdEmpresa", p_IdEmpresa);
			paramsRel.put("pIdFornecedor", pIdFornecedor);
			paramsRel.put("pdataIniVenda", p_dataIniVenda);
			paramsRel.put("pdataFinVenda", p_dataFinVenda);
			paramsRel.put("pDataIncialCompra", pDataIncialCompra);
			paramsRel.put("pDataFinalCompra", pDataFinalCompra);

			Connection conexao = Oracle.conectar();

			JasperPrint print = JasperFillManager.fillReport(jasper, paramsRel, conexao);

			OutputStream saida = new FileOutputStream(pdf);

			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, saida);

			// GERA O RELATÓRIO NO FLUXO DE SAÍDA
			exporter.exportReport();

			System.out.println("Relatório " + "Levantamento" + ".pdf gerado com sucesso em " + getPathRelatorios());

//			Runtime.getRuntime().exec("cmd.exe /C " + pdf);
//			Runtime.getRuntime().exec("C:\\Arius\\AriusEtiqueta\\relatorios\\gerados\\crelevantamento.pdf");

			String comando = "C:\\Arius\\AriusLevantamento\\relatorios\\gerados\\creLevantamentovendidos.pdf";
			try {
				Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + comando);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			
//	        Desktop desktop = Desktop.getDesktop();  
//	        desktop.open(new File("C:\\Arius\\AriusEtiqueta\\relatorios\\gerados\\creetiquetas.pdf"));

			System.out.println(pdf);
			JOptionPane.showMessageDialog(null, "Levantamento criado com sucesso", "Impresso",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Oracle.desconectar(null);
		}
	}

}
