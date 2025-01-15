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

import javax.swing.JOptionPane;

import modelo.Horarios;
import modelo.Users;
import vista.Principal;
import vista.Principal.enumAcciones;

public class Controlador implements ActionListener, MouseListener {

	private vista.Principal vistaPrincipal;
	private Socket cliente;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private int id = 0;

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
		default:
			break;

		}
	}

	private void mConfirmarLogin(enumAcciones accion) {
		// TODO Auto-generated method stub

		try {
			dos.writeInt(1);
			dos.flush();
			dos.writeUTF(this.vistaPrincipal.getPanelLogin().getTextFieldUser().getText());
			dos.flush();
			dos.writeUTF(new String(this.vistaPrincipal.getPanelLogin().getTextFieldPass().getPassword()));
			dos.flush();
			id = dis.readInt();
		} catch (IOException e) {
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
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoOtros()) {;
			mAbrirHorarioOtros();
		} else if (source == this.vistaPrincipal.getPanelMenu().getLblFotoReuniones()) {
			mAbrirReuniones();
		}

	}

	private void mAbrirReuniones() {
		// TODO Auto-generated method stub
		try {
			dos.writeInt(4);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void mAbrirHorarioOtros() {
		// TODO Auto-generated method stub
		try {
			dos.writeInt(3);
			dos.flush();
			dos.writeInt(id);
			dos.flush();
			ArrayList<Users> profesores = (ArrayList<Users>) dis.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void mAbrirHorario() {
		// TODO Auto-generated method stub
		try {
			dos.writeInt(2);
			dos.flush();
			dos.writeInt(id);
			dos.flush();

			ArrayList<Horarios> horario = (ArrayList<Horarios>) dis.readObject();

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
