package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8937077879772348762L;
	
	private String nombre;
	private int puntaje;
	private ArrayList<Carta> cartas;
	private estadoJugador estado;
	private int numeroJugador = -1;
	private boolean enTurno = false;
	
	private boolean levanto = false;
	private boolean tiro = false;
	
	private ArrayList<Integer> huecos;
	
	public Jugador(String nombre,int numeroJugador) {
		this(nombre,0,new ArrayList<>(),estadoJugador.JUGANDO,false,false,false,new ArrayList<>(),numeroJugador);
	}
	public Jugador(String nombre, int puntaje, ArrayList<Carta> cartas, estadoJugador estado,boolean enTurno,boolean levanto,boolean tiro,ArrayList<Integer> huecos,int numeroJugador) {
		this.nombre = nombre;
		this.puntaje = puntaje;
		this.cartas = cartas;
		this.estado = estado;
		this.enTurno = enTurno;
		this.levanto = levanto;
		this.tiro = tiro;
		this.numeroJugador = numeroJugador;
		this.huecos = huecos;
	}

	public void recivirCarta(Carta carta) {
		cartas.add(carta);
	}
	public void levantarCarta(Carta carta) {
		if (!huecos.isEmpty()) {
			cartas.add(huecos.get(huecos.indexOf(Collections.min(huecos))),carta);
			huecos.remove(0);
		}
		else
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
		if (cartaADescartar >= 0 && cartaADescartar < cartas.size()) {
			cartaRemovida = cartas.get(cartaADescartar);
			if (cartaADescartar != cartas.size()-1) { 
				int cartaADescartarAuxiliar = cartaADescartar;
				while (huecos.contains(cartaADescartarAuxiliar)) //Si ese hueco ya existe
					cartaADescartarAuxiliar++; //Busco un hueco que no exista
				huecos.add(cartaADescartarAuxiliar);
			}
			cartas.remove(cartaADescartar);
		}
		return cartaRemovida;
	}
	
	public String getEstado() {
		return estado.toString();
	}
	
	public ArrayList<Integer> getHuecos() {
		return huecos;
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
		this.enTurno = false;
		this.tiro = false;
		this.levanto = false;
		this.huecos.clear();
	} 
	
	public Jugador duplicar() {
		ArrayList<Carta> cartasDuplicadas = new ArrayList<>();
		for (Carta carta:cartas) {
			cartasDuplicadas.add(carta.duplicar());
		}
		Jugador jugadorDuplicado = new Jugador(this.nombre,this.puntaje,cartasDuplicadas,this.estado,this.enTurno,this.levanto,this.tiro,this.huecos,this.numeroJugador);
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
	public int getNumeroJugador() {
		return numeroJugador;
	}
	public void reemplazarCarta(Carta cartaOrigen, Carta cartaDestino) {
		if ((cartaOrigen != null) && (cartaDestino != null)) {
			int indexOfCartaOrigen = cartas.indexOf(cartaOrigen);
			cartas.remove(indexOfCartaOrigen);
			cartas.add(indexOfCartaOrigen,cartaDestino);
		}
	}
	}

