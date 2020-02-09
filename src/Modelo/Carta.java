package Modelo;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Carta implements Cloneable,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1071615811335128882L;
	
	private tipoPalo palo;
	private int numero;
	private boolean visible;
	private int valor;
	private ImageIcon icon;
	private ImageIcon dorso;
	
	public Carta(tipoPalo palo ,int numero,boolean visible) {
		this.numero = numero;
		this.palo = palo;
		this.visible = visible;
		icon = new ImageIcon(getClass().getResource("/Cartas/"+this.numero+"DE"+this.palo.toString()+".png"));
		dorso = new ImageIcon(getClass().getResource("/Cartas/DORSO.png"));
		
		if ((numero == 12) && (palo == tipoPalo.ORO || palo == tipoPalo.COPA)) 
			this.valor = 0;
		else
			this.valor = numero;
	}
	
	
	
	@Override
	public String toString() {
	if (visible)
		return numero+" de "+palo;
	else
		 return "X";
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getValor() {
		return valor;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		return ( (this.numero == ((Carta) obj).numero) && (this.palo == ((Carta) obj).palo) );
	}

	public Carta duplicar() {
		Carta cartaDuplicada = new Carta(this.palo,this.numero,this.visible);
		return cartaDuplicada;
	}

	public int getNumero() {
		return numero;
	}

	public ImageIcon getIcon() {
		if (visible)//visible
			return icon;
		else
			return dorso;
		
	}



	public boolean isVisible() {
		return visible;
	}

}
