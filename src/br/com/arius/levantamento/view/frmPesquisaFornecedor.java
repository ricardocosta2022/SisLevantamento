package br.com.arius.levantamento.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDesktopPane;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import br.com.arius.levantamento.oracle.Oracle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class frmPesquisaFornecedor extends JFrame {

	private JPanel contentPane;
	public static JTextField txtPesqFornecedor;
	public JTable grid1Fornecedor;

	/**
	 * INICIO DA APLICACAO
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPesquisaFornecedor frame = new frmPesquisaFornecedor();
					// frame.setLocationRelativeTo(null); // CENTRALIZAR A TELA
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// METODO PESQUISA FORNECEDOR PELA RAZAO SOCIAL(DESCRITIVO)
	@SuppressWarnings("unused")
	public void LocalizaRazaoSocial() throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dtm;

		String razaoSocial = txtPesqFornecedor.getText().toUpperCase();

		try {

			conn = Oracle.conectar();
			String sql = "select * from fornecedores where descritivo like '%" + razaoSocial + "%'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			int QuantidadeColunas = rs.getMetaData().getColumnCount();

			DefaultTableModel modelo = (DefaultTableModel) grid1Fornecedor.getModel();
			modelo.setNumRows(0); // INICIA O GRID LIMPO

			while (rs.next()) {
				String Dados[] = new String[QuantidadeColunas];
				for (int I = 1; I <= QuantidadeColunas; I++) {
					Dados[I - 1] = rs.getString(I);

				}

				modelo.addRow(Dados);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao carregar o grid!\n" + e.toString());
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}
	}

	// METODO PESQUISA FORNECEDOR PELA FANTASIA
	@SuppressWarnings("unused")
	public void LocalizaFantasia() throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dtm;

		String fantasia = txtPesqFornecedor.getText().toUpperCase();

		try {

			conn = Oracle.conectar();
			String sql = "select * from fornecedores where fantasia like '%" + fantasia + "%'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			int QuantidadeColunas = rs.getMetaData().getColumnCount();

			DefaultTableModel modelo = (DefaultTableModel) grid1Fornecedor.getModel();
			modelo.setNumRows(0); // INICIA O GRID LIMPO

			while (rs.next()) {
				String Dados[] = new String[QuantidadeColunas];
				for (int I = 1; I <= QuantidadeColunas; I++) {
					Dados[I - 1] = rs.getString(I);

				}

				modelo.addRow(Dados);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao carregar o grid!\n" + e.toString());
		} finally {
			if (rs != null) {
				rs.close();
			}
			Oracle.desconectar(conn, stmt);
		}
	}

//  INICIALIZA OS COMPONENTES
	public frmPesquisaFornecedor() throws Exception {
		setResizable(false);
		setTitle("Localizar Fornecedor ");
		setLocationRelativeTo(null); // CENTRALIZAR A TELA
		setBounds(100, 100, 736, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				frmPesquisaFornecedor.class.getResource("/br/com/arius/levantamento/view/imgs/ariusico.png")));

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBorder(
				new TitledBorder(null, "Tipo de Pesquisa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		desktopPane.setBackground(SystemColor.control);

		JDesktopPane desktopPane_1 = new JDesktopPane();
		desktopPane_1.setBorder(
				new TitledBorder(null, "Campo de Pesquisa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		desktopPane_1.setBackground(SystemColor.control);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_contentPane.createSequentialGroup().addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(desktopPane, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(desktopPane_1, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(11).addComponent(desktopPane_1,
								GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
						.addComponent(desktopPane, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addContainerGap()));

		grid1Fornecedor = new JTable();
		JRadioButton rdbtnRazaoSocial = new JRadioButton("Razao Social");
		rdbtnRazaoSocial.setSelected(true);
		JRadioButton rdbtnNewFantasia = new JRadioButton("Fantasia");

		// ACAO DO CLICK NO GRID LEVA O CODIGO PARA TELA PRINCIPAL.
		grid1Fornecedor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (rdbtnRazaoSocial.isSelected() == true) {

					int linha = grid1Fornecedor.getSelectedRow(); // SABER QUAL LINHA FOI CLICADO
					frmLevantamentoFornecedor.txtIdFornecedor.setText(grid1Fornecedor.getValueAt(linha, 0).toString());
					frmLevantamentoFornecedor.txtDescFornecedor
							.setText(grid1Fornecedor.getValueAt(linha, 1).toString());
					frmLevantamentoFornecedor.txtIdFornecedor.requestFocus();
					setVisible(false);

				} else if (rdbtnNewFantasia.isSelected() == true) {
					int linha = grid1Fornecedor.getSelectedRow(); // SABER QUAL LINHA FOI CLICADO
					frmLevantamentoFornecedor.txtIdFornecedor.setText(grid1Fornecedor.getValueAt(linha, 0).toString());
					frmLevantamentoFornecedor.txtDescFornecedor
							.setText(grid1Fornecedor.getValueAt(linha, 1).toString());
					frmLevantamentoFornecedor.txtIdFornecedor.requestFocus();
					setVisible(false);
				}

			}
		});
		grid1Fornecedor.setModel(
				new DefaultTableModel(new Object[][] { { null, null }, }, new String[] { "Código", "Descritivo" }));
		grid1Fornecedor.getColumnModel().getColumn(0).setPreferredWidth(70);
		grid1Fornecedor.getColumnModel().getColumn(1).setPreferredWidth(610);

		grid1Fornecedor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
		esquerda.setHorizontalAlignment(SwingConstants.LEFT);

		DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
		direita.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(SwingConstants.CENTER);

		grid1Fornecedor.getColumnModel().getColumn(0).setCellRenderer(centro);
		grid1Fornecedor.getColumnModel().getColumn(1).setCellRenderer(esquerda);

		scrollPane.setViewportView(grid1Fornecedor);

		txtPesqFornecedor = new JTextField();
		txtPesqFornecedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					if (rdbtnRazaoSocial.isSelected() == true) {
						LocalizaRazaoSocial();

					} else if (rdbtnNewFantasia.isSelected() == true) {
						LocalizaFantasia();

					} else {
						JOptionPane.showMessageDialog(null, "Selecione uma opção de busca");
						txtPesqFornecedor.setText("");
						txtPesqFornecedor.requestFocus();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Falha ao carregar o grid!\n" + e.toString());
				}

			}
		});
		txtPesqFornecedor.setBounds(10, 35, 533, 20);
		desktopPane_1.add(txtPesqFornecedor);
		txtPesqFornecedor.setColumns(10);

		rdbtnRazaoSocial.setBounds(6, 31, 109, 23);
		desktopPane.add(rdbtnRazaoSocial);

		rdbtnNewFantasia.setBounds(6, 69, 109, 23);
		desktopPane.add(rdbtnNewFantasia);
		contentPane.setLayout(gl_contentPane);
	}
}
