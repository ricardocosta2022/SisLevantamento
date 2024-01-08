package br.com.arius.levantamento.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import br.com.arius.levantamento.controller.QrPesqVendidos;
import br.com.arius.levantamento.controller.QrPesqc;
import br.com.arius.levantamento.controller.QrProdutos;
import br.com.arius.levantamento.controller.QrTroca;
import br.com.arius.levantamento.controller.SisGeraRelatorioQrPesqVendidos;
import br.com.arius.levantamento.controller.SisGeraRelatorioQrPesqc;
import br.com.arius.levantamento.controller.SisGeraRelatorioQrProdutos;
import br.com.arius.levantamento.model.QrPesqcVO;
import br.com.arius.levantamento.model.QrProdutosVO;
import br.com.arius.levantamento.model.QrTrocaVO;
import br.com.arius.levantamento.model.infoLoginVO;
import br.com.arius.levantamento.oracle.Oracle;

@SuppressWarnings("serial")
public class frmLevantamentoFornecedor extends JFrame {

	private JPanel contentPane;
	public static JTextField txtIdFornecedor;
	public static JTextField txtIdProduto;
	public static JTextField txtDescFornecedor;
	public static JTextField txtDescProduto;
	public static JTextField txtDataInicialVenda;
	public static JTextField txtDataInicialCompra;
	public static JTextField txtDataFinalVenda;
	public static JTextField txtDataFinalCompra;
	private static JTable gridTrocas;
	private static JTable grid2;

	ButtonGroup grp = new ButtonGroup();

	/**
	 * INICIO DA APLICACAO.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmLevantamentoFornecedor frame = new frmLevantamentoFornecedor();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null); // CENTRALIZAR A TELA
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// METODO PARA TRAZER A DATA DO SISTEMA ATUAL
	public String MostraData() {

		Date data = new Date();
		SimpleDateFormat dformatador = new SimpleDateFormat("dd/MM/yyyy");
		String sData = dformatador.format(data);

		return sData;
	}

	// LISTA A PESQUISA PELO ID DO FORNECEDOR
	public static void listaPesqIdFornecedor() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean temSql = Boolean.FALSE;

		int pIdFornecedor = Integer.parseInt(txtIdFornecedor.getText());

		try {
			conn = Oracle.conectar();
			String sql = "select * from fornecedores where id like '" + pIdFornecedor + "'";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				temSql = true;
				txtIdFornecedor.setText(rs.getString("id"));
				txtDescFornecedor.setText(rs.getString("descritivo"));
			}

			if (!temSql) {
				JOptionPane.showMessageDialog(null, "Fornecedor não encontrado.", "Arius", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao Listar " + e.getMessage(), "Arius", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}
	}

	// LISTA A PESQUISA PELO ID DO PRODUTO
	public static void listaPesqIdProduto() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean temSql = Boolean.FALSE;

		String pIdProduto = txtIdProduto.getText();

		try {
			conn = Oracle.conectar();
			String sql = "select * from produtos where id  like '" + pIdProduto + "'";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				temSql = true;
				txtIdProduto.setText(rs.getString("id"));
				txtDescProduto.setText(rs.getString("descritivo"));
			}

			if (!temSql) {
				JOptionPane.showMessageDialog(null, "Produto não encontrado.", "Atencao", JOptionPane.ERROR_MESSAGE);
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
	}

	// METODO PARA CARREGAR O GRID 1 DURANTE A PESQUISA DE CONTROLE E VENDIDOS
	@SuppressWarnings("unused")
	private static void carregaGridTroca() throws Exception {

		DefaultTableModel modelo = (DefaultTableModel) gridTrocas.getModel();
		modelo.setNumRows(0); // INICIA O GRID LIMPO

		gridTrocas.getColumnModel().getColumn(0);
		gridTrocas.getColumnModel().getColumn(1);
		gridTrocas.getColumnModel().getColumn(2);

		// INSTANCIO MEU CONTROLER --
		QrTroca carregagrid1 = new QrTroca();
		try {
			for (QrTrocaVO ptroca : QrTroca.listatroca()) {
				modelo.addRow(
						new Object[] { ptroca.getProduto(), ptroca.getDescritivo_produto(), ptroca.getEstoque_atual(),

						});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carrega grid1 " + e.getMessage(), "Arius",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/// NA ACAO DO BOTAO PESQUISA CHAMA ESSE METODO E EXECUTA A CLASSE QrPesqc
	@SuppressWarnings("unused")
	private static void carregaGridControle() {

		DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
		modelo.setNumRows(0); // INICIA O GRID LIMPO

		grid2.getColumnModel().getColumn(0);
		grid2.getColumnModel().getColumn(1);
		grid2.getColumnModel().getColumn(2);
		grid2.getColumnModel().getColumn(3);
		grid2.getColumnModel().getColumn(4);
		grid2.getColumnModel().getColumn(5);
		grid2.getColumnModel().getColumn(6);
		grid2.getColumnModel().getColumn(7);
		grid2.getColumnModel().getColumn(8);
		grid2.getColumnModel().getColumn(9);
		grid2.getColumnModel().getColumn(10);
		grid2.getColumnModel().getColumn(11);
		grid2.getColumnModel().getColumn(12);
		grid2.getColumnModel().getColumn(13);
		grid2.getColumnModel().getColumn(14);
		grid2.getColumnModel().getColumn(15);
		grid2.getColumnModel().getColumn(16);
		grid2.getColumnModel().getColumn(17);
		grid2.getColumnModel().getColumn(18);
		grid2.getColumnModel().getColumn(19);
		grid2.getColumnModel().getColumn(20);
		grid2.getColumnModel().getColumn(21);
		grid2.getColumnModel().getColumn(22);
		grid2.getColumnModel().getColumn(23);
		grid2.getColumnModel().getColumn(24);
		grid2.getColumnModel().getColumn(25);
		grid2.getColumnModel().getColumn(26);
		grid2.getColumnModel().getColumn(27);
		grid2.getColumnModel().getColumn(28);
		grid2.getColumnModel().getColumn(29);
		grid2.getColumnModel().getColumn(30);
		grid2.getColumnModel().getColumn(31);
		grid2.getColumnModel().getColumn(32);
		grid2.getColumnModel().getColumn(33);

		// INSTANCIO MEU CONTROLER -- QrPesqc
		QrPesqc carregagrid2 = new QrPesqc();
		try {
			for (QrPesqcVO pe : QrPesqc.listaPesqCompleto()) {
				modelo.addRow(new Object[] { pe.getId(), pe.getRef(), pe.getProduto1(), pe.getVenda(),
						pe.getCusto_tabela_unitario(), pe.getCusto_ult_enrada_ped2(), pe.getTotal(), pe.getGta1(),
						pe.getVdgta1(), pe.getLor1(), pe.getVdlor1(), pe.getLor2(), pe.getVdlor2(), pe.getCAÇP(),
						pe.getVDCAÇP(), pe.getSjc(), pe.getVdsjc(), pe.getCrz(), pe.getVdcrz(), pe.getPinda(),
						pe.getVdpinda(), pe.getTaub(), pe.getVdtaub(), pe.getTaub2(), pe.getVdtaub2(), pe.getJac(),
						pe.getVdjac(), pe.getSjchumel(), pe.getVdsjchumel(), pe.getBuriti(), pe.getVdburiti(),
						pe.getCaraguatatuba(), pe.getVdcaraguatatuba(), pe.getGuara(), pe.getVdguara()

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carrega grid " + e.getMessage(), "Arius",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/// NA ACAO DO BOTAO PESQUISA CHAMA ESSE METODO E EXECUTA A CLASSE
	/// QrPesqVendidos
	@SuppressWarnings("unused")
	private static void carregaGridVendidos() {

		DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
		modelo.setNumRows(0); // INICIA O GRID LIMPO

		grid2.getColumnModel().getColumn(0);
		grid2.getColumnModel().getColumn(1);
		grid2.getColumnModel().getColumn(2);
		grid2.getColumnModel().getColumn(3);
		grid2.getColumnModel().getColumn(4);
		grid2.getColumnModel().getColumn(5);
		grid2.getColumnModel().getColumn(6);
		grid2.getColumnModel().getColumn(7);
		grid2.getColumnModel().getColumn(8);
		grid2.getColumnModel().getColumn(9);
		grid2.getColumnModel().getColumn(10);
		grid2.getColumnModel().getColumn(11);
		grid2.getColumnModel().getColumn(12);
		grid2.getColumnModel().getColumn(13);
		grid2.getColumnModel().getColumn(14);
		grid2.getColumnModel().getColumn(15);
		grid2.getColumnModel().getColumn(16);
		grid2.getColumnModel().getColumn(17);
		grid2.getColumnModel().getColumn(18);
		grid2.getColumnModel().getColumn(19);
		grid2.getColumnModel().getColumn(20);
		grid2.getColumnModel().getColumn(21);
		grid2.getColumnModel().getColumn(22);
		grid2.getColumnModel().getColumn(23);
		grid2.getColumnModel().getColumn(24);
		grid2.getColumnModel().getColumn(25);
		grid2.getColumnModel().getColumn(26);
		grid2.getColumnModel().getColumn(27);
		grid2.getColumnModel().getColumn(28);
		grid2.getColumnModel().getColumn(29);
		grid2.getColumnModel().getColumn(30);
		grid2.getColumnModel().getColumn(31);
		grid2.getColumnModel().getColumn(32);
		grid2.getColumnModel().getColumn(33);

		// INSTANCIO MEU CONTROLER -- QrPesqc
		QrPesqVendidos carregagridVendidos = new QrPesqVendidos();
		try {
			for (QrPesqcVO pe : QrPesqVendidos.listaPesqVendidos()) {
				modelo.addRow(new Object[] { pe.getId(), pe.getRef(), pe.getProduto1(), pe.getVenda(),
						pe.getCusto_tabela_unitario(), pe.getCusto_ult_enrada_ped2(), pe.getTotal(), pe.getGta1(),
						pe.getVdgta1(), pe.getLor1(), pe.getVdlor1(), pe.getLor2(), pe.getVdlor2(), pe.getCAÇP(),
						pe.getVDCAÇP(), pe.getSjc(), pe.getVdsjc(), pe.getCrz(), pe.getVdcrz(), pe.getPinda(),
						pe.getVdpinda(), pe.getTaub(), pe.getVdtaub(), pe.getTaub2(), pe.getVdtaub2(), pe.getJac(),
						pe.getVdjac(), pe.getSjchumel(), pe.getVdsjchumel(), pe.getBuriti(), pe.getVdburiti(),
						pe.getCaraguatatuba(), pe.getVdcaraguatatuba(), pe.getGuara(), pe.getVdguara()

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carrega grid " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/// NA ACAO DO BOTAO PESQUISA CHAMA ESSE METODO E EXECUTA A CLASSE QrProdutos
	@SuppressWarnings("unused")
	private static void carregaGridProdutos() {

		DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
		modelo.setNumRows(0); // INICIA O GRID LIMPO

		grid2.getColumnModel().getColumn(0);
		grid2.getColumnModel().getColumn(1);
		grid2.getColumnModel().getColumn(2);
		grid2.getColumnModel().getColumn(3);
		grid2.getColumnModel().getColumn(4);
		grid2.getColumnModel().getColumn(5);
		grid2.getColumnModel().getColumn(6);
		grid2.getColumnModel().getColumn(7);
		grid2.getColumnModel().getColumn(8);
		grid2.getColumnModel().getColumn(9);
		grid2.getColumnModel().getColumn(10);
		grid2.getColumnModel().getColumn(11);
		grid2.getColumnModel().getColumn(12);
		grid2.getColumnModel().getColumn(13);
		grid2.getColumnModel().getColumn(14);
		grid2.getColumnModel().getColumn(15);
		grid2.getColumnModel().getColumn(16);
		grid2.getColumnModel().getColumn(17);
		grid2.getColumnModel().getColumn(18);
		grid2.getColumnModel().getColumn(19);
		grid2.getColumnModel().getColumn(20);
		grid2.getColumnModel().getColumn(21);
		grid2.getColumnModel().getColumn(22);
		grid2.getColumnModel().getColumn(23);
		grid2.getColumnModel().getColumn(24);
		grid2.getColumnModel().getColumn(25);
		grid2.getColumnModel().getColumn(26);
		grid2.getColumnModel().getColumn(27);
		grid2.getColumnModel().getColumn(28);
		grid2.getColumnModel().getColumn(29);
		grid2.getColumnModel().getColumn(30);
		grid2.getColumnModel().getColumn(31);
		grid2.getColumnModel().getColumn(32);
		grid2.getColumnModel().getColumn(33);

		// INSTANCIO MEU CONTROLER -- QrPesqc
		QrProdutos carregagridProd = new QrProdutos();
		try {
			for (QrProdutosVO prod : QrProdutos.listaPesqProdutos()) {
				modelo.addRow(new Object[] { prod.getId(), prod.getRef(), prod.getProduto1(), prod.getVenda(),
						prod.getCusto_tabela_unitario(), prod.getCusto_ult_enrada_ped2(), prod.getTotal(), prod.getGta1(),
						prod.getVdgta1(), prod.getLor1(), prod.getVdlor1(), prod.getLor2(), prod.getVdlor2(),
						prod.getCAÇP(), prod.getVDCAÇP(), prod.getSjc(), prod.getVdsjc(), prod.getCrz(),
						prod.getVdcrz(), prod.getPinda(), prod.getVdpinda(), prod.getTaub(), prod.getVdtaub(),
						prod.getTaub2(), prod.getVdtaub2(), prod.getJac(), prod.getVdjac(), prod.getSjchumel(),
						prod.getVdsjchumel(), prod.getBuriti(), prod.getVdburiti(), prod.getCaraguatatuba(),
						prod.getVdcaraguatatuba(), prod.getGuara(), prod.getVdguara()

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carrega grid " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// INICIALIZA OS COMPONENTES
	@SuppressWarnings("deprecation")
	public frmLevantamentoFornecedor() throws Exception {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Levantamento de Fornecedor ");
		setBounds(100, 100, 1247, 609);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				frmLevantamentoFornecedor.class.getResource("/br/com/arius/levantamento/view/imgs/ariusico.png")));

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(15, 5, 97, 177);
		desktopPane.setBorder(new TitledBorder(null, "Tipo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		desktopPane.setBackground(SystemColor.control);

		JDesktopPane desktopPane_1 = new JDesktopPane();
		desktopPane_1.setBounds(118, 16, 629, 156);
		desktopPane_1.setBackground(SystemColor.control);
		desktopPane_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(757, 37, 440, 132);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(15, 200, 1190, 295);

		JRadioButton opt1 = new JRadioButton("Completo");
		JRadioButton opt2 = new JRadioButton("Vendidos");
		JRadioButton opt3 = new JRadioButton("Produtos");

		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.setBounds(971, 506, 234, 42);
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// COMPLETO
				if (opt1.isSelected() == true) {

					SisGeraRelatorioQrPesqc relatorio = new SisGeraRelatorioQrPesqc();
					try {
						relatorio.geraRelatorioQrPesqc(infoLoginVO.pIdEmpresa,
								frmLevantamentoFornecedor.txtIdFornecedor.getText(),
								frmLevantamentoFornecedor.txtDataInicialVenda.getText(),
								frmLevantamentoFornecedor.txtDataFinalVenda.getText(),
								frmLevantamentoFornecedor.txtDataInicialCompra.getText(),
								frmLevantamentoFornecedor.txtDataFinalCompra.getText());

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Erro ao gerar " + e2.getMessage(), "Arius",
								JOptionPane.ERROR_MESSAGE);
					}

				}
				// VENDIDOS
				else if (opt1.isSelected() == false && opt2.isSelected() == true) {

					SisGeraRelatorioQrPesqVendidos relatorioVendidos = new SisGeraRelatorioQrPesqVendidos();
					try {
						relatorioVendidos.geraRelatorioQrPesqVendidos(infoLoginVO.pIdEmpresa,
								frmLevantamentoFornecedor.txtIdFornecedor.getText(),
								frmLevantamentoFornecedor.txtDataInicialVenda.getText(),
								frmLevantamentoFornecedor.txtDataFinalVenda.getText(),
								frmLevantamentoFornecedor.txtDataInicialCompra.getText(),
								frmLevantamentoFornecedor.txtDataFinalCompra.getText());

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Erro ao gerar " + e2.getMessage(), "Arius",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				// PRODUTOS
				else if (opt3.isSelected() == true && txtIdFornecedor.getText().trim().length() > 0
						&& txtIdProduto.getText().trim().length() > 0) {

					SisGeraRelatorioQrProdutos relatorioProdutos = new SisGeraRelatorioQrProdutos();
					try {

						relatorioProdutos.geraRelatorioQrProdutos(infoLoginVO.pIdEmpresa,
								frmLevantamentoFornecedor.txtIdProduto.getText(),
								frmLevantamentoFornecedor.txtIdFornecedor.getText(),
								frmLevantamentoFornecedor.txtDataInicialVenda.getText(),
								frmLevantamentoFornecedor.txtDataFinalVenda.getText(),
								frmLevantamentoFornecedor.txtDataInicialCompra.getText(),
								frmLevantamentoFornecedor.txtDataFinalCompra.getText());

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Erro ao gerar " + e2.getMessage(), "Arius",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}// FIM

		});

		JLabel lblNewLabel_4 = new JLabel("Trocas do Fornecedor");
		lblNewLabel_4.setBounds(757, 5, 461, 21);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		// radioButton
		JButton btnPesquisaProduto = new JButton("New button");
		opt1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
				modelo.setNumRows(0); // INICIA O GRID LIMPO

				txtIdProduto.setText("");
				txtDescProduto.setText("");
				txtIdFornecedor.requestFocus();
				txtIdProduto.setEnabled(false);
				txtDescProduto.setEnabled(false);
				btnPesquisaProduto.setEnabled(false);
			}
		});
		opt1.setBounds(6, 18, 85, 23);
		opt1.setActionCommand("C");
		desktopPane.add(opt1);

		opt2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
				modelo.setNumRows(0); // INICIA O GRID LIMPO
				txtIdFornecedor.setText("");
				txtDescFornecedor.setText("");
				txtIdProduto.setText("");
				txtDescProduto.setText("");
				txtIdFornecedor.requestFocus();
				txtIdProduto.setEnabled(false);
				txtDescProduto.setEnabled(false);
				btnPesquisaProduto.setEnabled(false);
			}
		});
		opt2.setBounds(6, 60, 85, 23);
		opt2.setActionCommand("V");
		desktopPane.add(opt2);

		opt3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel modelo = (DefaultTableModel) grid2.getModel();
				modelo.setNumRows(0); // INICIA O GRID LIMPO
				txtIdFornecedor.setText("");
				txtDescFornecedor.setText("");
				txtIdProduto.setEnabled(true);
				txtDescProduto.setEnabled(true);
				btnPesquisaProduto.setEnabled(true);
				txtIdProduto.requestFocus();
			}
		});
		opt3.setBounds(6, 104, 85, 23);
		opt3.setActionCommand("P");
		desktopPane.add(opt3);

		grp.add(opt1);
		grp.add(opt2);
		grp.add(opt3);

		// OPCAO/
		if (opt1.isSelected() == true) {
			txtIdProduto.setEnabled(false);
			txtDescProduto.setEnabled(false);
			JOptionPane.showMessageDialog(null, "Desabilita Produto ", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		} else if (opt1.isSelected() == false && opt2.isSelected() == true) {
			txtIdProduto.disable();
			txtDescProduto.disable();
			opt3.setSelected(true);
		} else if (opt3.isSelected() == true && txtIdFornecedor.getText().trim().length() > 0) {
			txtIdProduto.enable(true);
			txtDescProduto.enable(true);
		}
		///

		grid2 = new JTable();
		grid2.setModel(new DefaultTableModel(
				new Object[][] { { null, null, null, null, null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
						null, null, null, null }, },
				new String[] { "CODIGO", "REFERENCIA", "PRODUTO", "VENDA", "CUSTO", "QTDA COMPRA", "ESTOQUE TOTAL",
						"EST-GA1", "VD-GA1", "EST-GA2", "VD-GA2", "EST-GA3", "VD-GA3", "EST-CZ", "VD-CZ", "EST-LO1",
						"VD-LO1", "EST-LO2", "VD-LO2", "EST-C\u00C7", "VD-C\u00C7", "EST-PI", "VD-PI", "VD-TTE1",
						"EST-TTE2", "VD-TTE2", "EST-SJ1", "VD-SJ1", "EST-JACAREI", "VD-JACAREI", "EST-HUMEL",
						"VD-HUMEL", "EST-CARAGUA", "VD-CARAGUA" }));
		grid2.getColumnModel().getColumn(0).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(1).setPreferredWidth(90);
		grid2.getColumnModel().getColumn(2).setPreferredWidth(300);
		grid2.getColumnModel().getColumn(3).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(4).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(5).setPreferredWidth(100);
		grid2.getColumnModel().getColumn(6).setPreferredWidth(100);
		grid2.getColumnModel().getColumn(7).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(8).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(9).setPreferredWidth(77);
		grid2.getColumnModel().getColumn(10).setPreferredWidth(72);
		grid2.getColumnModel().getColumn(11).setPreferredWidth(90);
		grid2.getColumnModel().getColumn(12).setPreferredWidth(90);
		grid2.getColumnModel().getColumn(13).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(14).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(15).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(16).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(17).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(18).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(19).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(20).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(21).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(22).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(23).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(24).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(25).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(26).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(27).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(28).setPreferredWidth(81);
		grid2.getColumnModel().getColumn(29).setPreferredWidth(81);
		grid2.getColumnModel().getColumn(30).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(31).setPreferredWidth(70);
		grid2.getColumnModel().getColumn(32).setPreferredWidth(90);
		grid2.getColumnModel().getColumn(33).setPreferredWidth(90);

		grid2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
		esquerda.setHorizontalAlignment(SwingConstants.LEFT);

		DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
		direita.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(SwingConstants.CENTER);

		grid2.getColumnModel().getColumn(0).setCellRenderer(esquerda);
		grid2.getColumnModel().getColumn(1).setCellRenderer(esquerda);
		grid2.getColumnModel().getColumn(2).setCellRenderer(esquerda);
		grid2.getColumnModel().getColumn(3).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(4).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(5).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(6).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(7).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(8).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(9).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(10).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(11).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(12).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(13).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(14).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(15).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(16).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(17).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(18).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(19).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(20).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(21).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(22).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(23).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(24).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(25).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(26).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(27).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(28).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(29).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(30).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(31).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(32).setCellRenderer(direita);
		grid2.getColumnModel().getColumn(33).setCellRenderer(direita);
		scrollPane_1.setViewportView(grid2);

		gridTrocas = new JTable();
		gridTrocas.setFont(new Font("Tahoma", Font.PLAIN, 10));
		gridTrocas.setModel(new DefaultTableModel(new Object[][] { { null, null, null }, },
				new String[] { "Codigo", "Produto", "Estoque" }));
		gridTrocas.getColumnModel().getColumn(0).setPreferredWidth(70);
		gridTrocas.getColumnModel().getColumn(1).setPreferredWidth(297);
		gridTrocas.getColumnModel().getColumn(2).setPreferredWidth(70);

		gridTrocas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		DefaultTableCellRenderer esquerda01 = new DefaultTableCellRenderer();
		esquerda01.setHorizontalAlignment(SwingConstants.LEFT);

		DefaultTableCellRenderer direita01 = new DefaultTableCellRenderer();
		direita01.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer centro01 = new DefaultTableCellRenderer();
		centro01.setHorizontalAlignment(SwingConstants.CENTER);

		gridTrocas.getColumnModel().getColumn(0).setCellRenderer(direita);
		gridTrocas.getColumnModel().getColumn(1).setCellRenderer(esquerda);
		gridTrocas.getColumnModel().getColumn(2).setCellRenderer(direita);

		scrollPane.setViewportView(gridTrocas);
		desktopPane_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("Fornecedor");
		lblNewLabel.setBounds(10, 11, 86, 14);
		desktopPane_1.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Produto");
		lblNewLabel_1.setBounds(10, 69, 46, 14);
		desktopPane_1.add(lblNewLabel_1);

		txtIdFornecedor = new JTextField();
		txtIdFornecedor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					txtDescFornecedor.requestFocus();
					try {
						listaPesqIdFornecedor();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		txtIdFornecedor.setBounds(10, 26, 56, 20);
		desktopPane_1.add(txtIdFornecedor);
		txtIdFornecedor.setColumns(10);

		txtIdProduto = new JTextField();
		txtIdProduto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					txtDescProduto.requestFocus();
					try {
						listaPesqIdProduto();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		txtIdProduto.setBounds(10, 83, 56, 20);
		desktopPane_1.add(txtIdProduto);
		txtIdProduto.setColumns(10);

		JButton btnPesquisaFornecedor = new JButton("New button");
		btnPesquisaFornecedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					frmPesquisaFornecedor ffornecedor = new frmPesquisaFornecedor();
					ffornecedor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		btnPesquisaFornecedor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					txtDataFinalVenda.requestFocus();
				}
			}
		});
		txtDescFornecedor = new JTextField();
		txtDescFornecedor.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtDescFornecedor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					btnPesquisaFornecedor.requestFocus();
				}
			}
		});
		txtDescFornecedor.setBounds(76, 26, 255, 20);
		desktopPane_1.add(txtDescFornecedor);
		txtDescFornecedor.setColumns(10);

		btnPesquisaProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmPesquisaProduto fProduto = new frmPesquisaProduto();
				fProduto.setVisible(true);

			}
		});
		btnPesquisaProduto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					txtDataInicialCompra.requestFocus();
				}
			}
		});
		txtDescProduto = new JTextField();
		txtDescProduto.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtDescProduto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					btnPesquisaProduto.requestFocus();
				}
			}
		});
		txtDescProduto.setBounds(76, 83, 255, 20);
		desktopPane_1.add(txtDescProduto);
		txtDescProduto.setColumns(10);

		btnPesquisaFornecedor.setBounds(335, 26, 35, 23);
		desktopPane_1.add(btnPesquisaFornecedor);

		btnPesquisaProduto.setBounds(335, 83, 35, 23);
		desktopPane_1.add(btnPesquisaProduto);

		JLabel lblNewLabel_2 = new JLabel("Periodo de Vendas");
		lblNewLabel_2.setBounds(437, 11, 121, 14);
		desktopPane_1.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Periodo de Compras");
		lblNewLabel_3.setBounds(437, 69, 134, 14);
		desktopPane_1.add(lblNewLabel_3);

		txtDataInicialVenda = new JFormattedTextField();
		txtDataInicialVenda.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					txtDataFinalVenda.requestFocus();
				}
			}
		});
		txtDataInicialVenda.setBounds(396, 26, 86, 20);
		desktopPane_1.add(txtDataInicialVenda);
		MaskFormatter maskData = new MaskFormatter("##/##/####");
		maskData.install((JFormattedTextField) txtDataInicialVenda);
		txtDataInicialVenda.setColumns(10);

		txtDataInicialCompra = new JFormattedTextField();
		txtDataInicialCompra.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					txtDataFinalCompra.requestFocus();
				}
			}
		});
		txtDataInicialCompra.setBounds(396, 83, 86, 20);
		desktopPane_1.add(txtDataInicialCompra);
		MaskFormatter maskDataIncialCompra = new MaskFormatter("##/##/####");
		maskDataIncialCompra.install((JFormattedTextField) txtDataInicialCompra);
		txtDataInicialCompra.setColumns(10);

		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addKeyListener(new KeyAdapter() {
		});
		txtDataFinalVenda = new JFormattedTextField();
		txtDataFinalVenda.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					txtDataInicialCompra.requestFocus();
				}
			}
		});
		txtDataFinalVenda.setBounds(509, 26, 86, 20);
		desktopPane_1.add(txtDataFinalVenda);
		MaskFormatter maskDataFinalVenda = new MaskFormatter("##/##/####");
		// MaskFormatter maskDataFinalVenda = new MaskFormatter("21/11/2018");
		maskDataFinalVenda.install((JFormattedTextField) txtDataFinalVenda);
		txtDataFinalVenda.setColumns(10);

		txtDataFinalCompra = new JFormattedTextField();
		txtDataFinalCompra.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					btnPesquisar.requestFocus();
				}
			}
		});
		txtDataFinalCompra.setBounds(509, 83, 86, 20);
		desktopPane_1.add(txtDataFinalCompra);
		MaskFormatter maskDataFinalCompra = new MaskFormatter("##/##/####");
		// MaskFormatter maskDataFinalCompra = new MaskFormatter("21/11/2018");
		maskDataFinalCompra.install((JFormattedTextField) txtDataFinalCompra);
		txtDataFinalCompra.setColumns(10);

		// ACAO BOTAO CLICK
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (txtIdFornecedor.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Escolha um fornecedor ", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					txtIdFornecedor.requestFocus();
				}
				// COMPLETO
				if (opt1.isSelected() == true) {
					carregaGridControle();
					try {
						carregaGridTroca();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// VENDIDOS
				else if (opt1.isSelected() == false && opt2.isSelected() == true) {
					carregaGridVendidos();
					try {
						carregaGridTroca();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// PRODUTOS
				else if (opt3.isSelected() == true && txtIdFornecedor.getText().trim().length() > 0
						&& txtIdProduto.getText().trim().length() > 0) {
					carregaGridProdutos();
				}

			}

		});
		btnPesquisar.setBounds(365, 122, 230, 23);
		desktopPane_1.add(btnPesquisar);

		// SETANDO A DATA ATUAL DO SISTEMAS
		txtDataInicialVenda.setText(MostraData());
		txtDataFinalVenda.setText(MostraData());

		txtDataInicialCompra.setText(MostraData());
		txtDataFinalCompra.setText(MostraData());
		contentPane.setLayout(null);
		contentPane.add(scrollPane_1);
		contentPane.add(desktopPane);
		contentPane.add(desktopPane_1);
		contentPane.add(lblNewLabel_4);
		contentPane.add(scrollPane);
		contentPane.add(btnImprimir);

	}
}
