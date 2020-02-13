package Vista;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;

import javax.swing.WindowConstants;

public class VentanaPrincipal extends JFrame implements IVista {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9012510155557147328L;

	private Controlador controlador;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private JPanel contentPane;
	private JPanel panelConfiguracion;
	private JLabel lblEstado;
	private JPanel panelCartasPropias;
	private JLabel lblCartaDescartada = new JLabel();;
	private JButton btnCubo;
	private JLabel lblJugadorcubo;
	private JLabel lblInformativo;
	private int vistaDelJugadorNro = -1;
	private int jugadorEnTurno = -1;
	private JButton btnEspejito;
	private JLabel lblCartaMazo = new JLabel();;
	private JButton btnCartasVistas;
	private JButton btnAgregarJugador;
	private JButton btnIntercambiarCartas;
	private JButton btnVerCarta;
	private boolean intercambiarCartaActivado;

	private boolean espejitoActivado = false;
	private boolean verCartaActivado = false;
	private JLabel lblEstadoDeJuego;
	private JLabel lblDijoCubo;
	private JPanel panelCentral;
	private JButton btnComenzarJuego;
	private JScrollPane playerScroll;
	private JTable playerTable;
	private JPanel panelMazo;
	private JPanel panelIzquierdo;

	private int jugadorOrigen = -1;
	private int jugadorDestino = -1;
	private int cartaOrigen = -1;
	private int cartaDestino = -1;
	
	private Timestamp ultimaInteraccion = new Timestamp(System.currentTimeMillis());


	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(Controlador controlador) {

		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		this.controlador = controlador;
		this.controlador.setVista(this);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 921, 491);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panelIzquierdo = new JPanel();
		contentPane.add(panelIzquierdo, BorderLayout.WEST);
		panelIzquierdo.setLayout(new MigLayout("", "[]", "[]"));

		JPanel panelDerecho = new JPanel();
		contentPane.add(panelDerecho, BorderLayout.EAST);
		panelDerecho.setLayout(new MigLayout("", "[center,grow]", "[center][grow]"));

		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new MigLayout("", "[][][][][][][]", "[]"));

		panelCentral = new JPanel();
		panelCentral.setLayout(new MigLayout("center , center"));

		JPanel panelInferior = new JPanel();
		contentPane.add(panelInferior, BorderLayout.SOUTH);
		panelInferior.setLayout(new MigLayout("", "[]", "[]"));

		// Defino el panel derecho
		playerTable = new JTable();
		playerScroll = new JScrollPane(playerTable);
		playerScroll.setMaximumSize(new Dimension(250,7000));
		playerScroll.setMinimumSize(new Dimension(100,100));
		playerTable.disable();
		playerTable.getTableHeader().setResizingAllowed(false);
		playerTable.getTableHeader().setReorderingAllowed(false);
		panelDerecho.add(new JLabel("Jugadores"), "cell 0 0,center");
		panelDerecho.add(playerScroll, "cell 0 1,grow");

		// Defino componentes del panel superior
		lblEstadoDeJuego = new JLabel("Estado de Juego:");
		panelSuperior.add(lblEstadoDeJuego, "cell 0 0");
		lblEstadoDeJuego.setVisible(true);

		lblEstado = new JLabel("Inicial");
		panelSuperior.add(lblEstado, "cell 1 0");
		lblEstado.setVisible(true);

		lblDijoCubo = new JLabel("Dijo Cubo:");
		panelSuperior.add(lblDijoCubo, "cell 5 0");
		lblDijoCubo.setVisible(false);

		lblJugadorcubo = new JLabel("Nadie");
		panelSuperior.add(lblJugadorcubo, "cell 6 0");
		lblJugadorcubo.setVisible(false);

		// Defino componentes del panel inferior
		btnCubo = new JButton("Cubo");
		panelInferior.add(btnCubo, "cell 1 0");
		btnCubo.setVisible(false);

		btnEspejito = new JButton("Espejito");
		panelInferior.add(btnEspejito, "cell 0 0");
		btnEspejito.setVisible(false);

		btnCartasVistas = new JButton("CartasVistas");
		panelInferior.add(btnCartasVistas, "cell 2 0");
		btnCartasVistas.setVisible(false);

		btnIntercambiarCartas = new JButton("Intercambiar Cartas");
		panelInferior.add(btnIntercambiarCartas, "cell 4 0");
		btnIntercambiarCartas.setVisible(false);

		btnVerCarta = new JButton("Ver Carta");
		panelInferior.add(btnVerCarta, "cell 3 0");
		btnVerCarta.setVisible(false);

		// Defino el panel de configuracion inicial

		panelConfiguracion = new JPanel();
		contentPane.add(panelConfiguracion,BorderLayout.CENTER);
		panelConfiguracion.setLayout(new MigLayout("", "[grow,center]", "[grow][grow][grow][grow]"));

		JLabel lblBienvenida = new JLabel("Bienvenido al CUBO");
		panelConfiguracion.add(lblBienvenida,"cell 0 0");

		lblInformativo = new JLabel("Debe agregar al menos 2 jugadores para poder jugar");
		panelConfiguracion.add(lblInformativo,"cell 0 1");

		btnAgregarJugador = new JButton ("Agregar Jugador");
		panelConfiguracion.add(btnAgregarJugador,"cell 0 2");

		btnComenzarJuego = new JButton ("Comenzar Juego");
		btnComenzarJuego.setVisible(false); //Lo hago visible cuando el juego sea JUGABLE
		panelConfiguracion.add(btnComenzarJuego,"cell 0 3");


		// Defino el menu de la pantalla principal
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmGuardarPartida = new JMenuItem("Guardar Partida");
		mnArchivo.add(mntmGuardarPartida);

		JMenuItem mntmCargarPartida = new JMenuItem("Cargar Partida");
		mnArchivo.add(mntmCargarPartida);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);

		// Action Listeners
		lblCartaMazo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (jugadorEnTurno == vistaDelJugadorNro && !verCartaActivado && !espejitoActivado && !intercambiarCartaActivado)
					controlador.levantarDelMazo();
			}
		});

		lblCartaDescartada.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (jugadorEnTurno == vistaDelJugadorNro && !verCartaActivado && !espejitoActivado && !intercambiarCartaActivado)
					controlador.levantarDeDescartadas();
			}
		});

		btnCartasVistas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.cartasMostradas(vistaDelJugadorNro);
				btnCartasVistas.setVisible(false);
			}
		});

		btnIntercambiarCartas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!intercambiarCartaActivado) {
					intercambiarCartaActivado = true;
					btnIntercambiarCartas.setText("Cancelar Intercambio");
				} else {
					intercambiarCartaActivado = false;
					btnIntercambiarCartas.setText("Intercambiar Cartas");	
				}
			}
		});

		btnVerCarta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (controlador.jugadorDeseaVerCarta(jugadorEnTurno)) {
					verCartaActivado = true;
					mostrarMensaje("Seleccione la carta que desea ver", "Ok");
				}
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


		btnAgregarJugador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (vistaDelJugadorNro == -1) {
					String nombreJugador = JOptionPane.showInputDialog("Ingrese el nombre del jugador ");
					if (nombreJugador != null) {
						vistaDelJugadorNro = controlador.agregarJugador(nombreJugador);
						btnAgregarJugador.setVisible(false);
						setTitle(nombreJugador);
					}
				} else {
					error("Solo se permite 1 jugador por pantalla",vistaDelJugadorNro);
					btnAgregarJugador.setVisible(false);
				}
			}
		});

		btnComenzarJuego.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.jugar();
			}
		});


		btnCubo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.Cubo(jugadorEnTurno);
				btnCubo.setVisible(false);
			}
		});

		mntmSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmGuardarPartida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.guardarPartida();
				System.exit(0);
			}
		});
		mntmCargarPartida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.cargarPartida();
			}
		});


		//
	}

	@Override
	public void iniciar() {
		this.setVisible(true);
	}

	@Override
	public void nuevasCartasJugadorAMostrarCartas(int jugadorAMostrarCartas) {
		
		/* Muestra las cartas a todos los jugadores, casi todos las verán ocultas, excepto 1 jugador (el jugadorAMostrarCartas) y a este 
		 * tambien se le hará visible el boton "Cartas Vistas" */
		if (jugadorAMostrarCartas == vistaDelJugadorNro) 
			btnCartasVistas.setVisible(true);
		else
			jugadorAMostrarCartas = vistaDelJugadorNro; //Esto hace que se les muestren las cartas a todos los jugadores, pero ocultas (queda mejor)

		Jugador jugadorAMostrarCarta = jugadores.get(jugadorAMostrarCartas);
		Component[] componentes = panelIzquierdo.getComponents(); 
		panelIzquierdo.remove(componentes[1]);
		panelIzquierdo.add(new JLabel("Tus Cartas, "+jugadorAMostrarCarta.getNombre()), "cell 0 0,center");
		imprimirCartas(jugadorAMostrarCarta,panelCartasPropias,true);
	}

	@Override
	public void nuevoEstadoJuego(String estadoJuego) {
		/* Actualiza el estado del juego en el lblEstado, ubicado en en panel superior
		 * y adapta la vista al estado de juego correspondiente*/
		lblEstado.setText(estadoJuego);
		if (estadoJuego.equals("JUGABLE")) {
			lblInformativo.setText("Puedes comenzar a jugar o continuar agregando jugadores.");
			btnComenzarJuego.setVisible(true);
		}
		if (estadoJuego.equals("JUGANDO")) {
			lblJugadorcubo.setVisible(true);
			btnEspejito.setVisible(true);
			lblDijoCubo.setVisible(true);
			lblCartaDescartada.setVisible(true);
			lblCartaMazo.setVisible(true);
		}
		if (estadoJuego.equals("MANO_TERMINADA")) {
			btnEspejito.setVisible(false);
		}
	}

	@Override
	public void nuevasCartasJugadores(ArrayList<Jugador> jugadores) {
		/* Actualiza las cartas de todos los jugadores*/
		this.jugadores = jugadores;
		int columna = 1;
		panelCentral.removeAll();
		panelCentral.add(panelMazo,"cell 0 0"); //Agrega el mazo y las cartas descartadas en la primer posicion
		for (Jugador jugador:jugadores) {
			if (jugadores.indexOf(jugador) != vistaDelJugadorNro) { // Las imprime en el panel central
				JPanel panelCartasJugador = new JPanel();
				JPanel cartasJugador = new JPanel();
				panelCartasJugador.setLayout(new MigLayout("","[][]","[]"));
				cartasJugador = new JPanel();
				cartasJugador.setLayout(new MigLayout("","[][]","[][]"));
				JScrollPane scrollCartasJugador = new JScrollPane(cartasJugador);
				scrollCartasJugador.setBorder(null);
				scrollCartasJugador.setMinimumSize(new Dimension(200,220));
				scrollCartasJugador.setMaximumSize(new Dimension(200,500));

				panelCartasJugador.add(new JLabel("   Cartas de "+jugador.getNombre()),"cell 0 0");
				panelCartasJugador.add(scrollCartasJugador,"cell 0 1,grow");
				panelCentral.add(panelCartasJugador,"cell "+columna++ +" 0"); //Luego del mazo, una al lado de la otra.
				imprimirCartas(jugador,cartasJugador,false); //Indica el jugador, el panel donde debe imprimir las cartas, y que no se trata de las cartas propias

			} else { //Las imprime en el panel izquierdo

				Component[] componentes = panelIzquierdo.getComponents(); 
				panelIzquierdo.remove(componentes[1]);
				panelIzquierdo.add(new JLabel("Tus Cartas, "+jugador.getNombre()), "cell 0 0,center");
				imprimirCartas(jugador,panelCartasPropias,true); // Igual a la llamada anterior, indicando que se trata de las cartas propias
			}
		}
		panelCentral.revalidate();
		panelCentral.repaint();
	}

	public void imprimirCartas(Jugador jugador,JPanel panelCartas, boolean cartasPropias) {
		/*Crea y ubica en los paneles los label que contienen las cartas*/
		panelCartas.removeAll();
		ArrayList<Carta> cartasJugador = jugador.getCartas();
		ArrayList<Integer> huecos = jugador.getHuecos(); //Se trata de espacios en las cartas del jugador, para que sea mas intuitivo el comportamiento de las cartas
		int indiceCarta = 0;
		int numeroCarta = 0;
		int columna = 0;
		int fila = 0;
		for (Carta carta : cartasJugador) {
			JLabel lblCarta = new JLabel();
			lblCarta.setVisible(true);
			lblCarta.setName(Integer.toString(numeroCarta++)); //Numera las cartas en el nombre del label

			if (cartasPropias)
				lblCarta.setIcon(carta.getIcon());
			else
				lblCarta.setIcon(new ImageIcon(getClass().getResource("/Cartas/SMALL_DORSO.png"))); //Establece un dorso mas pequeño a las cartas ajenas

			panelCartas.setMinimumSize(new Dimension(lblCarta.getIcon().getIconHeight()*2,lblCarta.getIcon().getIconWidth()*2));
			//if (jugadorEnTurno == vistaDelJugadorNro) { //Restringo las acciones con las cartas
				lblCarta.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (!espejitoActivado && !verCartaActivado && !intercambiarCartaActivado)
							if (cartasPropias && vistaDelJugadorNro == jugadorEnTurno)
								controlador.descartarCarta(vistaDelJugadorNro, Integer.valueOf(lblCarta.getName()));

						if (espejitoActivado && !intercambiarCartaActivado && !verCartaActivado) {
							if (cartasPropias) {
								long diferencia = System.currentTimeMillis() - ultimaInteraccion.getTime();
								controlador.espejito(vistaDelJugadorNro, Integer.valueOf(lblCarta.getName()),diferencia);
								espejitoActivado = false;
								btnEspejito.setText("Espejito");
							}
						}
						if (!espejitoActivado && intercambiarCartaActivado && !verCartaActivado) 
							if (cartasPropias && vistaDelJugadorNro == jugadorEnTurno) {
								jugadorOrigen = vistaDelJugadorNro;
								cartaOrigen = Integer.valueOf(lblCarta.getName());
							}
							else 
								if (jugadorOrigen != -1) {
									boolean intercambioRealizado;
									jugadorDestino = jugadores.indexOf(jugador);
									cartaDestino = Integer.valueOf(lblCarta.getName());
									intercambioRealizado = controlador.intercambiarCartas(jugadorOrigen,cartaOrigen,jugadorDestino,cartaDestino);
									jugadorOrigen = -1;
									jugadorDestino = -1;
									cartaOrigen = -1;
									cartaDestino = -1;
									intercambiarCartaActivado = false;
									btnIntercambiarCartas.setText("Intercambiar Cartas");
									if (intercambioRealizado)
										btnIntercambiarCartas.setVisible(false);
								}
									else
										error("Primero selecciona la carta tuya que queres intercambiar",vistaDelJugadorNro);
						if (!espejitoActivado && !intercambiarCartaActivado && verCartaActivado) {
							if (cartasPropias) {
								controlador.mostrarCarta(vistaDelJugadorNro, Integer.valueOf(lblCarta.getName()));
								verCartaActivado = false;
								btnVerCarta.setVisible(false);
							}
						}

					}});
			
			while (huecos.contains(indiceCarta)) { //Si hay un hueco en esa posicion y en las siguientes, las salteo
				JLabel cartaVacia = new JLabel();
				if (cartasPropias)
					cartaVacia.setIcon(new ImageIcon(getClass().getResource("/Cartas/VACIO.png")));
				else
					cartaVacia.setIcon(new ImageIcon(getClass().getResource("/Cartas/SMALL_VACIO.png")));
				if (fila == 2) { //Imprimo 2 cartas por fila
					fila = 0;
					columna++;
				}
				panelCartas.add(cartaVacia, String.format("cell %s %s", fila,columna));
				fila++;
				indiceCarta++;
			}

			if (fila == 2) { //Imprimo 2 cartas por fila
				fila = 0;
				columna++;
			}
			panelCartas.add(lblCarta, String.format("cell %s %s", fila,columna));
			fila++;
			indiceCarta++;
		}
		panelCartas.revalidate();
		panelCartas.repaint();
	}

	@Override
	public void nuevoTurnoJugador(int numeroJugador) {
		jugadorEnTurno = numeroJugador;

		btnCubo.setVisible(false);
		btnVerCarta.setVisible(false);
		btnIntercambiarCartas.setVisible(false);
		btnCartasVistas.setVisible(false);

		if (numeroJugador == vistaDelJugadorNro) 
			if (controlador.puedoDecirCubo()) //Si nadie dijo cubo, hago visible el boton 
				btnCubo.setVisible(true);
	}

	@Override
	public void nuevaCartaDescartada(Carta carta) {
		if (carta != null)
			lblCartaDescartada.setIcon(carta.getIcon());
		else
			lblCartaDescartada.setIcon(new ImageIcon(getClass().getResource("/Cartas/VACIO.png")));
		ultimaInteraccion  = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public void terminarJuego(int ganador) {
		FrameMessage MessageFrame;
		if (ganador != -1) {
			Jugador jugadorGanador = jugadores.get(ganador);
			MessageFrame = new FrameMessage(this,"El ganador es: "+jugadorGanador.getNombre()+"!","Ok");
		} else
			MessageFrame = new FrameMessage(this,"No gano nadie!!","Ok");

	}
	@Override
	public void unJugadorDijoCubo(String nombre) {
		lblJugadorcubo.setText(nombre);
	}

	@Override
	public void error(String errorMessage,int indiceJugadorError) {
		if (indiceJugadorError == vistaDelJugadorNro || indiceJugadorError == 5)
			mostrarMensaje(errorMessage,"Ok");
	}

	private void inicializarPaneles() {
		panelCentral.removeAll();
		

		panelIzquierdo.removeAll();
		

		panelMazo = new JPanel();		
		panelMazo.setLayout(new MigLayout());

		ImageIcon dorsoCarta = new ImageIcon(getClass().getResource("/Cartas/DORSO.png"));
		lblCartaMazo.setIcon(dorsoCarta);
		lblCartaMazo.setVisible(false);
		panelMazo.add(lblCartaMazo);

		lblCartaDescartada.setIcon(dorsoCarta);
		lblCartaDescartada.setVisible(false);
		panelMazo.add(lblCartaDescartada);

		// Defino panel izquierdo
		panelCartasPropias = new JPanel();
		panelCartasPropias.setLayout(new MigLayout("","[][]","[][]"));
		JScrollPane scrollCartasPropias = new JScrollPane(panelCartasPropias);
		scrollCartasPropias.setBorder(null);
		scrollCartasPropias.setMinimumSize(new Dimension(200,220));
		panelIzquierdo.add(scrollCartasPropias, "cell 0 1,grow");
		panelIzquierdo.add(new JLabel(""),"cell 0 0,center");
		
		panelCentral.revalidate();
		panelCentral.repaint();
		
		panelIzquierdo.revalidate();
		panelIzquierdo.repaint();
	}

	@Override
	public void actualizarListaJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;

		Object[] columnNames = new Object[]{"Nombre", "Estado", "Puntaje", "En Turno"};
		Object[][] data = new Object[jugadores.size()][columnNames.length];
		int row = 0;
		for (Jugador jugador:jugadores) {
			data[row][0] = jugador.getNombre();
			data[row][1] = jugador.getEstado();
			data[row][2] = jugador.getPuntaje();
			data[row][3] = jugador.isEnTurno()?"X":"";
			row++;
		}
		DefaultTableModel model = new DefaultTableModel(data,columnNames);
		playerTable.setModel(model);
		playerTable.getColumn("Nombre").setMaxWidth(75);;
		playerTable.getColumn("Estado").setMaxWidth(75);
		playerTable.getColumn("Puntaje").setMaxWidth(55);
		playerTable.getColumn("En Turno").setMaxWidth(65);
	}

	@Override
	public void puedeVerCarta() {
		if (jugadorEnTurno == vistaDelJugadorNro)
			btnVerCarta.setVisible(true);
	}

	@Override
	public void puedeIntercambiarCarta() {
		if (jugadorEnTurno == vistaDelJugadorNro)
			btnIntercambiarCartas.setVisible(true);
	}

	private void mostrarMensaje(String mensajeLabel,String mensajeBoton) {
		FrameMessage errorMessageFrame = new FrameMessage(this,mensajeLabel,mensajeBoton);
		errorMessageFrame.setAlwaysOnTop(true);
	}

	@Override
	public void seleccionarJugador(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
		btnAgregarJugador.setVisible(false);
		FrameSeleccionJugador playerSelectionFrame = new FrameSeleccionJugador(this,jugadores);
		playerSelectionFrame.setVisible(true);
	}

	public void setVistaDelJugadorNro(int vistaDelJugadorNro) {
		this.vistaDelJugadorNro = vistaDelJugadorNro;
		if (controlador.vistaCargada(vistaDelJugadorNro)) {
			setTitle(jugadores.get(vistaDelJugadorNro).getNombre());
		}
		else {
			seleccionarJugador(controlador.getJugadoresSinAsignarVista());
			vistaDelJugadorNro = -1;
			error("Ese jugador ya fue asignado, seleccione otro", 5);
		}
	}

	@Override
	public void comenzoJuego() {
		inicializarPaneles();
		contentPane.remove(panelConfiguracion);
		contentPane.add(panelCentral, BorderLayout.CENTER);
	}

}
