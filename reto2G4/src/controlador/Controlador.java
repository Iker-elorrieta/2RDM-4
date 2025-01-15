package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Profesor;
import vista.Principal;
import vista.Principal.enumAcciones;

public class Controlador implements ActionListener, MouseListener {

	private vista.Principal vistaPrincipal;
	private Socket cliente;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private int id = 0;
	private ArrayList<Profesor> profesores;

	public Controlador(vista.Principal vistaPrincipal) {
		this.vistaPrincipal = vistaPrincipal;
		this.inicializarControlador();
	}

	private void inicializarControlador() {

		try {
			cliente = new Socket("localhost", 2000);
			dos = new ObjectOutputStream(cliente.getOutputStream());
			dis = new ObjectInputStream(cliente.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// VENTANA LOGIN
		this.vistaPrincipal.getPanelLogin().getBtnLogin().addActionListener(this);
		this.vistaPrincipal.getPanelLogin().getBtnLogin().setActionCommand(Principal.enumAcciones.LOGIN.toString());
		// VENTANA MENU
		this.vistaPrincipal.getPanelMenu().getBtnDesconectar().addActionListener(this);
		this.vistaPrincipal.getPanelMenu().getBtnDesconectar()
				.setActionCommand(Principal.enumAcciones.DESCONECTAR.toString());

		this.vistaPrincipal.getPanelMenu().getLblFotoHorario().addMouseListener(this);
		this.vistaPrincipal.getPanelMenu().getLblFotoOtros().addMouseListener(this);
		this.vistaPrincipal.getPanelMenu().getLblFotoReuniones().addMouseListener(this);
		// VENTANA HORARIO
		this.vistaPrincipal.getPanelHorario().getBtnVolver().addActionListener(this);
		this.vistaPrincipal.getPanelHorario().getBtnVolver().setActionCommand(Principal.enumAcciones.VOLVER.toString());

		this.vistaPrincipal.getPanelHorario().getBtnPendientes().addActionListener(this);
		this.vistaPrincipal.getPanelHorario().getBtnPendientes()
				.setActionCommand(Principal.enumAcciones.TAREAS_PENDIENTES.toString());
		// VENTANA LISTA
		this.vistaPrincipal.getPanelLista().getBtnConfirmar().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnConfirmar()
				.setActionCommand(Principal.enumAcciones.CONFIRMAR_REUNION.toString());

		this.vistaPrincipal.getPanelLista().getBtnRechazar().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnRechazar()
				.setActionCommand(Principal.enumAcciones.RECHAZAR_REUNION.toString());

		this.vistaPrincipal.getPanelLista().getBtnSeleccionar().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnSeleccionar()
				.setActionCommand(Principal.enumAcciones.SELECCIONAR_PROFESOR.toString());

		this.vistaPrincipal.getPanelLista().getBtnVolver().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnVolver().setActionCommand(Principal.enumAcciones.VOLVER.toString());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Principal.enumAcciones accion = Principal.enumAcciones.valueOf(e.getActionCommand());

		switch (accion) {
		case LOGIN:
			this.mConfirmarLogin(accion);
			break;
		case DESCONECTAR:
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_LOGIN);
			break;
		case VOLVER:
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_MENU);
			this.vistaPrincipal.getPanelLista().getBtnSeleccionar().setVisible(false);
			this.vistaPrincipal.getPanelLista().getBtnRechazar().setVisible(false);
			this.vistaPrincipal.getPanelLista().getBtnConfirmar().setVisible(false);
			break;
		case TAREAS_PENDIENTES:
			break;
		case SELECCIONAR_PROFESOR:
			seleccionarProfesor();
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);
			break;
		case CONFIRMAR_REUNION:
			break;
		case RECHAZAR_REUNION:
			break;

		default:
			break;

		}
	}

	private void seleccionarProfesor() {
		// TODO Auto-generated method stub
		if (!this.vistaPrincipal.getPanelLista().getListaProfesor().isSelectionEmpty()) {
			try {
				dos.writeObject(2);
				dos.flush();
				int idprofesor = 0;
				for (Profesor profesor : profesores) {
					if (profesor.getNombre()
							.equals(this.vistaPrincipal.getPanelLista().getListaProfesor().getSelectedValue())) {
						idprofesor = profesor.getId();

					}
				}
				dos.writeObject(idprofesor);
				dos.flush();
				cargarHorario((String[][]) dis.readObject(), this.vistaPrincipal.getPanelHorario().getTablaHorario());
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(null, "Debes seleccionar un profesor de la lista");
		}
	}

	private void mConfirmarLogin(enumAcciones accion) {
		// TODO Auto-generated method stub

		try {
			dos.writeObject(1);
			dos.flush();
			dos.writeObject(this.vistaPrincipal.getPanelLogin().getTextFieldUser().getText());
			dos.flush();
			dos.writeObject(new String(this.vistaPrincipal.getPanelLogin().getTextFieldPass().getPassword()));
			dos.flush();
			id = (int) dis.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (id != 0) {
			System.out.println("Hola");
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_MENU);
		} else {
			JOptionPane.showMessageDialog(null, "No existe ningun profesor con esas credenciales");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		Object source = e.getSource();

		if (source == this.vistaPrincipal.getPanelMenu().getLblFotoHorario()) {
			mAbrirHorario();
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoOtros()) {
			mAbrirHorarioOtros();
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoReuniones()) {
			mAbrirReuniones();
		}

	}

	private void mAbrirReuniones() {
		// TODO Auto-generated method stub
		try {
			dos.writeObject(4);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void mAbrirHorarioOtros() {
		// TODO Auto-generated method stub
		try {
			dos.writeObject(3);
			dos.flush();
			dos.writeObject(id);
			dos.flush();
			profesores = (ArrayList<Profesor>) dis.readObject();
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_LISTA);
			ArrayList<String> modelo = new ArrayList<String>();
			for (Profesor profesor : profesores) {
				modelo.add(profesor.getNombre());
			}
			this.vistaPrincipal.getPanelLista().getBtnSeleccionar().setVisible(true);
			cargarLista(modelo);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cargarLista(ArrayList<String> datos) {
		// TODO Auto-generated method stub
		String[] arrayDatos = datos.toArray(new String[0]);

		DefaultListModel<String> modelo = new DefaultListModel<>();
		for (String dato : arrayDatos) {
			modelo.addElement(dato);
		}

		this.vistaPrincipal.getPanelLista().getListaProfesor().setModel(modelo);
	}

	private void mAbrirHorario() {
		// TODO Auto-generated method stub
		try {
			dos.writeObject(2);
			dos.flush();
			dos.writeObject(id);
			dos.flush();

			String[][] horario = (String[][]) dis.readObject();
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);
			cargarHorario(horario, this.vistaPrincipal.getPanelHorario().getTablaHorario());
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cargarHorario(String[][] horario, JTable tabla) {
		// TODO Auto-generated method stub

		// Crear un modelo de tabla no editable
		DefaultTableModel modelo = new DefaultTableModel(horario,
				new String[] { "Hora/Día", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabla.setModel(modelo);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
