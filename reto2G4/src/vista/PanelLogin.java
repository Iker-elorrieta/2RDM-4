package vista;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;

import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class PanelLogin extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textFieldUser;
	private JPasswordField pfPass;
	private JButton btnLogin;
	JLabel lblNewLabel;

	/**
	 * Create the panel.
	 */
	public PanelLogin() {
		setBackground(new Color(230, 230, 250));
		setBounds(288, 11, 829, 658);
		setLayout(null);

		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Arial", Font.BOLD, 28));
		lblLogin.setBounds(337, 59, 200, 50);
		add(lblLogin);

		JLabel lblLogo = new JLabel("LOGO");
		lblLogo.setFont(new Font("Arial", Font.PLAIN, 16));
		lblLogo.setBounds(360, 151, 163, 89);
		add(lblLogo);
		lblLogo.setIcon(new ImageIcon(new ImageIcon("Multimedia/Logo.png").getImage()
				.getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH)));

		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setFont(new Font("Arial", Font.PLAIN, 16));
		lblUser.setBounds(256, 285, 347, 20);
		add(lblUser);

		textFieldUser = new JTextField();
		textFieldUser.setBounds(360, 282, 243, 30);
		textFieldUser.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(textFieldUser);

		JLabel lblPass = new JLabel("Contraseña:");
		lblPass.setFont(new Font("Arial", Font.PLAIN, 16));
		lblPass.setBounds(256, 361, 347, 20);
		add(lblPass);

		pfPass = new JPasswordField();
		pfPass.setBounds(360, 358, 243, 30);
		pfPass.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(pfPass);

		btnLogin = new JButton("Aceptar");
		btnLogin.setBounds(256, 472, 347, 41);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
		btnLogin.setBackground(new Color(100, 149, 237));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorderPainted(false);
		btnLogin.setFocusPainted(false);
		add(btnLogin);
	}

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}


	public JTextField getTextFieldUser() {
		return textFieldUser;
	}

	public void setTextFieldUser(JTextField textFieldUser) {
		this.textFieldUser = textFieldUser;
	}

	public JPasswordField getTextFieldPass() {
		return pfPass;
	}

	public void setTextFieldPass(JPasswordField pfPass) {
		this.pfPass = pfPass;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(JButton btnLogin) {
		this.btnLogin = btnLogin;
	}
}
