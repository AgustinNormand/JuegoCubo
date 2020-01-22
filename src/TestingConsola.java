import Vista.VistaConsola;
public class TestingConsola {
	
	public static void main(String[] args) {
		VistaConsola vistaConsola = new VistaConsola();
		
		while (vistaConsola.isJugable()) {
			vistaConsola.jugar();
			//Leer una tecla para que no este en un loop infinito
			//Scanner myObj = new Scanner(System.in);
			//String userName = myObj.nextLine();
			//
		}
	}
}
