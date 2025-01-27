package vista;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PanelMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblFotoHorario, lblFotoOtros, lblFotoReuniones;
	private JButton btnDesconectar;

	/**
	 * Create the panel.
	 */
	public PanelMenu() {
		setBackground(new Color(255, 255, 255));
		setBounds(288, 11, 829, 658);
		setLayout(null);

		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setFont(new Font("Arial", Font.BOLD, 28));
		lblMenu.setBounds(277, 82, 290, 72);
		add(lblMenu);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDesconectar.setBounds(633, 40, 162, 41);
		add(btnDesconectar);

		lblFotoHorario = new JLabel("");
		lblFotoHorario.setBounds(140, 208, 204, 116);
		add(lblFotoHorario);
		lblFotoHorario.setIcon(new ImageIcon(new ImageIcon("archivos/horario.png").getImage()
				.getScaledInstance(lblFotoHorario.getWidth(), lblFotoHorario.getHeight(), Image.SCALE_SMOOTH)));

		lblFotoOtros = new JLabel("");
		lblFotoOtros.setBounds(488, 208, 204, 116);
		add(lblFotoOtros);
		lblFotoOtros.setIcon(new ImageIcon(new ImageIcon("archivos/otros_horarios.png").getImage()
				.getScaledInstance(lblFotoOtros.getWidth(), lblFotoOtros.getHeight(), Image.SCALE_SMOOTH)));

		lblFotoReuniones = new JLabel();
		lblFotoReuniones.setBounds(311, 374, 204, 116);
		add(lblFotoReuniones);
		lblFotoReuniones.setIcon(new ImageIcon(new ImageIcon("archivos/reuniones.png").getImage()
				.getScaledInstance(lblFotoReuniones.getWidth(), lblFotoReuniones.getHeight(), Image.SCALE_SMOOTH)));

		JLabel lblHorario = new JLabel("Horario");
		lblHorario.setFont(new Font("Arial", Font.PLAIN, 16));
		lblHorario.setHorizontalAlignment(SwingConstants.CENTER);
		lblHorario.setBounds(140, 335, 204, 28);
		add(lblHorario);

		JLabel lblOtros = new JLabel("Otros horarios");
		lblOtros.setHorizontalAlignment(SwingConstants.CENTER);
		lblOtros.setFont(new Font("Arial", Font.PLAIN, 16));
		lblOtros.setBounds(488, 335, 204, 28);
		add(lblOtros);

		JLabel lblReuniones = new JLabel("Reuniones");
		lblReuniones.setHorizontalAlignment(SwingConstants.CENTER);
		lblReuniones.setFont(new Font("Arial", Font.PLAIN, 16));
		lblReuniones.setBounds(311, 501, 204, 28);
		add(lblReuniones);

	}

	public JLabel getLblFotoHorario() {
		return lblFotoHorario;
	}

	public void setLblFotoHorario(JLabel lblFotoHorario) {
		this.lblFotoHorario = lblFotoHorario;
	}

	public JLabel getLblFotoOtros() {
		return lblFotoOtros;
	}

	public void setLblFotoOtros(JLabel lblFotoOtros) {
		this.lblFotoOtros = lblFotoOtros;
	}

	public JLabel getLblFotoReuniones() {
		return lblFotoReuniones;
	}

	public void setLblFotoReuniones(JLabel lblFotoReuniones) {
		this.lblFotoReuniones = lblFotoReuniones;
	}

	public JButton getBtnDesconectar() {
		return btnDesconectar;
	}

	public void setBtnDesconectar(JButton btnDesconectar) {
		this.btnDesconectar = btnDesconectar;
	}
}
