package ModeloControlador;

import java.rmi.RemoteException;

import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

public interface IObservador {

	void actualizar(IObservadorRemoto observble, Object cambio) throws RemoteException;


}
