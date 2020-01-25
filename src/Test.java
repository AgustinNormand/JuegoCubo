
public class Test {

	public static void main(String[] args) {
		
		
		Message m1 = new Message("Leiste","Se");
		Message m2 = new Message("Leiste","Se","Na");
		int aux = -1;
		while (aux == -1) {
			aux = m2.getResponse();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(aux);
		}

}
