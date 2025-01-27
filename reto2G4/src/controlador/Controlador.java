package controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Centros;
import modelo.Profesor;
import modelo.Reuniones;
import vista.Principal;
import vista.Principal.enumAcciones;

public class Controlador implements ActionListener, MouseListener {

	private vista.Principal vistaPrincipal;
	private Socket cliente;
	private DataOutputStream dos;
	private ObjectOutputStream oos;
	private DataInputStream dis;
	private ObjectInputStream ois;
	private int id = 0;
	private int tipo = 0;
	private ArrayList<Profesor> profesores = new ArrayList<Profesor>();
	private ArrayList<Reuniones> reuniones = new ArrayList<Reuniones>();
	private ArrayList<Centros> centros = new ArrayList<Centros>();

	public Controlador(vista.Principal vistaPrincipal) {
		this.vistaPrincipal = vistaPrincipal;
		this.inicializarControlador();
	}

	private void inicializarControlador() {

		// EVENTO CERRAR VENTANA
		this.vistaPrincipal.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					dos.writeInt(6);
					dos.flush();
					dis.close();
					ois.close();
					dos.close();
					oos.close();
					cliente.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.getWindow().dispose();
			}
		});
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
		// VENTANA TAREAS
		this.vistaPrincipal.getPanelTareas().getBtnConfirmar().addActionListener(this);
		this.vistaPrincipal.getPanelTareas().getBtnConfirmar()
				.setActionCommand(Principal.enumAcciones.CONFIRMAR_REUNION.toString());

		this.vistaPrincipal.getPanelTareas().getBtnRechazar().addActionListener(this);
		this.vistaPrincipal.getPanelTareas().getBtnRechazar()
				.setActionCommand(Principal.enumAcciones.RECHAZAR_REUNION.toString());

		this.vistaPrincipal.getPanelTareas().getBtnVolver().addActionListener(this);
		this.vistaPrincipal.getPanelTareas().getBtnVolver().setActionCommand(Principal.enumAcciones.VOLVER.toString());

		this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {

						habilitarBotones();
					}
				});

		// VENTANA LISTA
		this.vistaPrincipal.getPanelLista().getBtnSeleccionar().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnSeleccionar()
				.setActionCommand(Principal.enumAcciones.SELECCIONAR_PROFESOR.toString());

		this.vistaPrincipal.getPanelLista().getBtnVolver().addActionListener(this);
		this.vistaPrincipal.getPanelLista().getBtnVolver().setActionCommand(Principal.enumAcciones.VOLVER.toString());

	}

	protected void habilitarBotones() {
		// TODO Auto-generated method stub
		if (this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow() != -1) {
			if (reuniones.get(this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow()).getEstado()
					.equalsIgnoreCase("pendiente")
					|| reuniones.get(this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow())
							.getEstado().equalsIgnoreCase("conflicto")) {
				this.vistaPrincipal.getPanelTareas().getBtnConfirmar().setEnabled(true);
				this.vistaPrincipal.getPanelTareas().getBtnRechazar().setEnabled(true);
			} else {
				this.vistaPrincipal.getPanelTareas().getBtnConfirmar().setEnabled(false);
				this.vistaPrincipal.getPanelTareas().getBtnRechazar().setEnabled(false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Principal.enumAcciones accion = Principal.enumAcciones.valueOf(e.getActionCommand());

		switch (accion) {
		case LOGIN:
			incializarServidor();
			try {
				centros = (ArrayList<Centros>) ois.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.mConfirmarLogin(accion);

			break;
		case DESCONECTAR:
			try {
				dos.writeInt(6);
				dos.flush();
				dis.close();
				ois.close();
				dos.close();
				oos.close();

				cliente.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_LOGIN);

			break;
		case VOLVER:
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_MENU);
			break;
		case TAREAS_PENDIENTES:
			cargarPendiendes();
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_TAREAS);
			break;
		case SELECCIONAR_PROFESOR:
			seleccionarProfesor();
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);
			break;
		case CONFIRMAR_REUNION:
			cambiarTarea("aceptada");
			break;
		case RECHAZAR_REUNION:
			cambiarTarea("denegada");
			break;

		default:
			break;

		}
	}

	private void cambiarTarea(String estado) {
		// TODO Auto-generated method stub
		try {
			dos.writeInt(5);
			dos.flush();
			dos.writeInt(reuniones.get(this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow())
					.getIdReunion());
			dos.flush();
			dos.writeUTF(estado);
			dos.flush();
			this.vistaPrincipal.getPanelTareas().getTablaHorario().getModel().setValueAt(estado,
					this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow(), 5);
			reuniones.get(this.vistaPrincipal.getPanelTareas().getTablaHorario().getSelectedRow()).setEstado(estado);
			this.vistaPrincipal.getPanelTareas().getBtnConfirmar().setEnabled(false);
			this.vistaPrincipal.getPanelTareas().getBtnRechazar().setEnabled(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	private void cargarPendiendes() {
		// TODO Auto-generated method stub
		DefaultTableModel modelo = new DefaultTableModel(
				new String[] { "Titulo", "Asunto", "Centro", "Aula", "Fecha", "Estado" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
		for (Reuniones reunion : reuniones) {
			String centroReu = "";
			for (Centros centro : centros) {
				if (centro.getIdCentro() == Integer.parseInt(reunion.getIdCentro())) {
					centroReu = centro.getNombre();
				}
			}
			Object[] fila = { reunion.getTitulo(), reunion.getAsunto(), centroReu, reunion.getAula(),
					reunion.getFecha().toString(), reunion.getEstado() };
			modelo.addRow(fila);
		}
		this.vistaPrincipal.getPanelTareas().getTablaHorario().setModel(modelo);
	}

	private void incializarServidor() {
		// TODO Auto-generated method stub
		try {
			cliente = new Socket("localhost", 2000);
			dos = new DataOutputStream(cliente.getOutputStream());
			oos = new ObjectOutputStream(cliente.getOutputStream());
			dis = new DataInputStream(cliente.getInputStream());
			ois = new ObjectInputStream(cliente.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void seleccionarProfesor() {
		// TODO Auto-generated method stub
		if (!this.vistaPrincipal.getPanelLista().getListaProfesor().isSelectionEmpty()) {
			try {
				dos.writeInt(2);
				dos.flush();
				int idprofesor = 0;
				for (Profesor profesor : profesores) {
					if (profesor.getNombre()
							.equals(this.vistaPrincipal.getPanelLista().getListaProfesor().getSelectedValue())) {
						idprofesor = profesor.getId();
					}
				}
				dos.writeInt(idprofesor);
				dos.flush();
				cargarHorario((String[][]) ois.readObject(), this.vistaPrincipal.getPanelHorario().getTablaHorario());

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
		String contrasenaCifrada = "";
		try {
			dos.writeInt(1);
			dos.flush();
			dos.writeUTF(this.vistaPrincipal.getPanelLogin().getTextFieldUser().getText());
			dos.flush();
			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte dataBytes[] = new String(this.vistaPrincipal.getPanelLogin().getTextFieldPass().getPassword())
						.getBytes();
				md.update(dataBytes);
				byte resumen[] = md.digest();
				contrasenaCifrada = new String(resumen);

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			// CAMBIAR*********************************************************************************************************************
			// dos.writeUTF(contrasenaCifrada);
			dos.writeUTF(contrasenaCifrada);

			dos.flush();
			id = dis.readInt();
			tipo = dis.readInt();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (id != 0 && tipo == 3) {
			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_MENU);
		} else {
			JOptionPane.showMessageDialog(null, "No existe ningun profesor con esas credenciales");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		Object source = e.getSource();
		this.vistaPrincipal.getPanelHorario().getBtnPendientes().setVisible(false);
		if (source == this.vistaPrincipal.getPanelMenu().getLblFotoHorario()) {
			mAbrirHorario();
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoOtros()) {
			mAbrirHorarioOtros();
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoReuniones()) {
			mAbrirReuniones();
			this.vistaPrincipal.getPanelHorario().getBtnPendientes().setVisible(true);
		}

	}

	@SuppressWarnings("unchecked")
	private void mAbrirReuniones() {
		// TODO Auto-generated method stub
		this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);

		try {
			dos.writeInt(4);
			dos.flush();
			dos.writeInt(id);
			dos.flush();
			reuniones = (ArrayList<Reuniones>) ois.readObject();
			cargarHorario((String[][]) ois.readObject(), this.vistaPrincipal.getPanelHorario().getTablaHorario());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


	@SuppressWarnings("unchecked")
	private void mAbrirHorarioOtros() {
		// TODO Auto-generated method stub

		this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_LISTA);
		profesores.removeAll(profesores);
		try {
			dos.writeInt(3);
			dos.flush();
			dos.writeInt(id);
			dos.flush();
			profesores = (ArrayList<Profesor>) ois.readObject();

			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_LISTA);
			ArrayList<String> modelo = new ArrayList<String>();
			for (Profesor profesor : profesores) {
				modelo.add(profesor.getNombre());
			}
			this.vistaPrincipal.getPanelLista().getBtnSeleccionar().setVisible(true);
			cargarLista(modelo);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void cargarLista(ArrayList<String> datos) { // TODO Auto-generated
		String[] arrayDatos = datos.toArray(new String[0]);

		DefaultListModel<String> modelo = new DefaultListModel<>();
		for (String dato : arrayDatos) {
			modelo.addElement(dato);
		}

		this.vistaPrincipal.getPanelLista().getListaProfesor().setModel(modelo);
	}

	private void mAbrirHorario() {
		// TODO Auto-generated method stub
		this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);

		try {
			dos.writeInt(2);
			dos.flush();
			dos.writeInt(id);
			dos.flush();

			String[][] horario = (String[][]) ois.readObject();

			this.vistaPrincipal.mVisualizarPaneles(enumAcciones.CARGAR_PANEL_HORARIO);
			cargarHorario(horario, this.vistaPrincipal.getPanelHorario().getTablaHorario());
		} catch (IOException | ClassNotFoundException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void cargarHorario(String[][] horario, JTable tabla) {
		
		DefaultTableModel modelo = new DefaultTableModel(horario,
				new String[] { "Hora/Día", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};

		tabla.setModel(modelo);

		DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				JTextArea textArea = new JTextArea();
				textArea.setText(value == null ? "" : value.toString());
				textArea.setWrapStyleWord(true); 
				textArea.setLineWrap(true); 
				textArea.setOpaque(true); 
				
				if (value != null && value instanceof String) {
					String cellValue = (String) value;

					if (cellValue.contains("-R")) {
						textArea.setBackground(Color.RED);
						textArea.setForeground(Color.BLACK);
					} else if (cellValue.contains("-C")) {
						textArea.setBackground(Color.GREEN);
						textArea.setForeground(Color.BLACK);
					} else if (cellValue.contains("-P")) {
						textArea.setBackground(Color.GRAY);
						textArea.setForeground(Color.BLACK);
					} else if (cellValue.contains("-E")) {
						textArea.setBackground(Color.ORANGE);
						textArea.setForeground(Color.BLACK);
					} else {
						textArea.setBackground(table.getBackground());
						textArea.setForeground(table.getForeground());
					}
				}

				if (isSelected) {
					textArea.setBackground(table.getSelectionBackground());
					textArea.setForeground(table.getSelectionForeground());
				}

				return textArea;
			}
		};

		
		for (int i = 1; i < tabla.getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setCellRenderer(renderizador);
		}


		tabla.setRowHeight(75); 
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
