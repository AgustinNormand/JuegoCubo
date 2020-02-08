package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Mazo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8034617837934141086L;
	
	private ArrayList<Carta> cartas = new ArrayList<Carta>();
	private int cartaDisponibleMazo = 0;
	private int cartaDisponibleDescartadas = -1;
	private ArrayList<Carta> cartasDescartadas = new ArrayList<Carta>();
	
	public Mazo() {
		for (tipoPalo x:tipoPalo.values())
			for (int i = 1; i <= 12 ; i++) 
				cartas.add(new Carta(x,i,false));
		mezclar();
		}
	
	private void mezclar() {
		for (int i = 0; i < cartas.size();i++) {
			Random ran = new Random();
			int x = ran.nextInt(cartas.size());
			Carta cartaAux;
			cartaAux = cartas.get(i);
			cartas.set(i, cartas.get(x));
			cartas.set(x,cartaAux);
		}
	}
	
	public Carta getCartaMazo(boolean visible) {
		Carta carta;
		if (cartaDisponibleMazo != cartas.size()) { //12 != 12
			carta = cartas.get(cartaDisponibleMazo++).duplicar();
			carta.setVisible(visible);
		} else 
			if (cartaDisponibleDescartadas != 0) {
			carta = cartasDescartadas.get(0).duplicar();
			cartasDescartadas.remove(carta);
			carta.setVisible(visible);
			cartaDisponibleDescartadas--;
			} else
				carta = null;				
		return carta;
	}
	public Carta getCartaDescartadas() {
		Carta carta = null;
		if (cartaDisponibleDescartadas >= 0) {
			Carta cartaLevantada = cartasDescartadas.get(cartaDisponibleDescartadas);
			carta = cartaLevantada.duplicar();
			cartaDisponibleDescartadas--;
			cartasDescartadas.remove(cartaLevantada);
			carta.setVisible(false);
		}
		return carta;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Carta x:cartas) {
			s += x + "\n";
		}
		return s;
	}

	public void darVueltaCarta() {
		Carta carta = getCartaMazo(true).duplicar();
		carta.setVisible(true);
		cartasDescartadas.add(carta);
		cartaDisponibleDescartadas++;
	}

	public Carta showCartaDescartada() {
		Carta carta = null;
		if (cartaDisponibleDescartadas != -1) 
			carta = this.cartasDescartadas.get(cartaDisponibleDescartadas).duplicar();
		return carta;
	}

	public void descartarCarta(Carta cartaDescartada) {
		cartaDescartada.setVisible(true);
		cartasDescartadas.add(cartaDescartada);
		cartaDisponibleDescartadas++;
		
	}

	public Carta espejito(Carta cartaEspejito) {
		if (cartaDisponibleDescartadas != -1) {
			Carta cartaDescartada = cartasDescartadas.get(cartaDisponibleDescartadas);
			if (cartaDescartada.getNumero() != cartaEspejito.getNumero()) {
				descartarCarta(cartaEspejito.duplicar());
				return null;
			} else {
				return getCartaDescartadas();
			}
		} else return null;
	}

	public boolean hayCartaDescartada() {
		return cartaDisponibleDescartadas != -1;
	}
		
}
