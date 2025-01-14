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
		CARGAR_PANEL_LOGIN, CARGAR_PANEL_MENU,LOGIN,DESCONECTAR

	}

	private JPanel panelContenedor;
	private PanelLogin panelLogin;
	private PanelMenu panelMenu;

	public Principal() {
		setResizable(false);

		// Panel que contiene todo el contenido de la ventana
		mCrearPanelContenedor();
		// Panel que contiene el listado de contactos.
		mCrearPanelLogin();
		mCrearPanelMenu();
	}

	private void mCrearPanelMenu() {
		// TODO Auto-generated method stub
		panelMenu = new PanelMenu();
		panelMenu.setLocation(0, 11);
		panelContenedor.add(panelMenu);
		panelContenedor.setBounds(panelMenu.getBounds());
		panelMenu.setVisible(true);
	}

	// *** Creaci�n de paneles ***

	private void mCrearPanelContenedor() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 584);
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

		switch (panel) {
		case CARGAR_PANEL_LOGIN:
			panelLogin.setVisible(true);
			break;
		case CARGAR_PANEL_MENU:
			panelMenu.setVisible(true);
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

}