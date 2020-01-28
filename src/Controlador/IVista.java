package Controlador;

import java.util.ArrayList;

import Modelo.Carta;
import Modelo.Jugador;

public interface IVista {
	void nuevasCartasJugadores();
	void nuevoEstadoJuego(String estado);
	void nuevoTurnoJugador(int numeroJugador);
	void nuevaCartaDescartada(Carta cartaDescartada);
	void verificarTodosListos();
	void mostrar2CartasJugadores();
	void terminarJuego(int ganador);
	void cartaLevantada();
	void unJugadorDijoCubo(String nombre);
	void error(String string);
	void manoTerminada(/*ArrayList<Jugador> puntajes*/);
	void actualizarListaJugadores(ArrayList<Jugador> jugadores);
	void finTurnoHabilitado(int numeroJugador);
	void verificarVioCarta();
	void puedeVerCarta();
	void puedeIntercambiarCarta();
	void iniciar();
	void nuevasCartasJugadorAMostrarCartas();
}
