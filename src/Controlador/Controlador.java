package Controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import Modelo.JuegoPublico;
import Modelo.Jugador;
import ModeloControlador.posiblesCambios;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto{
	
	private JuegoPublico juego;
	private IVista vista;

	public <T extends IObservableRemoto> Controlador(T modelo) {
		try {
			this.setModeloRemoto(modelo);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Controlador() {
		
	}
	
	public void setVista(IVista vista) {
		this.vista =  vista;
	}

	public void hayCambiosEn(Object cambio) {
		switch ((posiblesCambios) cambio) {
		case NUEVAS_CARTAS_JUGADORES:
			vista.nuevasCartasJugadores();
			break;
		case ACTUALIZAR_LISTA_JUGADORES:
			try {
				vista.actualizarListaJugadores(juego.getJugadores());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case ESTADO_JUEGO:
			try {
				vista.nuevoEstadoJuego(juego.getEstado().toString());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case NUEVO_TURNO_JUGADOR:
			try {
				vista.nuevoTurnoJugador(juego.getJugadorEnTurno());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case NUEVA_CARTA_DESCARTADA:
			try {
				vista.nuevaCartaDescartada(juego.getCartaDescartada());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case VERIFICAR_TODOS_LISTOS:
			vista.verificarTodosListos();
			break;
		case MOSTRAR_2_CARTAS_JUGADORES:
			vista.mostrar2CartasJugadores();
			break;
		case JUEGO_TERMINADO:
			try {
				vista.terminarJuego(juego.getGanador());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case LEVANTA_CARTA:
			vista.cartaLevantada();
			break;
		case UN_JUGADOR_DIJO_CUBO:
			try {
				vista.unJugadorDijoCubo(juego.getNombre(juego.getJugadorCubo()));
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case ERROR:
			try {
				vista.error(juego.getErrorMessage());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MANO_TERMINADA:
			vista.manoTerminada(/*juego.getJugadores()*/);
			break;
		case FIN_TURNO_HABILITADO:
			try {
				vista.finTurnoHabilitado(juego.getJugadorEnTurno());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case VERIFICAR_VIO_CARTA:
			vista.verificarVioCarta();
			break;
		case PUEDE_VER_CARTA:
			vista.puedeVerCarta();
			break;
		case PUEDE_INTERCAMBIAR_CARTA:
			vista.puedeIntercambiarCarta();
			break;
		case NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA:
			vista.nuevasCartasJugadorAMostrarCartas();
			break;
		}
	}

	public void agregarJugador(String nombre){
		try {
		juego.agregarJugador(nombre);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void jugar() {
		try {
			juego.jugar();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public boolean juegoJugable() {
		try {
			return juego.isJugable();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void levantarDelMazo(int numeroJugador){
		try {
			juego.levantarDeMazo(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void levantarDeDescartadas(int numeroJugador){
		try {
			juego.levantarDeDescartadas(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Jugador> getJugadores() {
		try {
			return juego.getJugadores();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getNombre(int numeroJugador) {
		try {
			return juego.getNombre(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void descartarCarta(int numeroJugador, int cartaADescartar){
		try {
			juego.descartarCarta(numeroJugador,cartaADescartar);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int cantidadDeCartas(int numeroJugador) {
		try {
			return juego.cantidadDeCartas(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numeroJugador;
	}

	public void Cubo(int numeroJugador){
		try {
			juego.cubo(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean puedoDecirCubo() {
		try {
			return juego.puedoDecirCubo();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void espejito(int numeroJugador, int cartaAHacerEspejito){
		try {
			juego.espejito(numeroJugador, cartaAHacerEspejito);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int cantidadJugadores() {
		try {
			return juego.cantidadJugadores();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public boolean puedoFinalizarTurno(int numeroJugador){
		try {
			return juego.puedoFinalizarTurno(numeroJugador);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void mostrarCarta(int numeroJugador, int cartaAMostrar) {
		try {
			juego.mostrarCarta(numeroJugador,cartaAMostrar);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void jugadorDeseaVerCarta(int jugadorEnTurno){
		try {
			juego.jugadorDeseaVerCarta(jugadorEnTurno);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void intercambiarCartas(int jugadorEnTurno, int jugadorOrigen, int numeroCarta) {
		try {
			juego.intercambiarCartas(jugadorEnTurno,jugadorOrigen,numeroCarta);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizar(IObservableRemoto observable, Object arg0){
		hayCambiosEn(arg0);
	}

	@Override
	public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
		this.juego = (JuegoPublico) modeloRemoto;
	}

	
	public void cartasMostradas() {
		try {
			juego.cartasMostradas();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getJugadorAMostrarCarta() {
		int aux = -1;
		try {
			aux = juego.getJugadorAMostrarCarta();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aux;
	}

}
