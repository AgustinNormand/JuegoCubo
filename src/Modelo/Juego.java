package Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ModeloControlador.posiblesCambios;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto implements JuegoPublico,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4999680254757134575L;

	private static final int TODOS = 5; //Constante para enviarle un mensaje de error a todos los jugadores

	private Mazo mazo = new Mazo();

	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private ArrayList<Jugador> jugadoresSinAsignarVista = new ArrayList<>();
	private int jugadorEnTurno = -1;
	private estadoJuego estado = estadoJuego.INICIAL;
	private int jugadorQueDijoCubo = -1;
	private int ganador = -1;
	private int vistasCargadas = 0; //Cantidad de vistas cargadas cuando se carga una partida
	private int indiceJugadorAMostrarCarta = -1; //Indice de jugador a quien se le debe mostrar cartas
	private String errorMessage = ""; //Mensaje de error para enviar a los jugadores
	private int indiceJugadorError = -1; //Indice de jugador para enviarle un mensaje de error a un jugador especifico
	
	private ArrayList<String> peticiones = new ArrayList<String>();
	
	
	private GestorTiempos gestorTiempos;

	public Juego() {
		
	}
	
	public void gestionarEspejito(int numeroJugador, int cartaAHacerEspejito,long diferencia) {
		ThreadGroup threadGroup = new ThreadGroup("ThreadsEspejito");
		gestorTiempos = new GestorTiempos(this,peticiones,numeroJugador,cartaAHacerEspejito,diferencia);
		new Thread(threadGroup,gestorTiempos,"EspejitoThread").start();
	}
	
	public GestorTiempos getGestorTiempos() {
		return gestorTiempos;
	}

	@Override
	public void jugar() throws RemoteException {
		if (!estado.equals(estadoJuego.TERMINADO)) 
			if (estado == estadoJuego.CONFIGURANDO || estado.equals( estadoJuego.INICIAL)) 
				configurarJuego();
			else
				if (estado == estadoJuego.JUGABLE) 
					comenzarJuego();
				else
					if (estado == estadoJuego.JUGANDO)
						jugarMano();
					else 
						arrojarError("El juego esta terminado!",TODOS);
					
	}
	@Override
	public void configurarJuego() throws RemoteException {
		if (estado.equals(estadoJuego.INICIAL))
			cambiarEstado(estadoJuego.CONFIGURANDO);
		if (jugadores.size() < 2)
			arrojarError("Faltan jugadores para jugar",TODOS);
		else 
			cambiarEstado(estadoJuego.JUGABLE);
	}

	public void comenzarJuego() throws RemoteException{
		notificarObservadores(posiblesCambios.COMENZO_JUEGO);
		cambiarEstado(estadoJuego.MOSTRANDO_CARTAS_INICIALES);
		for (Jugador jugador:jugadores) {
			jugador.levantarCarta(mazo.getCartaMazo(false));
			jugador.levantarCarta(mazo.getCartaMazo(false));
			jugador.levantarCarta(mazo.getCartaMazo(false));
			jugador.levantarCarta(mazo.getCartaMazo(false));
		}
		repartirCartas();
	}

	@Override
	public void repartirCartas() throws RemoteException{
		indiceJugadorAMostrarCarta = siguienteJugadorActivo(indiceJugadorAMostrarCarta);
		Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
		jugador.getCartas().get(2).setVisible(true);;
		jugador.getCartas().get(3).setVisible(true);

		jugador.setEnTurno(true);
		notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES); //Actualiza la tabla y el arraylist.
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
		/*
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	@Override
	public int getJugadorAMostrarCarta() {
		return indiceJugadorAMostrarCarta;
	}

	@Override
	public void jugarMano() throws RemoteException {
		jugadorEnTurno = siguienteJugadorActivo(jugadorEnTurno);
		if (jugadorEnTurno == -1) {
			cambiarEstado(estadoJuego.TERMINADO);
			notificarObservadores(posiblesCambios.JUEGO_TERMINADO);
		}
		else {
			if (jugadorEnTurno == jugadorQueDijoCubo) 
				finalizarMano();
			else {
				jugadores.get(jugadorEnTurno).setEnTurno(true);
				notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR);
				notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);//Es para mostrar las cartas de los otros
				notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
			}
		}
	}
	@Override
	public int getJugadorEnTurno() {
		return this.jugadorEnTurno;
	}
	@Override
	public void verificarFinTurno(Jugador jugador) throws RemoteException {
		if (jugador.yaLevanto() && jugador.yaTiro()) {
			jugador.setLevanto(false);
			jugador.setTiro(false);
			jugador.setEnTurno(false);
			jugarMano();
		}
	}

	@Override
	public void levantarDeDescartadas() throws RemoteException {
		Jugador jugador = jugadores.get(jugadorEnTurno);
		jugador.ocultarCartas();
		cambiarEstado(estadoJuego.JUGANDO);
		if (!jugador.yaLevanto()) {
			Carta cartaDescartada = mazo.getCartaDescartadas();
			if (cartaDescartada != null) {
				jugador.levantarCarta(cartaDescartada);
				jugador.setLevanto(true);
				notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
				notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
			}
			else
				arrojarError("No se puede levantar una carta nula",jugadorEnTurno);
		} else
			arrojarError("No se puede levantar dos veces",jugadorEnTurno);
	}
	@Override
	public void levantarDeMazo() throws RemoteException {
		Jugador jugador = jugadores.get(jugadorEnTurno);
		jugador.ocultarCartas();
		cambiarEstado(estadoJuego.JUGANDO);
		if (!jugador.yaLevanto()) {
			Carta cartaMazo = mazo.getCartaMazo(true);
			if (cartaMazo != null) {
				jugador.levantarCarta(cartaMazo);
				jugador.setLevanto(true);
				cambiarEstado(estadoJuego.CARTA_MAZO_LEVANTADA);
				mostrarCarta(jugadorEnTurno, cartaMazo);

				if (cartaMazo.getValor() == 10) 
					notificarObservadores(posiblesCambios.PUEDE_VER_CARTA);
				if (cartaMazo.getValor() == 11) 
					notificarObservadores(posiblesCambios.PUEDE_INTERCAMBIAR_CARTA);
			} else
				arrojarError("El mazo no tiene mas cartas.",jugadorEnTurno);
		} else
			arrojarError("No se puede levantar dos veces",jugadorEnTurno);

	}


	public void cartasMostradas(int vistaDelJugadorNro) throws RemoteException{
		if (estado.equals(estadoJuego.MOSTRANDO_CARTAS_INICIALES))
			cartasMostradasInicial(vistaDelJugadorNro);
		else
			cartaMostrada(vistaDelJugadorNro);
	}

	public void cartasMostradasInicial(int vistaDelJugadorNro) throws RemoteException{
		Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
		jugador.ocultarCartas();
		jugador.setEnTurno(false);
		notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
		if (indiceJugadorAMostrarCarta == jugadores.size()-1) {
			indiceJugadorAMostrarCarta = -1;
			cambiarEstado(estadoJuego.JUGANDO);
			mazo.darVueltaCarta();
			notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
			jugar();
		} else
			repartirCartas();
	}

	public void cartaMostrada(int vistaDelJugadorNro) throws RemoteException {
		cambiarEstado(estadoJuego.JUGANDO);
		Jugador jugador = jugadores.get(vistaDelJugadorNro);
		jugador.ocultarCartas();
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
	}



	@Override
	public void descartarCarta(int numeroJugador, int numeroCartaADescartar) throws RemoteException {
		if (numeroJugador >= 0 && numeroJugador < jugadores.size()) {
			Jugador jugadorADescartar = jugadores.get(numeroJugador);
			jugadorADescartar.ocultarCartas();
			cambiarEstado(estadoJuego.JUGANDO);
			if (numeroCartaADescartar >=0 && (numeroCartaADescartar < jugadorADescartar.cantidadDeCartas())) {
				if (jugadorADescartar.yaLevanto()) {
					Carta cartaDescartada = jugadorADescartar.quitarCarta(numeroCartaADescartar);
					if (cartaDescartada != null) 
						if (!jugadorADescartar.yaTiro()) {
							mazo.descartarCarta(cartaDescartada);
							notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
							notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
							jugadorADescartar.setTiro(true);
							verificarFinTurno(jugadorADescartar);
							if (estado.equals(estadoJuego.ESPEJITO_REALIZADO)) {
								estado = estadoJuego.JUGANDO;
								notificarObservadores(posiblesCambios.ESTADO_JUEGO);
							}
						} else arrojarError("No es posible tirar 2 veces",jugadorEnTurno);
					else arrojarError("Carta nula en descartarCarta()",jugadorEnTurno);
				} else arrojarError("Primero debes levantar una carta",jugadorEnTurno); 
			} else arrojarError("Numero de carta invalida en descartarCarta()",jugadorEnTurno);
		} else arrojarError("Numero de jugador invalido en descartarCarta()",jugadorEnTurno);
	}



	private void finalizarMano() throws RemoteException {
		notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR);

		for (Jugador jugador:jugadores) 
			if (jugador.isJugando())
				jugador.sumarCartas();

		cambiarEstado(estadoJuego.MANO_TERMINADA);
		notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);

		if (cantidadJugadoresActivos() < 2) 
			finalizarJuego();
		else {
			this.mazo = new Mazo();
			jugadorQueDijoCubo = -1;
			this.indiceJugadorAMostrarCarta = -1;
			this.jugadorEnTurno = -1;
			for (Jugador jugador:jugadores) 
				if (jugador.isJugando())
					jugador.resetearCartas();
			int primerActivoEnLaLista = primerActivo();
			jugadores.add(jugadores.get(primerActivoEnLaLista));
			jugadores.remove(primerActivoEnLaLista);

			comenzarJuego();
		}
	}



	@Override
	public void espejito(int numeroJugador, int numeroCarta) throws RemoteException {
		if (mazo.hayCartaDescartada()) {
			if (!estado.equals(estadoJuego.ESPEJITO_REALIZADO)) {
				cambiarEstado(estadoJuego.JUGANDO);
				if (numeroJugador >= 0 && numeroJugador < jugadores.size()) {
					Jugador jugadorEspejito = jugadores.get(numeroJugador);
					jugadorEspejito.ocultarCartas();
					if (jugadorEspejito.isJugando()) {
						ArrayList<Carta> cartasJugadorEspejito = jugadorEspejito.getCartas();
						if (numeroCarta >= 0 && numeroCarta < cartasJugadorEspejito.size()) {
							Carta cartaEspejito = cartasJugadorEspejito.get(numeroCarta);
							Carta cartaRetorno = mazo.espejito(cartaEspejito);
							if (cartaRetorno != null) {
								jugadorEspejito.levantarCarta(cartaRetorno);
								mostrarCartasEspejito(numeroJugador,jugadorEspejito.getCartas().indexOf(cartaEspejito),jugadorEspejito.getCartas().indexOf(cartaRetorno)); //Busco los indices de nuevo, porque pudo haberme agregado una carta en un hueco
							} else {
								jugadorEspejito.quitarCarta(numeroCarta);
								cambiarEstado(estadoJuego.ESPEJITO_REALIZADO);
							}
							notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
							notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
						} else
							arrojarError("Error en juego > espejito() > numeroCarta",numeroJugador);
					} else
						arrojarError("Error en juego > espejito() > jugadorNoActivo",numeroJugador);
				} else
					arrojarError("Error en juego > espejito() > numeroJugador",numeroJugador);
			} else
				arrojarError("No se puede hacer dos espejitos a una misma carta.",numeroJugador);
		} else
			arrojarError("No se puede hacer espejito a una carta nula",numeroJugador);
	}

	@Override
	public boolean jugadorDeseaVerCarta(int numeroJugador) throws RemoteException { //Verificar que sea visible 

		boolean puedeVerCarta = false;
		Jugador jugadorAMostrarCarta = jugadores.get(numeroJugador);
		ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
		int indice = cartasJugadorAMostrarCarta.size()-1;
		int indexOfDiez = -1;
		while (indice >= 0 && indexOfDiez == -1) {
			Carta carta = cartasJugadorAMostrarCarta.get(indice);
			if(carta.getValor() == 10)
				indexOfDiez = indice;
			indice--;
		}
		if (indexOfDiez != -1) {
			puedeVerCarta = true;
			descartarCarta(numeroJugador, indexOfDiez);
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
		}
		else
			arrojarError("No tenes un 10 para poder ver carta",numeroJugador);
		return puedeVerCarta;
	}
	
	public void mostrarCartasEspejito(int indiceJugador, int indiceCarta1, int indiceCarta2) throws RemoteException{
		cambiarEstado(estadoJuego.MOSTRANDO_CARTA);
		Jugador jugadorAMostrarCarta = jugadores.get(indiceJugador);
		ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
		Carta carta1AMostrar = cartasJugadorAMostrarCarta.get(indiceCarta1);
		carta1AMostrar.setVisible(true);
		Carta carta2AMostrar = cartasJugadorAMostrarCarta.get(indiceCarta2);
		carta2AMostrar.setVisible(true);
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);

		indiceJugadorAMostrarCarta = indiceJugador;
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
	}

	private void mostrarCarta(int numeroJugador,Carta cartaAMostrar) throws RemoteException {
		int indiceCartaAMostrar = jugadores.get(numeroJugador).getCartas().indexOf(cartaAMostrar);
		mostrarCarta(numeroJugador,indiceCartaAMostrar);
	}

	@Override
	public void mostrarCarta(int numeroJugador, int indiceCartaAMostrar) throws RemoteException {
		cambiarEstado(estadoJuego.MOSTRANDO_CARTA);

		Jugador jugadorAMostrarCarta = jugadores.get(numeroJugador);
		ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
		Carta cartaAMostrar = cartasJugadorAMostrarCarta.get(indiceCartaAMostrar);
		cartaAMostrar.setVisible(true);
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);

		indiceJugadorAMostrarCarta = numeroJugador;
		notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
	}


	@Override
	public boolean intercambiarCartas(int indiceJugadorOrigen, int indiceCartaOrigen, int indiceJugadorDestino, int indiceCartaDestino) throws RemoteException {
		boolean intercambioRealizado = false;
		if (jugadorEnTurno == indiceJugadorOrigen) {
			Jugador jugadorOrigen = jugadores.get(indiceJugadorOrigen);
			Jugador jugadorDestino = jugadores.get(indiceJugadorDestino);
			ArrayList<Carta> cartasJugadorOrigen = jugadorOrigen.getCartas();
			ArrayList<Carta> cartasJugadorDestino = jugadorDestino.getCartas();
			if (indiceCartaOrigen > 0 && indiceCartaOrigen < cartasJugadorOrigen.size() && indiceCartaDestino > 0 && indiceCartaDestino < cartasJugadorDestino.size()) {
				int indexOfOnce = -1;
				int indice = cartasJugadorOrigen.size()-1;
				while (indice >= 0 && indexOfOnce == -1) {
					Carta carta = cartasJugadorOrigen.get(indice);
					if (carta.getValor() == 11 && carta.isVisible()) {
						indexOfOnce = indice;
					}
					indice--;
				}
				if (indexOfOnce != -1) {
					if (indexOfOnce != indiceCartaOrigen) {
						Carta cartaOrigen = cartasJugadorOrigen.get(indiceCartaOrigen);
						Carta cartaDestino = cartasJugadorDestino.get(indiceCartaDestino);
						descartarCarta(indiceJugadorOrigen, indexOfOnce);
						jugadorOrigen.reemplazarCarta(cartaOrigen,cartaDestino.duplicar());
						jugadorDestino.reemplazarCarta(cartaDestino,cartaOrigen.duplicar());
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
						intercambioRealizado = true;
					} else
						arrojarError("No podes intercambiar el 11",indiceJugadorOrigen);
				} else 
					arrojarError("No tenes un 11 para intercambiar",indiceJugadorOrigen);


			} else
				arrojarError("Numero de carta invalido",indiceJugadorOrigen);
		} else
			arrojarError("No es tu turno, no podes intercambiar carta", indiceJugadorOrigen);
		return intercambioRealizado;
	}

	@Override
	public void guardarPartida() throws RemoteException {
		try
		{
			FileOutputStream file = null;
			file = new FileOutputStream("tempFile.tmp");

			ObjectOutputStream out = null;
			out = new ObjectOutputStream(file);

			out.writeObject(this);
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override 
	public boolean vistaCargada(int nroJugadorAsignado) throws RemoteException {
		boolean asignado = false;
		boolean encontrado = false;
		int index = 0;
		while (!encontrado && index < jugadoresSinAsignarVista.size()){
			Jugador jugador = jugadoresSinAsignarVista.get(index);
			if (jugador.getNumeroJugador() == nroJugadorAsignado) {
				nroJugadorAsignado = index;
				encontrado = true;
			}
			index++;
		}
		if (encontrado) {
			asignado = true;
			jugadoresSinAsignarVista.remove(nroJugadorAsignado);
			vistasCargadas++;
			if (vistasCargadas == jugadores.size()) {
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
				notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
				if (!estado.equals(estadoJuego.JUGABLE) && !estado.equals(estadoJuego.CONFIGURANDO)) {
					notificarObservadores(posiblesCambios.COMENZO_JUEGO);
					notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR);
					if (estado.equals(estadoJuego.MOSTRANDO_CARTAS_INICIALES))
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
					else
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
				}
			}
		}
		return asignado;
	}

	@Override
	public ArrayList<Jugador> getJugadoresSinAsignarVista(){
		return jugadoresSinAsignarVista;
	}
	@Override
	public void cargarPartida() throws RemoteException{
		try
		{
			File temp = new File("tempFile.tmp");
			boolean exists = temp.exists();
			if (exists) {
				FileInputStream file = new FileInputStream("tempFile.tmp");
				ObjectInputStream in = new ObjectInputStream(file);
				cargarJuego((Juego) in.readObject());
				notificarObservadores(posiblesCambios.CARGANDO_JUEGO);
				temp.delete();
				in.close();
			} else
				arrojarError("No hay partidas para cargar.",TODOS);
		} catch (IOException e)
		{
			arrojarError("Error en cargarPartida",TODOS);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			arrojarError("Error en cargarPartida",TODOS);
			e.printStackTrace();
		}	
	}

	private void cargarJuego(Juego juegoNuevo) {
		this.estado = juegoNuevo.estado;
		this.ganador = juegoNuevo.ganador;
		this.indiceJugadorAMostrarCarta = juegoNuevo.indiceJugadorAMostrarCarta;
		this.jugadorEnTurno = juegoNuevo.jugadorEnTurno;
		this.jugadores = juegoNuevo.jugadores;
		this.mazo = juegoNuevo.mazo;
		this.jugadorQueDijoCubo = juegoNuevo.jugadorQueDijoCubo;
		this.errorMessage = juegoNuevo.errorMessage;
		this.vistasCargadas = 0;

		for (Jugador jugador:jugadores)
			jugadoresSinAsignarVista.add(jugador.duplicar());
	}
	@Override
	public int agregarJugador(String nombre) throws RemoteException {
		int numeroJugador = -1;
		if (jugadores.size() != 4) {
			Jugador jugadorNuevo = new Jugador(nombre,jugadores.size());
			jugadores.add(jugadorNuevo);
			numeroJugador = jugadores.indexOf(jugadorNuevo);
			notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
		} else
			arrojarError("Alcanzaste la cantidad maxima de jugadores.",TODOS);
		
		if (jugadores.size() == 1) 
			cambiarEstado(estadoJuego.CONFIGURANDO);
		if (jugadores.size() == 2) 
			cambiarEstado(estadoJuego.JUGABLE);
		return numeroJugador;
	}
	@Override
	public int cantidadJugadores() {
		return jugadores.size();
	}
	@Override
	public int getGanador() {
		return ganador;
	}

	@Override
	public void arrojarError(String errorMessage, int indiceJugadorError) throws RemoteException {
		this.errorMessage = errorMessage;
		this.indiceJugadorError = indiceJugadorError;
		notificarObservadores(posiblesCambios.ERROR);
	}

	@Override
	public int getIndiceJugadorError() {
		return indiceJugadorError;
	}

	private void cambiarEstado(estadoJuego estadoNuevo) throws RemoteException {
		switch (estadoNuevo) {
		case JUGANDO:
			if (estado.equals(estadoJuego.MOSTRANDO_CARTA_ESPEJITO_REALIZADO))
				estado = estadoJuego.ESPEJITO_REALIZADO;
			else
				if (estado.equals(estadoJuego.MOSTRANDO_CARTA_MAZO_ESPEJITO_REALIZADO))
					estado = estadoJuego.ESPEJITO_REALIZADO;
				else
					if (!estado.equals(estadoJuego.ESPEJITO_REALIZADO)) //Si el estado anterior es ESPEJITO_REALIZADO, no lo cambio
						estado = estadoJuego.JUGANDO;
			break;
		case MOSTRANDO_CARTA:
			if (estado.equals(estadoJuego.ESPEJITO_REALIZADO))
				estado = estadoJuego.ESPEJITO_REALIZADO;
			else
				if (estado.equals(estadoJuego.CARTA_MAZO_LEVANTADA))
					estado = estadoJuego.MOSTRANDO_CARTA_MAZO;
				else
					if (estado.equals(estadoJuego.CARTA_MAZO_LEVANTADA_ESPEJITO_REALIZADO))
						estado = estadoJuego.MOSTRANDO_CARTA_MAZO_ESPEJITO_REALIZADO;
					else
						estado = estadoJuego.MOSTRANDO_CARTA;
			break;
		case CARTA_MAZO_LEVANTADA:
			if (estado.equals(estadoJuego.ESPEJITO_REALIZADO))
				estado = estadoJuego.CARTA_MAZO_LEVANTADA_ESPEJITO_REALIZADO;
			else
				estado = estadoJuego.CARTA_MAZO_LEVANTADA;
		case MOSTRANDO_CARTA_MAZO:
			if (estado.equals(estadoJuego.ESPEJITO_REALIZADO))
				estado = estadoJuego.MOSTRANDO_CARTA_MAZO_ESPEJITO_REALIZADO;
			else
				estado = estadoJuego.MOSTRANDO_CARTA_MAZO;
		default:
			estado = estadoNuevo;
		}
		notificarObservadores(posiblesCambios.ESTADO_JUEGO);
	}

	private void finalizarJuego() throws RemoteException {
		cambiarEstado(estadoJuego.TERMINADO);
		for (Jugador jugador:jugadores) 
			if (jugador.isJugando() && cantidadJugadoresActivos() == 1 && jugador.getPuntaje() < 100) 
				ganador = jugadores.indexOf(jugador);
		notificarObservadores(posiblesCambios.JUEGO_TERMINADO);
	}

	private int primerActivo() throws RemoteException {
		int index = 0;
		int activo = -1;
		while (index < jugadores.size() && activo == -1) 
			if (jugadores.get(index).isJugando())
				activo = index;
			else
				index++;

		if (activo == -1) 
			arrojarError("Deberia haber encontrado algun jugador activo en primerActivo()",TODOS);

		return activo;
	}
	@Override
	public int cantidadDeCartas(int numeroJugador) {
		return jugadores.get(numeroJugador).cantidadDeCartas();
	}

	@Override
	public void cubo(int numeroJugador) throws RemoteException {
		if (jugadorQueDijoCubo == -1) {
			jugadorQueDijoCubo = numeroJugador;
			notificarObservadores(posiblesCambios.UN_JUGADOR_DIJO_CUBO);
		} else	{
			errorMessage = "Alguien ya dijo cubo!";
			notificarObservadores(posiblesCambios.ERROR);
		}
	}

	@Override
	public int getJugadorCubo() {
		return this.jugadorQueDijoCubo;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public boolean puedoDecirCubo() {
		return jugadorQueDijoCubo == -1;
	}

	private int siguienteJugadorActivo(int jugadorActivoActual) {
		/* Dado un indice de jugador, indica el indice del proximo jugador que se encuentra en estado JUGANDO */
		int activo = -1;
		int i;
		if (jugadorActivoActual == -1)
			i = 0;
		else
			i = jugadorActivoActual+1;

		while ((activo == -1) && (i < jugadores.size())){
			if (jugadores.get(i).isJugando())
				activo = i;
			else
				i++;
		}
		i = 0;
		while ((activo == -1) && ( i <= jugadorEnTurno)) {
			if (jugadores.get(i).isJugando())
				activo = i;
			else
				i++;
		}
		return activo;
	}
	private int cantidadJugadoresActivos() {
		int activos = 0;
		for (Jugador jugador:jugadores) {
			if (jugador.isJugando())
				activos++;
		}
		return activos;
	}

	@Override
	public String getEstado () {
		return estado.name();
	}

	@Override
	public Carta getCartaDescartada() {
		Carta carta = mazo.showCartaDescartada();
		return carta;
	}

	@Override
	public boolean isJugable() {
		if (estado == estadoJuego.TERMINADO) 
			return false;
		else
			return true;
	}
	@Override
	public ArrayList<Jugador> getJugadores() {
		ArrayList<Jugador> jugadoresDuplicados = new ArrayList<>();
		for (Jugador jugador:jugadores) 
			jugadoresDuplicados.add(jugador.duplicar());
		return jugadoresDuplicados;
	}

	@Override
	public String getNombre(int numeroJugador) {
		return jugadores.get(numeroJugador).getNombre();
	}
}