package vista;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelTareas extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton btnVolver, btnConfirmar, btnRechazar;
	private JTable tablaHorario;

	/**
	 * Create the panel.
	 */
	public PanelTareas() {
		setBackground(new Color(255, 255, 255));
		setBounds(288, 11, 829, 658);
		setLayout(null);

		btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Arial", Font.PLAIN, 16));
		btnVolver.setBounds(640, 45, 129, 37);
		add(btnVolver);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 16));
		scrollPane.setBounds(77, 109, 657, 360);
		add(scrollPane);

		tablaHorario = new JTable();
		tablaHorario.setModel(new DefaultTableModel(new Object[][] {}, new String[] {

		}));
		tablaHorario.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setViewportView(tablaHorario);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setEnabled(false);
		btnConfirmar.setFont(new Font("Arial", Font.PLAIN, 16));
		btnConfirmar.setBounds(97, 518, 217, 37);
		add(btnConfirmar);

		btnRechazar = new JButton("Rechazar");
		btnRechazar.setEnabled(false);
		btnRechazar.setFont(new Font("Arial", Font.PLAIN, 16));
		btnRechazar.setBounds(490, 518, 217, 37);
		add(btnRechazar);

	}

	public JButton getBtnVolver() {
		return btnVolver;
	}

	public void setBtnVolver(JButton btnVolver) {
		this.btnVolver = btnVolver;
	}

	public JButton getBtnConfirmar() {
		return btnConfirmar;
	}

	public void setBtnConfirmar(JButton btnConfirmar) {
		this.btnConfirmar = btnConfirmar;
	}

	public JButton getBtnRechazar() {
		return btnRechazar;
	}

	public void setBtnRechazar(JButton btnRechazar) {
		this.btnRechazar = btnRechazar;
	}

	public JTable getTablaHorario() {
		return tablaHorario;
	}

	public void setTablaHorario(JTable tablaHorario) {
		this.tablaHorario = tablaHorario;
	}
}
