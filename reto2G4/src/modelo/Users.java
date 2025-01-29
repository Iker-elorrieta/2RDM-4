package modelo;
// Generated 29 ene 2025, 8:10:48 by Hibernate Tools 6.5.1.Final

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
	private String idioma;
	private Set<Object> matriculacioneses = new HashSet<Object>(0);
	private Set<Object> reunionesesForProfesorId = new HashSet<Object>(0);
	private Set<Object> reunionesesForAlumnoId = new HashSet<Object>(0);
	private Set<Object> horarioses = new HashSet<Object>(0);

	public Users() {
	}

	public Users(int id, Tipos tipos, String idioma) {
		this.id = id;
		this.tipos = tipos;
		this.idioma = idioma;
	}

	public Users(int id, Tipos tipos, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia, String idioma,
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
		this.idioma = idioma;
		this.matriculacioneses = matriculacioneses;
		this.reunionesesForProfesorId = reunionesesForProfesorId;
		this.reunionesesForAlumnoId = reunionesesForAlumnoId;
		this.horarioses = horarioses;
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

	public String getIdioma() {
		return this.idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
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

	public Users Login(String usuario, String contrasena) {
		Users usuarioComprobado = null;
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users where tipos.name = :tipoProfesor or tipos.name = :tipoAlumno";
		Query q = session.createQuery(hql);
		q.setParameter("tipoProfesor", "profesor");
		q.setParameter("tipoAlumno", "alumno");
		List<?> usuarios = q.list();
		for (int i = 0; i < usuarios.size(); i++) {

			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte dataBytes[] = ((Users) usuarios.get(i)).getPassword().getBytes();
				md.update(dataBytes);
				byte resumen[] = md.digest();
				((Users) usuarios.get(i)).setPassword(new String(resumen));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (((Users) usuarios.get(i)).getUsername().equalsIgnoreCase(usuario)
					&& ((Users) usuarios.get(i)).getPassword().equalsIgnoreCase(contrasena)) {
				usuarioComprobado = (Users) usuarios.get(i);
			}
		}
		return usuarioComprobado;

	}

	public String[][] getHorarioById(int idUsuario) {
		// TODO Auto-generated method stub
		String[][] planSemanal = { { "1ra", "", "", "", "", "" }, { "2da", "", "", "", "", "" },
				{ "3ra", "", "", "", "", "" }, { "4ta", "", "", "", "", "" }, { "5ta", "", "", "", "", "" } };

		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Horarios where users = " + idUsuario + " ";
		Query q = session.createQuery(hql);
		List<?> filas = q.list();

		for (int i = 0; i < filas.size(); i++) {
			Horarios horario = (Horarios) filas.get(i);
			int dia = conseguirDia(horario.getId().getDia());
			int hora = Integer.parseInt(horario.getId().getHora());
			planSemanal[hora - 1][dia] = horario.getModulos().getNombre();
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

	public void cifrarContrasenas() {
		Transaction tx = null;
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		tx = session.beginTransaction();
		String hql = "from Users";
		Query q = session.createQuery(hql);
		List<?> usuarios = q.list();
		for (int i = 0; i < usuarios.size(); i++) {

			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte dataBytes[] = ((Users) usuarios.get(i)).getPassword().getBytes();
				md.update(dataBytes);
				byte resumen[] = md.digest();
				System.out.println("Resumen: " + new String(resumen));
				((Users) usuarios.get(i)).setPassword(new String(resumen));
				session.update(usuarios.get(i));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

		}

		tx.commit();
	}

	// Añado esto
	public ArrayList<Users> getProfesores() {

		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users where  tipos.name = 'profesor'";
		Query q = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Users> filas = q.list();
		return (ArrayList<Users>) filas;

	}

	public ArrayList<Users> getAlumnos() {
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users where  tipos.name = 'alumno'";
		Query q = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Users> filas = q.list();
		return (ArrayList<Users>) filas;

	}

	public void actualizarIdioma(int idUsuario, String nuevoIdioma) {
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		Transaction tx = null;

		try {
			tx = (Transaction) session.beginTransaction();

			// Obtenemos el usuario de la base de datos
			Users usuario = session.get(Users.class, idUsuario);

			if (usuario != null) {
				// Actualizamos el idioma preferido
				usuario.setIdioma(nuevoIdioma);
				// Guardamos el usuario actualizado
				session.save(usuario);
			}

			// Completamos la transacción
			tx.commit();
			System.out.println("Idiomna preferido" + nuevoIdioma);
			System.out.println("Idioma actualizado correctamente");

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback(); // Si ocurre un error, revertimos los cambios
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private String cicloNombre;
	private int curso;

	public Users(int id, Tipos tipos, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia, String cicloNombre,
			int curso) {
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
		this.cicloNombre = cicloNombre;
		this.curso = curso;
	}

	public String getCicloNombre() {
		return cicloNombre;
	}

	public void setCicloNombre(String cicloNombre) {
		this.cicloNombre = cicloNombre;
	}

	public int getCurso() {
		return curso;
	}

	public void setCurso(int curso) {
		this.curso = curso;
	}

	public Users obtenerPerfilA(int idUsuario) {
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users u where u.id = :alumnoId";
		Query query = session.createQuery(hql);
		query.setParameter("alumnoId", idUsuario);

		Users clienteExistente = (Users) query.uniqueResult();
		String nombreCiclo;
		int curso;
		if (clienteExistente != null) {
			Set<Object> matriculacionesSet = clienteExistente.getMatriculacioneses();
			for (Object obj : matriculacionesSet) {
				if (obj instanceof Matriculaciones) {
					Matriculaciones matriculacion = (Matriculaciones) obj;

					Ciclos ciclo = matriculacion.getCiclos();
					nombreCiclo = ciclo.getNombre();
					curso = matriculacion.getId().getCurso();
					clienteExistente.setCicloNombre(nombreCiclo);
					clienteExistente.setCurso(curso);
				}
			}
		}
		return clienteExistente;
	}

	public Users obtenerPerfilP(int idUsuario) {
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		String hql = "from Users u where u.id = :profesorId";
		Query query = session.createQuery(hql);
		query.setParameter("profesorId", idUsuario);

		Users clienteExistente = (Users) query.uniqueResult();
		return clienteExistente;
	}

	public ArrayList<Users> obtenerAlumnosPorProfesor(int idProfesor) {
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		// Pte
		String hql = "SELECT DISTINCT u " + "FROM Matriculaciones m " + "JOIN Users u ON m.id.alumId = u.id "
				+ "JOIN Modulos mod ON m.id.cicloId = mod.ciclos.id " + "JOIN Horarios h ON h.modulos.id = mod.id "
				+ "WHERE h.id.profeId = :profesorId";

		Query q = session.createQuery(hql);
		q.setParameter("profesorId", idProfesor);
		@SuppressWarnings("unchecked")
		ArrayList<Users> filas = (ArrayList<Users>) q.list();

		for (Users u : filas) {
			System.out.println("Usuario " + u.toString());
		}

		return new ArrayList<>(filas);
	}
}
