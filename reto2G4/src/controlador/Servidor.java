package controlador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import modelo.HiloServidor;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ServerSocket servidor;
		boolean terminar = false;
		try {
			servidor = new ServerSocket(2000);
			while (!terminar) {
				Socket conexionCli = servidor.accept();
				HiloServidor hiloServidor = new HiloServidor(conexionCli);
				hiloServidor.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
