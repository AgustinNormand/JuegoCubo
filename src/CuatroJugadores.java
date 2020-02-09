import java.rmi.RemoteException;

import Controlador.Controlador;
import Controlador.IVista;
import Modelo.Juego;
import Vista.VentanaPrincipal;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class CuatroJugadores {

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
		
		
		Controlador controlador2 = new Controlador();
		IVista vista2 = new VentanaPrincipal(controlador2);
		Cliente cliente2 = new Cliente("127.0.0.1", 7777, "127.0.0.1", 8888);
		vista2.iniciar();
		
		
		Controlador controlador3 = new Controlador();
		IVista vista3 = new VentanaPrincipal(controlador3);
		Cliente cliente3 = new Cliente("127.0.0.1", 1111, "127.0.0.1", 8888);
		vista3.iniciar();
		
		Controlador controlador4 = new Controlador();
		IVista vista4 = new VentanaPrincipal(controlador4);
		Cliente cliente4 = new Cliente("127.0.0.1", 2222, "127.0.0.1", 8888);
		vista4.iniciar();
		
		try {
			cliente.iniciar(controlador); // enlaza el controlador con el modelo remoto
			cliente2.iniciar(controlador2);
			cliente3.iniciar(controlador3);
			cliente4.iniciar(controlador4);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
		
	}
}