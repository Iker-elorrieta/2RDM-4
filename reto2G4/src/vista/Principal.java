package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Principal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Acciones
	public static enum enumAcciones {
		CARGAR_PANEL_LOGIN, CARGAR_PANEL_MENU, LOGIN, DESCONECTAR, CARGAR_PANEL_HORARIO, CARGAR_PANEL_LISTA, VOLVER,
		TAREAS_PENDIENTES, CONFIRMAR_REUNION, RECHAZAR_REUNION, SELECCIONAR_PROFESOR, CARGAR_PANEL_TAREAS

	}

	private JPanel panelContenedor;
	private PanelLogin panelLogin;
	private PanelMenu panelMenu;
	private PanelHorario panelHorario;
	private PanelLista panelLista;
	private PanelTareas panelTareas;

	public Principal() {
		setResizable(false);

		// Panel que contiene todo el contenido de la ventana
		mCrearPanelContenedor();
		// Panel que contiene el listado de contactos.
		mCrearPanelLogin();
		mCrearPanelMenu();
		mCrearPanelHorario();
		mCrearPanelLista();
		mCrearPanelTareas();
	}

	private void mCrearPanelTareas() {
		// TODO Auto-generated method stub
		panelTareas = new PanelTareas();
		panelTareas.setLocation(0, 11);
		panelContenedor.add(panelTareas);
		panelContenedor.setBounds(panelTareas.getBounds());
		panelTareas.setVisible(false);
	}

	private void mCrearPanelLista() {
		// TODO Auto-generated method stub
		panelLista = new PanelLista();
		panelLista.setLocation(0, 11);
		panelContenedor.add(panelLista);
		panelContenedor.setBounds(panelLista.getBounds());
		panelLista.setVisible(false);
	}

	private void mCrearPanelHorario() {
		// TODO Auto-generated method stub
		panelHorario = new PanelHorario();
		panelHorario.setLocation(0, 11);
		panelContenedor.add(panelHorario);
		panelContenedor.setBounds(panelHorario.getBounds());
		panelHorario.setVisible(false);
	}

	private void mCrearPanelMenu() {
		// TODO Auto-generated method stub
		panelMenu = new PanelMenu();
		panelMenu.setLocation(0, 11);
		panelContenedor.add(panelMenu);
		panelContenedor.setBounds(panelMenu.getBounds());
		panelMenu.setVisible(false);
	}

	// *** Creaci�n de paneles ***

	private void mCrearPanelContenedor() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(288, 11, 840, 700);
		panelContenedor = new JPanel();
		panelContenedor.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelContenedor);
		panelContenedor.setLayout(null);

	}

	private void mCrearPanelLogin() {
		panelLogin = new PanelLogin();
		panelLogin.setLocation(0, 11);
		panelContenedor.add(panelLogin);
		panelContenedor.setBounds(panelLogin.getBounds());
		panelLogin.setVisible(true);
	}

	// *** FIN creaci�n de paneles ***

	public void mVisualizarPaneles(enumAcciones panel) {
		panelLogin.setVisible(false);
		panelMenu.setVisible(false);
		panelHorario.setVisible(false);
		panelLista.setVisible(false);
		panelTareas.setVisible(false);
		switch (panel) {
		case CARGAR_PANEL_LOGIN:
			panelLogin.setVisible(true);
			break;
		case CARGAR_PANEL_MENU:
			panelMenu.setVisible(true);
			break;
		case CARGAR_PANEL_HORARIO:
			panelHorario.setVisible(true);
			break;
		case CARGAR_PANEL_LISTA:
			panelLista.setVisible(true);
			break;
		case CARGAR_PANEL_TAREAS:
			panelTareas.setVisible(true);
		default:
			break;

		}
	}

	public JPanel getPanelContenedor() {
		return panelContenedor;
	}

	public void setPanelContenedor(JPanel panelContenedor) {
		this.panelContenedor = panelContenedor;
	}

	public PanelLogin getPanelLogin() {
		return panelLogin;
	}

	public void setPanelLogin(PanelLogin panelLogin) {
		this.panelLogin = panelLogin;
	}

	public PanelMenu getPanelMenu() {
		return panelMenu;
	}

	public void setPanelMenu(PanelMenu panelMenu) {
		this.panelMenu = panelMenu;
	}

	public PanelHorario getPanelHorario() {
		return panelHorario;
	}

	public void setPanelHorario(PanelHorario panelHorario) {
		this.panelHorario = panelHorario;
	}

	public PanelLista getPanelLista() {
		return panelLista;
	}

	public void setPanelLista(PanelLista panelLista) {
		this.panelLista = panelLista;
	}

	public PanelTareas getPanelTareas() {
		return panelTareas;
	}

	public void setPanelTareas(PanelTareas panelTareas) {
		this.panelTareas = panelTareas;
	}

}