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

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import br.com.arius.levantamento.oracle.Oracle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class frmPesquisaProduto extends JFrame {

	private JPanel contentPane;
	public static JTextField txtProduto;
	public JTable gridProduto;

	/**
	 * INICIO DA APLICACAO
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPesquisaProduto frame = new frmPesquisaProduto();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// METODO PESQUISA PRODUTO PELO DESCRITIVO
	public void LocalizaProduto() throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String descricaoProduto = txtProduto.getText().toUpperCase();

		try {

			conn = Oracle.conectar();
			String sql = "select * from produtos where descritivo like '%" + descricaoProduto + "%'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			int QuantidadeColunas = rs.getMetaData().getColumnCount();

			DefaultTableModel modelo = (DefaultTableModel) gridProduto.getModel();
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

	// INICIA OS COMPONETES
	public frmPesquisaProduto() {
		setResizable(false);
		setTitle("Localizar Produto ");
		setBounds(100, 100, 736, 300);
		setLocationRelativeTo(null); // CENTRALIZAR A TELA
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(frmPesquisaProduto.class.getResource("/br/com/arius/levantamento/view/imgs/ariusico.png")));

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBorder(
				new TitledBorder(null, "Localiza Produto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		desktopPane.setBackground(SystemColor.control);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 690,
										Short.MAX_VALUE)
								.addComponent(desktopPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 690,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(2)
						.addComponent(desktopPane, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE).addContainerGap()));

		// ACAO DO CLICK NO GRID LEVA O CODIGO PARA TELA PRINCIPAL.
		gridProduto = new JTable();
		gridProduto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int linha = gridProduto.getSelectedRow(); // SABER QUAL LINHA FOI CLICADO
				frmLevantamentoFornecedor.txtIdProduto.setText(gridProduto.getValueAt(linha, 0).toString());
				frmLevantamentoFornecedor.txtDescProduto.setText(gridProduto.getValueAt(linha, 1).toString());
				frmLevantamentoFornecedor.txtIdProduto.requestFocus();
				setVisible(false);
			}
		});
		gridProduto.setModel(
				new DefaultTableModel(new Object[][] { { null, null }, }, new String[] { "Código", "Descritivo" }));
		gridProduto.getColumnModel().getColumn(0).setPreferredWidth(70);
		gridProduto.getColumnModel().getColumn(1).setPreferredWidth(610);

		gridProduto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
		esquerda.setHorizontalAlignment(SwingConstants.LEFT);

		DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
		direita.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(SwingConstants.CENTER);

		gridProduto.getColumnModel().getColumn(0).setCellRenderer(centro);
		gridProduto.getColumnModel().getColumn(1).setCellRenderer(esquerda);
		scrollPane.setViewportView(gridProduto);

		txtProduto = new JTextField();
		txtProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					LocalizaProduto();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		txtProduto.setBounds(10, 22, 670, 20);
		desktopPane.add(txtProduto);
		txtProduto.setColumns(10);
		contentPane.setLayout(gl_contentPane);
	}
}
