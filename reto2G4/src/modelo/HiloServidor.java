package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {

	Socket conexionCli;

	public HiloServidor(Socket conexionCli) {
		// TODO Auto-generated constructor stub
		this.conexionCli = conexionCli;
	}

	public void run() {

		int opcion = 0;
		boolean terminar = false;

		try {
			DataOutputStream dos = new DataOutputStream(conexionCli.getOutputStream());
			ObjectOutputStream oos = new ObjectOutputStream(conexionCli.getOutputStream());
			DataInputStream dis = new DataInputStream(conexionCli.getInputStream());
			ObjectInputStream ois = new ObjectInputStream(conexionCli.getInputStream());

			oos.writeObject(new Centros().obtenerCentros());
			oos.flush();
			while (!terminar) {

				opcion = dis.readInt();
				switch (opcion) {
				case 1:
					login(dis, dos);
					break;
				case 2:
					verHorario(dis, oos);
					break;
				case 3:
					verOtrosHorarios(dis, oos);
					break;

				case 4:
					verReuniones(dis, oos);
					break;
				case 5:
					cambiarEstadoReunion(dis, oos);
					break;
				case 6:
					terminar = true;
					break;
				default:

					break;
				}

			}
			ois.close();
			dis.close();
			oos.close();
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cambiarEstadoReunion(DataInputStream dis, ObjectOutputStream oos) {
		// TODO Auto-generated method stub
		try {
			int idReunion = dis.readInt();
			String estado = dis.readUTF();
			new Reuniones().cambiarEstadoReunion(idReunion, estado);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// BLOQUEADO PARA ESTE SPRING

	private void verReuniones(DataInputStream dis, ObjectOutputStream oos) {
		// TODO Auto-generated method stub
		try {
			int idUsuario = (int) dis.readInt();
			ArrayList<Reuniones> reuniones = new Reuniones().getReunionesById(idUsuario);
			String[][] reunionesModelo = new Reuniones().getModeloReuniones(reuniones);
			oos.writeObject(reuniones);
			oos.flush();
			oos.writeObject(reunionesModelo);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void verOtrosHorarios(DataInputStream dis, ObjectOutputStream oos) { // TODO Auto-generated method stub
		try {
			int idUsuario = dis.readInt();
			ArrayList<String> profesores = new Users().getOtrosProfesores(idUsuario);
			oos.writeObject(profesores);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void verHorario(DataInputStream dis, ObjectOutputStream dos) { //
		try {
			int idUsuario = dis.readInt();
			String[][] horario = new Users().getHorarioById(idUsuario);
			dos.writeObject(horario);
			dos.flush();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void login(DataInputStream dis, DataOutputStream dos) {
		// TODO Auto-generated method stub

		try {
			String usuario = dis.readUTF();
			String password = dis.readUTF();
			int usuarioComprobado = new Users().Login(usuario, password);
			dos.writeInt(usuarioComprobado);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
