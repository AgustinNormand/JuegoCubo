package Vista;

import java.util.Scanner;

public class EntradaConsola {
	private static Scanner s = new Scanner(System.in);
	
	
	public static char tomarChar() {
		s = new Scanner(System.in);
		String opcion = s.nextLine();
		return opcion.charAt(0);
	}

	public static char tomarUpperChar() {
		String opcion = EntradaConsola.tomarUpperString();
		return opcion.charAt(0);
	}

	
	public static String tomarString() {
		s = new Scanner(System.in);
		String entrada = s.nextLine();
		return entrada;
	}

	public static String tomarUpperString() {
		String entrada = EntradaConsola.tomarString().toUpperCase();
		return entrada;
	}
	
	public static int tomarEntero() {
		s = new Scanner(System.in);
		boolean correcto = false;
		int entrada = 0;
		do {
			try {
				entrada = s.nextInt();
				correcto = true;
			} catch (Exception e) {
				System.out.println("El valor ingresado no es entero. Pruebe de nuevo");
			}
		}	while (!correcto);
		return entrada;
	}

	public static double tomarDoble() {
		s = new Scanner(System.in);
		boolean correcto = false;
		double entrada = 0.0;
		do {
			try {
				entrada = s.nextDouble();
				correcto = true;
			} catch (Exception e) {
				System.out.println("El valor ingresado no es doble. Pruebe de nuevo");
			}
		}	while (!correcto);
		return entrada;
	}

	
	
}
