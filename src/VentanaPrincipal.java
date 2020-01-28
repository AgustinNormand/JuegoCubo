
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import Controlador.Controlador;
import Controlador.IVista;
import Modelo.Carta;
import Modelo.Jugador;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.WindowConstants;

public class VentanaPrincipal extends JFrame implements IVista {
	private Controlador controlador;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private JPanel contentPane;
	private JPanel panelConfiguracion;
	private JLabel lblEstado;
	private JList playerList;
	private JPanel panel_cartas;
	private JLabel lblCartaDescartada;
	private JButton btnCubo;
	private JLabel lblJugadorcubo;
	private JLabel lblInformativo;
	
	private int jugadorEnTurno;
	private JButton btnEspejito;
	private JLabel lblCartaMazo;
	private JButton btnFinDeTurno;
	private JButton btnCartasVistas;
	private JButton btnCartasVistasInicial;
	private JButton btnIntercambiarCartas;
	private JButton btnVerCarta;
	
	private boolean espejitoActivado = false;
	private boolean verCartaActivado = false;
	private JLabel lblEstadoDeJuego;
	private JLabel lblDijoCubo;
	private JPanel panel_4;
	private JButton btnComenzarJuego;
	
	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(Controlador controlador) {		
		this.controlador = controlador;
		this.controlador.setVista(this);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 921, 491);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new MigLayout("", "[]", "[]"));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));
		
		playerList = new JList();
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel_1.add(playerList, "cell 0 0,grow");
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new MigLayout("", "[][][][][][][]", "[]"));
		
		lblEstadoDeJuego = new JLabel("Estado de Juego:");
		panel_2.add(lblEstadoDeJuego, "cell 0 0");
		lblEstadoDeJuego.setVisible(false);
		
		lblEstado = new JLabel("Inicial");
		panel_2.add(lblEstado, "cell 1 0");
		lblEstado.setVisible(false);
		
		lblDijoCubo = new JLabel("Dijo Cubo:");
		panel_2.add(lblDijoCubo, "cell 5 0");
		lblDijoCubo.setVisible(false);
		
		lblJugadorcubo = new JLabel("Nadie");
		panel_2.add(lblJugadorcubo, "cell 6 0");
		lblJugadorcubo.setVisible(false);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new MigLayout("", "[]", "[]"));
		
		panel_4 = new JPanel();
		panel_4.setLayout(new MigLayout("", "[]", "[]"));
		lblCartaMazo = new JLabel();
		ImageIcon dorsoCarta = new ImageIcon(getClass().getResource("/Cartas/DORSO.png"));
		lblCartaMazo.setIcon(dorsoCarta);
		lblCartaMazo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controlador.levantarDelMazo(jugadorEnTurno);
			}
		});
		panel_4.add(lblCartaMazo, "cell 4 2");
		lblCartaMazo.setVisible(false);
		
		
		lblCartaDescartada = new JLabel();
		lblCartaDescartada.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controlador.levantarDeDescartadas(jugadorEnTurno);
			}
		});
		panel_4.add(lblCartaDescartada, "cell 7 2");
		lblCartaDescartada.setVisible(false);
		
		panel_cartas = new JPanel();
		panel_cartas.setLayout(new MigLayout("","[]","[]"));
		panel_4.add(panel_cartas, "cell 0 7 13 1,grow");
		
		btnCubo = new JButton("Cubo");
		panel_4.add(btnCubo, "cell 4 8");
		btnCubo.setVisible(false);
		
		btnEspejito = new JButton("Espejito");
		panel_4.add(btnEspejito, "cell 7 8");
		btnEspejito.setVisible(false);
		
		btnCartasVistas = new JButton("CartasVistas");
		panel_4.add(btnCartasVistas, "cell 16 4");
		btnCartasVistas.setVisible(false);
		btnCartasVistas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.cartasMostradasMazo();
				btnCartasVistas.setVisible(false);
			}
		});
		btnCartasVistasInicial = new JButton("CartasVistas");
		panel_4.add(btnCartasVistasInicial, "cell 14 2");
		btnCartasVistasInicial.setVisible(false);
		btnCartasVistasInicial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnCartasVistasInicial.setVisible(false);
				controlador.cartasMostradasInicial();
			}
		});
		
		btnIntercambiarCartas = new JButton("Intercambiar Cartas");
		panel_4.add(btnIntercambiarCartas, "cell 15 3");
		btnIntercambiarCartas.setVisible(false);
		btnIntercambiarCartas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int jugadorOrigen = Integer.valueOf(JOptionPane.showInputDialog("Jugador?"));
				int numeroCarta = Integer.valueOf(JOptionPane.showInputDialog("Carta?"));
				controlador.intercambiarCartas(jugadorEnTurno,jugadorOrigen,numeroCarta);
					//controlador.jugadorDeseaIntercambiarCarta(jugadorEnTurno);
					//intercambiarCartaActivado = true;
					//JOptionPane.showMessageDialog(this, "Seleccione la carta que desea intercambiar");
			}
		});
		
		btnVerCarta = new JButton("Ver Carta");
		panel_4.add(btnVerCarta, "cell 15 4");
		btnVerCarta.setVisible(false);
		btnVerCarta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.jugadorDeseaVerCarta(jugadorEnTurno);
				verCartaActivado = true;
				mostrarMensaje("Seleccione la carta que desea ver", "Ok");	
			}
		});
		
		btnFinDeTurno = new JButton("Fin De Turno");
		panel_4.add(btnFinDeTurno, "cell 9 8");
		btnFinDeTurno.setVisible(false);

		btnCubo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.Cubo(jugadorEnTurno);
				btnCubo.setVisible(false);
			}
		});
		
		btnFinDeTurno.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		btnEspejito.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!espejitoActivado) {
					espejitoActivado = true;
					btnEspejito.setText("Cancelar Espejito");
				}else {
					espejitoActivado = false;
					btnEspejito.setText("Espejito");
				}
					
					
			}
		});
		//
		
		// Defino el panel de configuracion inicial
		panelConfiguracion = new JPanel();
		contentPane.add(panelConfiguracion,BorderLayout.CENTER);
		panelConfiguracion.setLayout(new MigLayout("", "[][][][][][][][][][][][][center][][][][][][][][][][][][][][]", "[][][][][][][][][][][][]"));
		
		JLabel lblBienvenida = new JLabel("Bienvenido al CUBO");
		panelConfiguracion.add(lblBienvenida,"cell 12 4");
		
		lblInformativo = new JLabel("Debe agregar al menos 2 jugadores para poder jugar");
		panelConfiguracion.add(lblInformativo,"cell 12 6");
		
		JButton btnAgregarJugador = new JButton ("Agregar Jugador");
		panelConfiguracion.add(btnAgregarJugador,"cell 12 7");
		btnAgregarJugador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nombreJugador = JOptionPane.showInputDialog("Ingrese el nombre del jugador ");
				controlador.agregarJugador(nombreJugador);}
			});
		
		btnComenzarJuego = new JButton ("Comenzar Juego");
		btnComenzarJuego.setVisible(false); //Lo hago visible cuando el juego sea JUGABLE
		panelConfiguracion.add(btnComenzarJuego,"cell 12 8");
		btnComenzarJuego.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.jugar();}
		});
		//
		
		// Defino el menu de la pantalla principal
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmAgregarJugador = new JMenuItem("Agregar Jugador");
		mnArchivo.add(mntmAgregarJugador);

		JMenuItem mntmComenzarJuego = new JMenuItem("Comenzar Juego");
		mnArchivo.add(mntmComenzarJuego);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);

		// Action Listeners
		mntmAgregarJugador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nombreJugador = JOptionPane.showInputDialog("Ingrese el nombre del jugador ");
				controlador.agregarJugador(nombreJugador);
			}
		});

		mntmComenzarJuego.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.jugar();;
			}
		});

		mntmSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		//
	}
	
	@Override
	public void iniciar() {
		this.setVisible(true);
	}
	
	@Override
	public void nuevasCartasJugadorAMostrarCartas() {
		jugadores = controlador.getJugadores();
		int jugadorAMostrarCarta = controlador.getJugadorAMostrarCarta();
		imprimirCartas(jugadores.get(jugadorAMostrarCarta));
		playerList.setSelectedIndex(jugadorAMostrarCarta);
		
	}
	
	@Override
	public void nuevasCartasJugadores() {
		jugadores = controlador.getJugadores();
		imprimirCartas(jugadores.get(jugadorEnTurno)); //Para actualizar la pantalla del jugador que esta jugando
	}

	@Override
	public void nuevoEstadoJuego(String estadoJuego) {
		lblEstado.setText(estadoJuego);
		if (estadoJuego.equals("JUGABLE")) {
			lblInformativo.setText("Puedes comenzar a jugar o continuar agregando jugadores.");
			btnComenzarJuego.setVisible(true);
		}
		if (estadoJuego.equals("JUGANDO")) {
			lblCartaDescartada.setVisible(true);
			lblCartaMazo.setVisible(true);
			
		}
		
	}

	public void imprimirCartas(Jugador jugador) {
		panel_cartas.removeAll();
		panel_cartas.revalidate();
		panel_cartas.repaint();
		ArrayList<Carta> cartasJugador = jugador.getCartas();
		int numeroCarta = 1;
		int columna = 0;
		int fila = 0;
		for (Carta carta : cartasJugador) {
			JLabel lblCarta = new JLabel();
			lblCarta.setIcon(carta.getIcon());
			lblCarta.setVisible(true);
			lblCarta.setName(Integer.toString(numeroCarta++));
			lblCarta.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if ((espejitoActivado) && (verCartaActivado)) 
						error("No es posible hacer espejito y ver una carta al mismo tiempo.");
					if (!espejitoActivado && !verCartaActivado)
						controlador.descartarCarta(jugadorEnTurno, Integer.valueOf(lblCarta.getName()));
					if (espejitoActivado && !verCartaActivado) {
							controlador.espejito(jugadorEnTurno, Integer.valueOf(lblCarta.getName()));
							espejitoActivado = false;
							btnEspejito.setText("Espejito");}
					if (!espejitoActivado && verCartaActivado) {
						controlador.mostrarCarta(jugadorEnTurno, Integer.valueOf(lblCarta.getName()));
						verCartaActivado = false;}
					}
			});
			if (fila == 2) {
				fila = 0;
				columna++;
			}
			if (columna == columnasNecesarias(cartasJugador.size()))
				columna = 0;				
			panel_cartas.add(lblCarta, String.format("cell %s %s", fila,columna));
			fila++;
		}
	}
	
	public int columnasNecesarias(int cantidadCartasJugador) {
		int necesarias = 0;
		switch (cantidadCartasJugador) {
		case 1:
		case 2:
		case 3:
		case 4:
			necesarias = 2;
			break;
		default:
			if (cantidadCartasJugador % 2 == 0) 
				necesarias = cantidadCartasJugador / 2;
			else
				necesarias = cantidadCartasJugador+1 / 2;
		}
		return necesarias;
	}
	@Override
	public void nuevoTurnoJugador(int numeroJugador) {
		btnFinDeTurno.setVisible(false);
		btnCubo.setVisible(false);
		btnVerCarta.setVisible(false);
		btnIntercambiarCartas.setVisible(false);
		
		playerList.setSelectedIndex(numeroJugador);
		
		jugadorEnTurno = numeroJugador;
		
		imprimirCartas(jugadores.get(jugadorEnTurno));
		
		if (controlador.puedoDecirCubo()) 
			btnCubo.setVisible(true);
		btnEspejito.setVisible(true);
		
	}

	@Override
	public void nuevaCartaDescartada(Carta carta) {
		if (carta != null)
			lblCartaDescartada.setIcon(carta.getIcon());
		else
			lblCartaDescartada.setIcon(new ImageIcon("/home/agustin/Desktop/Objetos/img/VACIO.png"));
	}

	@Override
	public void verificarTodosListos() {
		
	}
	
	@Override
	public void verificarVioCarta() {
		btnCartasVistas.setVisible(true);
	}
	
	@Override
	public void verificarVioCartaInicial() {
		btnCartasVistasInicial.setVisible(true);
		System.out.println("Its Here");
	}



	@Override
	public void mostrar2CartasJugadores() { //Esto no me parece que este bien aca 
		Message msgFrame = new Message(this,"Vio la carta?","Si");
	}
	
	@Override
	public void terminarJuego(int ganador) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaLevantada() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unJugadorDijoCubo(String nombre) {
		lblJugadorcubo.setText(nombre);
	}

	@Override
	public void error(String errorMessage) {
		mostrarMensaje(errorMessage,"Ok");
	}

	@Override
	public void manoTerminada() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizarListaJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
		int index = 0;
		String[] strJugadores = new String[controlador.cantidadJugadores()];
		for (Jugador jugador:jugadores) 
			strJugadores[index++] = jugador.getNombre() + " " + jugador.getEstado().toString() + " " +jugador.getPuntaje();
		playerList.setListData(strJugadores);
	}
	
	@Override
	public void finTurnoHabilitado(int numeroJugador) {
		btnFinDeTurno.setVisible(true);
	}

	@Override
	public void comenzoElJuego() {
		lblEstado.setVisible(true);
		lblJugadorcubo.setVisible(true);
		lblEstadoDeJuego.setVisible(true);
		lblDijoCubo.setVisible(true);
		cambiarPanelJuego();
	}
	
	public void cambiarPanelJuego() {
		contentPane.remove(panelConfiguracion);
		contentPane.add(panel_4, BorderLayout.CENTER);
	}

	@Override
	public void puedeVerCarta() {
		btnVerCarta.setVisible(true);
	}

	@Override
	public void puedeIntercambiarCarta() {
		btnIntercambiarCartas.setVisible(true);
		}
	
	public void respuestaMensaje(boolean respuesta) {
		
	}
	
	private void mostrarMensaje(String mensajeLabel,String mensajeBoton) {
		Message msgFrame = new Message(this,mensajeLabel,mensajeBoton);
	}
	
}
