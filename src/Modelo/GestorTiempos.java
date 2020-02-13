package Modelo;

import java.io.Serializable;
import java.rmi.RemoteException;

public class GestorTiempos extends Thread implements Serializable{
	
	int cantidadPeticiones = 0;
	
	//Para espejito necesito:
	int numeroJugador;
	int numeroCarta;
	//
	
	long diferencia;

	JuegoPublico juego;
	
	public GestorTiempos(JuegoPublico juego) {
		this.juego = juego;
	}
	
	
	public void espejito(int numeroJugador, int numeroCarta, long diferencia) throws RemoteException{
		System.out.println("Entro en el gestor");
		if (cantidadPeticiones == 0) {
			this.diferencia = diferencia;
			cantidadPeticiones = 1;
			this.numeroJugador = numeroJugador;
			this.numeroCarta = numeroCarta;
			dormir(5000);
		} else {
			System.out.println("Entro a comparar las diferencias");
			if (diferencia < this.diferencia) {
				diferencia = 0;			
				juego.espejito(numeroJugador, numeroCarta);
			}	
			else {
				juego.espejito(this.numeroJugador,this.numeroCarta);
				diferencia = 0;
			}
		}
	}
	private void dormir(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
	}
	
}
