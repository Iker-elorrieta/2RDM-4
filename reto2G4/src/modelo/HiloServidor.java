package modelo;

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
			ObjectOutputStream dos = new ObjectOutputStream(conexionCli.getOutputStream());
			ObjectInputStream dis = new ObjectInputStream(conexionCli.getInputStream());
			while (!terminar) {

				opcion = dis.readInt();

				switch (opcion) {
				case 1:
					login(dis, dos);
					break;
				case 2:
					verHorario(dis, dos);
					break;
				case 3:
					verOtrosHorarios(dis, dos);
					break;
				default:
					break;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void verOtrosHorarios(ObjectInputStream dis, ObjectOutputStream dos) {
		// TODO Auto-generated method stub
		try {
			int idUsuario = dis.readInt();
			ArrayList<Users> profesores = new Users().getOtrosProfesores(idUsuario);
			dos.writeObject(profesores);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void verHorario(ObjectInputStream dis, ObjectOutputStream dos) {
		// TODO Auto-generated method stub
		try {
			int idUsuario = dis.readInt();
			ArrayList<Horarios> horarios = new Users(idUsuario).getHorarioById();
			dos.writeObject(horarios);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void login(ObjectInputStream dis, ObjectOutputStream dos) {
		// TODO Auto-generated method stub

		try {
			String usuario = dis.readUTF();
			String password = dis.readUTF();
			int usuarioComprobado = new Users().Login(usuario, password);
			dos.writeObject(usuarioComprobado);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
