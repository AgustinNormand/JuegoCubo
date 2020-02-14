package Modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class GestorTiempos extends Thread implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -547679968256186780L;

	ArrayList<String> peticiones;
	int numeroJugador;
	int cartaAHacerEspejito;
	long diferencia;

	JuegoPublico juego;

	public GestorTiempos(JuegoPublico juego,ArrayList<String> peticiones,int numeroJugador, int cartaAHacerEspejito,long diferencia) {
		this.peticiones = peticiones;
		this.juego = juego;
		this.numeroJugador = numeroJugador;
		this.cartaAHacerEspejito = cartaAHacerEspejito;
		this.diferencia = diferencia;
	}

	@Override
	public void run() {
		synchronized (peticiones) {
			String stringPeticion = numeroJugador+" "+cartaAHacerEspejito+" "+diferencia;
			peticiones.add(stringPeticion);
			peticiones.notify();
			System.out.println();
			try {
				peticiones.wait(500);
				int numeroThreads = this.getThreadGroup().activeGroupCount()-1;
				if (this.getName().equals("Thread-"+numeroThreads)) {
					boolean firstIteration = true;
					int minimo = -1;
					int indiceMinimo = -1;
					
					ArrayList<Integer> indiceIgual = new ArrayList<Integer>();
					
					for (String peticion:peticiones) {
						String[] peticionesStr = peticion.split(" ");
						int diferencia = Integer.valueOf(peticionesStr[2]);
						if (firstIteration) {
							minimo = diferencia;
							indiceMinimo = peticiones.indexOf(peticion);
							firstIteration = false;
						} else
							if (diferencia < minimo) {
								minimo = diferencia;
								indiceMinimo = peticiones.indexOf(peticion);
								indiceIgual.clear();
							} else 
								if(diferencia == minimo) {
									indiceIgual.add(peticiones.indexOf(peticion));
								}
						
					}
					if (!indiceIgual.isEmpty()) { //Si hay peticiones que hicieron el mismo tiempo
						indiceIgual.add(indiceMinimo); //Agrego el minimo ya que no estaba en la lista
						System.out.println("Se eligio random entre los iguales");
						int rnd = new Random().nextInt(indiceIgual.size());
						String[] peticionMasVeloz = peticiones.get(rnd).split(" ");
						juego.espejito(Integer.valueOf(peticionMasVeloz[0]), Integer.valueOf(peticionMasVeloz[1]));
					} else
					if (indiceMinimo != -1) {
						String[] peticionMasVeloz = peticiones.get(indiceMinimo).split(" ");
						juego.espejito(Integer.valueOf(peticionMasVeloz[0]), Integer.valueOf(peticionMasVeloz[1]));
					} else
						juego.arrojarError("Error en GestorTiempos", 5);
				} else
					this.join();
			} catch (InterruptedException | NumberFormatException | RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
