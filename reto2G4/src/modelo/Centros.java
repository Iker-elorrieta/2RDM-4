package modelo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Centros implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idCentro;
	private String nombre;

	public int getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(int idCentro) {
		this.idCentro = idCentro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Centros(int idCentro, String nombre) {

		this.idCentro = idCentro;
		this.nombre = nombre;
	}

	public Centros() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Centros> obtenerCentros() {
		final String url = "archivos/Centros-Lat-Lon.json"; 
		ArrayList<Centros> centros = new ArrayList<>();
		JsonParser parser = new JsonParser();
		try {
			FileReader fr = new FileReader(url);
			JsonElement datos = parser.parse(fr);
			JsonObject jsonObject = datos.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("CENTROS");
			Iterator<JsonElement> iter = array.iterator();
			while (iter.hasNext()) {
				JsonElement entrada = iter.next();
				JsonObject objeto = entrada.getAsJsonObject();
				Centros centro = new Centros(objeto.get("CCEN").getAsInt(), objeto.get("NOM").getAsString());
				centros.add(centro);
			}

		} catch (FileNotFoundException e) {
			System.err.println("Error: archivo no encontrado en la ruta especificada.");
			e.printStackTrace();
		}
		return centros;
	}
}
