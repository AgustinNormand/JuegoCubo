package Controlador;

import java.util.ArrayList;

import Modelo.Carta;
import Modelo.Jugador;

public interface IVista {
	void nuevasCartasJugadores(ArrayList<Jugador> jugadores);
	void nuevoEstadoJuego(String estado);
	void nuevoTurnoJugador(int numeroJugador);
	void nuevaCartaDescartada(Carta cartaDescartada);
	void terminarJuego(int ganador);
	void unJugadorDijoCubo(String nombre);
	void error(String string,int indiceJugadorError);
	void actualizarListaJugadores(ArrayList<Jugador> jugadores);
	void puedeVerCarta();
	void puedeIntercambiarCarta();
	void iniciar();
	void nuevasCartasJugadorAMostrarCartas(int jugadorAMostrarCartas);
	void seleccionarJugador(ArrayList<Jugador> jugadores);
	void comenzoJuego();
}
