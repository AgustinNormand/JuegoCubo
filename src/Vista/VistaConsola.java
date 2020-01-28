package Vista;

import java.util.ArrayList;

import Controlador.Controlador;
import Controlador.IVista;
import Modelo.Carta;
import Modelo.Jugador;

public class VistaConsola implements IVista{
	
	private Controlador controlador;
	
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	
	public void VistaConsola(Controlador controlador) {
		this.controlador = controlador;
	}

	@Override
	public void nuevasCartasJugadores() {
		jugadores = controlador.getJugadores();
		ArrayList<Carta> cartasJugador = new ArrayList<>();
		for (Jugador jugador:jugadores) {
			cartasJugador = jugador.getCartas();
			System.out.println("Cartas de "+jugador.getNombre());
			for (Carta carta:cartasJugador)
				System.out.println(carta.toString());
		}
	}
	@Override
	public void actualizarListaJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}
	@Override
	public void nuevoEstadoJuego(String estado) {
		System.out.println("El estado de juego es "+estado);
	}
	@Override
	public void nuevoTurnoJugador(int numeroJugador) {
		Jugador jugadorEnTurno = jugadores.get(numeroJugador);
		ArrayList<Carta> cartasJugadorEnTurno = jugadorEnTurno.getCartas();
		System.out.println("Es el turno del jugador "+jugadorEnTurno.getNombre());
		System.out.println("Cartas: ");
		
		//Muestro las cartas
		for (Carta carta:cartasJugadorEnTurno) {
			System.out.println(
					cartasJugadorEnTurno.indexOf(carta)+1 //+1 Para que no arranquen en 0
					+" > "
					+carta.toString());
		}
		
		System.out.println("1) Levantar del mazo");
		System.out.println("2) Levantar carta descartada");
		System.out.println("");
		System.out.println("3) Hacer espejito");
		
		System.out.print("Elija una opcion > ");
		int opcion = EntradaConsola.tomarEntero();
		switch (opcion) {
		case 1:
			controlador.levantarDelMazo(numeroJugador);
			break;
		case 2:
			controlador.levantarDeDescartadas(numeroJugador);
			break;
		case 3:
			System.out.println(jugadorEnTurno.getNombre()+" indique que el numero de la carta quiere hacer espejito (1 A "+controlador.cantidadDeCartas(numeroJugador)+")");
			int cartaAHacerEspejito = EntradaConsola.tomarEntero();
			while (cartaAHacerEspejito < 0 || cartaAHacerEspejito > controlador.cantidadDeCartas(numeroJugador))
				cartaAHacerEspejito = EntradaConsola.tomarEntero();
			controlador.espejito(numeroJugador,cartaAHacerEspejito);
			break;
				
		}
		System.out.println("Cartas: ");
		cartasJugadorEnTurno = jugadorEnTurno.getCartas();
		for (Carta carta:cartasJugadorEnTurno) {
			System.out.println(cartasJugadorEnTurno.indexOf(carta)+1+" > "+carta.toString());
		}
		System.out.println(jugadorEnTurno.getNombre()+" indique que el numero de la carta quiere descartar (1 A "+controlador.cantidadDeCartas(numeroJugador)+")");
		int cartaADescartar = EntradaConsola.tomarEntero();
		while (cartaADescartar < 0 || cartaADescartar > controlador.cantidadDeCartas(numeroJugador))
			cartaADescartar = EntradaConsola.tomarEntero();
		controlador.descartarCarta(numeroJugador,cartaADescartar);
		
		if (controlador.puedoDecirCubo()) {
			System.out.println("1) Decir CUBO y finalizar turno");
			System.out.println("2) Finalizar turno");
			System.out.println("");
			System.out.println("3) Hacer espejito");
			
			opcion = EntradaConsola.tomarEntero();
			switch (opcion) {
				case 1:
					controlador.Cubo(numeroJugador);
					break;
				case 3:
					System.out.println(jugadorEnTurno.getNombre()+" indique que el numero de la carta quiere hacer espejito (1 A "+controlador.cantidadDeCartas(numeroJugador)+")");
					int cartaAHacerEspejito = EntradaConsola.tomarEntero();
					while (cartaAHacerEspejito < 0 || cartaAHacerEspejito > controlador.cantidadDeCartas(numeroJugador))
						cartaAHacerEspejito = EntradaConsola.tomarEntero();
					controlador.espejito(numeroJugador,cartaAHacerEspejito);
					break;
				}
			}
	}
	@Override
	public void nuevaCartaDescartada(Carta carta) {
		System.out.println("Se tiró la carta "+carta.toString());
	}
	@Override
	public void verificarTodosListos() {
		System.out.println("Presione cualquier tecla si los jugadores ya vieron las cartas.");
		try{System.in.read();}
		catch(Exception e){}
	}
	public void agregarJugador(String nombre) {
		controlador.agregarJugador(nombre);
	}
	public void jugar() {
		controlador.jugar();
	}
	@Override
	public void mostrar2CartasJugadores() {
		System.out.println("Se deben mostrar solo 2 cartas a los jugadores");
	}
	@Override
	public void terminarJuego(int ganador) {
		System.out.println("Juego Terminado");
		if (ganador == -1)
			System.out.println("No gano nadie.");
		else {
			Jugador jugadorGanador = jugadores.get(ganador);
			System.out.println("Ganador "+jugadorGanador.getNombre());
		}
	}
	@Override
	public void cartaLevantada() {
		System.out.println("Nueva carta levantada");
	}
	public boolean isJugable() {
		return controlador.juegoJugable();
	}
	@Override
	public void unJugadorDijoCubo(String nombre) {
		System.out.println(nombre+" dijo cubo!");
	}
	@Override
	public void error(String errorMessage) {
		System.out.println(errorMessage);
	}
	@Override
	public void manoTerminada() {
		for (Jugador jugador:jugadores) {
			System.out.println(jugador.getNombre());
			System.out.println(jugador.getPuntaje());
			System.out.println(jugador.getEstado());
		}
	}
	@Override
	public void finTurnoHabilitado(int numeroJugador) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void verificarVioCarta() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void puedeVerCarta() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void puedeIntercambiarCarta() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void iniciar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nuevasCartasJugadorAMostrarCartas() {
		// TODO Auto-generated method stub
		
	}
}
