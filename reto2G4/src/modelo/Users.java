package modelo;
// Generated 13 ene 2025, 12:32:46 by Hibernate Tools 6.5.1.Final

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Users generated by hbm2java
 */
public class Users implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Tipos tipos;
	private String email;
	private String username;
	private String password;
	private String nombre;
	private String apellidos;
	private String dni;
	private String direccion;
	private Integer telefono1;
	private Integer telefono2;
	private byte[] argazkia;
	private Set<Object> matriculacioneses = new HashSet<Object>(0);
	private Set<Object> reunionesesForProfesorId = new HashSet<Object>(0);
	private Set<Object> reunionesesForAlumnoId = new HashSet<Object>(0);
	private Set<Object> horarioses = new HashSet<Object>(0);

	public Users() {
	}

	public Users(int id, Tipos tipos) {
		this.id = id;
		this.tipos = tipos;
	}

	public Users(int id, Tipos tipos, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia,
			Set<Object> matriculacioneses, Set<Object> reunionesesForProfesorId, Set<Object> reunionesesForAlumnoId,
			Set<Object> horarioses) {
		this.id = id;
		this.tipos = tipos;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.direccion = direccion;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.argazkia = argazkia;
		this.matriculacioneses = matriculacioneses;
		this.reunionesesForProfesorId = reunionesesForProfesorId;
		this.reunionesesForAlumnoId = reunionesesForAlumnoId;
		this.horarioses = horarioses;
	}

	public Users(int idUsuario) {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tipos getTipos() {
		return this.tipos;
	}

	public void setTipos(Tipos tipos) {
		this.tipos = tipos;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(Integer telefono1) {
		this.telefono1 = telefono1;
	}

	public Integer getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(Integer telefono2) {
		this.telefono2 = telefono2;
	}

	public byte[] getArgazkia() {
		return this.argazkia;
	}

	public void setArgazkia(byte[] argazkia) {
		this.argazkia = argazkia;
	}

	public Set<Object> getMatriculacioneses() {
		return this.matriculacioneses;
	}

	public void setMatriculacioneses(Set<Object> matriculacioneses) {
		this.matriculacioneses = matriculacioneses;
	}

	public Set<Object> getReunionesesForProfesorId() {
		return this.reunionesesForProfesorId;
	}

	public void setReunionesesForProfesorId(Set<Object> reunionesesForProfesorId) {
		this.reunionesesForProfesorId = reunionesesForProfesorId;
	}

	public Set<Object> getReunionesesForAlumnoId() {
		return this.reunionesesForAlumnoId;
	}

	public void setReunionesesForAlumnoId(Set<Object> reunionesesForAlumnoId) {
		this.reunionesesForAlumnoId = reunionesesForAlumnoId;
	}

	public Set<Object> getHorarioses() {
		return this.horarioses;
	}

	public void setHorarioses(Set<Object> horarioses) {
		this.horarioses = horarioses;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", tipos=" + tipos + ", email=" + email + ", username=" + username + ", password="
				+ password + ", nombre=" + nombre + ", apellidos=" + apellidos + ", dni=" + dni + ", direccion="
				+ direccion + ", telefono1=" + telefono1 + ", telefono2=" + telefono2 + ", argazkia="
				+ Arrays.toString(argazkia) + ", matriculacioneses=" + matriculacioneses + ", reunionesesForProfesorId="
				+ reunionesesForProfesorId + ", reunionesesForAlumnoId=" + reunionesesForAlumnoId + ", horarioses="
				+ horarioses + "]";
	}

	public int Login(String usuario, String contrasena) {
		// TODO Auto-generated method stub

		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users where username = '" + usuario + "' AND password = '" + contrasena
				+ "' AND tipos.name = 'profesor' ";
		Query q = session.createQuery(hql);
		Users usuarioComprobado = (Users) q.uniqueResult();
		if (usuarioComprobado == null) {
			return 0;
		} else {
			return usuarioComprobado.id;
		}
	}

	public String[][] getHorarioById(int idUsuario) {
		// TODO Auto-generated method stub
		String[][] planSemanal = {
				{ "1ra", "", "", "", "", "", "", "" }, { "2da", "", "", "", "", "", "", "" },
				{ "3ra", "", "", "", "", "", "", "" }, { "4ta", "", "", "", "", "", "", "" },
				{ "5ta", "", "", "", "", "", "", "" } };

		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Horarios where users = " + idUsuario + " ";
		Query q = session.createQuery(hql);
		List<?> filas = q.list();

		for (int i = 0; i < filas.size(); i++) {
			Horarios horario = (Horarios) filas.get(i);
			int dia = conseguirDia(horario.getId().getDia());
			int hora = Integer.parseInt(horario.getId().getHora());
			planSemanal[hora-1][dia] = horario.getModulos().getNombre();
		}

		return planSemanal;
	}

	private int conseguirDia(String string) {
		// TODO Auto-generated method stub
		int dia = 0;
		if (string.equals("L/A")) {
			dia = 1;
		} else if (string.equals("M/A")) {
			dia = 2;
		} else if (string.equals("X")) {
			dia = 3;
		} else if (string.equals("J/O")) {
			dia = 4;
		} else if (string.equals("V/O")) {
			dia = 5;
		} else if (string.equals("S/L")) {
			dia = 6;
		} else if (string.equals("D/I")) {
			dia = 7;
		}
		return dia;
	}

	public ArrayList<Profesor> getOtrosProfesores(int idUsuario) {
		ArrayList<Profesor> profesores = new ArrayList<Profesor>();
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users where id != " + idUsuario + " AND tipos.name = 'profesor'";
		Query q = session.createQuery(hql);
		List<?> filas = q.list();

		for (int i = 0; i < filas.size(); i++) {
			Users usuario = (Users) filas.get(i);
			profesores.add(new Profesor(usuario.getId(), usuario.getNombre()));
		}

		return profesores;
	}
	
	public void crearClientePrueba() {
	    SessionFactory sesion = HibernateUtil.getSessionFactory();
	    Session session = sesion.openSession();
	    session.beginTransaction();

	    String hql = "from Users where username = 'cliente_prueba'";
	    Query query = session.createQuery(hql);
	    Users clienteExistente = (Users) query.uniqueResult();

	    if (clienteExistente == null) {
	        Users clientePrueba = new Users();
	        clientePrueba.setUsername("cliente_prueba");
	        clientePrueba.setPassword("1234");
	        clientePrueba.setNombre("Cliente");
	        clientePrueba.setApellidos("Prueba");
	        clientePrueba.setEmail("cliente@prueba.com");
	        Tipos tipo = new Tipos();
	        tipo.setId(3);
	    
	        clientePrueba.setTipos(tipo); 

	        session.save(clientePrueba);
	        System.out.println("Cliente de prueba creado .");
	    } else {
	        System.out.println("El cliente de prueba ya existe.");
	    }

	    session.getTransaction().commit();
	    session.close();
	}


}
