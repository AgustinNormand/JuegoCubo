package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Cloneable,Serializable{
	private String nombre;
	private int puntaje;
	private ArrayList<Carta> cartas;
	private estadoJugador estado;
	private boolean enTurno = false;
	
	private boolean levanto = false;
	private boolean tiro = false;
	
	public Jugador(String nombre) {
		this(nombre,0,new ArrayList<>(),estadoJugador.JUGANDO,false);
	}
	public Jugador(String nombre, int puntaje, ArrayList<Carta> cartas, estadoJugador estado,boolean enTurno) {
		this.nombre = nombre;
		this.puntaje = puntaje;
		this.cartas = cartas;
		this.estado = estado;
		this.enTurno = enTurno;
	}

	public void recivirCarta(Carta carta) {
		cartas.add(carta);
		}
	
	public boolean isJugando() {
		return estado == estadoJugador.JUGANDO;
	}
	
	public void sumarCartas() {
		for(Carta carta:cartas) {
			puntaje += carta.getValor(); 
		}
		if (puntaje >= 100) 
			estado = estadoJugador.PASADO;
	}

	public void ocultarCartas() {
		for (Carta carta:cartas)
			carta.setVisible(false);	
	}
	
	public ArrayList<Carta> getCartas() {
		ArrayList<Carta> copy = new ArrayList<>();
		for (Carta carta:cartas) {
			copy.add(carta);
		}
		return copy;
	}
	
	public String getNombre() {
		return this.nombre;
	}

	public int cantidadDeCartas() {
		return cartas.size();
	}

	public Carta quitarCarta(int cartaADescartar) {
		Carta cartaRemovida = null;
		cartaADescartar = cartaADescartar -1;
		if (cartaADescartar >= 0 && cartaADescartar < cartas.size()) {
			cartaRemovida = cartas.get(cartaADescartar);
			cartas.remove(cartaADescartar);
		}
		return cartaRemovida;
	}
	
	public String getEstado() {
		return estado.toString();
	}
	
	public int getPuntaje() {
		return puntaje;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

	public void resetearCartas() {
		cartas.clear();
	} 
	
	public Jugador duplicar() {
		ArrayList<Carta> cartasDuplicadas = new ArrayList<>();
		for (Carta carta:cartas) {
			cartasDuplicadas.add(carta.duplicar());
		}
		Jugador jugadorDuplicado = new Jugador(this.nombre,this.puntaje,cartasDuplicadas,this.estado,this.enTurno);
		return jugadorDuplicado;
	}
	public boolean yaLevanto() {
		return levanto;
	}
	public void setLevanto(boolean levanto) {
		this.levanto = levanto;
	}
	public boolean yaTiro() {
		return tiro;
	}
	public void setTiro(boolean tiro) {
		this.tiro = tiro;
	}
	public void setEnTurno(boolean enTurno) {
		this.enTurno = enTurno;
	}
	public boolean isEnTurno() {
		return enTurno;
	}
	}

