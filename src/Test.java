import java.rmi.RemoteException;

import Controlador.Controlador;
import Controlador.IVista;
import Modelo.Juego;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class Test {

	public static void main(String[] args) {
		
		Juego modelo = new Juego(); // modelo
		Servidor servidor = new Servidor("127.0.0.1", 8888);
		try {
			servidor.iniciar(modelo);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
		
		Controlador controlador = new Controlador();
		IVista vista = new VentanaPrincipal(controlador);
		Cliente cliente = new Cliente("127.0.0.1", 9999, "127.0.0.1", 8888);
		vista.iniciar();
		try {
			cliente.iniciar(controlador); // enlaza el controlador con el modelo remoto 
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
		
		try {
			modelo.agregarJugador("Pepe");
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			modelo.agregarJugador("Jose");
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
