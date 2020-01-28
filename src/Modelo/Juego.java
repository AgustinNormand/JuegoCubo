package Modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ModeloControlador.posiblesCambios;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto implements JuegoPublico {
	private Mazo mazo = new Mazo();
	private ArrayList<Jugador> jugadores;
	private int jugadorEnTurno = -1;
	private estadoJuego estado = estadoJuego.CONFIGURANDO;
	private int jugadorQueDijoCubo = -1;
	private int ganador = -1;
	
	private int jugadorAMostrarCarta = 0;
	
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
			estado = estadoJuego.JUGANDO;
			notificarObservadores(posiblesCambios.ESTADO_JUEGO);
			notificarObservadores(posiblesCambios.COMENZO_EL_JUEGO);
			repartirCartas();
		}
		
		@Override
		public void repartirCartas() throws RemoteException{
			Jugador jugador = jugadores.get(jugadorAMostrarCarta);
			jugador.recivirCarta(mazo.getCartaMazo(true));
			jugador.recivirCarta(mazo.getCartaMazo(true));
			jugador.recivirCarta(mazo.getCartaMazo(false));
			jugador.recivirCarta(mazo.getCartaMazo(false));
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
			notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
		}
		
		@Override
		public int getJugadorAMostrarCarta() {
			return jugadorAMostrarCarta;
		}
		@Override
		public void cartasMostradas() throws RemoteException{
			//notificarObservadores(posiblesCambios.VERIFICAR_TODOS_LISTOS);
			Jugador jugador = jugadores.get(jugadorAMostrarCarta);
			jugador.ocultarCartas();
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA);
			jugadorAMostrarCarta++;
			if (jugadorAMostrarCarta == jugadores.size()) {
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
					else
						notificarObservadores(posiblesCambios.NUEVO_TURNO_JUGADOR);
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
				jugarMano();
			}
		}
		
		@Override
		public void levantarDeDescartadas(int indiceJugador) throws RemoteException {
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
		}
		@Override
		public void levantarDeMazo(int indiceJugador) throws RemoteException {
			Jugador jugador = jugadores.get(indiceJugador);
			if (!jugador.yaLevanto()) {
				Carta cartaMazo = mazo.getCartaMazo(true);
				if (cartaMazo != null) {
				jugador.recivirCarta(cartaMazo);
				jugador.setLevanto(true);
				notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
				notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
				jugador.ocultarCartas();
				notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
				
				if (cartaMazo.getValor() == 10) 
					notificarObservadores(posiblesCambios.PUEDE_VER_CARTA);
				if (cartaMazo.getValor() == 11) 
					notificarObservadores(posiblesCambios.PUEDE_INTERCAMBIAR_CARTA);
				
				verificarFinTurno(jugador);
				} else
					arrojarError("El mazo no tiene mas cartas.");
			} else
				arrojarError("No se puede levantar dos veces");
		}
		private void ocultarCartas() {
			for (Jugador jugador:jugadores) 
				jugador.ocultarCartas();
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
			jugadores.add(new Jugador(nombre));
			notificarObservadores(posiblesCambios.ACTUALIZAR_LISTA_JUGADORES);
			notificarObservadores(posiblesCambios.NUEVO_JUGADOR);
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
							jugadorEspejito.recivirCarta(cartaRetorno);}
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
			indiceCartaAMostrar = indiceCartaAMostrar -1; //Funcion de transformacion.
			
			Jugador jugadorAMostrarCarta = jugadores.get(numeroJugador);
			ArrayList<Carta> cartasJugadorAMostrarCarta = jugadorAMostrarCarta.getCartas();
			Carta cartaAMostrar = cartasJugadorAMostrarCarta.get(indiceCartaAMostrar);
			cartaAMostrar.setVisible(true);
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
			notificarObservadores(posiblesCambios.VERIFICAR_VIO_CARTA);
			cartaAMostrar.setVisible(false);
			notificarObservadores(posiblesCambios.NUEVAS_CARTAS_JUGADORES);
			}

		@Override
		public void intercambiarCartas(int jugadorEnTurno, int jugadorOrigen, int numeroCarta) {
			
		}
}