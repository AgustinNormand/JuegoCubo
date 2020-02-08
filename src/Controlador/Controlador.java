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
		case COMENZO_JUEGO:
			vista.comenzoJuego();
			break;
		case CARGANDO_JUEGO:
			try {
				vista.seleccionarJugador(juego.getJugadoresSinAsignarVista());
			} catch (RemoteException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			break;
		case NUEVAS_CARTAS_JUGADORES:
			try {
				vista.nuevasCartasJugadores(juego.getJugadores());
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
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
		case JUEGO_TERMINADO:
			try {
				vista.terminarJuego(juego.getGanador());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				vista.error(juego.getErrorMessage(),juego.getIndiceJugadorError());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case PUEDE_VER_CARTA:
			vista.puedeVerCarta();
			break;
		case PUEDE_INTERCAMBIAR_CARTA:
			vista.puedeIntercambiarCarta();
			break;
		case NUEVAS_CARTAS_JUGADOR_A_MOSTRAR_CARTA:
			try {
				vista.nuevasCartasJugadorAMostrarCartas(juego.getJugadorAMostrarCarta());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	public int agregarJugador(String nombre){
		int numeroJugador = -1;
		try {
		numeroJugador = juego.agregarJugador(nombre);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numeroJugador;
	}
	public int getIndiceJugadorError() {
		int indiceJugadorError = -1;
		try {
			indiceJugadorError = juego.getIndiceJugadorError();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indiceJugadorError;
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

	public void levantarDelMazo(){
		try {
			juego.levantarDeMazo();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void levantarDeDescartadas(){
		try {
			juego.levantarDeDescartadas();
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

	public void mostrarCarta(int numeroJugador, int cartaAMostrar) {
		try {
			juego.mostrarCarta(numeroJugador,cartaAMostrar);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean jugadorDeseaVerCarta(int jugadorEnTurno){
		try {
			return juego.jugadorDeseaVerCarta(jugadorEnTurno);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean intercambiarCartas(int indiceJugadorOrigen, int indiceCartaOrigen, int indiceJugadorDestino, int indiceCartaDestino) {
		try {
			return juego.intercambiarCartas(indiceJugadorOrigen,indiceCartaOrigen,indiceJugadorDestino,indiceCartaDestino);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/*
	public void intercambiarCartasOrigen(Jugador jugadorDestino, String numeroCarta) {
		try {
			juego.intercambiarCartasOrigen(jugadorDestino,numeroCarta);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 */
	@Override
	public void actualizar(IObservableRemoto observable, Object arg0){
		hayCambiosEn(arg0);
	}

	@Override
	public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
		this.juego = (JuegoPublico) modeloRemoto;
	}

	
	public void cartasMostradas(int vistaDelJugadorNro) {
		try {
			juego.cartasMostradas(vistaDelJugadorNro);
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

	public void guardarPartida() {
		try {
			juego.guardarPartida();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cargarPartida() {
		try {
			juego.cargarPartida();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	public boolean vistaCargada(int vistaDelJugadorNro) {
		try {
			return juego.vistaCargada(vistaDelJugadorNro);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Jugador> getJugadoresSinAsignarVista() {
		try {
			return juego.getJugadoresSinAsignarVista();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
