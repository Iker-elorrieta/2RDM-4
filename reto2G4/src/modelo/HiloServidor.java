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
				case 7:
					obtenerProfesoresAlumno(dis, oos);
					break;
				case 8:
					crearReunion(ois, dos);
					break;

				case 9:
					actualizarIdioma(dis, dos);
					break;

				case 10:
					datosPerfil(dis, oos);
					break;
				case 11:
					obtnerAlumnosPorProfesor(dis, oos);
					break;
				case 12:
					consultarHorarioAlumno(dis,oos);
					break;
				case 13:
					loginAndroid(dis, dos);
					break;
				case 14:
					obtenerCiclos(dis,oos);
					break;
				case 15:
					getReunionesbyID(dis,oos);
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

	private void getReunionesbyID(DataInputStream dis, ObjectOutputStream oos) throws IOException {
		int idUsuario = (int) dis.readInt();
		ArrayList<Reuniones> reuniones = new Reuniones().getReunionesById(idUsuario);
		oos.writeObject(reuniones);
		oos.flush();
	}

	private void obtenerCiclos(DataInputStream dis, ObjectOutputStream oos) throws IOException {
		// TODO Auto-generated method stub        
		ArrayList<Ciclos> horario = new Ciclos().getCiclos();
		oos.writeObject(horario);
		oos.flush();

	}

	private void consultarHorarioAlumno(DataInputStream dis, ObjectOutputStream oos) throws IOException {
		// TODO Auto-generated method stub        

		int idUsuario = dis.readInt();
		String[][] horario = new Users().getHorarioAlumno(idUsuario);
		oos.writeObject(horario);
		oos.flush();


	}

	private void loginAndroid(DataInputStream dis, DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub

		String usuario = dis.readUTF();
		String password = dis.readUTF();

		Users usuarioComprobado = new Users().LoginA(usuario, password);

		if (usuarioComprobado != null) {
			dos.writeInt(usuarioComprobado.getId());
			dos.flush();
			dos.writeInt(usuarioComprobado.getTipos().getId());
			dos.flush();
		} else {
			dos.writeInt(0); 
			dos.flush();
			dos.writeInt(0);
			dos.flush();
		}


	}

	// Especifico de Andorid

	private void obtnerAlumnosPorProfesor(DataInputStream dis, ObjectOutputStream oos) throws IOException {

		int idUsuario = dis.readInt(); // id del profesor
		ArrayList<Users> alumnosProfe = new Users().obtenerAlumnosPorProfesor(idUsuario);
		if (alumnosProfe.isEmpty()) {
			oos.writeObject(null); // en caso de que no tenga alumnos
			oos.flush();
		}
		{
			oos.writeObject(alumnosProfe); // Lista de alumnos
			oos.flush();
		}

	}

	private void datosPerfil(DataInputStream dis, ObjectOutputStream oos) throws IOException {
			int idUsuario = dis.readInt();
			int tipo = dis.readInt();
			System.out.println("Id " + idUsuario);
			Users u;
			if (tipo != 3) {
				u = new Users().obtenerPerfilA(idUsuario);
				System.out.println(" Tipo de u " + tipo);

			} else {
				u = new Users().obtenerPerfilP(idUsuario);

			}
			u.toString();
			oos.writeObject(u);
			oos.flush();
	
	}

	private void actualizarIdioma(DataInputStream dis, DataOutputStream dos) throws IOException {
		
			int idUsuario = dis.readInt();
			String nuevoIdioma = dis.readUTF();

			new Users().actualizarIdioma(idUsuario, nuevoIdioma);

			dos.writeUTF("Idioma actualizado con éxito.");
			dos.flush();
	
	}

	private void crearReunion(ObjectInputStream ois, DataOutputStream dos) {
		try {
			String texto = new Reuniones().guardarReunionEnBD((Reuniones) ois.readObject());

			dos.writeUTF(texto);
			dos.flush();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void obtenerProfesoresAlumno(DataInputStream dis, ObjectOutputStream oos) throws IOException {

			int tipo = dis.readInt();

			// SI NO ES PROFESOR
			if (tipo == 3) {
				oos.writeObject(new Users().getProfesores());
			} else {
				oos.writeObject(new Users().getAlumnos());
			}
			oos.flush();
	
	}

	// Aqui para abajo es compartido
	private void cambiarEstadoReunion(DataInputStream dis, ObjectOutputStream oos) throws IOException {
		// TODO Auto-generated method stub
	
			int idReunion = dis.readInt();
			String estado = dis.readUTF();
			new Reuniones().cambiarEstadoReunion(idReunion, estado);
		
	}

	// BLOQUEADO PARA ESTE SPRING

	private void verReuniones(DataInputStream dis, ObjectOutputStream oos) {
		// TODO Auto-generated method stub
		try {
			int idUsuario = (int) dis.readInt();
			ArrayList<Reuniones> reuniones = new Reuniones().getReunionesById(idUsuario);
			String[][] reunionesModelo = new Reuniones().getModeloReuniones(reuniones);
	

			oos.writeObject(reunionesModelo);
			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}








	private void verOtrosHorarios(DataInputStream dis, ObjectOutputStream oos) { // TODO Auto-generated method stub
		try {
			int idUsuario = dis.readInt();
			ArrayList<Profesor> profesores = new Users().getOtrosProfesores(idUsuario);
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

			Users usuarioComprobado = new Users().Login(usuario, password);

			if (usuarioComprobado != null) {
				dos.writeInt(usuarioComprobado.getId());
				dos.flush();
				dos.writeInt(usuarioComprobado.getTipos().getId());
				dos.flush();
			} else {
				dos.writeInt(0); // Para el supuesto en el que no sea correcto
				dos.flush();
				dos.writeInt(0);
				dos.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





}
