package modelo;
// Generated 13 ene 2025, 12:32:46 by Hibernate Tools 6.5.1.Final

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Reuniones generated by hbm2java
 */
public class Reuniones implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idReunion;
	private Users usersByProfesorId;
	private Users usersByAlumnoId;
	private String estado;
	private String estadoEus;
	private String idCentro;
	private String titulo;
	private String asunto;
	private String aula;
	private Timestamp fecha;

	public Reuniones() {
	}

	public Reuniones(Users usersByProfesorId, Users usersByAlumnoId) {
		this.usersByProfesorId = usersByProfesorId;
		this.usersByAlumnoId = usersByAlumnoId;
	}

	public Reuniones(Integer idReunion, String estado, String idCentro, String titulo, String asunto, String aula,
			Timestamp fecha) {
		super();
		this.idReunion = idReunion;
		this.estado = estado;
		this.idCentro = idCentro;
		this.titulo = titulo;
		this.asunto = asunto;
		this.aula = aula;
		this.fecha = fecha;
	}

	public Reuniones(Users usersByProfesorId, Users usersByAlumnoId, String estado, String estadoEus, String idCentro,
			String titulo, String asunto, String aula, Timestamp fecha) {
		this.usersByProfesorId = usersByProfesorId;
		this.usersByAlumnoId = usersByAlumnoId;
		this.estado = estado;
		this.estadoEus = estadoEus;
		this.idCentro = idCentro;
		this.titulo = titulo;
		this.asunto = asunto;
		this.aula = aula;
		this.fecha = fecha;
	}

	public Integer getIdReunion() {
		return this.idReunion;
	}

	public void setIdReunion(Integer idReunion) {
		this.idReunion = idReunion;
	}

	public Users getUsersByProfesorId() {
		return this.usersByProfesorId;
	}

	public void setUsersByProfesorId(Users usersByProfesorId) {
		this.usersByProfesorId = usersByProfesorId;
	}

	public Users getUsersByAlumnoId() {
		return this.usersByAlumnoId;
	}

	public void setUsersByAlumnoId(Users usersByAlumnoId) {
		this.usersByAlumnoId = usersByAlumnoId;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEus() {
		return this.estadoEus;
	}

	public void setEstadoEus(String estadoEus) {
		this.estadoEus = estadoEus;
	}

	public String getIdCentro() {
		return this.idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return this.aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public ArrayList<Reuniones> getReunionesById(int idUsuario) {
		// TODO Auto-generated method stub
		ArrayList<Reuniones> reuniones = new ArrayList<Reuniones>();
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		System.out.println(java.sql.Date.valueOf(LocalDate.now().with(DayOfWeek.MONDAY)));
		System.out.println(java.sql.Date.valueOf(LocalDate.now().with(DayOfWeek.SUNDAY)));
		String hql = "from Reuniones where usersByProfesorId = " + idUsuario + " and fecha between '"
				+ java.sql.Date.valueOf(LocalDate.now().with(DayOfWeek.MONDAY)) + "' and '"
				+ java.sql.Date.valueOf(LocalDate.now().with(DayOfWeek.SUNDAY))+"'";
		Query q = session.createQuery(hql);
		List<?> filas = q.list();

		for (int i = 0; i < filas.size(); i++) {
			reuniones.add((Reuniones) filas.get(i));
		}

		return reuniones;
	}

	public String[][] getModeloReuniones(ArrayList<Reuniones> reuniones) {
		// TODO Auto-generated method stub
		String[][] planSemanal = { { "1ra", "", "", "", "", "" }, { "2da", "", "", "", "", "" },
				{ "3ra", "", "", "", "", "" }, { "4ta", "", "", "", "", "" }, { "5ta", "", "", "", "", "" } };

		for (int i = 0; i < reuniones.size(); i++) {
			Reuniones reunion = (Reuniones) reuniones.get(i);
			int hora = consegiorHora(reunion.getFecha().toLocalDateTime().getHour());
			planSemanal[hora][reunion.getFecha().toLocalDateTime().getDayOfWeek().getValue()] = reunion.getTitulo()
					+ ponerEstado(reunion.getEstado());
		}

		return planSemanal;
	}

	private String ponerEstado(String estado) {
		// TODO Auto-generated method stub

		if (estado.equalsIgnoreCase("pendiente")) {
			estado = "-P";
		} else if (estado.equalsIgnoreCase("aceptada")) {
			estado = "-C";
		} else if (estado.equalsIgnoreCase("denegada")) {
			estado = "-R";
		} else if (estado.equalsIgnoreCase("conflicto")) {
			estado = "-E";
		}
		return estado;
	}

	private int consegiorHora(int hora) {
		// TODO Auto-generated method stub
		if (hora == 8) {
			hora = 0;
		} else if (hora == 9) {
			hora = 1;
		} else if (hora == 10) {
			hora = 2;
		} else if (hora == 11) {
			hora = 3;
		} else if (hora == 12) {
			hora = 4;
		}
		return hora;
	}

	public void cambiarEstadoReunion(int idReunionModificar, String estado) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();
		tx = session.beginTransaction();
		String hql = "from Reuniones where idReunion = " + idReunionModificar + " ";
		Query q = session.createQuery(hql);
		Reuniones reunion = (Reuniones) q.uniqueResult();
		reunion.setEstado(estado);
		session.update(reunion);
		tx.commit();
	}

}
