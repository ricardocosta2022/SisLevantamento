package br.com.arius.levantamento.view;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import br.com.arius.levantamento.controller.Usuarios;
import br.com.arius.levantamento.model.infoLoginVO;
import br.com.arius.levantamento.oracle.Oracle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

public class frmLogin extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frmLogin;
	public static JTextField txtLogin;
	public static JTextField txtSenha;
	public static JComboBox<Object> comboEmpresa;

	public static String pUsuarioLogado;
	public static String pEmpresaLogada;

	// MAIN
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmLogin window = new frmLogin();
					window.frmLogin.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// POPULANDO O COMBOBOXEMPRESA
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List populaComboboxEmpresa() throws Exception {
		List retorno = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Oracle.conectar();
			String sql = "select lpad(id_empresa,3,'0')||' - '||nome_fantasia as empresa from bas_t_empresas order by id_empresa";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				retorno.add(rs.getString("empresa"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao Listar ComboEmpresa " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				Oracle.desconectar(conn, stmt);
			}

			if (rs != null) {
				rs.close();
			}
		}

		return retorno;
	}

	// CONFIRMACAO DO LOGIN = SE EXISTIR O USUARIO CHAMA A TELA PRINCIPAL
	public void confirmaLogin() throws Exception {
		new infoLoginVO(Integer.parseInt(String.valueOf(comboEmpresa.getSelectedItem()).substring(0, 3)),
				txtLogin.getText().toUpperCase());

		frmLevantamentoFornecedor frmPrincipal = new frmLevantamentoFornecedor();
		frmPrincipal.setVisible(true);

		// CLOSE LOGIN FORM
		frmLogin.dispose();
	}

	// CONSTRUTOR, INICIALIZA A CONSTRUCAO DOS COMPONENTES
	public frmLogin() throws Exception {
		initialize();
		frmLogin.setLocationRelativeTo(null); // CENTRALIZAR A TELA
	}

	// INICIALIZA OS COMPONENTES
	@SuppressWarnings("unchecked")
	private void initialize() throws Exception {
		frmLogin = new JFrame();
		//frmLogin.setTitle("Arius Levantamento - Login - 01/11/2021 R1"); // TITULO DO FRAME
		frmLogin.setTitle("Arius Levantamento - Login - 12/2022 R1"); // TITULO DO FRAME
		frmLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLogin.setBounds(100, 100, 472, 214);
		frmLogin.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(frmLogin.class.getResource("/br/com/arius/levantamento/view/imgs/ariusico.png")));

		// BOTAO LOGAR
		JButton btnLogar = new JButton("Logar");
		btnLogar.setBounds(330, 129, 86, 23);
		btnLogar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {

				if (arg0.getKeyCode() == 10) {
					Usuarios usuario = new Usuarios();

					try {
						String logado = usuario.logar(txtLogin.getText().toLowerCase(), txtSenha.getText());

						if (!logado.equals(null)) {
							confirmaLogin();
						} else {
							throw new Exception("Usuário ou senha inválido.");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// LISTENER DO BOTAO LOGAR
		btnLogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Usuarios usuario = new Usuarios();

				try {

					String logado = usuario.logar(txtLogin.getText().toLowerCase(), txtSenha.getText());

					if (!logado.equals(null)) {
						confirmaLogin();
					} else {
						throw new Exception("Usuário ou senha inválido.");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// TXT LOGIN
		JLabel lblNewLabel_1 = new JLabel("Usuário");
		lblNewLabel_1.setBounds(164, 36, 62, 14);

		txtLogin = new JTextField();
		txtLogin.setBounds(236, 33, 180, 20);
		txtLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					txtSenha.requestFocus();
				}
			}
		});

		txtLogin.setColumns(10);

		// TXT SENHA
		JLabel lblNewLabel_2 = new JLabel("Senha");
		lblNewLabel_2.setBounds(164, 67, 62, 14);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(236, 64, 180, 20);
		txtSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					comboEmpresa.requestFocus();
				}
			}
		});

		txtSenha.setColumns(10);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 11, 148, 146);
		lblNewLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(frmLogin.class.getResource("/br/com/arius/levantamento/view/imgs/arius128128.png"))));

		// COMBO EMPRESA
		JLabel lblNewLabel_3 = new JLabel("Empresa");
		lblNewLabel_3.setBounds(164, 101, 62, 14);

		comboEmpresa = new JComboBox<>();
		comboEmpresa.setBounds(236, 98, 180, 20);
//		comboEmpresa.addItem(null); //ADICIONA UM ITEM EM BRANCO.

		populaComboboxEmpresa().forEach(x -> {
			comboEmpresa.addItem(x);
		});

		comboEmpresa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					btnLogar.requestFocus();
				}
			}
		});
		frmLogin.getContentPane().setLayout(null);
		frmLogin.getContentPane().add(lblNewLabel);
		frmLogin.getContentPane().add(lblNewLabel_1);
		frmLogin.getContentPane().add(lblNewLabel_3);
		frmLogin.getContentPane().add(lblNewLabel_2);
		frmLogin.getContentPane().add(txtLogin);
		frmLogin.getContentPane().add(comboEmpresa);
		frmLogin.getContentPane().add(txtSenha);
		frmLogin.getContentPane().add(btnLogar);

	}
}
