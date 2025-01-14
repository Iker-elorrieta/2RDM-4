package modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
				default:
					break;
				}

			}
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
			Users usuarioComprobado = new Users().Login(usuario, password);
			dos.writeObject(usuarioComprobado);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
