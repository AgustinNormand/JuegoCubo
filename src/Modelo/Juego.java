package Modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ModeloControlador.posiblesCambios;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto implements JuegoPublico {
	private final static Logger LOGGER = Logger.getLogger("Juego");
	private Mazo mazo = new Mazo();
	private ArrayList<Jugador> jugadores;
	private int jugadorEnTurno = -1;
	private estadoJuego estado = estadoJuego.CONFIGURANDO;
	private int jugadorQueDijoCubo = -1;
	private int ganador = -1;
	private Carta cartaAMostrar;
	private Jugador jugadorAMostrarCarta;
	
	private int indiceJugadorAMostrarCarta = 0;
	
	private String errorMessage = "";
	
		public Juego() {
			jugadores = new ArrayList<>();
		}
		
		@Override
		public void configurarJuego() throws RemoteException {
			estado = estadoJuego.CONFIGURANDO;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
		if (jugadores.size() < 2)
			arrojarError("Faltan jugadores para jugar");
		else { 
			estado = estadoJuego.JUGABLE;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			}
		}
		
		public void comenzarJuego() throws RemoteException{
			estado = estadoJuego.MOSTRANDO_CARTAS_INICIALES;//Mostrando Cartas
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			repartirCartas();
		}
		
		@Override
		public void repartirCartas() throws RemoteException{
			Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
			jugador.recivirCarta(mazo.getCartaMazo(false));
			jugador.recivirCarta(mazo.getCartaMazo(false));
			jugador.recivirCarta(mazo.getCartaMazo(true));
			jugador.recivirCarta(mazo.getCartaMazo(true));
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
			
			jugador.setEnTurno(true);
			notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
			
			notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
		}
		
		@Override
		public int getJugadorAMostrarCarta() {
			return indiceJugadorAMostrarCarta;
		}
		
		public void cartasMostradasInicial() throws RemoteException{
			//notificarObservadores(posiblesCambios.VERIFICAR_TODOS_LISTOS);
			Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
			jugador.ocultarCartas();
			jugador.setEnTurno(false);
			notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
			indiceJugadorAMostrarCarta++;
			if (indiceJugadorAMostrarCarta == jugadores.size()) {
				estado = estadoJuego.JUGANDO;
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
				mazo.darVueltaCarta();
				notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
				jugar();
			} else
				repartirCartas();
		}

		@Override
		public void jugar() throws RemoteException {
			if (estado != estadoJuego.TERMINADO) {
				if (estado == estadoJuego.CONFIGURANDO) 
					configurarJuego();
				else
					if (estado == estadoJuego.JUGABLE) 
						comenzarJuego();
					else
						if (estado == estadoJuego.JUGANDO)
							jugarMano();
				} else {
					errorMessage = "El juego esta terminado!";
					notificarObservadores(posiblesCambios.ERROR);
				}
		}
		@Override
		public void jugarMano() throws RemoteException {
			jugadorEnTurno = siguienteJugadorActivo();
			if (jugadorEnTurno == -1) {
				estado = estadoJuego.TERMINADO;
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
				notificarObservadores(posiblesCambios.JUEGO_TERMINADO);
			}
			else {
				if (jugadorEnTurno == jugadorQueDijoCubo) 
					finalizarMano();
				else {
					jugadores.get(jugadorEnTurno).setEnTurno(true);
					notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);//Es para mostrar las cartas de los otros
					
					notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
					notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR); 
				}
			}
		}
		@Override
		public int getJugadorEnTurno() {
			return this.jugadorEnTurno;
		}
		@Override
		public void jugarTurno() throws RemoteException {
			notificarObservadores(posiblesCambios.LEVANTA_CARTA);
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
		public void levantarDeDescartadas(int indiceJugador) throws RemoteException {
			if (estado.equals(estadoJuego.JUGANDO)) {
				Jugador jugador = jugadores.get(indiceJugador);
				if (!jugador.yaLevanto()) {
					Carta cartaDescartada = mazo.getCartaDescartadas();
					if (cartaDescartada != null) {
						jugador.recivirCarta(cartaDescartada);
						jugador.setLevanto(true);
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
						notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
						verificarFinTurno(jugador);
					}
					else
						arrojarError("No se puede levantar una carta nula");
				} else
					arrojarError("No se puede levantar dos veces");
			} else
				arrojarError("Primero debes ver las cartas");
		}
		@Override
		public void levantarDeMazo(int indiceJugador) throws RemoteException {
			if (estado.equals(estadoJuego.JUGANDO)) {
				indiceJugadorAMostrarCarta = indiceJugador;
				Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
				if (!jugador.yaLevanto()) {
					Carta cartaMazo = mazo.getCartaMazo(true);
					if (cartaMazo != null) {
						jugador.recivirCarta(cartaMazo);
						jugador.setLevanto(true);
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
						notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
						estado = estadoJuego.MOSTRANDO_CARTA_MAZO;
						notificarObservadores(posiblesCambios.ESTADO_JUEGO);

						if (cartaMazo.getValor() == 10) 
							notificarObservadores(posiblesCambios.PUEDE_VER_CARTA);
						if (cartaMazo.getValor() == 11) 
							notificarObservadores(posiblesCambios.PUEDE_INTERCAMBIAR_CARTA);
					} else
						arrojarError("El mazo no tiene mas cartas.");
				} else
					arrojarError("No se puede levantar dos veces");
			} else
				arrojarError("Primero debes ver las cartas");

		}
		public void cartasMostradas() throws RemoteException{
			if (estado.equals(estadoJuego.MOSTRANDO_CARTAS_INICIALES))
				cartasMostradasInicial();
			else
				if (estado.equals(estadoJuego.MOSTRANDO_CARTA_MAZO)) 
					cartasMostradasMazo();
				else
					if (estado.equals(estadoJuego.MOSTRANDO_CARTA))
						cartaMostrada();
					else {
						arrojarError("Error in cartasMostradas()");
						LOGGER.log(Level.INFO,estado.toString()+" was not spected here.");
					}
			
		}
		public void cartasMostradasMazo() throws RemoteException {
			estado = estadoJuego.JUGANDO;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			Jugador jugador = jugadores.get(indiceJugadorAMostrarCarta);
			jugador.ocultarCartas();
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
			verificarFinTurno(jugador);
		}
		@Override
		public void agregarJugador(String nombre) throws RemoteException {
			if (jugadores.size() == 0) {
				estado = estadoJuego.CONFIGURANDO;
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			}
			if (jugadores.size() == 1) {
				estado = estadoJuego.JUGABLE;
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			}
			if (jugadores.size() != 4) {
				jugadores.add(new Jugador(nombre));
				notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
				notificarObservadores(posiblesCambios.NUEVO_JUGADOR);
			} else
				arrojarError("Alcanzaste la cantidad maxima de jugadores.");
		}
		private int siguienteJugadorActivo() {
			int activo = -1;
			int i;
			if (jugadorEnTurno == -1)
				i = 0;
			else
				i = jugadorEnTurno+1;
		
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
		//@Override
		//public void notificarObservadores(Object cambio) {
		//	for(IObservador observador:observadores)
		//		try {
		//			observador.actualizar(this,cambio);
		//		} catch (RemoteException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//}
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

		@Override
		public void descartarCarta(int numeroJugador, int numeroCartaADescartar) throws RemoteException {
			if (estado.equals(estadoJuego.JUGANDO)) 
				if (numeroJugador >= 0 && numeroJugador < jugadores.size()) {
					Jugador jugadorADescartar = jugadores.get(numeroJugador);
					if (numeroCartaADescartar >=1 && numeroCartaADescartar <= jugadorADescartar.cantidadDeCartas()) {
						Carta cartaDescartada = jugadorADescartar.quitarCarta(numeroCartaADescartar);
						if (cartaDescartada != null) 
							if (!jugadorADescartar.yaTiro()) {
								mazo.descartarCarta(cartaDescartada);
								notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
								notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
								jugadorADescartar.setTiro(true);
								verificarFinTurno(jugadorADescartar);
							} else arrojarError("No es posible tirar 2 veces");
						else arrojarError("Carta nula en descartarCarta()");
					} else arrojarError("Numero de carta invalida en descartarCarta()");
				} else arrojarError("Numero de jugador invalido en descartarCarta()");
			else arrojarError("Primero debes ver las cartas");

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

		private void finalizarMano() throws RemoteException {
			notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR);
			
			for (Jugador jugador:jugadores) {
				if (jugador.isJugando()){
					jugador.sumarCartas();
				}
			}
			
			estado = estadoJuego.MANO_TERMINADA;
			notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			notificarObservadores(posiblesCambios.MANO_TERMINADA);
			
			if (cantidadJugadoresActivos() < 2) 
				finalizarJuego();
			else {
				this.mazo = new Mazo();
				jugadorQueDijoCubo = -1;
				for (Jugador jugador:jugadores) {
					if (jugador.isJugando()){
						jugador.resetearCartas();
					}
				}
				jugadorEnTurno = -1;
				int primerActivoEnLaLista = primerActivo();
				jugadores.add(jugadores.get(primerActivoEnLaLista));
				jugadores.remove(primerActivoEnLaLista);
				
				estado = estadoJuego.JUGABLE;
				notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			}
		}
		
		private void finalizarJuego() throws RemoteException {
			estado = estadoJuego.TERMINADO;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
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
				arrojarError("Deberia haber encontrado algun jugador activo en primerActivo()");
			
			return activo;
		}

		@Override
		public int getGanador() {
			return ganador;
		}
		
		@Override
		public void arrojarError(String errorMessage) throws RemoteException {
			this.errorMessage = errorMessage;
			notificarObservadores(posiblesCambios.ERROR);
		}
		
	@Override
	public void espejito(int numeroJugador, int numeroCarta) throws RemoteException {
		numeroCarta = numeroCarta - 1; // Equivalencia
		if (mazo.hayCartaDescartada()) {
			if (numeroJugador >= 0 && numeroJugador < jugadores.size()) {
				Jugador jugadorEspejito = jugadores.get(numeroJugador);
				if (jugadorEspejito.isJugando()) {
					ArrayList<Carta> cartasJugadorEspejito = jugadorEspejito.getCartas();
					if (numeroCarta >= 0 && numeroCarta < cartasJugadorEspejito.size()) {
						Carta cartaEspejito = cartasJugadorEspejito.get(numeroCarta);
						Carta cartaRetorno = mazo.espejito(cartaEspejito);
						if (cartaRetorno != null) {
							mostrarCarta(numeroJugador, numeroCarta+1);
							jugadorEspejito.recivirCarta(cartaRetorno);
							mostrarCarta(numeroJugador,jugadorEspejito.getCartas().indexOf(cartaRetorno)+1);}
						else
							jugadorEspejito.quitarCarta(numeroCarta);
						notificarObservadores(posiblesCambios.NUEVA_CARTA_DESCARTADA);
						notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
						notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
					} else
						arrojarError("Error en juego > espejito() > numeroCarta");
				} else
					arrojarError("Error en juego > espejito() > jugadorNoActivo");
			} else
				arrojarError("Error en juego > espejito() > numeroJugador");
		} else
			arrojarError("No se puede hacer espejito a una carta nula");
	}

		@Override
		public int cantidadJugadores() {
			return jugadores.size();
		}

		@Override
		public boolean puedoFinalizarTurno(int numeroJugador) throws RemoteException {
			boolean resultado = false;
			try {
				Jugador jugador = jugadores.get(numeroJugador);
				resultado =  (jugador.yaLevanto() && jugador.yaTiro());
			}
			catch (Exception e) { arrojarError("Numero de jugador nulo"); };
			return resultado;
		}

		
		@Override
		public void jugadorDeseaVerCarta(int numeroJugador) throws RemoteException {
			Jugador jugadorAMostrarCarta = jugadores.get(numeroJugador);
			ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
			descartarCarta(numeroJugador, cartasJugadorAMostrarCarta.size());
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
		}
		@Override
		public void mostrarCarta(int numeroJugador, int indiceCartaAMostrar) throws RemoteException {
			estado  = estadoJuego.MOSTRANDO_CARTA;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			indiceCartaAMostrar = indiceCartaAMostrar -1; //Funcion de transformacion.
			jugadorAMostrarCarta = jugadores.get(numeroJugador);
			ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
			cartaAMostrar = cartasJugadorAMostrarCarta.get(indiceCartaAMostrar);
			cartaAMostrar.setVisible(true);
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
			notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
		}
		public void cartaMostrada() throws RemoteException {
			//cartaAMostrar.setVisible(false);
			jugadorAMostrarCarta.ocultarCartas();
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
			estado = estadoJuego.JUGANDO;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
		}

		@Override
		public void intercambiarCartas(int jugadorEnTurno, int jugadorOrigen, int numeroCarta) {
			
		}
}