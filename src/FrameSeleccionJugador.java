import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Modelo.Jugador;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class FrameSeleccionJugador extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3712073508378495921L;
	
	static final int YES_OPTION = 1; 
	private JPanel contentPane;
	JPanel panel = new JPanel();
	VentanaPrincipal vista;

	/**
	 * Create the frame.
	 */
	public FrameSeleccionJugador(VentanaPrincipal vista,ArrayList<Jugador> jugadores) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 183, 147);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[]", "[][][]"));
		
		JLabel lblInformativo = new JLabel("Indique el indice del jugador que le pertenece");
		panel.add(lblInformativo, "cell 0 0,alignx center,aligny center");
		lblInformativo.setVisible(true);
		
		int fila = 1;
		for (Jugador jugador:jugadores) {
		panel.add(new JLabel(jugador.getNumeroJugador() +" "+jugador.getNombre()), "cell 0 "+fila++ +",alignx center,aligny center");
		}
		
		setMinimumSize(new Dimension(lblInformativo.getSize().width+40,140));
		
		JTextField jtxField = new JTextField();
		panel.add(jtxField, "cell 0 "+fila++ +",alignx center,aligny center");
		jtxField.setVisible(true);
		jtxField.setMinimumSize(new Dimension(35,20));
		
		JButton btnAfirmativo = new JButton("Confirmar");
		panel.add(btnAfirmativo, "cell 0 3,alignx center,aligny center");
		btnAfirmativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!jtxField.getText().equals("")) {
					vista.setVistaDelJugadorNro(Integer.valueOf(jtxField.getText()));
					dispose();
				}
			}
		});
	}
		
}